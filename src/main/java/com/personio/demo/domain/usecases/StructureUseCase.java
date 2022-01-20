package com.personio.demo.domain.usecases;

import com.personio.demo.domain.helper.StructureMapToNodeUtil;
import com.personio.demo.exceptions.CyclicStructureException;
import com.personio.demo.exceptions.MultipleRootSupervisorException;

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
    StructureMapToNodeUtil util;

    @Inject
    public StructureUseCase(StructureVerificationUsecase verificationUsecase, StructureMapToNodeUtil util) {
        this.verificationUsecase = verificationUsecase;
        this.util = util;
    }

    /**
     * Parse the hierarchy and generate the json structure.
     *
     * @param inputStructure the input dictionary
     * @return Returns the parsed json object
     * @throws MultipleRootSupervisorException thrown when more than one root supervisors are detected
     * @throws CyclicStructureException thrown when a cycle structure is detected
     */
    public JsonObject parseHierarchy(Map<String, String> inputStructure) throws MultipleRootSupervisorException, CyclicStructureException {
        Map<String, Node> tracker = util.mapToNode(inputStructure);

        verificationUsecase.verifyMultipleRoot(tracker);
        verificationUsecase.verifyCyclicReference(tracker);

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
