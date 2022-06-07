package cz.muni.ics.watchdog;

import cz.muni.ics.core.exceptions.ApplicationException;
import cz.muni.ics.watchdog.exceptions.WatchdogInitializationException;
import cz.muni.ics.watchdog.exceptions.WatchdogMessages;
import cz.muni.ics.watchdog.init.ModuleInitialisation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Module entry point
 *
 * @author Jozef Cibík
 */

@Slf4j
@SpringBootApplication(scanBasePackages = {"cz.muni.ics.watchdog"})
public class WatchdogApp implements CommandLineRunner {

    @Value("${startup.initialization}")
    private boolean initialise;

    private final ModuleInitialisation initialisator;

    @Autowired
    public WatchdogApp(ModuleInitialisation initialisator) { this.initialisator = initialisator; }

//    public static void main(String[] args) {
//        SpringApplication.run(WatchdogApp.class, args);
//    }

    @Override
    public void run(String... args) throws IOException {
        if (initialise) initialisator.initializeModule();
        log.info(WatchdogMessages.INFO_APP_STARTED);
    }
}
