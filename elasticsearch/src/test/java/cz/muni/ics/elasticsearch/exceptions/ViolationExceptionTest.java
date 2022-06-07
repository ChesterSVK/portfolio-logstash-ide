package cz.muni.ics.elasticsearch.exceptions;

import cz.muni.ics.elasticsearch.exceptions.ValidationException;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ViolationExceptionTest {

    @Test
    public void testExceptionCreateWithMessage() {
        String message = "message";
        assertThatThrownBy(() -> {
            throw new ValidationException(message);
        }).hasMessage(message).hasNoCause();
    }

    @Test
    public void testExceptionCreateWithViolations() {
        Set<? extends ConstraintViolation<?>> constraintViolations = new HashSet<>();
        assertThatThrownBy(() -> {
            throw new ValidationException(constraintViolations);
        }).hasNoCause();
    }

    @Test
    public void testExceptionCreateWithViolationsAndMessage() {
        String message = "message";
        Set<? extends ConstraintViolation<?>> constraintViolations = new HashSet<>();
        assertThatThrownBy(() -> {
            throw new ValidationException(message, constraintViolations);
        }).hasNoCause();
    }
}
