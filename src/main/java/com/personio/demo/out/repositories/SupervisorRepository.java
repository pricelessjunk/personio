package com.personio.demo.out.repositories;

import com.personio.demo.domain.Node;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;
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
public class SupervisorRepository {

    PgPool client;
    EmployeeRepository empRepo;

    @Inject
    public SupervisorRepository(EmployeeRepository empRepo, PgPool client) {
        this.empRepo = empRepo;
        this.client = client;
    }

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
