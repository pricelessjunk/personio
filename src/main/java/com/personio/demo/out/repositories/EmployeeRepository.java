package com.personio.demo.out.repositories;

import com.personio.demo.domain.Node;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Transactional
@ApplicationScoped
public class EmployeeRepository {
    PgPool client;

    @Inject
    public EmployeeRepository(PgPool client) {
        this.client = client;
    }

    public void saveEmployee(String name) throws EmployeeRepositoryException {
        try {
            client.preparedQuery("INSERT INTO employeemgmt.employee (name) VALUES ($1) ON CONFLICT DO NOTHING")
                    .execute(Tuple.of(name)).subscribeAsCompletionStage().get();
        } catch (InterruptedException | ExecutionException e) {
            Log.error(e);
            throw new EmployeeRepositoryException("An error when trying to save employee " + name);
        }
    }
}
