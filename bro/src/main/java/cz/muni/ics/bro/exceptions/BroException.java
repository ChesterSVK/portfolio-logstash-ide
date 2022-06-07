package cz.muni.ics.bro.exceptions;

import lombok.Getter;

import java.util.List;

/**
 * Exception class for Bro module
 *
 * @author Jozef Cibík
 */

public class BroException extends Exception {

    @Getter
    private List<String> arguments;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    public BroException(String message) {
        super(message);
    }
    public BroException(String message, Throwable t) {
        super(message, t);
    }
    public BroException(String message, List<String> arguments) {
        super(message);
        this.arguments = arguments;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public String toString() {
        String exceptionName = getClass().getName();
        Throwable cause = getCause();
        return cause == null ? exceptionName : createMessage(cause.getMessage(), exceptionName);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private String createMessage(String causeMessage, String exceptionName) {
        String res = exceptionName + ": " + super.getLocalizedMessage() +"\n" + causeMessage;
        return (arguments != null && !arguments.isEmpty()) ? (res + "; Args: " + arguments) : res;
    }
}