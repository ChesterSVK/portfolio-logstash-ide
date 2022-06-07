package cz.muni.ics.bro.utils;

import cz.muni.ics.bro.exceptions.BroMessages;
import cz.muni.ics.core.dataclasses.ExecutorData;
import cz.muni.ics.core.exceptions.ApplicationException;
import cz.muni.ics.core.os.SystemCommandExecutor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Bro executor component is responsible for executing commands invoking bro tool and bro processing
 * Valid value to binary file must be provided in properties.
 *
 * @author Jozef Cibík
 */

@Slf4j
@Component
public class BroExecutor {

    @Getter
    @Value("${bro.binary.path}")
    private String broBinPath;

    private final SystemCommandExecutor executor;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public BroExecutor(SystemCommandExecutor executor) {
        this.executor = executor;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Public

    public int executeCommand(ExecutorData data) throws IOException, ApplicationException {
        checkExecutionData(data);
        return executor.executeWithExitCode(data);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private void checkExecutionData(ExecutorData data) throws ApplicationException {
        checkBinary(data);
        if (!checkDirectory(data)) {
            log.debug(BroMessages.ERROR_INVALID_WORKING_DIR + data.getWorkingDirectory());
            data.setWorkingDirectory(new File("."));
            log.debug(BroMessages.WARN_CHANGING_DIR + data.getWorkingDirectory().getAbsolutePath());
        }
    }

    private boolean checkDirectory(ExecutorData data) {
        if (data.getWorkingDirectory() != null) {
            return data.getWorkingDirectory().exists();
        }
        return false;
    }

    private void checkBinary(ExecutorData data) throws ApplicationException {
        if (!data.getCommand().equals(broBinPath)) {
            throw new ApplicationException(BroMessages.ERROR_INVALID_BRO_BIN + data.getCommand());
        }
    }

    public int executeInitCommand() throws IOException, ApplicationException {
        ExecutorData data = new ExecutorData(broBinPath);
        data.setParams(new String[]{"-v"});
        return executeCommand(data);
    }
}
