package com.personio.demo.out.repositories;

import com.personio.demo.domain.entities.Node;
import com.personio.demo.domain.commons.StructureMapToNodeUtil;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.mutiny.pgclient.PgPool;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.personio.demo.domain.commons.Constants.NO_SUPERVISOR_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class SupervisorRepositoryIntegrationTest {

    @Inject
    Flyway flyway;

    @Inject
    PgPool client;

    @Inject
    SupervisorRepository repo;

    @Inject
    StructureMapToNodeUtil util;

    @AfterEach
    void cleanup() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testSave() throws SupervisorRepositoryException, EmployeeRepositoryException, ExecutionException, InterruptedException {
        Map<String, Node> input = prepareStructure();

        client.withTransaction(conn -> repo.saveEmployees(conn, input)).subscribeAsCompletionStage().get();

        String supervisor = repo.getEmployeeSupervisor(client, "Barbara");
        assertThat(supervisor).isEqualTo("Nick");

        supervisor = repo.getEmployeeSupervisor(client, "Nick");
        assertThat(supervisor).isEqualTo("Sophie");

        supervisor = repo.getEmployeeSupervisor(client, "Sophie");
        assertThat(supervisor).isEqualTo(NO_SUPERVISOR_FOUND);
    }

    @Test
    void testClearAll() throws ExecutionException, InterruptedException {
        Map<String, Node> input = prepareStructure();

        // Populating the db
        client.withTransaction(conn -> repo.saveEmployees(conn, input)).subscribeAsCompletionStage().get();

        // Clearing
        client.withTransaction(conn -> repo.clearAll(conn)).subscribeAsCompletionStage().get();

        SupervisorRepositoryException ex = assertThrows(SupervisorRepositoryException.class, () -> repo.getEmployeeSupervisor(client, "Barbara"),
                "An exception is expected to be thrown");
        assertThat(ex.getMessage()).contains("Employee not found");
    }

    private Map<String, Node> prepareStructure() {
        Map<String, String> inputStructure = new HashMap<>();
        inputStructure.put("Barbara", "Nick");
        inputStructure.put("Nick", "Sophie");
        return util.mapToNode(inputStructure);

    }
}