package cz.muni.ics.elasticsearch.validators;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.elasticsearch.exceptions.ValidationException;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(ZohhakRunner.class)
public class StringValidatorTest {

    private StringValidator stringValidator = new StringValidator();

    @TestWith({
            "test"
    })
    public void testStringValidator(String string){
        stringValidator.validate(string);
    }

    @TestWith({
            "",
            "null"
    })
    public void testStringValidatorInvalidInput(String string){
        assertThatThrownBy(() -> stringValidator.validate(string)).isInstanceOf(ValidationException.class);
    }
}
