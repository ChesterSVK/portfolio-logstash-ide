package cz.muni.ics.watchdog.config;

import cz.muni.ics.core.config.CoreConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Spring configuration class for Bro module
 *
 * @author Jozef Cibík
 */

@Configuration
@Import(CoreConfig.class)
@ComponentScan(basePackages = "cz.muni.ics.watchdog")
@PropertySource("classpath:application-watchdog.properties")
public class WatchdogAppConfig {}

