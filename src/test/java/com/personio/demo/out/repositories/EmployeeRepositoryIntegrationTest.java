package com.personio.demo.out.repositories;

import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class EmployeeRepositoryIntegrationTest {

    @Inject
    Flyway flyway;

    @Inject
    PgPool client;

    @Inject
    EmployeeRepository repo;

    @AfterEach
    void cleanup() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testSave() throws EmployeeRepositoryException, ExecutionException, InterruptedException {
        String name = "Test user";
        client.withTransaction(conn -> repo.saveEmployee(conn, name)).subscribeAsCompletionStage().get();
        String savedName = repo.getById(client,1L);
        assertThat(savedName).isEqualTo(name);
    }
}