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

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import com.google.events.cloud.firestore.v1.Value;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.protobuf.Timestamp;

/**
 * Formats protobuf Timestamp into a String representation.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class SimpleDateFormatTimestampValueMapper extends TimestampValueMapper {

    private final String pattern;

    private final ZoneOffset offset;

    public SimpleDateFormatTimestampValueMapper(String pattern, ZoneOffset offset) {
        this.pattern = pattern;
        this.offset = offset;
    }

    public SimpleDateFormatTimestampValueMapper(String pattern) {
        this(pattern, ZoneOffset.UTC);
    }

    public SimpleDateFormatTimestampValueMapper() {
        this("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", ZoneOffset.UTC);
    }

    @Override
    public final void convert(JsonObject jsonObject, String key, Value value) {
        String convert = formatTimestamp(value.getTimestampValue());
        jsonObject.addProperty(key, convert);
    }

    @Override
    public final void convert(JsonArray jsonArray, Value value) {
        String convert = formatTimestamp(value.getTimestampValue());
        jsonArray.add(convert);
    }

    private String formatTimestamp(Timestamp timestamp) {
        // Convert the Timestamp to an Instant
        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());

        // Format the Instant to RFC 3339 format
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(this.pattern).withZone(this.offset);

        return formatter.format(instant);
    }

}
