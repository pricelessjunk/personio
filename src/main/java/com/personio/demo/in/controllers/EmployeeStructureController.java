package com.personio.demo.in.controllers;

import com.personio.demo.domain.usecases.StructureUseCase;
import com.personio.demo.domain.usecases.employee.EmployeeUseCase;
import com.personio.demo.exceptions.CyclicStructureException;
import com.personio.demo.exceptions.MultipleRootSupervisorException;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;
import com.personio.demo.in.responses.SupervisorNameResponse;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

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
public class EmployeeStructureController {
    StructureUseCase structureUseCase;
    EmployeeUseCase employeeUseCase;

    @Inject
    public EmployeeStructureController(StructureUseCase structureUseCase, EmployeeUseCase employeeUseCase) {
        this.structureUseCase = structureUseCase;
        this.employeeUseCase = employeeUseCase;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response organize(Map<String, String> employees) throws CyclicStructureException, MultipleRootSupervisorException, EmployeeRepositoryException, SupervisorRepositoryException {
        JsonObject responseBody = structureUseCase.parseHierarchy(employees);
        return Response.ok(responseBody).build();
    }

    @GET
    @Path("/supervisor/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupervisor(@PathParam String name) throws SupervisorRepositoryException {
        String superVisorName =  employeeUseCase.getEmployeeSupervisor(name);
        return Response.ok(new SupervisorNameResponse(superVisorName)).build();
    }
}