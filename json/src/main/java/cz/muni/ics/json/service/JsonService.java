package cz.muni.ics.json.service;

import cz.muni.ics.json.exception.JsonException;

import java.io.File;

public interface JsonService {

    /**
     * Output file extension
     */
    String EXT = "json";

    /**
     * Output directory
     */
    String OUTPUT_DIR = "./";

    /**
     * Prefix of newly created file
     */
    String NEW_FILE_PREFIX = "mapped_json_";


    <T> T parseJsonFile(File file, Class<T> clazz) throws JsonException;
    File mapJsonObject(Object objectToMap, File outputDir, String fileName) throws JsonException;
}
