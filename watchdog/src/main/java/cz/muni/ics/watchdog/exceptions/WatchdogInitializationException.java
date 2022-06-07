package cz.muni.ics.watchdog.exceptions;

/**
 * Initialization exception class for Watchdog module
 *
 * @author Jozef Cibík
 */

public class WatchdogInitializationException extends WatchdogException {
    public WatchdogInitializationException(String message) {
        super(message);
    }

    public WatchdogInitializationException(String s, Throwable t) {
        super(s, t);
    }
}
