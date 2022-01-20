package com.personio.demo.domain.usecases;

import com.personio.demo.exceptions.CyclicStructureException;
import com.personio.demo.exceptions.MultipleRootSupervisorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.JsonObject;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StructureUseCaseTest {

    private StructureUseCase useCase;

    @BeforeEach
    void init() {
        StructureVerificationUsecase mockStructureVerificationUsecase = mock(StructureVerificationUsecase.class);
        this.useCase = new StructureUseCase(mockStructureVerificationUsecase);
    }

    @Test
    void testParseHierarchy() throws MultipleRootSupervisorException, CyclicStructureException {
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
        assertThat(peteJson.size()).isZero();

        JsonObject barbaraJson = nickJson.get("Barbara").asJsonObject();
        assertThat(barbaraJson.size()).isZero();
    }
}