package cz.muni.ics.core.exceptions;

import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ApplicationExceptionTest {

    @Test
    public void testExceptionCreateWithMessage() {
        String message = "message";
        assertThatThrownBy(() -> {
            throw new ApplicationException(message);
        }).hasMessage(message).hasNoCause();
    }

    @Test
    public void testExceptionCreateWithMessageAndCause() {
        String message = "message";
        Throwable t = new IOException("test");
        assertThatThrownBy(() -> {
            throw new ApplicationException(message, t);
        }).hasMessage(message).hasCause(t);
    }
}
