# Module - web

This module is responsible for endpoint web application execution of commands and server initialization


## Copas integration

If used in Copas container additional data will be shown in Web interface
Watched properties:

COPAS_IMAGE_VERSION, COPAS_VERSION, COPAS_ID, KIBANA_PORT

## Running tests

Test will be implemented on demand.

## Dependencies
This module is dependent on modules: services

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
-Dtomcat.start.binary.path  
-Dtomcat.stop.binary.path
-Dspring.mvc.locale         [cs, en]
-Dtomcat.host
-Dtomcat.port               [8080]
-Dtomcat.uptime.millis      [15000]

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


## Statistics (Backend)

Topic | Data (%) |
--- | --- 
Attribute hiding factor | 0.26
Attribute inheritance factor | 0.00
Coupling factor | 0.22
Method hiding factor | 0.16
Method inheritance factor | 0.00
Polymorphism factor | 0.00

Topic | Data |
--- | --- 
Files (Java) | 19
Files (XML)  | 1
LOC | 912
LOC (test) | 0
Average essential cyclomatic complexity | 1.12
Average design cyclomatic complexity | 1.25
Average cyclomatic complexity | 1.25

## Statistics (Frontend)

Topic | Data (%) |
--- | --- 
Attribute hiding factor | 0.68
Attribute inheritance factor | 0.08
Coupling factor | 0.16
Method hiding factor | 0.15
Method inheritance factor | 0.09
Polymorphism factor | 0.92

Topic | Data |
--- | --- 
Files (Java) | 325
Files (Js) | 483
Files (Css) | 118
Files (Groovy) | 2
Files (HTML) | 290
Files (XML) | 1438
LOC | 663897
LOC (test) | 0

Created: December 2018