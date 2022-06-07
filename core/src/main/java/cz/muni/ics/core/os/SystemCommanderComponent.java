package cz.muni.ics.core.os;

import cz.muni.ics.core.dataclasses.ExecutorData;
import cz.muni.ics.core.validators.ExecutorDataValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class SystemCommanderComponent implements SystemCommandExecutor {

    private ExecutorDataValidator validator;
    private DefaultExecutor executor = new DefaultExecutor();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public SystemCommanderComponent(ExecutorDataValidator validator) {
        this.validator = validator;
    }


    SystemCommanderComponent(ExecutorDataValidator validator, DefaultExecutor executor) {
        this.executor = executor;
        this.validator = validator;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public int executeWithExitCode(ExecutorData data) throws IOException {
        log.debug(executeWithOutput(data));
        return data.getExitValue();
    }

    @Override
    public String executeWithOutput(ExecutorData data) throws IOException {
        validator.validate(data);
        data.setWorkingDirectory(getWorkingDirectory(data.getWorkingDirectory()));
        return runCommand(data);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private String runCommand(ExecutorData data) throws IOException {
        CommandLine commandLine = new CommandLine(data.getCommand());
        commandLine.addArguments(data.getParams(), false);

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        PumpStreamHandler pumpStreamHandler;
        if (data.getInput() != null) {
            ByteArrayInputStream stdin = new ByteArrayInputStream(data.getInput().getBytes(StandardCharsets.UTF_8));
            pumpStreamHandler = new PumpStreamHandler(stdout, stderr, stdin);
        } else pumpStreamHandler = new PumpStreamHandler(stdout, stderr);

        ExecuteWatchdog watchdog = new ExecuteWatchdog(20000);  // 30s timeout
        return doIt(data, pumpStreamHandler, watchdog, commandLine, stdout);
    }

    private String doIt(ExecutorData data, PumpStreamHandler pumpStreamHandler, ExecuteWatchdog watchdog, CommandLine commandLine, ByteArrayOutputStream stdout) throws IOException {
        log.debug(data.toString());
        executor.setWorkingDirectory(data.getWorkingDirectory());
        executor.setStreamHandler(pumpStreamHandler);
        executor.setWatchdog(watchdog);
        executor.setExitValue(data.getExitValue());
        executor.execute(commandLine);
        return stdout.toString();
    }

    private File getWorkingDirectory(File workingDirectory) {
        return workingDirectory != null && workingDirectory.exists() ? workingDirectory : new File(System.getProperty("user.home"));
    }
}
