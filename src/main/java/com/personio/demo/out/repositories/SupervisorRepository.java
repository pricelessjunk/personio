package com.personio.demo.out.repositories;

import com.personio.demo.domain.entities.Node;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;
import io.quarkus.logging.Log;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
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

    PgPool client;
    EmployeeRepository empRepo;

    /**
     * Constructor
     *
     * @param empRepo the employee repository
     * @param client the pg pool reactive client
     */
    @Inject
    public SupervisorRepository(EmployeeRepository empRepo, PgPool client) {
        this.empRepo = empRepo;
        this.client = client;
    }

    /**
     * Gets the name of the supervisor of the given employee.
     *
     * @param name the name of the employee
     * @return the supervisor's name
     * @throws SupervisorRepositoryException thrown when an error occurs
     */
    public String getEmployeeSupervisor(String name) throws SupervisorRepositoryException {
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
            if(e instanceof ExecutionException && e.getCause() instanceof NoSuchElementException) {
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
     * @param tracker the name to {@link Node} map
     * @throws EmployeeRepositoryException thrown when an error occurs in the {@link EmployeeRepository}
     * @throws SupervisorRepositoryException thrown when an error occurs
     */
    public void saveEmployees(Map<String, Node> tracker) throws EmployeeRepositoryException, SupervisorRepositoryException {
        String insert = "INSERT INTO employeemgmt.supervisor (id_employee, id_supervisor) " +
                "VALUES ((SELECT id FROM employeemgmt.employee WHERE name=$1), (SELECT id FROM employeemgmt.employee WHERE name=$2)) RETURNING (id)";

        try {
            clearAll();

            // Saving each employee first
            for(String employee: tracker.keySet()) {
                empRepo.saveEmployee(employee);
            }

            // Preparing for batch input
            List<Tuple> employeeTuple = tracker.values().stream()
                    .map(v -> Tuple.of(v.getName(), v.getParent() != null ? v.getParent().getName() : null))
                    .collect(Collectors.toList());

            client.preparedQuery(insert)
                    .executeBatch(employeeTuple)
                    .subscribeAsCompletionStage().get();
        }  catch (EmployeeRepositoryException | SupervisorRepositoryException e) {
            throw e;
        } catch (Exception e) {
            throw new SupervisorRepositoryException("An error when trying to save the employee-supervisor relationship");
        }
    }

    /**
     * Clears out the supervisor table
     *
     * @throws SupervisorRepositoryException thrown when an error occurs
     */
    void clearAll() throws SupervisorRepositoryException {
        try {
            client.preparedQuery("delete from employeemgmt.supervisor")
                    .execute()
                    .subscribeAsCompletionStage().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new SupervisorRepositoryException("An error when trying to clear previous structure.");
        }
    }
}
