package cz.muni.ics.bro;

import cz.muni.ics.bro.utils.BroExecutor;
import cz.muni.ics.core.exceptions.ApplicationException;
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
@SpringBootApplication(scanBasePackages = {"cz.muni.ics.bro"})
public class BroApplication implements CommandLineRunner {

    @Value("${startup.initialization}")
    private boolean initialise;

    private final BroExecutor executor;

    @Autowired
    public BroApplication(BroExecutor executor) {
        this.executor = executor;
    }

//    public static void main(String[] args) {
//        SpringApplication.run(BroApplication.class, args);
//    }

    @Override
    public void run(String... args) throws IOException, ApplicationException {
        assert !initialise || executor.executeInitCommand() == 0;
        log.info("Module bro successfully loaded.");
    }
}
