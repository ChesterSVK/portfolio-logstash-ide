package cz.muni.ics.bro.config;

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
@ComponentScan(basePackages = "cz.muni.ics.bro")
@PropertySource("classpath:application-bro.properties")
@Import(CoreConfig.class)
public class BroConfig {}
