package cz.muni.ics.elasticsearch.utils;

import cz.muni.ics.elasticsearch.exceptions.ElasticsearchInitializationException;
import cz.muni.ics.elasticsearch.interfaces.ElasticsearchHealthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitializationService {

    private final ElasticsearchHealthService service;

    @Value("${fail.on.elasticsearch.down}")
    private boolean failOnESDOwn = false;

    @Autowired
    public InitializationService(ElasticsearchHealthService service) {
        this.service = service;
    }

    public void initialize() throws ElasticsearchInitializationException {
        String statusCode = service.checkElasticsearchHealth().getStatus().getCode();
        if (!statusCode.equals("UP")) {
            if (failOnESDOwn)
                throw new ElasticsearchInitializationException("Elasticsearch cluster status: " + statusCode);
            else
                log.warn("Elasticsearch is down during elasticsearch module initialization.");
        }
    }
}
