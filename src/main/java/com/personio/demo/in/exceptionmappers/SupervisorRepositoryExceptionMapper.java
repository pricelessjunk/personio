package com.personio.demo.in.exceptionmappers;

import com.personio.demo.in.responses.ErrorResponse;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for {@link SupervisorRepositoryException}
 */
@Provider
public class SupervisorRepositoryExceptionMapper implements ExceptionMapper<SupervisorRepositoryException> {
    @Override
    public Response toResponse(SupervisorRepositoryException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
    }
}
