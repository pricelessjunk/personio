package com.personio.demo.domain.usecases.structure;

import com.personio.demo.domain.entities.Node;
import com.personio.demo.domain.commons.StructureMapToNodeUtil;
import com.personio.demo.domain.usecases.employee.EmployeeUseCase;
import com.personio.demo.domain.exceptions.CyclicStructureException;
import com.personio.demo.domain.exceptions.MultipleRootSupervisorException;
import com.personio.demo.out.exceptions.EmployeeRepositoryException;
import com.personio.demo.out.exceptions.SupervisorRepositoryException;
import io.vertx.mutiny.pgclient.PgPool;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Map;

/**
 * This use case is responsible for parsing the given structure and generating the appropriate
 * Json structure
 */
@ApplicationScoped
public class StructureUseCase {

    StructureVerificationUsecase verificationUsecase;
    EmployeeUseCase employeeUseCase;
    StructureMapToNodeUtil util;

    /**
     * Constructor
     *
     * @param verificationUsecase the use case for verification of the structure
     * @param employeeUseCase the use case to process the employee related jobs
     * @param util a helper util class
     */
    @Inject
    public StructureUseCase(StructureVerificationUsecase verificationUsecase, EmployeeUseCase employeeUseCase,  StructureMapToNodeUtil util) {
        this.verificationUsecase = verificationUsecase;
        this.employeeUseCase = employeeUseCase;
        this.util = util;
    }

    /**
     * Parse the hierarchy and generate the json structure.
     *
     * @param client the reactive database client
     * @param inputStructure the input dictionary
     * @return Returns the parsed json object
     */
    public JsonObject parseHierarchy(PgPool client, Map<String, String> inputStructure)  {
        Map<String, Node> tracker = util.mapToNode(inputStructure);

        verificationUsecase.verifyMultipleRoot(tracker);
        verificationUsecase.verifyCyclicReference(tracker);

        employeeUseCase.saveEmployees(client, tracker);

        Node topMostNode = tracker.values().stream().filter(Node::isRoot).findFirst().get();
        return Json.createObjectBuilder().add(topMostNode.getName(), this.generateJsonStructure(topMostNode)).build();
    }

    /**
     * The recursive method that traverses through the tree and generates the json structure
     *
     * @param node A single node in the tree
     * @return The generated json object
     */
    private JsonObject generateJsonStructure(Node node) {
        JsonObjectBuilder json = Json.createObjectBuilder();

        for(Node child: node.getChildren()) {
            json.add(child.getName(), generateJsonStructure(child));
        }

        return json.build();
    }
}
