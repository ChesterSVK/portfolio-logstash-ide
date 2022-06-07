package cz.muni.ics.services.converters;

import cz.muni.ics.json.service.JsonService;
import cz.muni.ics.json.service.JsonServiceImpl;
import cz.muni.ics.services.interfaces.ConvertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("JsonConvertService")
public class JsonConvertService extends AbstractConvertService implements ConvertService {

    //No conversion needed

    @Override
    public List<Path> convert(List<String> paths) {
        //Json files are already json files so no conversion needed
        return paths.stream().map(Paths::get).collect(Collectors.toList());
    }

    //All paths are original files. No check required;
    @Override
    public List<Path> getOutputFiles(List<Path> outputPaths) {
        return super.getOutputFiles(outputPaths);
    }

    @Override
    public Set<String> parseAttributes(List<Path> convertedPaths) {
        return super.parseAttributes(convertedPaths);
    }
}
