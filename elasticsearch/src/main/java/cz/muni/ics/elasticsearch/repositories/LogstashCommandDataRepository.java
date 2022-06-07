package cz.muni.ics.elasticsearch.repositories;

import cz.muni.ics.elasticsearch.dataclasses.LogstashCommandData;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;

@Repository
public interface LogstashCommandDataRepository extends ElasticsearchRepository<LogstashCommandData, String> {
    void deleteById(String id);
}
