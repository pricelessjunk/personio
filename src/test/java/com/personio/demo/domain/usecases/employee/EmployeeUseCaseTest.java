package com.personio.demo.domain.usecases.employee;

import com.personio.demo.in.dto.SupervisorNameResponseData;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;
import com.personio.demo.out.repositories.EmployeeRepository;
import com.personio.demo.out.repositories.SupervisorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.personio.demo.domain.commons.Constants.NO_SUPERVISOR_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmployeeUseCaseTest {

    EmployeeUseCase useCase;
    SupervisorRepository supRepo;

    @BeforeEach
    void init() {
        supRepo = mock(SupervisorRepository.class);
        this.useCase = new EmployeeUseCase(supRepo, mock(EmployeeRepository.class));
    }

    @Test
    void getEmployeeSupervisors_allSupervisorsExist() throws SupervisorRepositoryException {
        when(supRepo.getEmployeeSupervisor(anyString())).thenReturn("level2").thenReturn("level1");
        SupervisorNameResponseData response = useCase.getEmployeeSupervisors("level3");

        assertThat(response.getSupervisor()).isEqualTo("level2");
        assertThat(response.getLevel2Supervisor()).isEqualTo("level1");
    }

    @Test
    void getEmployeeSupervisors_onlyOneSupervisorExists() throws SupervisorRepositoryException {
        when(supRepo.getEmployeeSupervisor(anyString())).thenReturn("level1").thenReturn(NO_SUPERVISOR_FOUND);
        SupervisorNameResponseData response = useCase.getEmployeeSupervisors("level2");

        assertThat(response.getSupervisor()).isEqualTo("level1");
        assertThat(response.getLevel2Supervisor()).isEqualTo(NO_SUPERVISOR_FOUND);
    }

    @Test
    void getEmployeeSupervisors_NoSupervisorsExist() throws SupervisorRepositoryException {
        when(supRepo.getEmployeeSupervisor(anyString())).thenReturn(NO_SUPERVISOR_FOUND).thenReturn(NO_SUPERVISOR_FOUND);
        SupervisorNameResponseData response = useCase.getEmployeeSupervisors("level1");

        assertThat(response.getSupervisor()).isEqualTo(NO_SUPERVISOR_FOUND);
        assertThat(response.getLevel2Supervisor()).isEqualTo(NO_SUPERVISOR_FOUND);
    }

    @Test
    void getEmployeeSupervisors_ErrorWrongEmployee() throws SupervisorRepositoryException {
        when(supRepo.getEmployeeSupervisor(anyString())).thenThrow(new SupervisorRepositoryException("No employee"));
        assertThrows(SupervisorRepositoryException.class, () -> useCase.getEmployeeSupervisors("level1"),
                "An exception is expected to be thrown");
    }
}