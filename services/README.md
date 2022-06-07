# Module - services

Module is used for integrating all required modules to be used in web endpoint of the application

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

## Dependencies

This module is dependent on modules: bro, filesystem, logstash, elasticsearch, watchdog, json


## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Build properties

Here is the list of available properties that can be used to customize build options:

*Default value is represented as first item in array.

```
-Dservices.root.output.directory.home                                                   :   Path to the root output directory of the application
-Dservices.bro.output.directory.home                                                    :   Path to the module bro output directory of the application
-Dservices.watchdog.output.directory.home                                               :   Path to the module watchdog directory of the application
-Dservices.logstash.output.directory.home                                               :   Path to the module logstash output directory of the application
-Dservices.json.output.directory.home                                                   :   Path to the module json output directory of the application

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
* https://www.baeldung.com/


##Statistics 

Topic | Data (%) |
--- | --- 
Test coverage (Class) | 0.80
Test coverage (Method) | 0.43
Test coverage (Line) | 0.40
Attribute hiding factor | 0.90
Attribute inheritance factor | 0.07
Coupling factor | 0.13
Method hiding factor | 0.09
Method inheritance factor | 0.03
Polymorphism factor | 1.23

Topic | Data |
--- | --- 
Files (Java) | 57
Files (XML)  | 2
LOC | 3005
LOC (test) | 964
Average essential cyclomatic complexity | 1.05
Average design cyclomatic complexity | 1.19
Average cyclomatic complexity | 1.23

Created: December 2018