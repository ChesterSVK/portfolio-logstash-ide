package cz.muni.ics.logstash.exception;

import java.util.ResourceBundle;

/**
 * Error messages for Json module
 *
 * @author Jozef Cibík
 */
public class LogstashMessages {

    private static final ResourceBundle logstashBundle = ResourceBundle.getBundle("logstash_messages");

    public static final String ERROR_INVALID_LEAF_COMMAND = logstashBundle.getString("ERROR_INVALID_LEAF_COMMAND");
    public static final String ERROR_NODE_INSTANTIATION = logstashBundle.getString("ERROR_NODE_INSTANTIATION") ;
    public static final String ERROR_FORBIDDEN_NODE_TYPE = logstashBundle.getString("ERROR_FORBIDDEN_NODE_TYPE");
    public static final String ERROR_INVALID_INPUT_TYPE_TO_SERIALIZE = logstashBundle.getString("ERROR_INVALID_INPUT_TYPE_TO_SERIALIZE");;
    public static final String ERROR_SERVER_INTERNAL = logstashBundle.getString("ERROR_SERVER_INTERNAL");
    public static final String ERROR_INVALID_INPUT_TYPE = logstashBundle.getString("ERROR_INVALID_INPUT_TYPE");
    public static final String ERROR_PARSING_LEAF = logstashBundle.getString("ERROR_PARSING_LEAF");
    public static final String MODE_ERROR = logstashBundle.getString("MODE_ERROR");
}
