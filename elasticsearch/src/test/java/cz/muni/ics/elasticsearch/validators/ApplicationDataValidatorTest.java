package cz.muni.ics.elasticsearch.validators;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.elasticsearch.dataclasses.ApplicationData;
import cz.muni.ics.elasticsearch.exceptions.ValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(ZohhakRunner.class)
public class ApplicationDataValidatorTest {

    private ApplicationDataValidator applicationDataValidator = new ApplicationDataValidator();

    @TestWith({
            "testId, testType"
    })
    public void testApplicationDataValidation(String logstashCommandId, String type){
        ApplicationData data = new ApplicationData(logstashCommandId, type);
        applicationDataValidator.validate(data);
    }

    @Test
    public void testNullApplicationDataValidation(){
        assertThatThrownBy(() -> applicationDataValidator.validate(null)).isInstanceOf(ValidationException.class);
    }

    @TestWith({
            ", testCreatorId",
            "testName, ",
            " , "
    })
    public void testInvalidApplicationDataValidation(String logstashCommandId, String type){
        ApplicationData data = new ApplicationData(logstashCommandId, type);
        assertThatThrownBy(() -> applicationDataValidator.validate(data)).isInstanceOf(ValidationException.class);
    }
}
