package cz.muni.ics.services.implementations.bro;

import cz.muni.ics.bro.exceptions.BroException;
import cz.muni.ics.bro.exceptions.BroMessages;
import cz.muni.ics.bro.impl.BroJsonConfigCreator;
import cz.muni.ics.bro.impl.BroPcapProcessing;
import cz.muni.ics.core.exceptions.ApplicationException;
import cz.muni.ics.services.interfaces.bro.BroService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Bro service
 *
 * @author Jozef Cibík
 */

@Slf4j
@Service
public class BroServiceImpl implements BroService {

    private final BroPcapProcessing pcapProcessing;
    private final BroJsonConfigCreator broConfigCreator;
    private static final String PARENT_PREFIX = "bro_process_";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public BroServiceImpl(BroPcapProcessing pcapProcessing, BroJsonConfigCreator broConfigCreator) {
        this.pcapProcessing = pcapProcessing;
        this.broConfigCreator = broConfigCreator;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Overrides

    @Override
    public Path createCustomConfig(String customCommand, Path outputDir) throws BroException {
        checkDirectory(outputDir);
        return broConfigCreator.createBroConfig(customCommand, outputDir);
    }

    @Override
    public boolean broProcessPcap(Collection<Path> files, Path broOutputDir, Path config) {
        Path outputDir = null;
        try {
            outputDir = createProcessOutputDir(createDateSuffix(), broOutputDir);
        } catch (IOException e) {
            return false;
        }
        return processMultiple(files, outputDir, config);
    }

    @Override
    public boolean broProcessPcap(Path file, Path broOutputDir, Path config) {
        Path outputDir = null;
        try {
            outputDir = createProcessOutputDir(file.toFile().getName(), broOutputDir);
            return processSingle(file, outputDir, config);
        } catch (IOException | ApplicationException | BroException e) {
            log.error(String.valueOf(e));
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private void checkDirectory(Path dirToCheck) throws BroException {
        if (!dirToCheck.toFile().exists()) {
            throw new BroException(
                    BroMessages.ERROR_DIR_NOT_EXISTS,
                    Collections.singletonList(dirToCheck.toString()));
        }
        if (!dirToCheck.toFile().isDirectory()) {
            throw new BroException(
                    BroMessages.ERROR_DIR_INVALID,
                    Collections.singletonList(dirToCheck.toString()));
        }
    }

    private boolean processMultiple(Collection<Path> files, Path outputDir, Path config) {
        Set<Boolean> results = new HashSet<>();
        for (Path file : files) {
            results.add(broProcessPcap(file, outputDir, config));
        }
        return results.size() == 1 && results.contains(true);
    }

    private boolean processSingle(Path file, Path outputDir, Path config) throws ApplicationException, BroException, IOException {
        log.debug("Processing: " + file);
        return pcapProcessing.processInputFile(file, outputDir, config) == 0;
    }

    private Path createProcessOutputDir(String directorySuffix, Path parentDir)
            throws IOException {
        String dirName = createDirParentName(directorySuffix);
        File dir = new File(parentDir.toFile(), dirName);
        Files.createDirectories(dir.toPath());
        return dir.toPath();
    }

    private String createDirParentName(String suffix) {
        return PARENT_PREFIX + suffix;
    }

    private String createDateSuffix() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd-HH_mm_ss"));
    }
}
