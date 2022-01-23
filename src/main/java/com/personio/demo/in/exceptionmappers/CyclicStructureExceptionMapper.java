package com.personio.demo.in.exceptionmappers;

import com.personio.demo.domain.exceptions.CyclicStructureException;
import com.personio.demo.in.dto.ErrorResponseData;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Exception mapper for {@link CyclicStructureException}
 */
@Provider
public class CyclicStructureExceptionMapper implements ExceptionMapper<CyclicStructureException> {

    /**
     * Returns the response when the exception occurs
     *
     * @param e the exception
     * @return the response with the exception message wrapped in a class
     */
    @Override
    public Response toResponse(CyclicStructureException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponseData(e.getMessage()))
                .build();
    }
}
