package com.personio.demo.in.exceptionmappers;

import com.personio.demo.domain.exceptions.CyclicStructureException;
import com.personio.demo.in.responses.ErrorResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

class CyclicStructureExceptionMapperTest {

    private final CyclicStructureExceptionMapper exceptionMapper;

    CyclicStructureExceptionMapperTest() {
        this.exceptionMapper = new CyclicStructureExceptionMapper();
    }

    @Test
    void toResponse() {
        CyclicStructureException expectedException = new CyclicStructureException("Expected exception");
        Response response = exceptionMapper.toResponse(expectedException);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        assertThat(((ErrorResponse)response.getEntity()).getMessage()).hasToString("Expected exception");
    }
}