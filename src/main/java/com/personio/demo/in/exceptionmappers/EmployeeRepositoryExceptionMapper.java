package com.personio.demo.in.exceptionmappers;

import com.personio.demo.in.responses.ErrorResponse;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for {@link EmployeeRepositoryException}
 */
@Provider
public class EmployeeRepositoryExceptionMapper implements ExceptionMapper<EmployeeRepositoryException> {
    @Override
    public Response toResponse(EmployeeRepositoryException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
    }
}
