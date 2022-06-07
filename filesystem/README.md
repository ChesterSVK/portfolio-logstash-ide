# Module - filesystem

This module is responsible for interacting with filesystem.

It is an extension of original filemanager project called [RichFilemanager](https://github.com/servocoder/RichFilemanager).

Java connector is extended from project [https://github.com/fabriceci/RichFilemanager-JAVA](https://github.com/fabriceci/RichFilemanager-JAVA)

## Running tests
```
mvn clean test
```

## Logging
Logging capability into the specific files provided.

Log files are stored in *logs/* folder

Logging into the files can be achieved by setting maven property

Profile **default** will not print to file.

Profile **dev-info** will print only basic information into the file.

Profile **dev-debug** will print debug information into the file.

```
-Dspring.profiles.active    [default,   dev-info, dev-debug]
```


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Build properties

Here is the list of available properties that can be used to customize build options:

*Default value is represented as first item in array.

```
-Dspring.profiles.active    [default,   dev-info, dev-debug]                            :   Spring logging profile, which is used for saving [INFO or DEBUG] logs into files
-Dmodule.logging.level      [INFO,      DEBUG, ERROR, WARN, TRACE, ALL, OFF, FATAL]     :   Sets console logging level for all module classes
-Dspring.logging.level      [INFO,      DEBUG, ERROR, WARN, TRACE, ALL, OFF, FATAL]     :   Sets console logging level for all spring classes
```
## Authors

* **Jozef Cibík** 

 I hereby declare that source codes and test codes are used from RichFilemanager plugin (see in the begining).
 I did not create code from this plugin only extended it's functionality and used it for further development purposes.

## License

Licensed under the Apache License - see the [LICENSE.txt](LICENSE.txt) file for details

## Acknowledgments

Special thanks to code snippets and standards, that this work was inspired and experience learned from
 
* https://www.mkyong.com/
* https://www.baeldung.com/
* [Richfilemanager plugin](https://github.com/servocoder/RichFilemanager)

##Test Statistics (Richfilemanager)

Topic | Data (%) |
--- | --- 
Test coverage (Class) | 0.68
Test coverage (Method) | 0.36
Test coverage (Line) | 0.22
Attribute hiding factor | 0.73
Attribute inheritance factor | 0.04
Coupling factor | 0.12
Method hiding factor | 0.16
Method inheritance factor | 0.07
Polymorphism factor | 0.92

Topic | Data |
--- | --- 
Files (Java) | 31
LOC | 3915
LOC (test) | 602
Average essential cyclomatic complexity | 1.44
Average design cyclomatic complexity | 1.74
Average cyclomatic complexity | 2.25


##Test Statistics (additional implementation)

Topic | Data (%) |
--- | --- 
Test coverage (Class) | 1.00
Test coverage (Method) | 1.00
Test coverage (Line) | 1.00
Attribute hiding factor | 0.83
Attribute inheritance factor | 0.4
Coupling factor | 1.0
Method hiding factor | 0.02
Method inheritance factor | 0.1
Polymorphism factor | 0.0

Topic | Data |
--- | --- 
Files (Java) | 31
LOC | 5
LOC (test) | 235
Average essential cyclomatic complexity | 1.17
Average design cyclomatic complexity | 1.31
Average cyclomatic complexity | 1.39


Created: December 2018