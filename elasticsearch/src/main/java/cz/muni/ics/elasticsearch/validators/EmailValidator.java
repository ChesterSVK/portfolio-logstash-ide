package cz.muni.ics.elasticsearch.validators;

import cz.muni.ics.elasticsearch.exceptions.ValidationException;
import org.elasticsearch.common.inject.Singleton;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Singleton
@Component
public class EmailValidator {

    private Validator obtainValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    public void validate(String email) {
        Set<ConstraintViolation<EmailObject>> constraintViolations =
                obtainValidator().validate(new EmailObject(email));

        if (!constraintViolations.isEmpty()) throw new ValidationException(constraintViolations);
    }

    private class EmailObject {
        @Email
        @NotEmpty
        @NotNull
        private String email;

        private EmailObject(String email) {
            this.email = email;
        }
    }
}
