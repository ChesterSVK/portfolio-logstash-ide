package cz.muni.ics.logstash.exception;

import lombok.Getter;

import java.util.List;

/**
 * Exception class for Json module
 *
 * @author Jozef Cibík
 */
public class LogstashException extends Exception {

    @Getter
    private List<String> arguments;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    public LogstashException(String message) {
        super(message);
    }
    public LogstashException(String message, Throwable cause) {
        super(message, cause);
    }
    public LogstashException(String message, List<String> arguments) {
        super(message);
        this.arguments = arguments;
    }
    public LogstashException(String message, List<String> arguments, Throwable cause) {
        super(message, cause);
        this.arguments = arguments;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public String toString() {
        String exceptionName = getClass().getName();
        Throwable cause = getCause();
        return cause == null ? exceptionName : createMessage(cause.getLocalizedMessage(), exceptionName);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private String createMessage(String causeMessage, String exceptionName) {
        String res = exceptionName + ": " + super.getLocalizedMessage() +"\n" + causeMessage;
        return (arguments != null && !arguments.isEmpty()) ? (res + "; Args: " + arguments) : res;
    }
}
