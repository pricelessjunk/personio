package com.personio.demo.domain.helper;

import com.personio.demo.domain.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.enterprise.context.RequestScoped;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RequestScoped
class StructureMapToNodeUtilTest {

    StructureMapToNodeUtil util;

    @BeforeEach
    void init() {
        util = new StructureMapToNodeUtil();
    }

    @Test
    void testMapToNode() {
        Map<String, String> inputStructure = new HashMap<>();
        inputStructure.put("Pete", "Nick");
        inputStructure.put("Nick", "Sophie");

        Map<String, Node> tracker = util.mapToNode(inputStructure);
        assertThat(tracker.get("Pete").getParent().getName()).isEqualTo("Nick");
        assertThat(tracker.get("Nick").getParent().getName()).isEqualTo("Sophie");
        assertThat(tracker.get("Sophie").isRoot()).isTrue();
    }
}