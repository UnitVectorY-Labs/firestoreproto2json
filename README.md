[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)[![Maven Central](https://img.shields.io/maven-central/v/com.unitvectory/firestoreproto2json)](https://mvnrepository.com/artifact/com.unitvectory/firestoreproto2json)

# firestoreproto2json

Helper library to convert Firestore Protocol Buffer to JSON Object

## Purpose

This library takes the Protocol Buffer sent from Firestore for a document and converts it to a JSON Object. Firestore stores the underlying documents as protocol buffers. While this is incredibly useful, when you have an event attached to Firestore to receive such as a Cloud Function that processes a change to the document.

This library takes the protocol buffer and converts it to a JSON Object using a set of assumptions that may or may not match how normal interactions with the document using the API.

## Getting Started

This library is available in the Maven Central Repository:

```
<dependency>
    <groupId>com.unitvectory</groupId>
    <artifactId>firestoreproto2json</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Usage

When processing a Firestore document change using a Cloud Function the `DocumentEventData` object will already be parsed from the binary representation and can be converted to JSON.

The value and old value can be convereted independently. If either the value or old value is not set null will be returned.

```java
// To GSON JsonObject
JsonObject valueJsonObject = FirestoreProto2Json.DEFAULT.valueToJsonObject(documentEventData);
JsonObject oldValueJsonObject = FirestoreProto2Json.DEFAULT.oldValueToJsonObject(documentEventData);

// To JSON String
String valueJsonString = FirestoreProto2Json.DEFAULT.valueToJsonString(documentEventData);
String oldVauleJsonString = FirestoreProto2Json.DEFAULT.oldValueToJsonString(documentEventData);
```

Additional helper functions are available for converting from the binary encoding of the protobuf or a base64 encoded version.

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
