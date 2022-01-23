package com.personio.demo.out.repositories;

import com.personio.demo.domain.entities.Node;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.personio.demo.domain.commons.Constants.NO_SUPERVISOR_FOUND;

/**
 * The respository for the supervisor
 */
@ApplicationScoped
public class SupervisorRepository {

    EmployeeRepository empRepo;

    /**
     * Constructor
     *
     * @param empRepo the employee repository
     */
    @Inject
    public SupervisorRepository(EmployeeRepository empRepo) {
        this.empRepo = empRepo;
    }

    /**
     * Gets the name of the supervisor of the given employee.
     *
     * @param name the name of the employee
     * @return the supervisor's name
     * @throws SupervisorRepositoryException thrown when an error occurs
     */
    public String getEmployeeSupervisor(PgPool client, String name) throws SupervisorRepositoryException {
        String selectQuery = "SELECT e.name FROM employeemgmt.supervisor s " +
                " LEFT OUTER JOIN employeemgmt.employee e ON s.id_supervisor = e.id  " +
                " WHERE s.id_employee = (SELECT id FROM employeemgmt.employee WHERE name = $1);";

        try {
            return client.preparedQuery(selectQuery)
                    .execute(Tuple.of(name))
                    .onItem().transform(pgRowSet -> pgRowSet.iterator().next().getString("name"))
                    .onItem().transform(val -> val == null ? NO_SUPERVISOR_FOUND : val)
                    .subscribeAsCompletionStage().get();
        } catch (Exception e) {
            Log.error(e);

            String message;
            if (e instanceof ExecutionException && e.getCause() instanceof NoSuchElementException) {
                message = "Employee not found: " + name;
            } else {
                message = "An error when trying to get the employee's supervisor: " + name;
            }

            throw new SupervisorRepositoryException(message);
        }
    }

    /**
     * Clears the supervisors table and then saves the new hierarchical structure
     *
     * @param conn An active transactional connection
     * @param tracker the name to {@link Node} map
     * @return the uni file to be used
     */
    public Uni<Void> saveEmployees(SqlConnection conn, Map<String, Node> tracker) {
        String insert = "INSERT INTO employeemgmt.supervisor (id_employee, id_supervisor) " +
                "VALUES ((SELECT id FROM employeemgmt.employee WHERE name=$1), (SELECT id FROM employeemgmt.employee WHERE name=$2)) RETURNING (id)";

        List<Uni<RowSet<Row>>> unis = new ArrayList<>();
        try {
            unis.add(clearAll(conn));

            // Saving each employee first
            for (String employee : tracker.keySet()) {
                unis.add(empRepo.saveEmployee(conn, employee));
            }

            // Preparing for batch input
            List<Tuple> employeeTuple = tracker.values().stream()
                    .map(v -> Tuple.of(v.getName(), v.getParent() != null ? v.getParent().getName() : null))
                    .collect(Collectors.toList());

            unis.add(conn.preparedQuery(insert)
                    .executeBatch(employeeTuple));
        } catch (Exception e) {
            Log.error(e);
            throw new SupervisorRepositoryException("An error when trying to save the employee-supervisor relationship");
        }

        return Uni.combine().all().unis(unis).discardItems();
    }

    /**
     * Clears out the supervisor table
     *
     * @param conn An active transactional connection
     * @return the uni file to be used
     */
    Uni<RowSet<Row>> clearAll(SqlConnection conn) {
        try {
            return conn.preparedQuery("delete from employeemgmt.supervisor").execute();
        } catch (Exception e) {
            throw new SupervisorRepositoryException("An error when trying to clear previous structure.");
        }
    }
}
