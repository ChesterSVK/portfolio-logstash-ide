package cz.muni.ics.json.utils;

import cz.muni.ics.json.exception.JsonException;

import java.io.File;

/**
 * Provides possibility to parse json host from file.
 * Extending classes should define in what class will be host parser.
 *
 * Conversion between mapper and parser must be equal
 * Object o = parser.parseFrom(f) == o
 * File f = mapper.mapToJson(o) == f
 *
 * @author Jozef Cibík
 */

public interface JsonParser {

    /**
     * Method will parse host from json file into object of specified class
     *
     * @param file source file to parse from
     * @param clazz to map host to
     * @param <T> specific class that will be parsed from json
     * @return map with json host, empty map if some check fails
     * @throws JsonException on parse error
     */
    <T> T parseFrom(File file, Class<T> clazz) throws JsonException;
}
