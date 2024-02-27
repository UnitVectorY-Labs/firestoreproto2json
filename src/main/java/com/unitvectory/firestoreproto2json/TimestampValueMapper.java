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

import com.google.events.cloud.firestore.v1.Value;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.protobuf.Timestamp;

/**
 * Mapper for Timestamp field type.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public abstract class TimestampValueMapper extends ValueMapper {


    final void convert(JsonObject jsonObject, String key, Value value) {
        convert(jsonObject, key, value.getTimestampValue());
    }

    final void convert(JsonArray jsonArray, Value value) {
        convert(jsonArray, value.getTimestampValue());
    }

    public abstract void convert(JsonObject jsonObject, String key, Timestamp timestamp);

    public abstract void convert(JsonArray jsonArray, Timestamp timestamp);

}
