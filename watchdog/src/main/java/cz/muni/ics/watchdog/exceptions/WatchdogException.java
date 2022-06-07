package cz.muni.ics.watchdog.exceptions;

import lombok.Getter;

import java.util.List;

/**
 * Exception class for Watchdog module
 *
 * @author Jozef Cibík
 */

public class WatchdogException extends Exception {

    @Getter
    private List<String> arguments;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    public WatchdogException(String message) {
        super(message);
    }

    public WatchdogException(String message, Throwable t) {
        super(message, t);
    }

    public WatchdogException(String message, List<String> arguments) {
        super(message);
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
        String res = exceptionName + ": " + super.getLocalizedMessage() + "\n" + causeMessage;
        return (arguments != null && !arguments.isEmpty()) ? (res + "; Args: " + arguments) : res;
    }
}
