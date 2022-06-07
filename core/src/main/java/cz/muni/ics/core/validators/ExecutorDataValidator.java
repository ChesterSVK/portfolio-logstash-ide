package cz.muni.ics.core.validators;

import cz.muni.ics.core.dataclasses.ExecutorData;
import cz.muni.ics.core.exceptions.ApplicationMessages;
import cz.muni.ics.core.exceptions.CoreValidationException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Component
public class ExecutorDataValidator {

    private Validator obtainValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    public void validate(ExecutorData executorData) throws CoreValidationException {
        if (executorData == null) throw new CoreValidationException(ApplicationMessages.ERROR_EXECUTOR_DATA_NULL);

        Set<ConstraintViolation<ExecutorData>> constraintViolations =
                obtainValidator().validate( executorData );

        if (!constraintViolations.isEmpty()) throw new CoreValidationException(String.valueOf(constraintViolations));
    }
}
