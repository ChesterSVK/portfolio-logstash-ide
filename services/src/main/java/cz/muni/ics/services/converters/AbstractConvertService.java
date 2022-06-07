package cz.muni.ics.services.converters;

import cz.muni.ics.json.exception.JsonException;
import cz.muni.ics.json.service.JsonService;
import cz.muni.ics.json.service.JsonServiceImpl;
import cz.muni.ics.services.exceptions.ServiceMessages;
import cz.muni.ics.services.interfaces.ConvertService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

@Slf4j
public abstract class AbstractConvertService implements ConvertService {

    private JsonService jsonService = new JsonServiceImpl();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public List<Path> getOutputFiles(List<Path> outputPaths) {
        List<Path> processedPaths = new ArrayList<>();
        process(outputPaths, processedPaths);
        return processedPaths;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private void process(List<Path> outputPaths, List<Path> processedPaths) {
        outputPaths.forEach(path -> {
            File pathFile = path.toFile();
            if (pathFile.isFile()){
                processedPaths.add(path);
            }
            if (pathFile.isDirectory()){
                process(processDirectory(pathFile), processedPaths);
            }
        });
    }

    private List<Path> processDirectory(File pathFile){
        List<Path> directoryContentPaths = new ArrayList<>();
        File[] dirContent = pathFile.listFiles();
        if (dirContent != null){
            for (File f : dirContent){
                directoryContentPaths.add(f.toPath());
            }
        }
        return directoryContentPaths;
    }

    @Override
    public Set<String> parseAttributes(List<Path> convertedPaths) {
        Set<String> parsedAttributes = new HashSet<>();
        convertedPaths.forEach( convertedPath -> {
            try {
                Set<String> keys = (Set<String>) jsonService.parseJsonFile(convertedPath.toFile(), Map.class).keySet();
                parsedAttributes.addAll(keys);
            } catch (JsonException e) {
                log.error(ServiceMessages.ERROR_DURING_FILE_PARSING + convertedPath.toString());
                log.error(String.valueOf(e));
            } catch (ClassCastException e){
                log.error(ServiceMessages.ERROR_ILLEGAL_KEYS + convertedPath.toString());
                log.error(String.valueOf(e));
            }
        });
        return parsedAttributes;
    }
}
