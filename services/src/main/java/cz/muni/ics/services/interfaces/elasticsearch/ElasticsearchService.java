package cz.muni.ics.services.interfaces.elasticsearch;

import cz.muni.ics.elasticsearch.dataclasses.ApplicationData;
import cz.muni.ics.elasticsearch.dataclasses.LogstashCommandData;
import cz.muni.ics.elasticsearch.interfaces.ElasticsearchHealthService;
import cz.muni.ics.services.enums.ConvertType;
import cz.muni.ics.services.implementations.web.dto.ApplicationDataDto;
import cz.muni.ics.services.implementations.web.dto.LogstashCommandDataDto;
import lombok.AccessLevel;
import lombok.Setter;
import org.elasticsearch.client.Client;
import org.springframework.boot.actuate.health.Health;

import java.util.List;

public interface ElasticsearchService {
    ApplicationDataService getApplicationDataService();
    LogstashCommandDataService getLogstashCommandDataService();
    ElasticsearchHealthService getHealthService();
//    UserDataService getUserDataService();
}
