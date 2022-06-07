package cz.muni.ics.logstash.utils;

import cz.muni.ics.core.dataclasses.ExecutorData;
import cz.muni.ics.core.os.SystemCommanderComponent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;

@Slf4j
@Component
public class LogstashExecutor {

    private final SystemCommanderComponent executor;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Values

    @Getter
    @Value("${logstash.binary.path}")
    private String logstashBinPath;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public LogstashExecutor(SystemCommanderComponent executor) {
        this.executor = executor;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Public

    //  Due to issue https://issues.apache.org/jira/browse/EXEC-101 input cannot be provided to executor.
    //  Workaround is to create script with pipe argument
    public int executePipedCommands(ExecutorData first, ExecutorData second, Path outputPath) throws IOException {
        File script = createTemporaryScript(first, second, outputPath);
        ExecutorData data = new ExecutorData(script.getAbsolutePath());
        data.setWorkingDirectory(outputPath.toFile());
        return executor.executeWithExitCode(data);
    }

    public int executeInitCommand() throws IOException {
        ExecutorData data = new ExecutorData(Paths.get(logstashBinPath).normalize().toString());
        data.setParams(new String[]{"-v"});
        return executor.executeWithExitCode(data);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private File createTemporaryScript(ExecutorData first, ExecutorData second, Path path) throws IOException {
        File tempBashScript = new File(path.toFile(), "tempLogstashBash.sh");
        tempBashScript.delete();
        String command = assembleCommands(first, second);
        PrintWriter writer = new PrintWriter(tempBashScript, "UTF-8");
        writer.println(command);
        writer.close();
        Files.setPosixFilePermissions(tempBashScript.toPath(), PosixFilePermissions.fromString("rwxr-xr-x"));
        return tempBashScript;
    }

    private String assembleCommands(ExecutorData first, ExecutorData second) {
        return "#!/bin/sh \n" +
                assembleData(first) +
                " | " +
                assembleData(second) +
                " \n";
    }

    private String assembleData(ExecutorData data) {
        StringBuilder res = new StringBuilder(data.getCommand());
        for (String s : data.getParams()) {
            res.append(" ").append(s);
        }
        return res.toString();
    }
}
