package cz.muni.ics.logstash.interfaces;

import cz.muni.ics.logstash.commands.commandsconfig.LogstashCommandsConfig;
import cz.muni.ics.logstash.exception.LogstashException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface ILogstashManager {
    void handleRequest(HttpServletRequest request, HttpServletResponse response);
    // GET
    Object actionDeleteFunction(HttpServletRequest request);
    Object actionGetUsedFunctions();
    Object actionGetRootCommands();
    Object actionGetNodeCommands(HttpServletRequest rootCommandName);
    Object actionGetLeafCommands(HttpServletRequest nodeCommandName);
    Object actionAddFunction(HttpServletRequest request) throws LogstashException;
    Object actionRemoveAllFunctions();
    Object actionGetDefaultTransformations();
    Object actionGetPredefinedTransformations();
    void parseCommandsToInitialConfiguration(List<String> logstashRootCommands);
    String getConfigurationJson();
    LogstashCommandsConfig getConfiguration();
    void setInputFilesType(String convertFileType);
    String getInputFilesType();
}
