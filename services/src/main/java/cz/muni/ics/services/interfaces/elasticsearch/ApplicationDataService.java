package cz.muni.ics.services.interfaces.elasticsearch;

import cz.muni.ics.elasticsearch.dataclasses.ApplicationData;
import cz.muni.ics.services.implementations.web.dto.ApplicationDataDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface ApplicationDataService {
    ApplicationData saveApplicationData(ApplicationDataDto applicationDataDto);
    void deleteApplicationData(ApplicationDataDto applicationDataDto);
    ApplicationData findApplicationDataById(String id);
//    Page<ApplicationData> findApplicationDataByDateCreated(LocalDateTime date);
    Page<ApplicationData> findApplicationDataByLogstashCommandId(String id);
    Page<ApplicationData> findApplicationDataByType(String type);
    Page<ApplicationData> findApplicationDataByDateCreatedBefore(LocalDateTime date);
    Page<ApplicationData> findApplicationDataByDateCreatedAfter(LocalDateTime date);
    Iterable<ApplicationData> findAllApplicationData();
    void deleteAllApplicationData();
}
