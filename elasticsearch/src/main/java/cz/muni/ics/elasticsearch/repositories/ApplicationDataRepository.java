package cz.muni.ics.elasticsearch.repositories;

import cz.muni.ics.elasticsearch.dataclasses.ApplicationData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationDataRepository extends ElasticsearchRepository<ApplicationData, String> {
    Page<ApplicationData> findApplicationDataByLogstashCommandId(String logstashCommandId, Pageable pageable);
    Page<ApplicationData> findApplicationDataByType(String type, Pageable pageable);
    Page<ApplicationData> findApplicationDataByDateCreatedBefore(String date, Pageable pageable);
    Page<ApplicationData> findApplicationDataByDateCreatedAfter(String date, Pageable pageable);
}
