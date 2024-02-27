# firestoreproto2json

Helper library to convert Firestore Proto Buff to JSON Object

## Purpose

This library takes the Protocol Buffer sent from Firestore for a document and converts it to a JSON Object. Firestore stores the underlying documents as protocol buffers. While this is incredibly useful, when you have an event attached to Firestore to receive such as a Cloud Function that processes a change to the document.

This library takes the protocol buffer and converts it to a JSON Object using a set of assumptions that may or may not match how normal interactions with the document using the API.

### Assumptions and Limitations

The reason Google does not provide this functionality is most likely because there is not a single clean conversion between the Protocol Buffer representation of the document and the JSON representation expected by the application.

Therefore, this library makes a few assumptions regarding the structure of the JSON payload to be generated. For example the formatting of the timestamp field into a string, there are multiple formats that could be used to represent the date.

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
