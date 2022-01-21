package com.personio.demo.out.repositories;

import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import io.quarkus.logging.Log;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class EmployeeRepository {
    PgPool client;

    @Inject
    public EmployeeRepository(PgPool client) {
        this.client = client;
    }

    public String getById(Long id) throws EmployeeRepositoryException {
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

    public void saveEmployee(String name) throws EmployeeRepositoryException {
        try {
            client.preparedQuery("INSERT INTO employeemgmt.employee (name) VALUES ($1) ON CONFLICT DO NOTHING")
                    .execute(Tuple.of(name))
                    .subscribeAsCompletionStage().get();
        } catch (InterruptedException | ExecutionException e) {
            Log.error(e);
            throw new EmployeeRepositoryException("An error when trying to save employee " + name);
        }
    }
}
