package cz.muni.ics.elasticsearch.validators;

import cz.muni.ics.elasticsearch.dataclasses.ApplicationData;
import cz.muni.ics.elasticsearch.exceptions.ElasticsearchMessages;
import cz.muni.ics.elasticsearch.exceptions.ValidationException;
import org.elasticsearch.common.inject.Singleton;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@Singleton
@Component
public class ApplicationDataValidator {

    private Validator obtainValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    public void validate(ApplicationData applicationData) {
        if (applicationData == null) throw new ValidationException(ElasticsearchMessages.ERROR_APPLICATION_DATA_NULL);

        Set<ConstraintViolation<ApplicationData>> constraintViolations =
                obtainValidator().validate(applicationData);

        if (!constraintViolations.isEmpty()) throw new ValidationException(constraintViolations);
    }
}
