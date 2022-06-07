package cz.muni.ics.watchdog.unit.dataclasses;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.watchdog.dataclasses.WatchdogConfig;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

@RunWith(ZohhakRunner.class)
public class WatchdogConfigTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @TestWith({
            "type",
    })
    public void testCreate(String type) {
        WatchdogConfig config = new WatchdogConfig(type);
        assertThat(config).isNotNull();
        assertThat(config.getConvertType()).isEqualTo(type);
        assertThat(config.getHost()).isNotNull();
        assertThat(config.toString()).isNotEmpty();
    }

    @TestWith({
            "type",
    })
    public void testSet(String type) {
        WatchdogConfig config = new WatchdogConfig(type);
        assertThat(config).isNotNull();
        assertThat(config.getConvertType()).isEqualTo(type);
        assertThat(config.getHost()).isNotNull();
        assertThat(config.toString()).isNotEmpty();
        config.setHost("testHost");
        assertThat(config.getHost()).isNotNull();
        assertThat(config.getHost()).isEqualTo("testHost");
    }

    @TestWith({
            "null"
    })
    public void testCreateNullType(String type) {
        assertThatThrownBy(() -> new WatchdogConfig(type)).isInstanceOf(NullPointerException.class);
    }

    @TestWith({
            ""
    })
    public void testCreateInvalidType(String type) {
        WatchdogConfig config = new WatchdogConfig(type);
        Set<ConstraintViolation<WatchdogConfig>> constraintViolations =
                validator.validate(config);

        assertThat(constraintViolations.size()).isGreaterThan(0);
    }

    @TestWith({
            "type"
    })
    public void testToString(String type) {
        WatchdogConfig config = new WatchdogConfig(type);
        System.out.println(config.toString());
        assertThat(config.toString()).isEqualTo("WatchdogConfig{convertType='type', host=''}");
    }
}
