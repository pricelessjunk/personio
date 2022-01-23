package com.personio.demo.out.repositories;

import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.SqlConnection;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

/**
 * The employee repository class
 */
@ApplicationScoped
public class EmployeeRepository {

    /**
     * Gets the name of an employee from its id
     *
     * @param client the reactive database client
     * @param id the id
     * @return the name
     */
    public String getById(PgPool client, Long id) {
        try {
            return client.preparedQuery("SELECT name FROM employeemgmt.employee WHERE id = $1 ")
                    .execute(Tuple.of(id))
                    .onItem().transform(pgRowSet -> pgRowSet.iterator().next().getString("name"))
                    .subscribeAsCompletionStage().get();
        } catch (Exception e) {
            Log.error(e);

            String message;
            if(e instanceof ExecutionException && e.getCause() instanceof NoSuchElementException) {
                message = "Employee not found for id " + id;
            } else {
                message = "An error when trying to get employee " + id;
            }

            throw new EmployeeRepositoryException(message);
        }
    }

    /**
     * Saves an employee by its name
     *
     * @param conn An active transactional connection
     * @param name the name
     * @return the uni file to be used
     */
    public Uni<RowSet<Row>> saveEmployee(SqlConnection conn, String name) {
        try {
            return conn.preparedQuery("INSERT INTO employeemgmt.employee (name) VALUES ($1) ON CONFLICT DO NOTHING").execute(Tuple.of(name));
        } catch (Exception e) {
            Log.error(e);
            throw new EmployeeRepositoryException("An error when trying to save employee " + name);
        }
    }
}
