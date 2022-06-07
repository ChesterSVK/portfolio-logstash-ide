package cz.muni.ics.elasticsearch.config;


import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Spring configuration class for Elasticsearch module
 *
 * @author Jozef Cibík
 */

@Configuration
@PropertySource("classpath:application-elasticsearch.properties")
@EnableElasticsearchRepositories(basePackages = "cz.muni.ics.elasticsearch")
public class EsConfig {

    @Value("${elasticsearch.home}")
    private String elasticsearchHome;

    @Value("${elasticsearch.cluster}")
    private String clusterName;

    @Value("${elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${elasticsearch.port}")
    private Integer elasticsearchPort;

    @Value("${elasticsearch.app.data.index}")
    private String appDataIndex;

    @Value("${elasticsearch.app.commands.index}")
    private String appCommandsIndex;

    @Value("${elasticsearch.app.user.index}")
    private String appUserIndex;

    @Value("${fail.on.elasticsearch.down}")
    private String failOnESDown;

    @Bean
    public String appDataIndex() {
        return appDataIndex;
    }

    @Bean
    public String appUserIndex() { return appUserIndex; }

    @Bean
    public Boolean failOnElasticSearchDown() { return Boolean.valueOf(failOnESDown); }

    @Bean
    public String appCommandsIndex() {
        return appCommandsIndex;
    }

    @Bean
    public Client client() throws UnknownHostException {
        Settings elasticsearchSettings = Settings.builder()
                .put("client.transport.sniff", true)
                .put("path.home", elasticsearchHome)
                .put("cluster.name", clusterName).build();
        TransportClient client = new PreBuiltTransportClient(elasticsearchSettings);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(elasticsearchHost), elasticsearchPort));
        return client;
    }


    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() throws UnknownHostException {
//        ElasticsearchOperations operations = new ElasticsearchTemplate(client());
//        operations.createIndex(ApplicationData.class);
//        operations.createIndex(LogstashCommandData.class);
//        //Feature disabled
////        operations.createIndex(UserData.class);
//        return operations;
//    }
}

