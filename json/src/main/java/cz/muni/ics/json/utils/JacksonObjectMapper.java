package cz.muni.ics.json.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.ics.json.exception.JsonException;
import cz.muni.ics.json.exception.JsonMessages;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Implementation of the JsonMapper
 *
 * @author Jozef Cibík
 */

public class JacksonObjectMapper implements JsonMapper {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Overrides

    @Override
    public File mapToJson(Object objectToMap, File outputDir, String fileName) throws JsonException {
        return mapTo(objectToMap, outputDir, fileName);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private File mapTo(Object o, File dir, String fileName) throws JsonException {
        File outFile = new File(dir, fileName);
        try {
            Files.createFile(outFile.toPath());
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(outFile, o);
        } catch (IOException e) {
            throw new JsonException(JsonMessages.ERROR_JSON_IO, e);
        }
        return outFile;
    }
}
