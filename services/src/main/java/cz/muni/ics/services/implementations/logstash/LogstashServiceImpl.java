package cz.muni.ics.services.implementations.logstash;

import cz.muni.ics.core.dataclasses.ExecutorData;
import cz.muni.ics.elasticsearch.dataclasses.LogstashCommandData;
import cz.muni.ics.logstash.commands.commandsconfig.LogstashCommandsConfig;
import cz.muni.ics.logstash.interfaces.ILogstashManager;
import cz.muni.ics.logstash.utils.LogstashExecutor;
import cz.muni.ics.services.enums.ConvertType;
import cz.muni.ics.services.interfaces.logstash.LogstashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LogstashServiceImpl implements LogstashService {

    @Value("${services.root.output.directory.home}")
    private String workspacePath;
    @Value("${services.logstash.output.directory.home}")
    private String logstashDirName;

    private final LogstashExecutor executor;
    private final ILogstashManager logstashManager;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public LogstashServiceImpl(LogstashExecutor executor, ILogstashManager logstashManager) {
        this.executor = executor;
        this.logstashManager = logstashManager;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Overrides

//    @Override
//    public boolean executeLogstashCommand(int expectedResultValue, List<String> commands) {
//        try {
//            return executor.executeCommand(commands) == expectedResultValue;
//        } catch (IOException e) {
//            log.error(String.valueOf(e));
//        }
//        return false;
//    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        logstashManager.handleRequest(request, response);
    }

    @Override
    public void reset() {
        logstashManager.actionRemoveAllFunctions();
        logstashManager.setInputFilesType("");
    }

    @Override
    public void parseLogstashCommands(LogstashCommandData data) {
        logstashManager.parseCommandsToInitialConfiguration(data.getLogstashRootCommands());
    }

    @Override
    public String getLogstashConfigurationAsJson() {
        return logstashManager.getConfigurationJson();
    }

    @Override
    public LogstashCommandsConfig getLogstashConfiguration() {
        return logstashManager.getConfiguration();
    }

    @Override
    public boolean executeLogstashUpload(List<Path> processedFiles, LogstashCommandsConfig logstashConfiguration) {
        try {
            return executor.
                    executePipedCommands(
                            getFirstCommand(processedFiles),
                            getSecondCommand(logstashConfiguration),
                            new File(workspacePath, logstashDirName).toPath()) == 0;
        } catch (IOException e) {
            log.error(String.valueOf(e));
        }
        return false;
    }

    @Override
    public void setProcessedFileType(ConvertType type) {
        this.logstashManager.setInputFilesType(type.toString());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private ExecutorData getSecondCommand(LogstashCommandsConfig logstashConfiguration){
        ExecutorData data = new ExecutorData(executor.getLogstashBinPath());
        data.setParams(new String[] {
                "-e",
                "'\n" + logstashConfiguration.toLogstashString(0) + "'"
        });
        data.setWorkingDirectory(new File(workspacePath, logstashDirName));
        return data;
    }

    private ExecutorData getFirstCommand(List<Path> beforeCommand) {
        ExecutorData data = new ExecutorData("cat");
        Object[] l = beforeCommand.stream().map(Path::toString).toArray((IntFunction<Object[]>) String[]::new);
        data.setParams((String[]) l);
        data.setWorkingDirectory(new File(workspacePath, logstashDirName));
        return data;
    }
}
