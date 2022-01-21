package com.personio.demo.domain.usecases.employee;

import com.personio.demo.domain.Node;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;
import com.personio.demo.out.repositories.EmployeeRepository;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import com.personio.demo.out.repositories.SupervisorRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;

@ApplicationScoped
public class EmployeeUseCase {
    SupervisorRepository supervisorRepo;
    EmployeeRepository empRepo;

    @Inject
    public EmployeeUseCase(SupervisorRepository supervisorRepo, EmployeeRepository empRepo) {
        this.supervisorRepo = supervisorRepo;
        this.empRepo = empRepo;
    }

    public void saveEmployees(Map<String, Node> tracker) throws EmployeeRepositoryException, SupervisorRepositoryException {
        this.supervisorRepo.saveEmployees(tracker);
    }

    public Uni<String> getEmployeeSupervisor(String name) {
        return null;//this.supervisorRepo.getEmployeeSupervisor(name);
    }
}
