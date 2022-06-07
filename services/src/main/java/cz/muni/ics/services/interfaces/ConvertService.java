package cz.muni.ics.services.interfaces;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public interface ConvertService {

    List<Path> convert(List<String> paths);
    Set<String> parseAttributes(List<Path> convertedPaths);
    List<Path> getOutputFiles(List<Path> outputPaths);
}
