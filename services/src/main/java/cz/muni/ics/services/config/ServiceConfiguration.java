package cz.muni.ics.services.config;

import cz.muni.ics.bro.config.BroConfig;
import cz.muni.ics.elasticsearch.config.EsConfig;
import cz.muni.ics.logstash.config.LogstashModuleConfig;
import cz.muni.ics.watchdog.config.WatchdogAppConfig;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Spring configuration class for Service module
 *
 * @author Jozef Cibík
 */

@Configuration
@PropertySource({"classpath:application-service.properties"})
@Import({ LogstashModuleConfig.class, WatchdogAppConfig.class, EsConfig.class, BroConfig.class})
@ComponentScan(basePackages = {"cz.muni.ics.services", "cz.muni.ics.elasticsearch", "cz.muni.ics.watchdog", "cz.muni.ics.bro", })
public class ServiceConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
