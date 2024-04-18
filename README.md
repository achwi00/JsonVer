# JsonVer

## Table of contents
* [General info](#general-info)
* [Content](#content)
* [Technologies](#technologies)

## General info
Programme written as a recruitment assignment for an internship at Remitly. The given task was to write a method validating a
Resource field in a json file, checking its value, returning false if it contains a single asterisk, and true in other cases.
The instructions provided left some room for interpretation, so I have decided to make the following assumptions:

* the json file must be of correct format,
* there needs to be a Resource field in the file,
* this field must be correctly nested,
* it cannot contain a single asterisk

in order for the method to return true. The method will only return true if there is a correctly placed Resource field within the file, and it contains content without a single asterisk. Otherwise, it will return false.

## Content
The main solution is in the JsonVerification class, the most important method being validateJsonResource() - the method returns
true or false based on the Resource field value. When it is called, it reads the content of a file and converts it to JsonNode.
Later, a private method is called, that processes the node recursively, so the Resource field is found and its value is assigned to 
the class' attribute, validatedResource. It also uses two flags to signal whether the field is correctly placed.
Then, the Resource attribute is being searched, and if a '*' is found, validateJsonResource() returns false.

The JsonVerificationTest provides testing the correctness of validateJsonResource() method's behaviour.

Edge cases covered:
* empty file
* lack of Resource field
* Resource field incorrectly placed

The tests also cover scenarios wih valid Resource field in a form of a String and Array, and an invalid Resource field with a 
single asterisk.

### Package structure
Main content can be found in:
src > main > java > org > jsonver > JsonVerification, Main

Tests can be found in:
src > test > JsonverificationTest 

### Running
The application structure is simple, navigate to src > main > java > org > jsonver > Main, and run the Main class, which 
contains the static method main. The method creates an instance of JsonVerification and runs the validateJsonResource().
This method takes a pathName of type String as a parameter, originally it is "data2.txt", a text file within the project 
structure. 
Testing the method relies on running it with a temporarily created test file and changing its content each time, checking if the 
output of the method is correct with assertion.

## Technologies
Project built using:

* Java version 19
* Maven version 3.8.1
* JUnit version 4.13.1 - for testing
* Jackson Databind version 2.16.1 - for json-related work
