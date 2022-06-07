# Project - logstashIDE

Basic logstash IDE for using [logstash](https://www.elastic.co/products/logstash) tool interactively
Used with [elasticsearch](https://www.elastic.co/products/elasticsearch) for uploading files and persistence of data

Motivation for this project is continuous work with Bachelor Thesis project and gaining more experience in development. 

Used Java 1.8.0_171

Build with Apache Maven 3.5.4

More information about properties or statistics can be found in README.md files of each module.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.


### Build

Build

```
mvn clean install
```

Without test

```
mvn clean install -DskipTests=true
```

If launched in COPAS container

```
 mvn clean install  -Dspring.profiles.active=dev-debug,dev-info  -Dmodule.logging.level=DEBUG  -Dstartup.initialization=true  -Dbro.binary.path=/usr/local/bro/bin/bro  -Dservices.root.output.directory.home=/home/application-workspace  -Dcopas.watchdog.script.path=/opt/CopAS/bin/copas-watchdog  -Dlogstash.binary.path=/usr/share/logstash/bin/logstash  -Delasticsearch.home=/usr/share/elasticsearch/bin/elasticsearch  -Delasticsearch.port=9300 -Delasticsearch.cluster=elasticsearch -DskipTests=true -Dfail.on.elasticsearch.down=false -Ddefault.output.host=http://localhost:9200/
```

`-Ddefault.output.host=http://localhost:9200/` - default logstash output host

`-Dspring.profiles.active=dev-debug,dev-info` - Logging profiles will create additional log directory with logs from each modules

`-Dmodule.logging.level=DEBUG` - Logging levels

`-Dstartup.initialization=true` - Initialization consists of initializing each module and its required functionality

Paths to directories and binaries
`-Dbro.binary.path=/usr/local/bro/bin/bro`

`-Dservices.root.output.directory.home=/home/application-workspace`

`-Dcopas.watchdog.script.path=/opt/CopAS/bin/copas-watchdog`

`-Dlogstash.binary.path=/usr/share/logstash/bin/logstash`

`-Delasticsearch.home=/usr/share/elasticsearch/bin/elasticsearch`

Additional properties

`-Dfail.on.elasticsearch.down=false`

`-Delasticsearch.port=9300`

`-Delasticsearch.cluster=elasticsearch`

`-DskipTests=true`

More info in individual README.md files
More properties to set in individual pom.xml files

## Launching

Executable jar:

```
java -jar ./web/target/NeckWeb.jar
```

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

Depth of logging level should be set with `-Dmodule.logging.level` property


## Built With

* [Spring Framework](https://spring.io/projects/spring-framework) - Web framework used
* [Maven](https://maven.apache.org/) - Dependency Management

## Authors

* **Jozef Cibík** 

## Cooperation

* **Tomáš Rebok**
* **Tomáš Svoboda**

## License

Licensed under the Apache License - see the [LICENSE.txt](LICENSE.txt) file for details


## Acknowledgments

This project was also used in gaining more experience in software development.
Special thanks to code snippets and standards, that this work was inspired and experience learned from
 
* https://www.mkyong.com/
* https://www.baeldung.com/
* https://dzone.com/
* https://docs.spring.io
* https://www.elastic.co/guide/index.html

Created: December 2018