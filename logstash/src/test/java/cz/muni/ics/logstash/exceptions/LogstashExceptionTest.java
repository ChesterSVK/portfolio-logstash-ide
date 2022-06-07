package cz.muni.ics.logstash.exceptions;

import cz.muni.ics.logstash.exception.LogstashException;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LogstashExceptionTest {

    @Test
    public void testExceptionCreateWithMessage(){
        String message = "message";
        assertThatThrownBy(() -> {
            throw new LogstashException(message);
        }).hasMessage(message).hasNoCause();
    }

    @Test
    public void testExceptionCreateWithMessageAndThrowable(){
        String message = "message";
        Throwable t = new Exception();
        assertThatThrownBy(() -> {
            throw new LogstashException(message, t);
        }).hasMessage(message).hasCause(t);
    }

    @Test
    public void testExceptionCreateWithMessageAndArguments() {
        String message = "message";
        List<String> arguments = new ArrayList<>();
        arguments.add("a");
        arguments.add("b");
        try {
            throw new LogstashException(message, arguments);
        } catch (LogstashException e) {
            assertThat(e.getMessage()).isNotEmpty();
            assertThat(e.getCause()).isNull();
            assertThat(e.getArguments().size()).isEqualTo(2);
            assertThat(e.toString()).isNotEmpty();
        }
    }

    @Test
    public void testExceptionCreateWithMessageAndArgumentsAndThrowable() {
        String message = "message";
        List<String> arguments = new ArrayList<>();
        arguments.add("a");
        arguments.add("b");
        Throwable t = new Throwable();
        try {
            throw new LogstashException(message, arguments, t);
        } catch (LogstashException e) {
            assertThat(e.getMessage()).isNotEmpty();
            assertThat(e.getCause()).isNotNull();
            assertThat(e.getArguments().size()).isEqualTo(2);
            assertThat(e.toString()).isNotEmpty();
        }
    }
}
