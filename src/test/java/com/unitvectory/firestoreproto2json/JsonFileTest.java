/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.unitvectory.firestoreproto2json;

import org.junit.jupiter.params.ParameterizedTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.InvalidProtocolBufferException;
import com.unitvectory.fileparamunit.ListFileSource;
import com.unitvectory.jsonparamunit.JsonNodeParamUnit;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Parameterized Tests for FirestoreProto2Json to test various input payloads.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class JsonFileTest extends JsonNodeParamUnit {

    private final ObjectMapper mapper = new ObjectMapper();

    @ParameterizedTest
    @ListFileSource(resources = "/tests/", fileExtension = ".json")
    void validateJSONFile(String fileName) {
        run(fileName);
    }

    @Override
    protected JsonNode process(JsonNode input, String context) {

        String protocolBuffer = input.get("protocolBuffer").asText();

        try {
            ObjectNode output = mapper.createObjectNode();

            String valueString = FirestoreProto2Json.DEFAULT.valueToJsonString(protocolBuffer);
            if (valueString != null) {
                JsonNode value = mapper.readTree(valueString);
                output.set("value", value);
            }

            String oldValueString =
                    FirestoreProto2Json.DEFAULT.oldValueToJsonString(protocolBuffer);
            if (oldValueString != null) {
                JsonNode oldValue = mapper.readTree(oldValueString);
                output.set("oldValue", oldValue);
            }

            return output;
        } catch (InvalidProtocolBufferException e) {
            fail("Failed to parse protocol buffer.", e);
        } catch (JsonProcessingException e) {
            fail("Failed to parse JSON.", e);
        }

        return null;
    }
}

