package cz.muni.ics.json.utils;

import cz.muni.ics.json.exception.JsonException;

import java.io.File;

/**
 * Json mapper task is to map Java objects to json file
 * Not responsible for objects that cannot be parsed to json form.
 *
 * @author Jozef Cibík
 */

public interface JsonMapper {

    /**
     * Maps object to json file in specific directory and given file name
     * @param objectToMap to file
     * @param fileName name of the created json file
     * @param outputDir where file will be created
     * @return created file if operation was successful
     * @throws JsonException on error
     */
    File mapToJson(Object objectToMap, File outputDir, String fileName) throws JsonException;
}
