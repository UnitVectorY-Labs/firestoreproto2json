[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Maven Central](https://img.shields.io/maven-central/v/com.unitvectory/firestoreproto2json)](https://central.sonatype.com/artifact/com.unitvectory/firestoreproto2json) [![javadoc](https://javadoc.io/badge2/com.unitvectory/firestoreproto2json/javadoc.svg)](https://javadoc.io/doc/com.unitvectory/firestoreproto2json) [![codecov](https://codecov.io/gh/UnitVectorY-Labs/firestoreproto2json/graph/badge.svg?token=N6CU9DZRIQ)](https://codecov.io/gh/UnitVectorY-Labs/firestoreproto2json)

# firestoreproto2json

Helper library to convert Firestore Protocol Buffer to JSON Object

## Purpose

This library takes the Protocol Buffer sent from Firestore for a document and converts it to a JSON Object. Firestore stores the underlying documents as Protocol Buffers and therefore that is what is sent to a Cloud Function when subscribed. While this is useful, when you have an event attached to Firestore to receive such as a Cloud Function that processes a change to the document.

This library takes the Protocol Buffer and converts it to a JSON Object using a set of assumptions that may or may not match how normal interactions with the document using the API. However, this conversion to JSON is likely useful for a generic implementation that needs to take an arbitrary document and convert it to the corresponding JSON representation.

## Getting Started

This library requires Java 17 and is available in the Maven Central Repository:

```
<dependency>
    <groupId>com.unitvectory</groupId>
    <artifactId>firestoreproto2json</artifactId>
    <version>0.0.3</version>
</dependency>
```

## Cloud Function Usage

The following example is adapted from the Java 17 example for a Cloud Function connected to Firestore. This library includes `com.google.cloud:google-cloudevent-types` and `com.google.protobuf:protobuf-java` as dependencies and therefore they do not need to be included again.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
<modelVersion>4.0.0</modelVersion>

<groupId>com.example.cloud.functions</groupId>
<artifactId>functions-firebase-firestore</artifactId>

<parent>
  <groupId>com.google.cloud.samples</groupId>
  <artifactId>shared-configuration</artifactId>
  <version>1.2.0</version>
</parent>

<properties>
  <maven.compiler.release>17</maven.compiler.release>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>

<dependencies>
  <dependency>
    <groupId>com.unitvectory</groupId>
    <artifactId>firestoreproto2json</artifactId>
    <version>0.0.3</version>
  </dependency>
  <dependency>
    <groupId>com.google.cloud.functions</groupId>
    <artifactId>functions-framework-api</artifactId>
    <version>1.0.4</version>
    <scope>provided</scope>
  </dependency>
</dependencies>

</project>
```

The following example taken from CloudFunction is adapted to show how firestoreproto2json can be used to convert the value and old value into a JSON string. If the value is not set a null value will be returned.

```java
package functions;

import com.google.cloud.functions.CloudEventsFunction;
import com.google.events.cloud.firestore.v1.DocumentEventData;
import com.google.protobuf.InvalidProtocolBufferException;
import com.unitvectory.firestoreproto2json.FirestoreProto2Json;
import io.cloudevents.CloudEvent;
import java.util.logging.Logger;

public class FirebaseFirestore implements CloudEventsFunction {
  private static final Logger logger = Logger.getLogger(FirebaseFirestore.class.getName());

  @Override
  public void accept(CloudEvent event) throws InvalidProtocolBufferException {
    DocumentEventData firestorEventData = DocumentEventData.parseFrom(event.getData().toBytes());

    // Convert the value to JSON
    String valueJsonString = FirestoreProto2Json.DEFAULT.valueToJsonString(firestorEventData);
    logger.info("Value: " + valueJsonString);

    // Convert the old value to JSON
    String oldVauleJsonString = FirestoreProto2Json.DEFAULT.oldValueToJsonString(firestorEventData);
    logger.info("Old Value: " + oldVauleJsonString);
  }
}
```

A GSON JsonObject can be returned instead of a String which may be useful for additional manipulation.

```java
JsonObject valueJsonObject = FirestoreProto2Json.DEFAULT.valueToJsonObject(firestorEventData);
JsonObject oldValueJsonObject = FirestoreProto2Json.DEFAULT.oldValueToJsonObject(firestorEventData);
```

Additional functions are available for converting from the `byte[]` of the Protocol Buffer for the [DocumentEventData](https://github.com/googleapis/google-cloudevents/blob/main/proto/google/events/cloud/firestore/v1/data.proto) or a base64 encoded version as well.

## Field Conversions

Firestore's [supported data types](https://cloud.google.com/firestore/docs/concepts/data-types) are converted to JSON using the following rules.

| Firestore Data Type   | JSON Data Type                 | Example                                                                     |
| --------------------- | ------------------------------ | --------------------------------------------------------------------------- |
| Array                 | JSON Array                     | `{"foo": [1, 2, 3]}`                                                        |
| Boolean               | Boolean                        | `{"foo": true}`                                                             |
| Bytes                 | JSON Object with Base64 String | `{"foo": {"_byteString": "ZXhhbXBsZQ=="} }`                                 |
| Date and Time         | String                         | `{"foo": "2024-02-26T23:18:55.296Z"}`                                       |
| Floating Point number | Number                         | `{"foo": 12.34}`                                                            |
| Geographical point    | JSON Object with Numbers       | `{"foo": {"latitude": 36.74050912505929,"longitude": -57.83434128116206} }` |
| Integer               | Number                         | `{"foo": 123}`                                                              |
| Map                   | JSON Object                    | `{"foo": {"bar": 123} }`                                                    |
| Null                  | Null                           | `{"foo": null}`                                                             |
| Reference             | String                         | `{"foo": "projects/example/databases/(default)/documents/example/record"}`  |
| Text string           | String                         | `{"foo": "bar"}`                                                            |

### Assumptions and Limitations

The likely reason Google does not provide this functionality is because there is not a single clean conversion between the Protocol Buffer representation of the document and the JSON representation expected by the application.

Therefore, this library makes a few assumptions regarding the structure of the JSON payload to be generated. For example the formatting of the timestamp field into a string, there are multiple formats that could be used to represent the date.

The field types with ambiguous conversions can be customized to meet the requirements of the specific use case if necessary.

### Bytes Field

Binary data stored in an attribute in Firestore when represented in JSON will need to be encoded. The encoding used is base64 with the field being stored in a JSON Object with the attribute "\_byteString"

```json
{
  "foo": {
    "_byteString": "ZXhhbXBsZQ=="
  }
}
```

This conversion can be customized by extending the ValueMapperBytes class. A custom FirestoreProto2Json can then be used instead of the default. The following is an example where the text is encoded as hexidecimal and directly assigned to the attribute name.

```java
FirestoreProto2Json firestoreProto2Json =
        FirestoreProto2Json.builder().valueMapperBytes(new ValueMapperBytes() {
            @Override
            public void convert(JsonObject jsonObject, String key, byte[] bytes) {
                jsonObject.addProperty(key, HexFormat.of().formatHex(bytes));
            }

            @Override
            public void convert(JsonArray jsonArray, byte[] bytes) {
                jsonArray.add(HexFormat.of().formatHex(bytes));
            }
        }).build();
```

### GeoPoint Field

Google discourages the use of the GeoPoint type, but it is fully supported. The default mapping being a JSON Object with the latitude and longitude as attribute. The mapping can be overridden by implementing the `ValueMapperGeoPoint` class.

```json
{
  "foo": {
    "latitude": 36.74050912505929,
    "longitude": -57.83434128116206
  }
}
```

### Timestamp Field

The most likely field whose format may need to be overridden is the Timestamp field. The default behavior is to encode the timestamp as "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" in the UTC timezone.

While `ValueMapperTimestamp` can be overridden to fully customize the conversion `ValueMapperTimestampDefault` provides additional constructors that allow the format and timezone to be specified to simplify customization. For example, the follow customization formats the time but only to the whole second.

```java
FirestoreProto2Json converter = FirestoreProto2Json.builder()
        .valueMapperTimestamp(
                new ValueMapperTimestampDefault("yyyy-MM-dd'T'HH:mm:ss'Z'", ZoneOffset.UTC))
        .build();
```
