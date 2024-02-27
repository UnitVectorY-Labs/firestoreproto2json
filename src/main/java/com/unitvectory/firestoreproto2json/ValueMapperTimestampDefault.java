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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.protobuf.Timestamp;

/**
 * The default timestamp ValueMapper.
 * 
 * @author Jared Hatfield (UnitVectorY Labs)
 */
public class ValueMapperTimestampDefault extends ValueMapperTimestamp {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    /**
     * The DateTimeFormatter.
     */
    private final DateTimeFormatter formatter;

    /**
     * Creates a new instance of the ValueMapperTimestampDefault.
     */
    public ValueMapperTimestampDefault() {
        this(DEFAULT_FORMAT, ZoneOffset.UTC);
    }

    /**
     * Creates a new instance of the ValueMapperTimestampDefault.
     * 
     * @param pattern the DateTimeFormatter pattern
     */
    public ValueMapperTimestampDefault(String pattern) {
        this(pattern, ZoneOffset.UTC);
    }

    /**
     * Creates a new instance of the ValueMapperTimestampDefault.
     * 
     * @param pattern the DateTimeFormatter pattern
     * @param offset the ZoneOffset
     */
    public ValueMapperTimestampDefault(String pattern, ZoneOffset offset) {
        this.formatter = DateTimeFormatter.ofPattern(pattern).withZone(offset);
    }

    @Override
    public final void convert(JsonObject jsonObject, String key, Timestamp timestamp) {
        String convert = formatTimestamp(timestamp);
        jsonObject.addProperty(key, convert);
    }

    @Override
    public final void convert(JsonArray jsonArray, Timestamp timestamp) {
        String convert = formatTimestamp(timestamp);
        jsonArray.add(convert);
    }

    private String formatTimestamp(Timestamp timestamp) {
        // Convert the Timestamp to an Instant
        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());

        // Now format the instant
        return this.formatter.format(instant);
    }
}
