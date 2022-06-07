package cz.muni.ics.elasticsearch.validators;

import cz.muni.ics.elasticsearch.dataclasses.LogstashCommandData;
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
public class LogstashCommandDataValidator {

    private Validator obtainValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    public void validate(LogstashCommandData commandData) {
        if (commandData == null) throw new ValidationException(ElasticsearchMessages.ERROR_LOGSTAS_DATA_NULL);

        Set<ConstraintViolation<LogstashCommandData>> constraintViolations =
                obtainValidator().validate(commandData);

        if (!constraintViolations.isEmpty()) throw new ValidationException(constraintViolations);
    }
}
