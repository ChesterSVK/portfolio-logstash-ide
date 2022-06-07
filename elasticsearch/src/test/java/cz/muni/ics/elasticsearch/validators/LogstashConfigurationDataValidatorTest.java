package cz.muni.ics.elasticsearch.validators;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.elasticsearch.dataclasses.LogstashCommandData;
import cz.muni.ics.elasticsearch.exceptions.ValidationException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(ZohhakRunner.class)
public class LogstashConfigurationDataValidatorTest {

    private LogstashCommandDataValidator validator = new LogstashCommandDataValidator();

    @TestWith({
            "testId"
    })
    public void testLogstashConfigurationDataValidation(String arg){
        LogstashCommandData data = new LogstashCommandData();
        data.getLogstashRootCommands().add(arg);
        validator.validate(data);
    }

    @Test
    public void testLogstashConfigurationDataNullListValidation(){
        LogstashCommandData data = new LogstashCommandData(null);
        assertThatThrownBy(() -> validator.validate(null)).isInstanceOf(ValidationException.class);
    }

    @Test
    public void testLogstashConfigurationDataEmptyListValidation(){
        LogstashCommandData data = new LogstashCommandData(new ArrayList<>());
        assertThatThrownBy(() -> validator.validate(null)).isInstanceOf(ValidationException.class);
    }

    @Test
    public void testNullLogstashConfigurationDataValidation(){
        assertThatThrownBy(() -> validator.validate(null)).isInstanceOf(ValidationException.class);
    }



    @Test
    public void testInvalidLogstashConfidurationDataValidation(){
        LogstashCommandData data = new LogstashCommandData();
        assertThatThrownBy(() -> validator.validate(data)).isInstanceOf(ValidationException.class);
    }
}
