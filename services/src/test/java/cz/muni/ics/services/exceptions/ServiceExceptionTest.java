package cz.muni.ics.services.exceptions;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ServiceExceptionTest {

    @Test
    public void testExceptionCreateWithMessage(){
        String message = "message";
        assertThatThrownBy(() -> {
            throw new ServiceException(message);
        }).hasMessage(message).hasNoCause();
    }

    @Test
    public void testExceptionCreateWithMessageAndThrowable(){
        String message = "message";
        Throwable t = new Exception();
        assertThatThrownBy(() -> {
            throw new ServiceException(message, t);
        }).hasMessage(message).hasCause(t);
    }

    @Test
    public void testExceptionCreateWithMessageAndArguments() {
        String message = "message";
        List<String> arguments = new ArrayList<>();
        arguments.add("text");
        try {
            throw new ServiceException(message, arguments);
        } catch (ServiceException e) {
            assertThat(e.getMessage()).isNotEmpty();
            assertThat(e.getMessage()).contains(message);
            assertThat(e.getCause()).isNull();
        }
    }

    @Test
    public void testExceptionCreateWithMessageAndArgumentsAndThrowable() {
        String message = "message";
        List<String> arguments = new ArrayList<>();
        arguments.add("text");
        Throwable t = new Throwable();
        try {
            throw new ServiceException(message, arguments, t);
        } catch (ServiceException e) {
            assertThat(e.getMessage()).isNotEmpty();
            assertThat(e.getMessage()).contains(message);
            assertThat(e.getCause()).isNotNull();
        }
    }
}
