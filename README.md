# JsonVer

## Table of contents
* [General info](#general-info)
* [Content](#content)
* [How to run](#how-to-run)
* [Technologies](#technologies)

## General info
The given task is to write a method validating a Resource field in a json file, checking its value, returning false if it contains a single asterisk, and true in other cases.
Assumptions made:

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

## How to run
The application structure is simple, and running it is straightforward.
### Run from IDE
After downloading (Code > Download ZIP) and unzipping the project from GitHub, open it in Your IDE and navigate to src > main > java > org > jsonver > Main, 
and run the current file, the Main class, which contains the static method main. The method creates an instance of JsonVerification and runs the validateJsonResource().
After running, You will be promped in the console, to provide a path to a json file, which contents You would like to verify.
You may type in:

src/main/resources/data2.txt

to use a provided text file, that already has json content inside (file can be found in src>main>resources and its content 
may be adjusted).

### Run tests
Testing the method relies on running it with a temporarily created test file and changing its content each time, checking if the 
output of the method is correct with assertion.
To run the tests, navigate to src > test > JsonverificationTest and run the current file, JsonverificationTest class.


### Run fat jar from CMD (Main class)
I have prepared a jar-with-dependencies file. To run the fat jar from CMD on Windows, after downloading the zip file and unzipping it, open CMD and 
navigate to the downloaded directory. Then, navigate to target directory (cd target) and run the jar file like this:

java -jar jsonresource-ver-1.0-SNAPSHOT-jar-with-dependencies.jar

This will prompt You to provide a path for a json file. You can provide the full path to the data2.txt within the project 
structure, and it will look somewhat like this:

C:\Users\user00\Downloads\JsonVer\src\main\resources\data2.txt

You may adjust the contents of the file accordingly.


## Technologies
Project built using:

* Java version 21
* Maven version 3.8.1
* JUnit version 4.13.1 - for testing
* Jackson Databind version 2.16.1 - for json-related work
