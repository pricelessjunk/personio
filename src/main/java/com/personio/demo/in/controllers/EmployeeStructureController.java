package com.personio.demo.in.controllers;

import com.personio.demo.domain.usecases.StructureUseCase;
import com.personio.demo.exceptions.CyclicStructureException;
import com.personio.demo.exceptions.MultipleRootSupervisorException;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("/employee")
public class EmployeeStructureController {
//
//    @GET
//    @Produces(MediaType.TEXT_PLAIN)
//    public String hello() {
//        return "Hello RESTEasy";
//    }

//    @GET
//    @Path("/employees")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Map<String, Object> data() {
//        Map<String, Object> m = new HashMap<>();
//        m.put("Pete", new HashMap<String, Object>());
//        m.put("Barbara", new HashMap<String, Object>());
//
//        Map<String, Object> m2 = new HashMap<>();
//        m2.put("Nick", m);
//
//        return m2;
//    }

    StructureUseCase structureUseCase;

    @Inject
    public EmployeeStructureController(StructureUseCase structureUseCase) {
        this.structureUseCase = structureUseCase;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response organize(Map<String, String> employees) throws CyclicStructureException, MultipleRootSupervisorException {
        JsonObject responseBody = structureUseCase.parseHierarchy(employees);
        return Response.ok(responseBody).build();
    }
}