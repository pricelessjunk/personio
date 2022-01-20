package com.personio.demo.domain.usecases;

import com.personio.demo.exceptions.CyclicStructureException;
import com.personio.demo.exceptions.MultipleRootSupervisorException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.HashMap;
import java.util.Map;

/**
 * This use case is responsible for parsing the given structure and generating the appropriate
 * Json structure
 */
@ApplicationScoped
public class StructureUseCase {

    StructureVerificationUsecase verificationUsecase;

    @Inject
    public StructureUseCase(StructureVerificationUsecase verificationUsecase) {
        this.verificationUsecase = verificationUsecase;
    }

    public JsonObject parseHierarchy(Map<String, String> inputStructure) throws MultipleRootSupervisorException, CyclicStructureException {
        Map<String, Node> tracker = new HashMap<>();

        inputStructure.forEach((key, value) -> {
            tracker.putIfAbsent(key, new Node(key));
            tracker.putIfAbsent(value, new Node(value));

            tracker.get(value).addChild(tracker.get(key));
        });

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
