package cz.muni.ics.core.exceptions;

import org.junit.Test;

import javax.validation.ConstraintViolation;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CoreValidationExceptionTest {

    @Test
    public void testExceptionCreateWithMessage() {
        String message = "message";
        assertThatThrownBy(() -> {
            throw new CoreValidationException(message);
        }).hasMessage(message).hasNoCause();
    }

    @Test
    public void testExceptionCreateWithViolations() {
        Set<? extends ConstraintViolation<?>> constraintViolations = new HashSet<>();
        assertThatThrownBy(() -> {
            throw new CoreValidationException(constraintViolations);
        }).hasNoCause();
    }

    @Test
    public void testExceptionCreateWithViolationsAndMessage() {
        String message = "message";
        Set<? extends ConstraintViolation<?>> constraintViolations = new HashSet<>();
        assertThatThrownBy(() -> {
            throw new CoreValidationException(message, constraintViolations);
        }).hasNoCause();
    }

}
