package cz.muni.ics.json.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.ics.json.exception.JsonException;
import cz.muni.ics.json.exception.JsonMessages;

import java.io.File;
import java.io.IOException;

/**
 * Implementation of the JsonParser
 *
 * @author Jozef Cibík
 */
public class JacksonObjectParser implements JsonParser {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public <T> T parseFrom(File file, Class<T> clazz) throws JsonException {
        return parse(file, clazz);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private <T> T parse(File file, Class<T> clazz) throws JsonException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new JsonException(JsonMessages.ERROR_JSON_PARSING, e);
        }
    }
}
