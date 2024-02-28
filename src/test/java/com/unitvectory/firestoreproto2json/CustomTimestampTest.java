package com.unitvectory.firestoreproto2json;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

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
