package cz.muni.ics.elasticsearch;

import cz.muni.ics.elasticsearch.exceptions.ElasticsearchInitializationException;
import cz.muni.ics.elasticsearch.utils.InitializationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Module entry point
 *
 * @author Jozef Cibík
 */

@Slf4j
@SpringBootApplication(scanBasePackages = {"cz.muni.ics.elasticsearch"})
public class ElasticsearchApplication implements CommandLineRunner {

    @Value("${startup.initialization}")
    private boolean initialise;

    private final InitializationService initialisator;

    @Autowired
    public ElasticsearchApplication(InitializationService initialisator) { this.initialisator = initialisator; }

//    public static void main(String[] args) {
//        SpringApplication.run(ElasticsearchApplication.class, args);
//    }

    @Override
    public void run(String... args) throws ElasticsearchInitializationException {
        if (initialise) initialisator.initialize();
        log.info("Module elastisearch successfully loaded.");
    }
}