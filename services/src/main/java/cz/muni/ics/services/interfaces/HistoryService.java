package cz.muni.ics.services.interfaces;

import cz.muni.ics.elasticsearch.dataclasses.ApplicationData;
import cz.muni.ics.services.implementations.web.dto.ApplicationDataDto;

import java.io.File;
import java.util.List;

public interface HistoryService {
    List<ApplicationData> getAllSavedApplicationData();
    void deleteApplicationData(ApplicationDataDto dataDto);
    File downloadLogstashCommands(ApplicationDataDto applicationDataDto);
}
