package com.personio.demo.domain.helper;

import com.personio.demo.domain.usecases.Node;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.JsonString;
import java.util.HashMap;
import java.util.Map;

/**
 * A helper utility class to change json object into a readable tree
 */
@ApplicationScoped
public class StructureMapToNodeUtil {

    public Map<String, Node> mapToNode(Map<String, String> inputStructure) {
        Map<String, Node> tracker = new HashMap<>();

        inputStructure.forEach((key, value) -> {
            tracker.putIfAbsent(key, new Node(key));
            tracker.putIfAbsent(value, new Node(value));

            tracker.get(value).addChild(tracker.get(key));
        });

        return tracker;
    }
}
