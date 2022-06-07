# Module - bro

This module is responsible for using Bro tool in order to process input files.
See https://www.bro.org/

## Running tests
```
mvn clean test
```

- To make sure if valid path to bro instance was given test `BroInitializationTest` should succeed

## Dependencies
This module is dependent on modules: core, filesystem

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

* [Spring Framework](https://spring.io/projects/spring-framework) - Web framework used
* [Maven](https://maven.apache.org/) - Dependency Management

## Build properties

Here is the list of available properties that can be used to customize build options:

*Default value is represented as first item in array.

```
-Dbro.binary.path                                                                       :   Path to bro binary instance in order to module work sucessfully

-Dspring.profiles.active    [default,   dev-info, dev-debug]                            :   Spring logging profile, which is used for saving [INFO or DEBUG] logs into files
-Dmodule.logging.level      [INFO,      DEBUG, ERROR, WARN, TRACE, ALL, OFF, FATAL]     :   Sets console logging level for all module classes
-Dspring.logging.level      [INFO,      DEBUG, ERROR, WARN, TRACE, ALL, OFF, FATAL]     :   Sets console logging level for all spring classes
```
## Authors

* **Jozef Cibík** 

## License

Licensed under the Apache License - see the [LICENSE.txt](LICENSE.txt) file for details

## Acknowledgments

Special thanks to code snippets and standards, that this work was inspired and experience learned from
 
* https://www.mkyong.com/
* https://www.bro.org/documentation/index.html

## Statistics

Topic | Data (%) |
--- | --- 
Test coverage (Class) | 1.00
Test coverage (Method) | 1.00
Test coverage (Line) | 1.00
Attribute hiding factor | 0.83
Attribute inheritance factor | 0.17
Coupling factor | 0.17
Method hiding factor | 0.13
Method inheritance factor | 0.04
Polymorphism factor | 1.5

Topic | Data |
--- | --- 
Files (Java) | 13
Files (XML)  | 2
LOC | 461
LOC (test) | 193
Average essential cyclomatic complexity | 1.06
Average design cyclomatic complexity | 1.17
Average cyclomatic complexity | 1.19


Created: December 2018