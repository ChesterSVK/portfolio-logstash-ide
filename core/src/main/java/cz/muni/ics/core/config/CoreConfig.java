package cz.muni.ics.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Spring configuration class for Core module
 *
 * @author Jozef Cibík
 */

@Configuration
@ComponentScan(basePackages = "cz.muni.ics.core")
@PropertySource("classpath:application-core.properties")
public class CoreConfig {}

