package cz.muni.ics.services.implementations.elasticsearch;

import cz.muni.ics.elasticsearch.dataclasses.LogstashCommandData;
import cz.muni.ics.elasticsearch.repositories.LogstashCommandDataRepository;
import cz.muni.ics.elasticsearch.validators.LogstashCommandDataValidator;
import cz.muni.ics.services.implementations.web.dto.LogstashCommandDataDto;
import cz.muni.ics.services.interfaces.elasticsearch.LogstashCommandDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class LogstashCommandDataServiceImpl implements LogstashCommandDataService {

    private final LogstashCommandDataValidator validator;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Resources

    @Resource
    private LogstashCommandDataRepository repository;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public LogstashCommandDataServiceImpl(LogstashCommandDataValidator validator) {
        this.validator = validator;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Overrides

    @Override
    @Transactional
    public LogstashCommandData saveLogstashCommandData(LogstashCommandDataDto commandDataDto) {
        LogstashCommandData commandData = getDataFromDto(commandDataDto);
        validator.validate(commandData);
        log.debug("Saving logstashCommandData {}", commandData);
        return repository.save(commandData);
    }

    @Override
    @Transactional
    public void deleteLogstashCommandData(String id) {
        log.debug("Deleting logstashCommandData with id {}", id);
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllLogstashCommandData() {
        log.debug("Deleting all logstashCommandData");
        repository.deleteAll();
    }

    @Override
    @Transactional
    public LogstashCommandData updateLogstashCommandData(LogstashCommandDataDto commandDataDto) {
        LogstashCommandData commandData = getDataFromDto(commandDataDto);
        log.debug("Updating logstashCommandData {}", commandData);
        Optional<LogstashCommandData> found = repository.findById(commandData.getId());
        if (found.isPresent()){
            log.debug("LogstashCommandData found: {}", found.get());
            repository.save(commandData);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public LogstashCommandData findLogstashDataById(LogstashCommandDataDto commandDataDto) {
        LogstashCommandData commandData = getDataFromDto(commandDataDto);
        log.debug("Searching for logstashCommandData with id {}", commandData.getId());
        Optional<LogstashCommandData> found = repository.findById(commandData.getId());
        log.debug("LogstashCommandData found: {}", found.isPresent());
        return found.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public LogstashCommandData findLogstashDataById(String commandDataId) {
        log.debug("Searching for logstashCommandData with id {}", commandDataId);
        Optional<LogstashCommandData> found = repository.findById(commandDataId);
        log.debug("LogstashCommandData found: {}", found.isPresent());
        return found.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<LogstashCommandData> findAllLogstashCommandData() {
        log.debug("Searching for all logstashCommands");
        return repository.findAll();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private LogstashCommandData getDataFromDto(LogstashCommandDataDto dataDto) {
        LogstashCommandData data = new LogstashCommandData();
        data.setId(dataDto.getId());
        data.setLogstashRootCommands(dataDto.getLogstashRootCommands());
        return data;
    }
}
