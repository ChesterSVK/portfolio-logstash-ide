package cz.muni.ics.services.interfaces.elasticsearch;

import cz.muni.ics.elasticsearch.dataclasses.LogstashCommandData;
import cz.muni.ics.services.implementations.web.dto.LogstashCommandDataDto;
import org.springframework.transaction.annotation.Transactional;

public interface LogstashCommandDataService {
    LogstashCommandData saveLogstashCommandData(LogstashCommandDataDto commandDataDto);
    void deleteLogstashCommandData(String id);
    void deleteAllLogstashCommandData();
    LogstashCommandData updateLogstashCommandData(LogstashCommandDataDto commandDataDto);
    LogstashCommandData findLogstashDataById(LogstashCommandDataDto commandDataDto);
    LogstashCommandData findLogstashDataById(String commandDataId);

    Iterable<LogstashCommandData> findAllLogstashCommandData();
}
