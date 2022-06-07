package cz.muni.ics.logstash.config;


import cz.muni.ics.core.config.CoreConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * Spring configuration class for Logstash module
 *
 * @author Jozef Cibík
 */

@Configuration
@Import(CoreConfig.class)
@ComponentScan(basePackages = "cz.muni.ics.logstash")
@PropertySource("classpath:application-logstash.properties")
public class LogstashModuleConfig {

    @Value("${logstash.binary.path}")
    private String logstashBinPath;

    @Value("${logstash.config.test.command}")
    private String configTestCommand;
}

