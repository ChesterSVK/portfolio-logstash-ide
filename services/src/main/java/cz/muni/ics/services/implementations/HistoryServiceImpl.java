package cz.muni.ics.services.implementations;

import cz.muni.ics.elasticsearch.dataclasses.ApplicationData;
import cz.muni.ics.services.implementations.web.dto.ApplicationDataDto;
import cz.muni.ics.services.interfaces.HistoryService;
import cz.muni.ics.services.interfaces.elasticsearch.ApplicationDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class HistoryServiceImpl implements HistoryService {

    private final ApplicationDataService applicationDataService;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public HistoryServiceImpl(ApplicationDataService applicationDataService) {
        this.applicationDataService = applicationDataService;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public List<ApplicationData> getAllSavedApplicationData() {
        Iterator<ApplicationData> iterable = applicationDataService.findAllApplicationData().iterator();
        return IteratorUtils.toList(iterable);
    }

    @Override
    public void deleteApplicationData(ApplicationDataDto dataDto) {
        applicationDataService.deleteApplicationData(dataDto);
    }

    @Override
    public File downloadLogstashCommands(ApplicationDataDto applicationDataDto) {
        return null;
    }
}
