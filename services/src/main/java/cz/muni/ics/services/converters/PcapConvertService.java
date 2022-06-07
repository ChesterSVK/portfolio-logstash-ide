package cz.muni.ics.services.converters;

import com.fabriceci.fmc.error.FileManagerException;
import cz.muni.ics.bro.exceptions.BroException;
import cz.muni.ics.bro.impl.BroPcapProcessing;
import cz.muni.ics.core.exceptions.ApplicationException;
import cz.muni.ics.filesystem.FileUtilities;
import cz.muni.ics.filesystem.FolderUtilities;
import cz.muni.ics.json.service.JsonService;
import cz.muni.ics.json.service.JsonServiceImpl;
import cz.muni.ics.services.interfaces.ConvertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service("PcapConvertService")
public class PcapConvertService extends AbstractConvertService implements ConvertService {

    private final BroPcapProcessing broProcessing;

    private static final String DIR_PREFIX = "processed_pcap";
    private static final String DEFAULT_CONFIG_VALUE = "@load policy/tuning/json-logs\n" +
            "redef LogAscii::json_timestamps = JSON::TS_ISO8601;";


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Values

    @Value("${services.root.output.directory.home}")
    private String workspacePath;
    @Value("${services.bro.output.directory.home}")
    private String broDirName;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public PcapConvertService(BroPcapProcessing broProcessing) {
        this.broProcessing = broProcessing;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override
    @Override
    public List<Path> convert(List<String> paths) {
        List<Path> processedFilesPaths = new ArrayList<>();
        paths.forEach(path -> addProcessedPathToList(path, processedFilesPaths));
        return processedFilesPaths;
    }

    @Override
    public List<Path> getOutputFiles(List<Path> outputPaths) {
        return super.getOutputFiles(outputPaths);
    }

    @Override
    public Set<String> parseAttributes(List<Path> convertedPaths) {
        return super.parseAttributes(convertedPaths);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private Path createNewOutputDir(Path parentDir) throws FileManagerException {
        return FolderUtilities.createFolder(createFolderName(), parentDir, true);
    }

    private void addProcessedPathToList(String path, List<Path> processedFilesPaths) {
        try {
            executeBroProcessing(path, processedFilesPaths);
        } catch (BroException | IOException | ApplicationException | FileManagerException e) {
            log.error(String.valueOf(e.getMessage()));
            log.error(String.valueOf(e.getCause()));
        }
    }

    private void executeBroProcessing(String path, List<Path> processedFilesPaths)
            throws FileManagerException, IOException, BroException, ApplicationException {

        Path parent = new File(workspacePath, broDirName).toPath();
        Path outputDir = createNewOutputDir(parent);
        File broConfig = new File(parent.toFile(), "default_config.bro");
        if (!broConfig.exists()){
            FileUtilities.createFile(DEFAULT_CONFIG_VALUE, broConfig.getName(), parent, false);
        }
        broProcessing.processInputFile(Paths.get(path), outputDir, broConfig.toPath());
        processedFilesPaths.add(outputDir.toAbsolutePath());
    }

    private static String createFolderName() {
        return DIR_PREFIX + "_" + new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date());
    }
}