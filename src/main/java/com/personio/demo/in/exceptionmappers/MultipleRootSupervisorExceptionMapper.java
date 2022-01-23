package com.personio.demo.in.exceptionmappers;

import com.personio.demo.domain.exceptions.MultipleRootSupervisorException;
import com.personio.demo.in.dto.ErrorResponseData;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for {@link MultipleRootSupervisorException}
 */
@Provider
public class MultipleRootSupervisorExceptionMapper implements ExceptionMapper<MultipleRootSupervisorException> {

    /**
     * Returns the response when the exception occurs
     *
     * @param e the exception
     * @return the response with the exception message wrapped in a class
     */
    @Override
    public Response toResponse(MultipleRootSupervisorException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponseData(e.getMessage()))
                .build();
    }
}
