# Module - watchdog

This module is responsible script communication and invocation with name Watchdog.

Script is specially designed to be used in overall parent project called CopAS. 

### Prerequisites

This module for it's successful usage need to have provided path to existing script known as *watchdog* script 

To set path to the script use this property
```
-Dcopas.watchdog.script.path
```


## Running tests
###Unit Tests
```
mvn clean test
```

## Dependencies
This module is dependent on modules: core

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
-Dcopas.watchdog.script.path                                                            :   Path to watchdog script

-Dspring.profiles.active    [default,   dev-info, dev-debug]                            :   Spring logging profile, which is used for saving [INFO or DEBUG] logs into files
-Dmodule.logging.level      [INFO,      DEBUG, ERROR, WARN, TRACE, ALL, OFF, FATAL]     :   Sets console logging level for all module classes
-Dspring.logging.level      [INFO,      DEBUG, ERROR, WARN, TRACE, ALL, OFF, FATAL]     :   Sets console logging level for all spring classes
```
## Authors

* **Jozef Cibík** 

## License

Licensed under the Apache License - see the [LICENSE.txt](LICENSE.txt) file for details

## Statistics

Topic | Data (%) |
--- | --- 
Test success | 1.00
Test coverage (Class) | 1.00
Test coverage (Method) | 1.00
Test coverage (Line) | 1.00
Attribute hiding factor | 0.68
Attribute inheritance factor | 0.08
Coupling factor | 0.16
Method hiding factor | 0.15
Method inheritance factor | 0.09
Polymorphism factor | 0.92

Topic | Data |
--- | --- 
Files (Java) | 23
Files (XML)  | 2
LOC | 1002
LOC (test) | 506
Average essential cyclomatic complexity | 1.07
Average design cyclomatic complexity | 1.22
Average cyclomatic complexity | 1.25


Created: December 2018