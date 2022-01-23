package com.personio.demo.domain.usecases.employee;

import com.personio.demo.domain.entities.Node;
import com.personio.demo.in.dto.SupervisorNameResponseData;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;
import com.personio.demo.out.repositories.EmployeeRepository;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import com.personio.demo.out.repositories.SupervisorRepository;
import io.vertx.mutiny.pgclient.PgPool;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.personio.demo.domain.commons.Constants.NO_SUPERVISOR_FOUND;

/**
 * This usecase deals with all employee related operations
 */
@ApplicationScoped
public class EmployeeUseCase {
    SupervisorRepository supervisorRepo;
    EmployeeRepository empRepo;

    /**
     * Constructor
     *
     * @param supervisorRepo the supervisor repository
     * @param empRepo the employee repository
     */
    @Inject
    public EmployeeUseCase(SupervisorRepository supervisorRepo, EmployeeRepository empRepo) {
        this.supervisorRepo = supervisorRepo;
        this.empRepo = empRepo;
    }

    /**
     * This method is responsible for saving the name to {@link Node} map.
     *
     * @param client the reactive database client
     * @param tracker the name to {@link Node} map
     */
    public void saveEmployees(PgPool client, Map<String, Node> tracker) {
        try {
            client.withTransaction(conn -> this.supervisorRepo.saveEmployees(conn, tracker))
                    .subscribeAsCompletionStage().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new SupervisorRepositoryException("An error occured while trying to save the employees");
        }
    }

    /**
     * Gets the name of the supervisor and his/her supervisor of the given employee. If the employee is a top level employee or a
     * 2nd level employee, a message is sent saying no supervisor is found.
     *
     * @param client the reactive client
     * @param name the name of the employee
     * @return a response with the supervisor's name and his supervisors name.
     */
    public SupervisorNameResponseData getEmployeeSupervisors(PgPool client, String name) {
        String supervisor =  this.supervisorRepo.getEmployeeSupervisor(client, name);
        String level2Supervisor = NO_SUPERVISOR_FOUND.equals(supervisor) ? NO_SUPERVISOR_FOUND : this.supervisorRepo.getEmployeeSupervisor(client, supervisor);

        return SupervisorNameResponseData.of(supervisor, level2Supervisor);
    }
}
