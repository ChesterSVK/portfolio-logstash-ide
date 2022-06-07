package cz.muni.ics.services.implementations;

import cz.muni.ics.elasticsearch.dataclasses.ApplicationData;
import cz.muni.ics.elasticsearch.dataclasses.LogstashCommandData;
import cz.muni.ics.logstash.commands.commandsconfig.LogstashCommandsConfig;
import cz.muni.ics.logstash.interfaces.LogstashSerializable;
import cz.muni.ics.services.converters.ConvertManager;
import cz.muni.ics.services.dataclasses.ImportProcessObject;
import cz.muni.ics.services.exceptions.ServiceException;
import cz.muni.ics.services.implementations.web.dto.ApplicationDataDto;
import cz.muni.ics.services.implementations.web.dto.ImportFileDto;
import cz.muni.ics.services.enums.ConvertType;
import cz.muni.ics.services.enums.ImportState;
import cz.muni.ics.services.exceptions.ImportException;
import cz.muni.ics.services.exceptions.ServiceMessages;
import cz.muni.ics.services.implementations.web.dto.LogstashCommandDataDto;
import cz.muni.ics.services.interfaces.*;
import cz.muni.ics.services.interfaces.elasticsearch.ElasticsearchService;
import cz.muni.ics.services.interfaces.watchdog.WatchDogService;
import cz.muni.ics.services.interfaces.logstash.LogstashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ImportServiceImpl implements ImportService {

    private final ConvertManager convertManager;
    private final LogstashService logstashService;
    private final WatchDogService watchDogService;
    private final FileImportService fileImportService;
    private final ElasticsearchService elasticsearchService;

    private String usedLogstashCommandsId;
    private ImportState state = ImportState.NOT_INITIALIZED;
    private ImportProcessObject processObject = new ImportProcessObject();
    private Set<String> defaultAttributes = new HashSet<>(Arrays.asList("@version", "host", "message", "path"));

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public ImportServiceImpl(
            WatchDogService watchDogService,
            FileImportService fileImportService,
            LogstashService logstashService,
            ConvertManager convertManager,
            ElasticsearchService elasticsearchService) {

        this.watchDogService = watchDogService;
        this.fileImportService = fileImportService;
        this.logstashService = logstashService;
        this.convertManager = convertManager;
        this.elasticsearchService = elasticsearchService;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Overrides

    @Override
    public void resetImport() {
        this.resetToImportFiles();

        this.state = ImportState.NOT_INITIALIZED;

        this.processObject.clearOriginalFiles();
        watchDogService.reset();
        fileImportService.reset();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////    Getters & Setters

    @Override
    public List<String> getWorkingFiles() {
        return this.fileImportService.getProcessedFiles().stream().map(Path::toString).collect(Collectors.toList());
    }

    @Override
    public List<String> getProcessedFiles() {
        return this.processObject.getProcessedFiles().stream().map(Path::toString).collect(Collectors.toList());
    }

    @Override
    public ConvertType getConvertType() throws ImportException {
        if (this.processObject.getConvertType() == null) {
            throw new ImportException(ServiceMessages.ERROR_NULL_IMPORT_TYPE);
        }
        return this.processObject.getConvertType();
    }

    @Override
    public void setState(ImportState state) {
        this.state = state;
    }

    @Override
    public ImportState getState() {
        return this.state;
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////    FileImport

    @Override
    public List<ImportFileDto> getOriginalFiles() {
        return new ArrayList<>(this.processObject.getOriginalFiles());
    }

    @Override
    public List<ConvertType> importFiles(List<ImportFileDto> files) {
        this.processObject.getOriginalFiles().clear();
        this.processObject.getOriginalFiles().addAll(files);

        fileImportService.processFiles(files);
        watchDogService.processFiles(files);

        return getProcessedTypes(fileImportService.getProcessedFiles());
    }

    @Override
    public void resetToImportFiles() {
        this.resetToConvert();

        this.processObject.resetConvertType();
        this.setState(ImportState.FILES_IMPORTING);
    }

    @Override
    public void chooseType(String type) {
        this.fileImportService.filterType(ConvertType.valueOf(type));
        this.processObject.setConvertType(ConvertType.valueOf(type));
        this.logstashService.setProcessedFileType(ConvertType.valueOf(type));
        this.state = ImportState.FILES_CONVERTING;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////    Convert

    @Override
    public void resetToConvert() {
        this.resetToTransform();

        this.logstashService.reset();
        this.setState(ImportState.FILES_CONVERTING);
    }

    @Override
    public void convertPaths(List<String> paths) throws ImportException {
        ConvertService service = getConvertService();
        executeConversion(service, paths);
    }

    @Override
    public List<ApplicationData> getSavedApplicationData(ConvertType convertType) {
        return elasticsearchService.getApplicationDataService().
                findApplicationDataByType(convertType.name()).stream().collect(Collectors.toList());
    }

    @Override
    public void loadLogstashTransformation(String logstashCommandId) {
        this.usedLogstashCommandsId = logstashCommandId;
        LogstashCommandData data = elasticsearchService.getLogstashCommandDataService().
                findLogstashDataById(logstashCommandId);
        if (data != null){
            logstashService.parseLogstashCommands(data);
        }
        else log.debug("LogstashCommandData was null");
    }

    @Override
    public void deleteLogstashTransformation(ApplicationDataDto applicationDataDto) {
        elasticsearchService.getApplicationDataService()
                .deleteApplicationData(applicationDataDto);
        elasticsearchService.getLogstashCommandDataService()
                .deleteLogstashCommandData(applicationDataDto.getLogstashCommandId());
    }

    @Override
    public List<String> getFoldersToWatch() {
        return watchDogService.getFoldersToWatch().stream().map(Path::toString).collect(Collectors.toList());
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////    Transform

    @Override
    public void resetToTransform() {
        this.setState(ImportState.TRANSFORMING);
    }

    @Override
    public Set<String> getParsedAttributes(){
        return new HashSet<>(this.processObject.getParsedAttributes());
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        logstashService.handleRequest(request, response);
    }

    @Override
    public String getLogstashConfigurationAsJson() {
        return this.logstashService.getLogstashConfigurationAsJson();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////    Summary

    @Override
    public void resetToSummary() {
        this.setState(ImportState.SUMMARY);
    }

    @Override
    public boolean uploadToEs(ApplicationDataDto applicationDataDto) throws ServiceException {
        if (applicationDataDto.isWatchFolders()){
            executeWatchingFolders();
        }
        if (applicationDataDto.isSaveLogstashConfig()){
            String savedCommandsId = executeSaveLogstashCommands();
            executeSaveApplicationData(applicationDataDto, savedCommandsId);
        }
        return upload();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private boolean upload() throws ServiceException {
        if (processObject.getProcessedFiles().isEmpty()){
            throw new ServiceException("No processed files were given");
        }
        return logstashService.executeLogstashUpload(processObject.getProcessedFiles(), logstashService.getLogstashConfiguration());
    }

    private ConvertService getConvertService() throws ImportException {
        if (this.getConvertType() == null){
            throw new ImportException("Conversion type was not set");
        }
        try {
            return dynamicallyCreateService(this.getConvertType());
        } catch (ServiceException e) {
            throw new ImportException("Conversion of files failed", e);
        }
    }

    private void executeConversion(ConvertService service, List<String> paths) {
        List<Path> outputPaths = service.convert(paths);
        List<Path> convertedFilesPaths = service.getOutputFiles(outputPaths);
        this.processObject.setProcessedFiles(convertedFilesPaths);
        this.processObject.setParsedAttributes(service.parseAttributes(convertedFilesPaths));
        this.processObject.getParsedAttributes().addAll(defaultAttributes);
    }


    private void executeSaveApplicationData(ApplicationDataDto applicationDataDto, String savedCommandsId) {
        applicationDataDto.setLogstashCommandId(savedCommandsId);
        elasticsearchService.getApplicationDataService().saveApplicationData(applicationDataDto);
    }

    private String executeSaveLogstashCommands() {
        log.debug("Saving logstashCommands");
        LogstashCommandsConfig commandsConfig = logstashService.getLogstashConfiguration();
        LogstashCommandDataDto commandDataDto = createFromConfig(commandsConfig);
        LogstashCommandData savedLogstashCommandData = elasticsearchService.getLogstashCommandDataService()
                .saveLogstashCommandData(commandDataDto);
        log.debug("Saved logstashCommands with id:" + commandDataDto.getId());
        return savedLogstashCommandData.getId();
    }

    private void executeWatchingFolders() {
        log.debug("Registering folders to watch.");
        watchDogService.registerFoldersToWatch(this.processObject.getConvertType());
    }

    private LogstashCommandDataDto createFromConfig(LogstashCommandsConfig commandsConfig) {
        LogstashCommandDataDto dataDto = new LogstashCommandDataDto();
        if (wasUsedSavedLogstashCommandsConfig()){
            dataDto.setId(this.usedLogstashCommandsId);
        }
        dataDto.setLogstashRootCommands(
                commandsConfig.stream().map(LogstashSerializable::toJsonString)
                        .collect(Collectors.toList())
        );
        return dataDto;
    }

    private boolean wasUsedSavedLogstashCommandsConfig() {
        return this.usedLogstashCommandsId != null && !this.usedLogstashCommandsId.isEmpty();
    }

    private List<ConvertType> getProcessedTypes(PathsCollection processedFiles) {
        List<ConvertType> res = new ArrayList<>();
        for (ConvertType type : ConvertType.values()) {
            if (processedFiles.containsExtension(type.toString())) {
                res.add(type);
            }
        }
        return res;
    }

    private ConvertService dynamicallyCreateService(ConvertType type) throws ServiceException {
        return convertManager.getConvertService(type);
    }
}
