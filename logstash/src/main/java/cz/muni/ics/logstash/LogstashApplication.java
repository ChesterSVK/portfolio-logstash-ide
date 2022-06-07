package cz.muni.ics.logstash;

import cz.muni.ics.logstash.commands.leafs.categories.CommonsLeaf;
import cz.muni.ics.logstash.utils.LogstashExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Module entry point
 *
 * @author Jozef Cibík
 */

@Slf4j
@SpringBootApplication(scanBasePackages = {"cz.muni.ics.logstash"})
public class LogstashApplication implements CommandLineRunner {

    @Value("${startup.initialization}")
    private boolean initialise;

    private final LogstashExecutor executor;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public LogstashApplication(LogstashExecutor executor) {
        this.executor = executor;
    }

//    public static void main(String[] args) {
//        SpringApplication.run(LogstashApplication.class, args);
//    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public void run(String... args) throws IOException {
        assert !initialise || executor.executeInitCommand() == 0;
        log.info("Module logstash successfully loaded.");
    }
}