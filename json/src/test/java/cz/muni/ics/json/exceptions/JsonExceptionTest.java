package cz.muni.ics.json.exceptions;

import cz.muni.ics.json.exception.JsonException;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JsonExceptionTest {

    @Test
    public void testExceptionCreateWithMessage(){
        String message = "message";
        assertThatThrownBy(() -> {
            throw new JsonException(message);
        }).hasMessage(message).hasNoCause();
    }

    @Test
    public void testExceptionToString(){
        String message = "message";
        try {
            throw new JsonException(message);
        } catch (JsonException e) {
            assertThat(e.getArguments()).isNull();
            assertThat(e.getMessage()).isEqualTo(message);
            assertThat(e.toString()).isNotEmpty();
        }
    }

    @Test
    public void testExceptionToStringNullMessage(){
        try {
            throw new JsonException(null);
        } catch (JsonException e) {
            assertThat(e.toString()).isNotEmpty();
            assertThat(e.getArguments()).isNull();
        }
    }

    @Test
    public void testExceptionCreateWithMessageAndCause(){
        Throwable c = new Throwable();
        try {
            throw new JsonException("Message", c);
        } catch (JsonException e) {
            assertThat(e.getCause()).isEqualTo(c);
            assertThat(e.toString()).contains("Message");
        }
    }
}
