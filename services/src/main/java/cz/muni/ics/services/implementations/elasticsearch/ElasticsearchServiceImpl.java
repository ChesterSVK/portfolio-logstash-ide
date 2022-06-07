package cz.muni.ics.services.implementations.elasticsearch;

import cz.muni.ics.elasticsearch.implementations.ElasticsearchHealthCheckService;
import cz.muni.ics.elasticsearch.interfaces.ElasticsearchHealthService;
import cz.muni.ics.services.interfaces.elasticsearch.ApplicationDataService;
import cz.muni.ics.services.interfaces.elasticsearch.ElasticsearchService;
import cz.muni.ics.services.interfaces.elasticsearch.LogstashCommandDataService;
//import cz.muni.ics.services.interfaces.elasticsearch.UserDataService;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Data
@Service
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Setter(AccessLevel.NONE)
    private Client client;
    @Setter(AccessLevel.NONE)
    private final ApplicationDataService applicationDataService;
    @Setter(AccessLevel.NONE)
    private final LogstashCommandDataService logstashCommandDataService;
    @Setter(AccessLevel.NONE)
    private final ElasticsearchHealthService healthService;
//    @Setter(AccessLevel.NONE)
//    private final UserDataService userDataService;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public ElasticsearchServiceImpl(Client client, ApplicationDataService applicationDataService, LogstashCommandDataService logstashCommandDataService) {
        this.client = client;
        this.healthService = new ElasticsearchHealthCheckService(client);
        this.applicationDataService = applicationDataService;
        this.logstashCommandDataService = logstashCommandDataService;
//        this.userDataService = userDataService;
    }
}
