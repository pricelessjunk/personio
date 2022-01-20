package com.personio.demo.controllers;

import com.personio.demo.usecases.StructureUseCase;

import javax.inject.Inject;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
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
    public Response organize(Map<String, String> employees) {
        return Response.ok(structureUseCase.parseHierarchy(employees)).build();
    }
}