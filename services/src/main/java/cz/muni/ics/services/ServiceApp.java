package cz.muni.ics.services;

import com.fabriceci.fmc.error.FileManagerException;
import cz.muni.ics.elasticsearch.interfaces.ElasticsearchHealthService;
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
@SpringBootApplication(scanBasePackages = {"cz.muni.ics.services"})
public class ServiceApp implements CommandLineRunner {

    @Value("${startup.initialization}")
    private boolean initialise;

    private final ServiceInitialisation initialisator;

    @Autowired
    public ServiceApp(ServiceInitialisation initialisator) { this.initialisator = initialisator; }

    public static void main(String[] args) {
        SpringApplication.run(ServiceApp.class, args);
    }

    @Override
    public void run(String... args) throws FileManagerException {
        if (initialise) initialisator.initialize();
        log.info("Module services successfully loaded.");
    }
}