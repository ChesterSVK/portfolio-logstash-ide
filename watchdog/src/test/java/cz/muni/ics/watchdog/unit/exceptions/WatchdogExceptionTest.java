package cz.muni.ics.watchdog.unit.exceptions;

import cz.muni.ics.watchdog.exceptions.WatchdogException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class WatchdogExceptionTest {

    @Test
    public void testExceptionCreateWithMessage() {
        String message = "message";
        assertThatThrownBy(() -> {
            throw new WatchdogException(message);
        }).hasNoCause();
    }

    @Test
    public void testExceptionCreateWithMessageAndThrowable() {
        String message = "message";
        Throwable t = new Exception();
        try {
            throw new WatchdogException(message, t);
        } catch (WatchdogException e) {
            assertThat(e.getMessage()).isNotEmpty();
            assertThat(e.getMessage()).contains(message);
            assertThat(e.getCause()).isEqualTo(t);
            assertThat(e.toString()).isNotEmpty();
        }
    }

    @Test
    public void testExceptionCreateWithMessageAndArguments() {
        String message = "message";
        List<String> arguments = new ArrayList<>();
        arguments.add("a");
        try {
            throw new WatchdogException(message, arguments);
        } catch (WatchdogException e) {
            assertThat(e.getMessage()).isNotEmpty();
            assertThat(e.getMessage()).contains(message);
            assertThat(e.getArguments().size()).isEqualTo(1);
            assertThat(e.getCause()).isNull();
            assertThat(e.toString()).isNotEmpty();
        }
    }
}
