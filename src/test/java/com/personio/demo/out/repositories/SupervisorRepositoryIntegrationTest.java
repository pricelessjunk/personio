package com.personio.demo.out.repositories;

import com.personio.demo.domain.entities.Node;
import com.personio.demo.domain.commons.StructureMapToNodeUtil;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;
import io.quarkus.test.junit.QuarkusTest;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static com.personio.demo.domain.commons.Constants.NO_SUPERVISOR_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class SupervisorRepositoryIntegrationTest {

    @Inject
    Flyway flyway;

    SupervisorRepository repo;
    StructureMapToNodeUtil util;

    @Inject
    public SupervisorRepositoryIntegrationTest(SupervisorRepository repo, StructureMapToNodeUtil util) {
        this.repo = repo;
        this.util = util;
    }

    @AfterEach
    void cleanup() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testSave() throws SupervisorRepositoryException, EmployeeRepositoryException {
        Map<String, Node> input = prepareStructure();

        repo.saveEmployees(input);

        String supervisor = repo.getEmployeeSupervisor("Barbara");
        assertThat(supervisor).isEqualTo("Nick");

        supervisor = repo.getEmployeeSupervisor("Nick");
        assertThat(supervisor).isEqualTo("Sophie");

        supervisor = repo.getEmployeeSupervisor("Sophie");
        assertThat(supervisor).isEqualTo(NO_SUPERVISOR_FOUND);
    }

    @Test
    void testClearAll() throws SupervisorRepositoryException, EmployeeRepositoryException {
        Map<String, Node> input = prepareStructure();

        repo.saveEmployees(input);
        repo.clearAll();

        SupervisorRepositoryException ex = assertThrows(SupervisorRepositoryException.class, () -> repo.getEmployeeSupervisor("Barbara"),
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