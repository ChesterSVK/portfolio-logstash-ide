package cz.muni.ics.watchdog.unit.dataclasses;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.watchdog.dataclasses.WatchdogData;
import cz.muni.ics.watchdog.enums.WatchdogAction;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

@RunWith(ZohhakRunner.class)
public class WatchdogDataTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @TestWith({
            "scriptPath, configPath"
    })
    public void testCreate(String scriptPath, String configPath) {
        WatchdogData data = new WatchdogData(scriptPath, configPath);

        assertThat(data).isNotNull();
        assertThat(data.getAction()).isNotNull();
        assertThat(data.getAction().getAction()).isEqualTo(WatchdogAction.REGISTER.getAction());
        assertThat(data.getConfigPath()).isNotNull();
        assertThat(data.getMinutes()).isNotNull();
        assertThat(data.getScriptPath()).isNotNull();
    }

    @TestWith({
            "null, configPath",
            "scriptPath, null",
            "null, null"
    })
    public void testCreateNullArgs(String scriptPath, String configPath) {
        assertThatThrownBy(() -> new WatchdogData(scriptPath, configPath)).isInstanceOf(NullPointerException.class);
    }


    @TestWith({
            ", configPath",
            "scriptPath, ",
            ","
    })
    public void testCreateEmptyArgs(String scriptPath, String configPath) {
        WatchdogData data = new WatchdogData(scriptPath, configPath);
        Set<ConstraintViolation<WatchdogData>> constraintViolations =
                validator.validate(data);

        assertThat(constraintViolations.size()).isGreaterThan(0);
    }

    @TestWith({
            "scriptPath, configPath, -1",
            "scriptPath, configPath, 0",
            "scriptPath, configPath, 100",
    })
    public void testCreateInvalidMinutes(String scriptPath, String configPath, Integer minutes) {
        WatchdogData data = new WatchdogData(scriptPath, configPath, minutes, WatchdogAction.REGISTER, new HashSet<>());
        Set<ConstraintViolation<WatchdogData>> constraintViolations =
                validator.validate(data);

        assertThat(constraintViolations.size()).isGreaterThan(0);
    }

    @TestWith({
            "scriptPath, configPath, 10"
    })
    public void testCreateAllArgs(String scriptPath, String configPath, int minutes) {
        WatchdogData config = new WatchdogData(scriptPath, configPath, minutes, WatchdogAction.REGISTER, new HashSet<>());
        assertThat(config).isNotNull();
        assertThat(config.getAction()).isNotNull();
        assertThat(config.getConfigPath()).isNotNull();
        assertThat(config.getMinutes()).isNotNull();
        assertThat(config.getScriptPath()).isNotNull();
        assertThat(config.getFoldersToWatch()).isNotNull();
    }

    @TestWith({
            "null, configPath, 10",
            "scriptPath, null, 10",
            "scriptPath, configPath, null",
            "null, null, 10",
            "scriptPath, null, null",
            "null, configPath, null",
            "null, null, null",
    })
    public void testCreateAllArgsNull1(String scriptPath, String configPath, Integer minutes) {
        assertThatThrownBy(() -> new WatchdogData(scriptPath, configPath, minutes, WatchdogAction.REGISTER, new HashSet<>())).isInstanceOf(NullPointerException.class);
    }

    @TestWith({
            "scriptPath, configPath, 10",
    })
    public void testCreateAllArgsNull2(String scriptPath, String configPath, int minutes) {
        assertThatThrownBy(() -> new WatchdogData(scriptPath, configPath, minutes, null, new HashSet<>())).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new WatchdogData(scriptPath, configPath, minutes, WatchdogAction.REGISTER, null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new WatchdogData(scriptPath, configPath, minutes, null, null)).isInstanceOf(NullPointerException.class);
    }

    @TestWith({
            "scriptPath, configPath, 10",
    })
    public void testConfigSettersInvalidArgs(String scriptPath, String configPath, int minutes) {
        WatchdogData data = new WatchdogData(scriptPath, configPath, minutes, WatchdogAction.REGISTER, new HashSet<>());
        Set<ConstraintViolation<WatchdogData>> constraintViolations;

        data.setMinutes(-1);
        constraintViolations = validator.validate(data);
        assertThat(constraintViolations.size()).isGreaterThan(0);
        constraintViolations.clear();

        data.setMinutes(0);
        constraintViolations = validator.validate(data);
        assertThat(constraintViolations.size()).isGreaterThan(0);
        constraintViolations.clear();

        data.setMinutes(100);
        constraintViolations = validator.validate(data);
        assertThat(constraintViolations.size()).isGreaterThan(0);
        constraintViolations.clear();
    }

    @TestWith({
            "scriptPath, configPath, 10",
    })
    public void testConfigSettersNullArgs(String scriptPath, String configPath, int minutes) {
        WatchdogData data = new WatchdogData(scriptPath, configPath, minutes, WatchdogAction.REGISTER, new HashSet<>());
        assertThatThrownBy(() -> data.setAction(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> data.setFoldersToWatch(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> data.setMinutes(null)).isInstanceOf(NullPointerException.class);
    }
}
