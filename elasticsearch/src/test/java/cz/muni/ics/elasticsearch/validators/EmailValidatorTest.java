package cz.muni.ics.elasticsearch.validators;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.elasticsearch.exceptions.ValidationException;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(ZohhakRunner.class)
public class EmailValidatorTest {

    private EmailValidator emailValidator = new EmailValidator();

    @TestWith({
            "test@test.test"
    })
    public void testStringValidator(String string){
        emailValidator.validate(string);
    }

    @TestWith({
            "",
            "null",
            "test",
            "test@"
    })
    public void testStringValidatorInvalidInput(String string){
        assertThatThrownBy(() -> emailValidator.validate(string)).isInstanceOf(ValidationException.class);
    }
}
