package cz.muni.ics.json.exception;

import lombok.Getter;

import java.util.List;

/**
 * Exception class for Json module
 *
 * @author Jozef Cibík
 */

public class JsonException extends Exception{

    @Getter
    private List<String> arguments;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    public JsonException(String message) { super(message); }
    public JsonException(String message, Throwable cause) { super(message, cause); }
    public JsonException(String message, List<String> arguments) { super(message); this.arguments = arguments; }

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
