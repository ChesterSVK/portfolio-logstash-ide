package cz.muni.ics.core.exceptions;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

public class CoreValidationException extends ConstraintViolationException {

    public CoreValidationException(String message, Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(message, constraintViolations);
    }

    public CoreValidationException(Set<? extends ConstraintViolation<?>> constraintViolations) {
        super(constraintViolations);
    }

    public CoreValidationException(String message){
        super(message,null);
    }
}
