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

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Custom Timestamp Test
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class CustomTimestampTest {

        private static final String timestampUpdate =
                        "CpgBClhwcm9qZWN0cy9maXJlc3RvcmVwcm90bzJqc29uL2RhdGFiYXNlcy8oZGVmYXVsdCkvZG9jdW1lbnRzL3Rlc3RkYXRhL3hJb1JwNm9keU41UjhpSGM3N2trEiAKDnRpbWVzdGFtcEZpZWxkEg5SDAjfuPSuBhCAtJKNARoMCOy49K4GEJijmrUBIgwI7Lj0rgYQmKOatQE=";

        @Test
        public void customTimestampDateOnlyTest() throws Exception {
                FirestoreProto2Json converter = FirestoreProto2Json.builder()
                                .valueMapperTimestamp(new ValueMapperTimestampDefault("yyyy-MM-dd"))
                                .build();
                JSONAssert.assertEquals("{ \"timestampField\": \"2024-02-26\" }",
                                converter.valueToJsonString(timestampUpdate), true);
        }

        @Test
        public void customTimestampYearOnlyTest() throws Exception {
                FirestoreProto2Json converter = FirestoreProto2Json.builder()
                                .valueMapperTimestamp(new ValueMapperTimestampDefault("yyyy"))
                                .valueMapperGeoPoint(new ValueMapperGeoPointDefault())
                                .valueMapperBytes(new ValueMapperBytesDefault()).build();
                JSONAssert.assertEquals("{ \"timestampField\": \"2024\" }",
                                converter.valueToJsonString(timestampUpdate), true);
        }
}
