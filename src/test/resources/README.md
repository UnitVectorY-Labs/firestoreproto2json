# Parameterized Test

To test FirestoreProto2Json the input is the protobuf and the output is the JSON for the value and old value. Therefore, unique Java code does not need to be written to handle each of the possible situations for various input and output combinations.

JUnit 5's ParameterizedTest capability is utilized by looping through JSON files contained in the `tests`. These JSON files are structured to contain 3 parameters.

- `protobuf`: the base64 encoded protobuf from Firestore
- `value`: the expected JSON output for the value
- `oldValue`: the expected JSON output for the old value

The benefit of this design is that additional test cases can be added as data only, no need to write any code.
