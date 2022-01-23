package com.personio.demo.in.controllers;

import com.personio.demo.domain.usecases.structure.StructureUseCase;
import com.personio.demo.domain.usecases.employee.EmployeeUseCase;
import com.personio.demo.domain.exceptions.CyclicStructureException;
import com.personio.demo.domain.exceptions.MultipleRootSupervisorException;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;
import com.personio.demo.in.dto.SupervisorNameResponseData;
import io.vertx.mutiny.pgclient.PgPool;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/employee")
@RolesAllowed("Admin")
public class EmployeeStructureController {
    StructureUseCase structureUseCase;
    EmployeeUseCase employeeUseCase;
    PgPool client;

    /**
     * Constructor
     *
     * @param structureUseCase the use case for structures
     * @param employeeUseCase the use case for employees
     */
    @Inject
    public EmployeeStructureController(StructureUseCase structureUseCase, EmployeeUseCase employeeUseCase, PgPool client) {
        this.structureUseCase = structureUseCase;
        this.employeeUseCase = employeeUseCase;
        this.client = client;
    }

    /**
     * This method is the endpoint to recieve the dictionary
     *
     * @param employees the dictionary
     * @return a response with the new hierarchical structure
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response organize(Map<String, String> employees) {
        JsonObject responseBody = structureUseCase.parseHierarchy(client, employees);
        return Response.ok(responseBody).build();
    }

    /**
     * This endpoint allows to retrieve the supervisor of the given employee
     *
     * @param name the name of the employee
     * @return a response with the supervisor's name and his/her supervisor's name
     * @throws SupervisorRepositoryException thrown when an error occurred in the supervisor repository
     */
    @GET
    @Path("/supervisor/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupervisor(@PathParam String name) throws SupervisorRepositoryException {
        SupervisorNameResponseData response = employeeUseCase.getEmployeeSupervisors(client, name);
        return Response.ok(response).build();
    }
}