package cz.muni.ics.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Module entry point
 *
 * @author Jozef Cibík
 */

@Slf4j
@SpringBootApplication(scanBasePackages = {"cz.muni.ics.web"})
public class WebApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("Module web successfully loaded.");
    }
}
