package com.personio.demo.out.repositories;

import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class EmployeeRepositoryIntegrationTest {

    @Inject
    Flyway flyway;

    EmployeeRepository repo;

    @Inject
    public EmployeeRepositoryIntegrationTest(EmployeeRepository repo) {
        this.repo = repo;
    }

    @AfterEach
    void cleanup() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testSave() throws EmployeeRepositoryException {
        String name = "Test user";
        repo.saveEmployee(name);
        String savedName = repo.getById(1L);
        assertThat(savedName).isEqualTo(name);
    }
}