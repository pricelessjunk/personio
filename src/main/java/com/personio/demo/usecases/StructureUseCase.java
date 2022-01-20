package com.personio.demo.usecases;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class StructureUseCase {

    public JsonObject parseHierarchy(Map<String, String> inputStructure){
        Map<String, Node> tracker = new HashMap<>();

        inputStructure.forEach((key, value) -> {
            tracker.putIfAbsent(key, new Node(key));
            tracker.putIfAbsent(value, new Node(value));

            tracker.get(value).addChild(tracker.get(key));
        });

        List<Node> topMostNodes = tracker.values().stream().filter(node -> node.isRoot()).collect(Collectors.toList());

        return Json.createObjectBuilder().add(topMostNodes.get(0).getName(), generateStructure(topMostNodes.get(0))).build();
    }

    private void verify(List<Node> topMostNode) {

    }

    private JsonObject generateStructure(Node node) {
        JsonObjectBuilder json = Json.createObjectBuilder();

        for(Node child: node.getChildren()) {
            json.add(child.getName(), generateStructure(child));
        }

        return json.build();
    }
}
