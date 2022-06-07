package cz.muni.ics.elasticsearch.dataclasses;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

@RunWith(ZohhakRunner.class)
public class LogstashCommandDataTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testCreate(){
        LogstashCommandData data = new LogstashCommandData();
        assertThat(data.getId()).isNull();
        assertThat(data.getLogstashRootCommands()).isEmpty();
        assertThat(data.toString()).isNotEmpty();
    }
}
