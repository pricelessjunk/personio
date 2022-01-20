package com.personio.demo.usecases;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StructureUseCaseTest {

    private StructureUseCase useCase;

    @BeforeEach
    void init() {
        this.useCase = new StructureUseCase();
    }

    @Test
    void parseHierarchy() {
        Map<String, String> input = new HashMap<>();
        input.put("Pete", "Nick");
        input.put("Barbara", "Nick");
        input.put("Nick", "Sophie");
        input.put("Sophie", "Jonas");

        JsonObject parentJson = useCase.parseHierarchy(input);


        JsonObject jonasJson = parentJson.get("Jonas").asJsonObject();
        assertThat(jonasJson.size()).isEqualTo(1);

        JsonObject sophieJson = jonasJson.get("Sophie").asJsonObject();
        assertThat(sophieJson.size()).isEqualTo(1);

        JsonObject nickJson = sophieJson.get("Nick").asJsonObject();
        assertThat(nickJson.size()).isEqualTo(2);

        JsonObject peteJson = nickJson.get("Pete").asJsonObject();
        assertThat(peteJson.size()).isEqualTo(0);

        JsonObject barbaraJson = nickJson.get("Barbara").asJsonObject();
        assertThat(barbaraJson.size()).isEqualTo(0);
    }
}