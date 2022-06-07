# Module - Elasticsearch

This module is used for interaction with Elasticsearch database. 
User Data, Validator, Test, Repository implemented, but disabled.

## Getting Started

Before using or running this module an elasticsearch cluster must be started.

### Prerequisites

Elasticsearch cluster needs to be installed on the working machine. 
Default Elasticsearch version 5.5.6 is used. 
This information should be displayed when running command using default ES running instance.

```
λ curl -XGET "localhost:9200"
{
  "name" : "your-name",
  "cluster_name" : "your-cluster",
  "cluster_uuid" : "your-uuid",
  "version" : {
    "number" : "5.6.6",
    "build_hash" : "7d99d36",
    "build_date" : "2018-01-09T23:55:47.880Z",
    "build_snapshot" : false,
    "lucene_version" : "6.6.1"
  },
  "tagline" : "You Know, for Search"
}
```


### Installing

Run ES instance:

Windows

```
path-to-your-es-instance/bin/elasticsearch.bat
```

Non windows:

```
path-to-your-es-instance/bin/elasticsearch
```

Default Es instance is located in instance folder of this module.

## Running tests
###Unit Tests
```
mvn clean test
```

- To make sure if valid path to elasticsearch instance was given test `ElasticsearchApplicationIT` should succeed

## Logging
Logging capability into the specific files provided.

Log files are stored in *logs/* folder

Profile **default** will not print to file.

Profile **dev-info** will print only basic information into the file.

Profile **dev-debug** will print debug information into the file.
```
-Dspring.profiles.active    [default,   dev-info, dev-debug]
```

## Build properties

Here is the list of available properties that can be used to customize build options:

*Default value is represented as first item in array.

```
-Delasticsearch.home                                                                    :   Addres of the started ES instance
-Delasticsearch.host                                                                    :   Host of the started ES instance
-Delasticsearch.port                                                                    :   Port of the started ES instance
-Delasticsearch.response.timeout                                                        :   Expected response delay [ms]
-Delasticsearch.cluster                                                                 :   Cluster name

-Dspring.profiles.active    [default,   dev-info, dev-debug]                            :   Spring logging profile, which is used for saving [INFO or DEBUG] logs into files
-Dmodule.logging.level      [INFO,      DEBUG, ERROR, WARN, TRACE, ALL, OFF, FATAL]     :   Sets console logging level for all module classes
-Dspring.logging.level      [INFO,      DEBUG, ERROR, WARN, TRACE, ALL, OFF, FATAL]     :   Sets console logging level for all spring classes
```


## Built With

* [Spring Framework](https://spring.io/projects/spring-framework) - Web framework used
* [Spring Elasticsearch Framework](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/) - Database framework used
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Jozef Cibík** 

## License

Licensed under the Apache License - see the [LICENSE.txt](LICENSE.txt) file for details


## Acknowledgments

Special thanks to code snippets and standards, that this work was inspired and experience learned from
 
* https://www.mkyong.com/
* https://www.baeldung.com/
* https://dzone.com/
* https://docs.spring.io
* https://www.elastic.co/guide/index.html

##Statistics 

Topic | Data (%) |
--- | --- 
Test coverage (Class) | 1.00
Test coverage (Method) | 1.00
Test coverage (Line) | 1.00
Attribute hiding factor | 0.89
Attribute inheritance factor | 0.08
Coupling factor | 0.1
Method hiding factor | 0.09
Method inheritance factor | 0.00
Polymorphism factor | 9.0

Topic | Data |
--- | --- 
Files (Java) | 34
Files (XML)  | 2
LOC | 1055
LOC (test) | 517
Average essential cyclomatic complexity | 1.13
Average design cyclomatic complexity | 1.02
Average cyclomatic complexity | 1.15


Created: December 2018