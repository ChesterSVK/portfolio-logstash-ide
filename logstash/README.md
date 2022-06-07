# Module - logstash

Backend for interactive creation of the logstash commands and communication with logstash product

Logstash version included in instance folder: 6.3.2

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
-Ddefault.output.host                                                                   :   Default host string to be set for logstash output plugin

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
Test coverage (Class) | 1.00
Test coverage (Method) | 0.89
Test coverage (Line) | 0.84
Attribute hiding factor | 0.58
Attribute inheritance factor | 0.47
Coupling factor | 0.08
Method hiding factor | 0.15
Method inheritance factor | 0.43
Polymorphism factor | 0.36

Topic | Data |
--- | --- 
Files (Java) | 112
Files (XML)  | 1
LOC | 4781
LOC (test) | 1932
Average essential cyclomatic complexity | 1.10
Average design cyclomatic complexity | 1.39
Average cyclomatic complexity | 1.46

Created: December 2018