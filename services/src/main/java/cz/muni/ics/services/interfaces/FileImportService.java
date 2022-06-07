package cz.muni.ics.services.interfaces;

import cz.muni.ics.services.implementations.web.dto.ImportFileDto;
import cz.muni.ics.services.enums.ConvertType;

import java.util.List;

public interface FileImportService {

    boolean processFiles(List<ImportFileDto> files);
    void reset();
    PathsCollection getProcessedFiles();
    void filterType(ConvertType type);
}
