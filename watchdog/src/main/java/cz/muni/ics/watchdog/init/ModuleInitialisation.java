package cz.muni.ics.watchdog.init;

import cz.muni.ics.core.dataclasses.ExecutorData;
import cz.muni.ics.core.exceptions.ApplicationException;
import cz.muni.ics.core.os.SystemCommandExecutor;
import cz.muni.ics.watchdog.exceptions.WatchdogInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Messages for Watchdog module
 *
 * @author Jozef Cibík
 */

@Component
public class ModuleInitialisation {

    private final SystemCommandExecutor executor;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Values

    @Value("${copas.watchdog.script.path}")
    private String scriptPath;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public ModuleInitialisation(SystemCommandExecutor executor) {
        this.executor = executor;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Public

    public void initializeModule() throws IOException {
        ExecutorData data = new ExecutorData(Paths.get(scriptPath).toString());
        data.setParams(new String[]{"-h"});
        assert executor.executeWithExitCode(data) != 1; }
}
