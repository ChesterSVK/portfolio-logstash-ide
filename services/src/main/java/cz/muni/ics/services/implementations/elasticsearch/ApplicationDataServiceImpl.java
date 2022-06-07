package cz.muni.ics.services.implementations.elasticsearch;

import cz.muni.ics.elasticsearch.dataclasses.ApplicationData;
import cz.muni.ics.elasticsearch.repositories.ApplicationDataRepository;
import cz.muni.ics.elasticsearch.validators.ApplicationDataValidator;
import cz.muni.ics.elasticsearch.utils.LocalDateTimeFormatterUtil;
import cz.muni.ics.elasticsearch.validators.StringValidator;
import cz.muni.ics.services.implementations.web.dto.ApplicationDataDto;
import cz.muni.ics.services.interfaces.elasticsearch.ApplicationDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ApplicationDataServiceImpl implements ApplicationDataService {

    private final StringValidator stringValidator;
    private final ApplicationDataValidator applicationDataValidator;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Resources

    @Resource
    private ApplicationDataRepository repository;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public ApplicationDataServiceImpl(ApplicationDataValidator applicationDataValidator, StringValidator stringValidator) {
        this.applicationDataValidator = applicationDataValidator;
        this.stringValidator = stringValidator;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Overrides

    @Override
    public ApplicationData saveApplicationData(ApplicationDataDto applicationDataDto) {
        ApplicationData applicationData = getApplicationData(applicationDataDto);
        applicationDataValidator.validate(applicationData);
        log.debug("Saving applicationData {}", applicationData);
        return repository.save(applicationData);
    }

    @Override
    public void deleteApplicationData(ApplicationDataDto applicationDataDto) {
        ApplicationData applicationData = getApplicationData(applicationDataDto);
        log.debug("Deleting applicationData {}", applicationData);
        repository.delete(applicationData);
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationData findApplicationDataById(String id) {
        stringValidator.validate(id);
        log.debug("Searching for applicationData with id {}", id);
        Optional<ApplicationData> found = repository.findById(id);
        return found.orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationData> findApplicationDataByLogstashCommandId(String id) {
        log.debug("Searching for applicationData logstashCommand with id {}", id);
        return repository.findApplicationDataByLogstashCommandId(id, Pageable.unpaged());
    }

    @Override
    public Page<ApplicationData> findApplicationDataByType(String type) {
        log.debug("Searching for applicationData with type {}", type);
        return repository.findApplicationDataByType(type, Pageable.unpaged());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationData> findApplicationDataByDateCreatedBefore(LocalDateTime date) {
        log.debug("Searching for applicationData with date created before {}", LocalDateTimeFormatterUtil.getNormalizedDate(date));
        return repository.findApplicationDataByDateCreatedBefore(LocalDateTimeFormatterUtil.getNormalizedDate(date), Pageable.unpaged());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ApplicationData> findApplicationDataByDateCreatedAfter(LocalDateTime date) {
        log.debug("Searching for applicationData with date created after {}", LocalDateTimeFormatterUtil.getNormalizedDate(date));
        return repository.findApplicationDataByDateCreatedAfter(LocalDateTimeFormatterUtil.getNormalizedDate(date), Pageable.unpaged());
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<ApplicationData> findAllApplicationData() {
        log.debug("Searching for all applicationData");
        return repository.findAll();
    }

    @Override
    @Transactional
    public void deleteAllApplicationData() {
        log.debug("Deleting all applicationData");
        repository.deleteAll();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private ApplicationData getApplicationData(ApplicationDataDto dataDto) {
        ApplicationData data = new ApplicationData();
        data.setId(dataDto.getId());
        data.setType(dataDto.getType().name().toLowerCase());
        data.setFigureNote(dataDto.getFigureNote());
        data.setLogstashCommandId(dataDto.getLogstashCommandId());
        return data;
    }

}
