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
import org.junit.jupiter.params.provider.MethodSource;
import org.skyscreamer.jsonassert.JSONAssert;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Parameterized Tests for FirestoreProto2Json to test various input payloads.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class JsonFileTest {

    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    @ParameterizedTest
    @MethodSource("provideFileNames")
    void validateJSONFile(String fileName) {
        validate(fileName);
    }

    private static Stream<String> provideFileNames() throws IOException {
        Path path = Paths.get("src/test/resources/tests");

        // Ensure the path is correct and the directory exists.
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            return Stream.empty();
        }

        // Get all of the files
        return Files.walk(path, 1).filter(Files::isRegularFile).map(path::relativize)
                .map(Path::toString);
    }

    private void validate(String fileName) {
        try {
            // Read in the file content
            Path filePath = Paths.get("src/test/resources/tests", fileName);
            String content = Files.readString(filePath);
            JsonElement rootElement = JsonParser.parseString(content);
            if (!rootElement.isJsonObject()) {
                fail("The root of the JSON document is not an object in " + fileName);
            }

            JsonObject rootObject = rootElement.getAsJsonObject();

            // Extract protobuf as a String
            String protobuf =
                    rootObject.has("protobuf") ? rootObject.get("protobuf").getAsString() : "";
            if (protobuf.isEmpty()) {
                fail("Protobuf field is missing or null in " + fileName);
            }

            // Process the value
            String expectedValue =
                    rootObject.has("value") ? GSON.toJson(rootObject.get("value")) : null;
            String actualValue = FirestoreProto2Json.DEFAULT.valueToJsonString(protobuf);
            JSONAssert.assertEquals("Value did not match.\nExpected: " + expectedValue
                    + "\nActual: " + actualValue + "\n", expectedValue, actualValue, true);

            // Process the old value
            String expectedOldValue =
                    rootObject.has("oldValue") ? GSON.toJson(rootObject.get("oldValue")) : null;
            String actualOldValue = FirestoreProto2Json.DEFAULT.oldValueToJsonString(protobuf);
            JSONAssert.assertEquals("Old value did not match.\nExpected: " + expectedOldValue
                    + "\nActual: " + actualOldValue + "\n", expectedOldValue, actualOldValue, true);


        } catch (Exception e) {
            fail("Unexpected Exception", e);
        }
    }
}

