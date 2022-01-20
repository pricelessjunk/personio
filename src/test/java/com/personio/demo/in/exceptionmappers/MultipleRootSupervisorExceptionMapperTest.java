package com.personio.demo.in.exceptionmappers;

import com.personio.demo.exceptions.MultipleRootSupervisorException;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

class MultipleRootSupervisorExceptionMapperTest {

    private final MultipleRootSupervisorExceptionMapper exceptionMapper;

    MultipleRootSupervisorExceptionMapperTest() {
        this.exceptionMapper = new MultipleRootSupervisorExceptionMapper();
    }

    @Test
    void toResponse() {
        MultipleRootSupervisorException expectedException = new MultipleRootSupervisorException("Expected exception");
        Response response = exceptionMapper.toResponse(expectedException);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        assertThat(response.getEntity()).hasToString("Expected exception");
    }
}