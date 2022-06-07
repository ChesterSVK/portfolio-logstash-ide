package cz.muni.ics.services.implementations;

import cz.muni.ics.services.implementations.web.dto.ImportFileDto;
import cz.muni.ics.services.enums.ConvertType;
import cz.muni.ics.services.interfaces.FileImportService;
import cz.muni.ics.services.interfaces.PathsCollection;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileImportServiceImpl implements FileImportService {

    private PathsCollection paths = new FilePathsHashSet();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Overrides

    @Override
    public boolean processFiles(List<ImportFileDto> files) {
        this.reset();
        return paths.addAllAsDirectory(files.stream().map( item -> Paths.get(item.getFileData().getId())).collect(Collectors.toList()));
    }

    @Override
    public void reset() {
        this.paths.clear();
    }

    @Override
    public PathsCollection getProcessedFiles() {
        return new FilePathsHashSet(paths);
    }

    @Override
    public void filterType(ConvertType type) {
        this.paths.clearPaths(this.paths.stream().filter(item -> FilenameUtils.getExtension(item.toString().toLowerCase()).equals(type.toString().toLowerCase())).collect(Collectors.toList()));
    }
}
