package cz.muni.ics.json.service;

import cz.muni.ics.json.exception.JsonException;
import cz.muni.ics.json.exception.JsonMessages;
import cz.muni.ics.json.utils.*;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Collections;

public class JsonServiceImpl implements JsonService {

    private JsonMapper mapper = new JacksonObjectMapper();
    private JsonParser parser = new JacksonObjectParser();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    public JsonServiceImpl() {
    }

    JsonServiceImpl(JsonParser parser, JsonMapper mapper) {
        this.mapper = mapper;
        this.parser = parser;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Overrides

    @Override
    public <T> T parseJsonFile(File jsonFile, Class<T> clazz) throws JsonException {
        checkFile(jsonFile);
        return parser.parseFrom(jsonFile, clazz);
    }

    @Override
    public File mapJsonObject(Object objectToMap, File outputDir, String fileName) throws JsonException {
        checkObject(objectToMap);
        checkOutputDir(outputDir);
        fileName = checkFileName(fileName);
        return mapper.mapToJson(objectToMap, outputDir, fileName);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private void checkFile(File file) throws JsonException {
        checkIfFileExists(file);
        checkIfFileNotHidden(file);
        checkIfFileNotDir(file);
    }

    private void checkIfFileNotDir(File file) throws JsonException {
        if (file.isDirectory()) {
            throw new JsonException(
                    JsonMessages.ERROR_JSON_INVALID_FORMAT,
                    Collections.singletonList(file.getName()));
        }
    }

    private void checkIfFileNotHidden(File file) throws JsonException {
        if (file.isHidden()) {
            throw new JsonException(
                    JsonMessages.ERROR_JSON_FILE_HIDDEN,
                    Collections.singletonList(file.getName()));
        }
    }

    private void checkIfFileExists(File file) throws JsonException {
        if (!file.exists()) {
            throw new JsonException(
                    JsonMessages.ERROR_JSON_NOT_EXISTS,
                    Collections.singletonList(file.getName()));
        }
    }

    private void checkOutputDir(File outputDir) throws JsonException {
        if (!outputDir.exists()) {
            throw new JsonException(
                    JsonMessages.ERROR_OUTPUT_DIR_NOT_EXISTS,
                    Collections.singletonList(outputDir.getName()));
        }
        if (!outputDir.isDirectory()) {
            throw new JsonException(
                    JsonMessages.ERROR_OUTPUT_DIR_NOT_DIR,
                    Collections.singletonList(outputDir.getName()));
        }
    }

    private String createFileName() {
        return NEW_FILE_PREFIX + JsonFileNameCreator.getFormattedTimeJsonName();
    }

    private String checkFileName(String fileName) {
        if (fileName.isEmpty()) {
            return createFileName();
        }
        if (!FilenameUtils.getExtension(fileName).equals(EXT)) {
            fileName = fileName + "." + EXT;
        }
        return fileName;
    }

    private void checkObject(Object objectToMap) throws JsonException {
        if (objectToMap == null) {
            throw new JsonException(JsonMessages.ERROR_OBJECT_TO_MAP_NULL);
        }
    }
}
