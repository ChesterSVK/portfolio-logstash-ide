package cz.muni.ics.elasticsearch.validators;

import cz.muni.ics.elasticsearch.exceptions.ElasticsearchMessages;
import cz.muni.ics.elasticsearch.exceptions.ValidationException;
import org.elasticsearch.common.inject.Singleton;
import org.springframework.stereotype.Component;

@Singleton
@Component
public class StringValidator {

    public void validate(String argument) {
        if (argument == null) {
            throw new ValidationException(ElasticsearchMessages.ERROR_STRING_NULL);
        }
        if (argument.isEmpty()) {
            throw new ValidationException(ElasticsearchMessages.ERROR_STRING_EMPTY);
        }
    }
}
