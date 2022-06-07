package cz.muni.ics.bro.exceptions;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BroExceptionTest {

    @Test
    public void testExceptionCreateWithMessage() {
        String message = "message";
        assertThatThrownBy(() -> {
            throw new BroException(message);
        }).hasMessage(message).hasNoCause();
    }

    @Test
    public void testExceptionToStringWithArg() {
        String message = "message";
        String arg = "testArg";
        try {
            throw new BroException(message, Collections.singletonList(arg));
        } catch (BroException e) {
            assertThat(e.getMessage()).isEqualTo(message);
            assertThat(e.getArguments().size()).isEqualTo(1);
            assertThat(e.toString()).contains(arg);
        }
    }

    @Test
    public void testExceptionToStringWithoutArg() {
        String message = "message";
        try {
            throw new BroException(message);
        } catch (BroException e) {
            assertThat(e.getMessage()).isEqualTo(message);
        }
    }

    @Test
    public void testExceptionCreateWithMessageAndThrowable() {
        String message = "message";
        Throwable t = new Exception();
        try {
            throw new BroException(message, t);
        } catch (BroException e) {
            System.out.println(
                    e.toString()
            );
            assertThat(e.getCause()).isEqualTo(t);
            assertThat(e.toString()).isNotEmpty();
            assertThat(e.getMessage().contains(message)).isTrue();
        }
    }

    @Test
    public void testExceptionCreateWithMessageAndArguments() {
        String message = "message";
        List<String> arguments = new ArrayList<>();
        arguments.add("a");
        arguments.add("b");
        try {
            throw new BroException(message, arguments);
        } catch (BroException e) {
            assertThat(e.getMessage()).isNotEmpty();
            assertThat(e.getCause()).isNull();
            assertThat(e.getArguments().size()).isEqualTo(2);
        }
    }
}
