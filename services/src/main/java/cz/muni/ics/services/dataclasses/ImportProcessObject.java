package cz.muni.ics.services.dataclasses;

import cz.muni.ics.services.implementations.web.dto.ImportFileDto;
import cz.muni.ics.services.enums.ConvertType;
import lombok.Data;

import java.nio.file.Path;
import java.util.*;

@Data
public class ImportProcessObject {
    private ConvertType convertType;
    private Set<ImportFileDto> originalFiles = new HashSet<>();
    private List<Path> processedFiles = new ArrayList<>();
    private Set<String> parsedAttributes = new HashSet<>();
    public void clearOriginalFiles() {
        this.originalFiles.clear();
    }
    public void resetConvertType() {
        this.convertType = null;
    }
}
