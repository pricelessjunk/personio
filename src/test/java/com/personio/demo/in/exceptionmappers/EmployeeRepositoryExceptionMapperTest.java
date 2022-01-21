package com.personio.demo.in.exceptionmappers;

import com.personio.demo.in.responses.ErrorResponse;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeRepositoryExceptionMapperTest {

    private final EmployeeRepositoryExceptionMapper exceptionMapper;

    EmployeeRepositoryExceptionMapperTest() {
        this.exceptionMapper = new EmployeeRepositoryExceptionMapper();
    }

    @Test
    void toResponse() {
        EmployeeRepositoryException expectedException = new EmployeeRepositoryException("Expected exception");
        Response response = exceptionMapper.toResponse(expectedException);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        assertThat(((ErrorResponse)response.getEntity()).getMessage()).hasToString("Expected exception");
    }
}