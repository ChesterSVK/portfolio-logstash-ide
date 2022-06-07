package cz.muni.ics.elasticsearch.exceptions;

/**
 * Initialization exception class for ES module
 *
 * @author Jozef Cibík
 */

public class ElasticsearchInitializationException extends Exception {
    public ElasticsearchInitializationException(String message) {
        super(message);
    }
}
