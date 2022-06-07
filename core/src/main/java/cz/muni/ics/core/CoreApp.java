package cz.muni.ics.core;

import cz.muni.ics.core.dataclasses.ExecutorData;
import cz.muni.ics.core.exceptions.ApplicationMessages;
import cz.muni.ics.core.os.SystemCommandExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Module entry point
 *
 * @author Jozef Cibík
 */

@Slf4j
@SpringBootApplication(scanBasePackages = {"cz.muni.ics.core"})
public class CoreApp implements CommandLineRunner {

//    public static void main(String[] args) { SpringApplication.run(CoreApp.class, args); }

    @Override
    public void run(String... args) {
        log.info(ApplicationMessages.INFO_APP_STARTED);
    }
}
