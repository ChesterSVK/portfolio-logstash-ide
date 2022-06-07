package cz.muni.ics.elasticsearch.interfaces;

import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Service;

public interface ElasticsearchHealthService {
    Health checkElasticsearchHealth();
}