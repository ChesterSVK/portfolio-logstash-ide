package cz.muni.ics.watchdog.unit.exceptions;

import cz.muni.ics.watchdog.exceptions.WatchdogInitializationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WatchdogInitializationExceptionTest {

    @Test
    public void testExceptionCreateWithMessage() {
        String message = "message";
        assertThatThrownBy(() -> {
            throw new WatchdogInitializationException(message);
        }).hasMessage(message).hasNoCause();
    }

    @Test
    public void testExceptionCreateWithMessageAndThrowable() {
        String message = "message";
        Throwable t = new Exception();
        assertThatThrownBy(() -> {
            throw new WatchdogInitializationException(message, t);
        }).hasMessage(message).hasCause(t);
    }
}
