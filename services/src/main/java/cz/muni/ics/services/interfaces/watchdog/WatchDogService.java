package cz.muni.ics.services.interfaces.watchdog;

import cz.muni.ics.services.enums.ConvertType;
import cz.muni.ics.services.implementations.web.dto.ImportFileDto;

import java.nio.file.Path;
import java.util.List;

public interface WatchDogService{

    boolean processFiles(List<ImportFileDto> files);
    void reset();
    boolean registerFoldersToWatch(ConvertType convertType);
    List<Path> getFoldersToWatch();
}
