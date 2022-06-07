package cz.muni.ics.elasticsearch.implementations;

import cz.muni.ics.elasticsearch.interfaces.ElasticsearchHealthService;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.elasticsearch.ElasticsearchHealthIndicator;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@PropertySource("classpath:application-elasticsearch.properties")
public class ElasticsearchHealthCheckService implements ElasticsearchHealthService {

    private final Client client;

    @Value("${elasticsearch.response.timeout}")
    private int responseTimeout = 14000;

    @Autowired
    public ElasticsearchHealthCheckService(Client client) {
        this.client = client;
    }

    public Health checkElasticsearchHealth() {
        AbstractHealthIndicator indicator =
                new ElasticsearchHealthIndicator(client, responseTimeout, new ArrayList<>());

        return indicator.health();
    }
}