package com.personio.demo.in.exceptionmappers;

import com.personio.demo.in.dto.ErrorResponseData;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for {@link SupervisorRepositoryException}
 */
@Provider
public class SupervisorRepositoryExceptionMapper implements ExceptionMapper<SupervisorRepositoryException> {

    /**
     * Returns the response when the exception occurs
     *
     * @param e the exception
     * @return the response with the exception message wrapped in a class
     */
    @Override
    public Response toResponse(SupervisorRepositoryException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponseData(e.getMessage()))
                .build();
    }
}
