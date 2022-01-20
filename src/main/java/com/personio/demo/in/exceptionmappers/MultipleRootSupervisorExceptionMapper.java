package com.personio.demo.in.exceptionmappers;

import com.personio.demo.exceptions.MultipleRootSupervisorException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for {@link MultipleRootSupervisorException}
 */
@Provider
public class MultipleRootSupervisorExceptionMapper implements ExceptionMapper<MultipleRootSupervisorException> {

    @Override
    public Response toResponse(MultipleRootSupervisorException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(e.getMessage())
                .build();
    }
}
