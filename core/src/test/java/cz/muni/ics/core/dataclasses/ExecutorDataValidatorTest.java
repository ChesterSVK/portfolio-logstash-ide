package cz.muni.ics.core.dataclasses;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.core.exceptions.CoreValidationException;
import cz.muni.ics.core.validators.ExecutorDataValidator;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(ZohhakRunner.class)
public class ExecutorDataValidatorTest {

    private ExecutorDataValidator validator = new ExecutorDataValidator();

    @TestWith({
            "testId"
    })
    public void testExecutorDataValidation(String command) {
        ExecutorData data = new ExecutorData(command);
        validator.validate(data);
    }

    @Test
    public void testNullExecutorDataValidation() {
        assertThatThrownBy(() -> validator.validate(null)).isInstanceOf(CoreValidationException.class);
    }

    @TestWith({
            ""
    })
    public void testInvalidExecutorDataValidation(String command) {
        ExecutorData data = new ExecutorData(command);
        assertThatThrownBy(() -> validator.validate(data)).isInstanceOf(CoreValidationException.class);
    }


}
