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

import java.util.Base64;
import org.junit.jupiter.api.Test;
import com.google.events.cloud.firestore.v1.DocumentEventData;
import com.unitvectory.jsonassertify.JSONAssert;

/**
 * FirestoreProto2Json tests for the builder
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class FirestoreProto2JsonTest {

        private static final String booleanUpdate =
                        "CokBClhwcm9qZWN0cy9maXJlc3RvcmVwcm90bzJqc29uL2RhdGFiYXNlcy8oZGVmYXVsdCkvZG9jdW1lbnRzL3Rlc3RkYXRhLzA3NFNTeERTenRYdDl2RnQxcnFyEhIKDGJvb2xlYW5GaWVsZBICCAAaDAipgPCuBhCY87faASILCPCG9a4GEJicjHUSigEKWHByb2plY3RzL2ZpcmVzdG9yZXByb3RvMmpzb24vZGF0YWJhc2VzLyhkZWZhdWx0KS9kb2N1bWVudHMvdGVzdGRhdGEvMDc0U1N4RFN6dFh0OXZGdDFycXISEgoMYm9vbGVhbkZpZWxkEgIIARoMCKmA8K4GEJjzt9oBIgwIqYDwrgYQmPO32gEaDgoMYm9vbGVhbkZpZWxk";

        private static final String expectedOld = "{ \"booleanField\": true }";

        private static final String expectedNew = "{ \"booleanField\": false }";

        @Test
        public void valueToJsonStringDocumentTest() throws Exception {
                JSONAssert.assertEquals(expectedNew, FirestoreProto2Json.DEFAULT
                                .valueToJsonString(DocumentEventData.parseFrom(
                                                Base64.getDecoder().decode(booleanUpdate))),
                                true);
        }

        @Test
        public void valueToJsonStringBytesTest() throws Exception {
                JSONAssert.assertEquals(expectedNew, FirestoreProto2Json.DEFAULT.valueToJsonString(
                                Base64.getDecoder().decode(booleanUpdate)), true);
        }

        @Test
        public void valueToJsonStringBase64Test() throws Exception {
                JSONAssert.assertEquals(expectedNew,
                                FirestoreProto2Json.DEFAULT.valueToJsonString(booleanUpdate), true);
        }

        @Test
        public void oldValueToJsonStringDocumentTest() throws Exception {
                JSONAssert.assertEquals(expectedOld, FirestoreProto2Json.DEFAULT
                                .oldValueToJsonString(DocumentEventData.parseFrom(
                                                Base64.getDecoder().decode(booleanUpdate))),
                                true);
        }

        @Test
        public void oldValueToJsonStringBytesTest() throws Exception {
                JSONAssert.assertEquals(expectedOld, FirestoreProto2Json.DEFAULT
                                .oldValueToJsonString(Base64.getDecoder().decode(booleanUpdate)),
                                true);
        }

        @Test
        public void oldValueToJsonStringBase64Test() throws Exception {
                JSONAssert.assertEquals(expectedOld,
                                FirestoreProto2Json.DEFAULT.oldValueToJsonString(booleanUpdate),
                                true);
        }
}
