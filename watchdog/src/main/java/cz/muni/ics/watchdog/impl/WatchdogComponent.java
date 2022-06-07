package cz.muni.ics.watchdog.impl;

import cz.muni.ics.core.dataclasses.ExecutorData;
import cz.muni.ics.core.os.SystemCommandExecutor;
import cz.muni.ics.watchdog.dataclasses.WatchdogData;
import cz.muni.ics.watchdog.exceptions.WatchdogException;
import cz.muni.ics.watchdog.exceptions.WatchdogMessages;
import cz.muni.ics.watchdog.interfaces.IWatchdog;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/**
 * @author Jozef Cibík
 */

@Slf4j
@Component
@NoArgsConstructor
public class WatchdogComponent implements IWatchdog {

    @Value("${copas.watchdog.script.path}")
    private String scriptPath;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private SystemCommandExecutor executor;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public WatchdogComponent(SystemCommandExecutor executor) {
        this.executor = executor;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public String getWatchdogScriptPath() {
        return this.scriptPath;
    }

    @Override
    public boolean executeWatchdog(WatchdogData watchdogData) {
        try {
            checkInputData(watchdogData);
            ExecutorData data = assembleCommands(watchdogData);
            int res = executor.executeWithExitCode(data);
            if (res != 0) {
                log.error(WatchdogMessages.WARN_SCRIPT_ENDED_WITH_DIFFERENT_VALUE, res);
                return false;
            } else {
                log.info(WatchdogMessages.INFO_SCRIPT_ENDED, res);
                return true;
            }
        } catch (IOException | WatchdogException e) {
            log.error(WatchdogMessages.ERROR_SCRIPT_FAILED, e);
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private void checkInputData(WatchdogData watchdogData) throws WatchdogException {
        checkConfigPath(watchdogData.getConfigPath());
        checkPath(Paths.get(watchdogData.getConfigPath()));
        checkScriptPath(watchdogData.getScriptPath());
        checkPath(Paths.get(watchdogData.getScriptPath()));
        //Filter
        filterInvalidFolders(watchdogData);
        //Then check
        checkFolders(watchdogData.getFoldersToWatch());
    }

    private void checkConfigPath(String configPath) throws WatchdogException {
        if (configPath == null || configPath.isEmpty()) {
            throw new WatchdogException(
                    WatchdogMessages.ERROR_INVALID_CONFIG_PATH,
                    Collections.singletonList(configPath));
        }
        checkPath(Paths.get(configPath));
    }

    private void checkPath(Path p) throws WatchdogException {
        if (!p.toFile().exists()) {
            throw new WatchdogException(
                    WatchdogMessages.ERROR_FILE_DOES_NOT_EXIST,
                    Collections.singletonList(p.toString()));
        }
    }

    private void checkScriptPath(String scriptPath) throws WatchdogException {
        if (scriptPath == null || scriptPath.isEmpty()) {
            throw new WatchdogException(
                    WatchdogMessages.ERROR_INVALID_SCRIPT_PATH,
                    Collections.singletonList(scriptPath));
        }
        checkPath(Paths.get(scriptPath));
    }

    private void checkFolders(Set<Path> foldersToWatch) throws WatchdogException {
        if (foldersToWatch.isEmpty()) {
            throw new WatchdogException(WatchdogMessages.ERROR_NO_DIRS_TO_WATCH);
        }
    }

    //Filter non-existing folders and non-folders
    private void filterInvalidFolders(WatchdogData watchdogData) {
        log.debug(WatchdogMessages.DEBUG_FILTERING_INVALID_FOLDERS);
        watchdogData.setFoldersToWatch(watchdogData.getFoldersToWatch().stream().filter(item -> item.toFile().exists() && item.toFile().isDirectory()).collect(Collectors.toSet()));
    }

    //Script arguments: script ACTION -a -c CONFIG.json -m MINUTES folder1 folder2 folder3 ...
    private ExecutorData assembleCommands(WatchdogData config) {
        ExecutorData data = new ExecutorData(config.getScriptPath());
        List<String> commands = getParametersAsList(config);
        data.setParams((String[]) commands.stream().toArray((IntFunction<Object[]>) String[]::new));
        return data;
    }

    private List<String> getParametersAsList(WatchdogData config) {
        List<String> commands = new ArrayList<>();
        commands.add(config.getAction().getAction());
        commands.add("-a");
        commands.add("-c");
        commands.add(config.getConfigPath());
        commands.add("-m");
        commands.add(String.valueOf(config.getMinutes()));
        commands.addAll(config.getFoldersToWatch().stream().map(Path::toString).collect(Collectors.toList()));
        return commands;
    }
}
