package com.unitvectory.firestoreproto2json;

import java.util.Base64;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import com.google.events.cloud.firestore.v1.DocumentEventData;

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
