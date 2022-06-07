package cz.muni.ics.services.interfaces.logstash;

import cz.muni.ics.elasticsearch.dataclasses.LogstashCommandData;
import cz.muni.ics.logstash.commands.commandsconfig.LogstashCommandsConfig;
import cz.muni.ics.services.enums.ConvertType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.List;

public interface LogstashService {

    void handleRequest(HttpServletRequest request, HttpServletResponse response);
    void reset();
    void parseLogstashCommands(LogstashCommandData data);
    String getLogstashConfigurationAsJson();
    LogstashCommandsConfig getLogstashConfiguration();
    boolean executeLogstashUpload(List<Path> processedFiles, LogstashCommandsConfig logstashConfiguration);

    void setProcessedFileType(ConvertType type);
}
