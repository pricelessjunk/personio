package com.personio.demo.domain.usecases.employee;

import com.personio.demo.domain.entities.Node;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;
import com.personio.demo.out.repositories.EmployeeRepository;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import com.personio.demo.out.repositories.SupervisorRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

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
     * @param tracker the name to {@link Node} map
     * @throws EmployeeRepositoryException thrown when an error occurs in the {@link EmployeeRepository}
     * @throws SupervisorRepositoryException thrown when an error occurs in the {@link SupervisorRepository}
     */
    public void saveEmployees(Map<String, Node> tracker) throws EmployeeRepositoryException, SupervisorRepositoryException {
        this.supervisorRepo.saveEmployees(tracker);
    }

    /**
     * Gets the name of the supervisor of the given employee.
     *
     * @param name the name of the employee
     * @return the supervisor's name
     * @throws SupervisorRepositoryException thrown when an error occurs in the {@link SupervisorRepository}
     */
    public String getEmployeeSupervisor(String name) throws SupervisorRepositoryException {
        return this.supervisorRepo.getEmployeeSupervisor(name);
    }
}
