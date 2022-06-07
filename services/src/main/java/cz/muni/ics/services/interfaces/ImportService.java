package cz.muni.ics.services.interfaces;

import cz.muni.ics.elasticsearch.dataclasses.ApplicationData;
import cz.muni.ics.services.exceptions.ServiceException;
import cz.muni.ics.services.implementations.web.dto.ApplicationDataDto;
import cz.muni.ics.services.implementations.web.dto.ImportFileDto;
import cz.muni.ics.services.enums.ConvertType;
import cz.muni.ics.services.enums.ImportState;
import cz.muni.ics.services.exceptions.ImportException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

public interface ImportService {

    //Reset
    void resetImport();
    void resetToImportFiles();
    void resetToConvert();
    void resetToTransform();
    void resetToSummary();

    //Getters and Setters
    void setState(ImportState state);
    ImportState getState();
    List<ImportFileDto> getOriginalFiles();

    List<String> getProcessedFiles();

    ConvertType getConvertType() throws ImportException;
    List<String> getWorkingFiles();
    Set<String> getParsedAttributes();
    //Import
    List<ConvertType> importFiles(List<ImportFileDto> files);
    void chooseType(String type);
    //Convert
    List<ApplicationData> getSavedApplicationData(ConvertType convertType);
    void convertPaths(List<String> paths) throws ImportException;
    void loadLogstashTransformation(String logstashCommandId);
    //Transform
    void handleRequest(HttpServletRequest request, HttpServletResponse response);
    String getLogstashConfigurationAsJson();
    //Summary
    boolean uploadToEs(ApplicationDataDto dataDto) throws ServiceException;

    void deleteLogstashTransformation(ApplicationDataDto id);

    List<String> getFoldersToWatch();
}
