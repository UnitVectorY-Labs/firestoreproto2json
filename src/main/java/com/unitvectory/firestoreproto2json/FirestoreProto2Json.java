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
import java.util.Map.Entry;
import com.google.events.cloud.firestore.v1.Document;
import com.google.events.cloud.firestore.v1.DocumentEventData;
import com.google.events.cloud.firestore.v1.MapValue;
import com.google.events.cloud.firestore.v1.Value;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.Builder;

/**
 * Utility for converting the protobuf from Firestore into a JSON object
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
@Builder
public class FirestoreProto2Json {

    /**
     * The default instance of FirestoreProto2Json
     */
    public static final FirestoreProto2Json DEFAULT = FirestoreProto2Json.builder().build();

    private static final Gson GSON = new GsonBuilder().serializeNulls().create();

    @Builder.Default
    private ValueMapperTimestamp timestampValueMapper = new ValueMapperTimestampDefault();

    @Builder.Default
    private ValueMapperGeoPoint geoPointValueMapper = new ValueMapperGeoPointDefault();

    /**
     * Convert a DocumentEventData value to a JSON string.
     * 
     * @param documentEventData the documentEventData
     * @return the JSON string for the value if set; otherwise null
     */
    public String valueToJsonString(DocumentEventData documentEventData) {
        return toJsonString(valueToJsonObject(documentEventData));
    }

    /**
     * Convert the base64 encoded protocol buffer of a DocumentEventData value to a JSON string.
     * 
     * @param base64DocumentBytes the base64 encoded protocol buffer of a DocumentEventData
     * @return the JSON string for the value if set; otherwise null
     * @throws InvalidProtocolBufferException
     */
    public String valueToJsonString(String base64DocumentBytes)
            throws InvalidProtocolBufferException {
        return toJsonString(valueToJsonObject(base64DocumentBytes));
    }

    /**
     * Convert the protocol buffer bytes of a DocumentEventData value to a JSON string.
     * 
     * @param documentBytes the protocol buffer bytes of a DocumentEventData
     * @return the JSON string for the value if set; otherwise null
     * @throws InvalidProtocolBufferException
     */
    public String valueToJsonString(byte[] documentBytes) throws InvalidProtocolBufferException {
        return toJsonString(valueToJsonObject(documentBytes));
    }

    /**
     * Convert a DocumentEventData value to a JSON object.
     * 
     * @param documentEventData the documentEventData
     * @return the JSON object for the value if set; otherwise null
     */
    public JsonObject valueToJsonObject(DocumentEventData documentEventData) {
        if (documentEventData.hasValue()) {
            Document document = documentEventData.getValue();
            return documentToJsonObject(document);
        } else {
            return null;
        }
    }

    /**
     * Convert the base64 encoded protocol buffer of a DocumentEventData value to a JSON object.
     * 
     * @param base64DocumentBytes the base64 encoded protocol buffer of a DocumentEventData
     * @return the JSON object for the value if set; otherwise null
     * @throws InvalidProtocolBufferException
     */
    public JsonObject valueToJsonObject(String base64DocumentBytes)
            throws InvalidProtocolBufferException {
        byte[] documentBytes = Base64.getDecoder().decode(base64DocumentBytes);
        return valueToJsonObject(documentBytes);
    }

    /**
     * Convert the protocol buffer bytes of a DocumentEventData value to a JSON object.
     * 
     * @param documentBytes the protocol buffer bytes of a DocumentEventData
     * @return the JSON object for the value if set; otherwise null
     * @throws InvalidProtocolBufferException
     */
    public JsonObject valueToJsonObject(byte[] documentBytes)
            throws InvalidProtocolBufferException {
        DocumentEventData documentEventData = DocumentEventData.parseFrom(documentBytes);
        return valueToJsonObject(documentEventData);
    }

    /**
     * Convert a DocumentEventData old value to a JSON string.
     * 
     * @param documentEventData the documentEventData
     * @return the JSON string for the old value if set; otherwise null
     */
    public String oldValueToJsonString(DocumentEventData documentEventData) {
        return toJsonString(oldValueToJsonObject(documentEventData));
    }

    /**
     * Convert the base64 encoded protocol buffer of a DocumentEventData old value to a JSON string.
     * 
     * @param base64DocumentBytes the base64 encoded protocol buffer of a DocumentEventData
     * @return the JSON string for the old value if set; otherwise null
     * @throws InvalidProtocolBufferException
     */
    public String oldValueToJsonString(String base64DocumentBytes)
            throws InvalidProtocolBufferException {
        return toJsonString(oldValueToJsonObject(base64DocumentBytes));
    }

    /**
     * Convert the protocol buffer bytes of a DocumentEventData old value to a JSON string.
     * 
     * @param documentBytes the protocol buffer bytes of a DocumentEventData
     * @return the JSON string for the old value if set; otherwise null
     * @throws InvalidProtocolBufferException
     */
    public String oldValueToJsonString(byte[] documentBytes) throws InvalidProtocolBufferException {
        return toJsonString(oldValueToJsonObject(documentBytes));
    }

    /**
     * Convert a DocumentEventData old value to a JSON object.
     * 
     * @param documentEventData the documentEventData
     * @return the JSON object for the old value if set; otherwise null
     */
    public JsonObject oldValueToJsonObject(DocumentEventData documentEventData) {
        if (documentEventData.hasOldValue()) {
            Document document = documentEventData.getOldValue();
            return documentToJsonObject(document);
        } else {
            return null;
        }
    }

    /**
     * Convert the base64 encoded protocol buffer of a DocumentEventData old value to a JSON object.
     * 
     * @param base64DocumentBytes the base64 encoded protocol buffer of a DocumentEventData
     * @return the JSON object for the old value if set; otherwise null
     * @throws InvalidProtocolBufferException
     */
    public JsonObject oldValueToJsonObject(String base64DocumentBytes)
            throws InvalidProtocolBufferException {
        byte[] documentBytes = Base64.getDecoder().decode(base64DocumentBytes);
        return oldValueToJsonObject(documentBytes);
    }

    /**
     * Convert the protocol buffer bytes of a DocumentEventData old value to a JSON object.
     * 
     * @param documentBytes the protocol buffer bytes of a DocumentEventData
     * @return the JSON object for the old value if set; otherwise null
     * @throws InvalidProtocolBufferException
     */
    public JsonObject oldValueToJsonObject(byte[] documentBytes)
            throws InvalidProtocolBufferException {
        DocumentEventData documentEventData = DocumentEventData.parseFrom(documentBytes);
        return oldValueToJsonObject(documentEventData);
    }

    private JsonObject documentToJsonObject(Document document) {
        JsonObject jsonObject = new JsonObject();
        for (Entry<String, Value> entry : document.getFieldsMap().entrySet()) {
            appendValue(jsonObject, entry.getKey(), entry.getValue());
        }

        return jsonObject;
    }

    private String toJsonString(JsonObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }

        return GSON.toJson(jsonObject);
    }

    private void appendValue(JsonObject jsonObject, String key, Value value) {
        if (value.hasMapValue()) {
            MapValue mapValue = value.getMapValue();

            JsonObject mapJsonObject = new JsonObject();
            for (Entry<String, Value> entry : mapValue.getFieldsMap().entrySet()) {
                appendValue(mapJsonObject, entry.getKey(), entry.getValue());
            }

            jsonObject.add(key, mapJsonObject);

        } else if (value.hasArrayValue()) {
            JsonArray jsonArray = new JsonArray();
            for (Value arrayValue : value.getArrayValue().getValuesList()) {
                appendValue(jsonArray, arrayValue);
            }
            jsonObject.add(key, jsonArray);
        } else if (value.hasIntegerValue()) {
            jsonObject.addProperty(key, value.getIntegerValue());
        } else if (value.hasStringValue()) {
            jsonObject.addProperty(key, value.getStringValue());
        } else if (value.hasDoubleValue()) {
            jsonObject.addProperty(key, value.getDoubleValue());
        } else if (value.hasBooleanValue()) {
            jsonObject.addProperty(key, value.getBooleanValue());
        } else if (value.hasNullValue()) {
            jsonObject.add(key, JsonNull.INSTANCE);
        } else if (value.hasTimestampValue()) {
            timestampValueMapper.convert(jsonObject, key, value);
        } else if (value.hasGeoPointValue()) {
            geoPointValueMapper.convert(jsonObject, key, value);
        } else if (value.hasReferenceValue()) {
            jsonObject.addProperty(key, value.getReferenceValue());
        }
    }

    private void appendValue(JsonArray jsonArray, Value value) {
        if (value.hasMapValue()) {
            MapValue mapValue = value.getMapValue();

            JsonObject mapJsonObject = new JsonObject();
            for (Entry<String, Value> entry : mapValue.getFieldsMap().entrySet()) {
                appendValue(mapJsonObject, entry.getKey(), entry.getValue());
            }

            jsonArray.add(mapJsonObject);

        } else if (value.hasIntegerValue()) {
            jsonArray.add(value.getIntegerValue());
        } else if (value.hasStringValue()) {
            jsonArray.add(value.getStringValue());
        } else if (value.hasDoubleValue()) {
            jsonArray.add(value.getDoubleValue());
        } else if (value.hasBooleanValue()) {
            jsonArray.add(value.getBooleanValue());
        } else if (value.hasNullValue()) {
            jsonArray.add(JsonNull.INSTANCE);
        } else if (value.hasTimestampValue()) {
            timestampValueMapper.convert(jsonArray, value);
        } else if (value.hasGeoPointValue()) {
            geoPointValueMapper.convert(jsonArray, value);
        } else if (value.hasReferenceValue()) {
            jsonArray.add(value.getReferenceValue());
        }

        // Nested arrays are not supported by Firestore and therefore not implemented as part of
        // this method while other datatype conversions are implemented
    }
}
