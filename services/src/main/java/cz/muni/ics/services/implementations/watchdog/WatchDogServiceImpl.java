package cz.muni.ics.services.implementations.watchdog;

import com.fabriceci.fmc.model.FileType;
import cz.muni.ics.services.enums.ConvertType;
import cz.muni.ics.services.implementations.web.dto.ImportFileDto;
import cz.muni.ics.services.interfaces.watchdog.WatchDogService;
import cz.muni.ics.watchdog.dataclasses.WatchdogConfig;
import cz.muni.ics.watchdog.dataclasses.WatchdogData;
import cz.muni.ics.watchdog.exceptions.WatchdogException;
import cz.muni.ics.watchdog.interfaces.IWatchdog;
import cz.muni.ics.watchdog.interfaces.WatchdogConfigCreator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.Watchdog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WatchDogServiceImpl implements WatchDogService {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Values

    @Value("${services.root.output.directory.home}")
    private String workspacePath;
    @Value("${services.watchdog.output.directory.home}")
    private String watchdogDirName;

    private final IWatchdog watchdog;
    private final WatchdogConfigCreator configCreator;
    private List<Path> foldersPaths = new ArrayList<>();


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public WatchDogServiceImpl(IWatchdog watchdog, WatchdogConfigCreator configCreator) {
        this.watchdog = watchdog;
        this.configCreator = configCreator;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public boolean processFiles(List<ImportFileDto> files) {
        this.reset();
        List<ImportFileDto> filtered =
                files.stream()
                        .filter(item -> (item.getFileData().getType() == FileType.folder && item.isWatch()))
                        .collect(Collectors.toList());
        filtered.forEach(item -> {
            foldersPaths.add(Paths.get(item.getFileData().getId()));
        });
        return true;
    }

    @Override
    public void reset() {
        this.foldersPaths.clear();
    }

    @Override
    public boolean registerFoldersToWatch(ConvertType convertType) {
        try {
            Path configPath = createWatchdogConfig(convertType);
            WatchdogData data = createWatchdogData(configPath);
            return watchdog.executeWatchdog(data);
        } catch (WatchdogException e) {
            log.error("Registering folders to watch failed. Reason: ", String.valueOf(e));
            return false;
        }
    }

    @Override
    public List<Path> getFoldersToWatch() {
        return foldersPaths;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private WatchdogData createWatchdogData(Path configPath) {
        WatchdogData data = new WatchdogData(watchdog.getWatchdogScriptPath(), configPath.toAbsolutePath().toString());
        data.getFoldersToWatch().addAll(foldersPaths);
        return data;
    }

    private Path createWatchdogConfig(ConvertType convertType) throws WatchdogException {
        WatchdogConfig config = new WatchdogConfig(convertType.name());
        File configFileParent = new File(workspacePath, watchdogDirName);
        return configCreator.createConfig(config, configFileParent.toPath());
    }
}
