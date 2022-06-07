package cz.muni.ics.logstash.types;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.logstash.enums.LType;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

@RunWith(ZohhakRunner.class)
public class LUriTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testEmptyLUri(){
        LUri string = new LUri();
        assertThat(string.toLogstashString(0)).isNotNull();
        assertThat(string.toLogstashString(0)).isNotEmpty();
        assertThat(string.toLogstashString(0)).startsWith("\"");
        assertThat(string.toLogstashString(0)).endsWith("\"");
        assertThat(string.isInitialised()).isFalse();
        assertThat(string.getType()).isEqualTo(LType.LURI);
        assertThat(string.toString()).isEmpty();
    }

    @TestWith({
            "test"
    })
    public void testLUri(String arg){
        LUri string = new LUri(arg);
        assertThat(string.toLogstashString(0)).isNotNull();
        assertThat(string.toLogstashString(0)).isNotEmpty();
        assertThat(string.toLogstashString(0)).startsWith("\"");
        assertThat(string.toLogstashString(0)).endsWith("\"");
        assertThat(string.getLString()).isEqualTo(arg);
        assertThat(string.isInitialised()).isTrue();
        assertThat(string.getType()).isEqualTo(LType.LURI);
        assertThat(string.toString()).isNotEmpty();
    }


    @TestWith({
            "null"
    })
    public void testNullLUri(String arg){
        assertThatThrownBy(() -> new LUri(arg)).isInstanceOf(NullPointerException.class);
    }

    @TestWith({
            ""
    })
    public void testInvalidLUri(String arg){
        LUri string = new LUri(arg);
        Set<ConstraintViolation<LUri>> constraintViolations =
                validator.validate( string );
        assertThat( constraintViolations.size() ).isGreaterThan(0);
    }
}
