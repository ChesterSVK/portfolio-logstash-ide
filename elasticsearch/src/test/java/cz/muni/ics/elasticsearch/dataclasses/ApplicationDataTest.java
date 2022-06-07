package cz.muni.ics.elasticsearch.dataclasses;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
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
public class ApplicationDataTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testApplicationDataCreateNoArgs(){
        assertThat(new ApplicationData()).isNotNull();
    }

    @TestWith({
            "TestId, testType, TestId, testType",
    })
    public void testApplicationDataEquals(String nameTest, String creatorTest,
                               String nameExpected, String creatorExpected){
        ApplicationData testWF = new ApplicationData(nameTest, creatorTest);
        ApplicationData expeWF = new ApplicationData(nameExpected, creatorExpected);

        assertThat(testWF.getId()).isEqualTo(expeWF.getId());
        assertThat(testWF.getType()).isEqualTo(expeWF.getType());
        assertThat(testWF.getLogstashCommandId()).isEqualTo(expeWF.getLogstashCommandId());
        assertThat(testWF.getFigureNote()).isEqualTo(expeWF.getFigureNote());
    }


    @TestWith({
            "TestId, TestNote, testType, testName, TestNote, testId",
            "TestId, TestNote, testType, TestName, testNote, testId",
            "TestId, TestNote, testType, TestName, TestNote, testId1",
    })
    public void testApplicationDataNotEquals(String idTest, String noteTest, String typeTest,
                                     String idExpected, String noteExpected, String typeExpected){
        ApplicationData testWF = new ApplicationData(idTest, typeTest);
        testWF.setFigureNote(noteTest);
        ApplicationData expeWF = new ApplicationData(idExpected, typeExpected);
        expeWF.setFigureNote(noteExpected);

        assertThat(testWF).isNotEqualTo(expeWF);
    }


    @TestWith({
            "testId, testType",
    })
    public void testApplicationDataCreate(String id, String type){
        ApplicationData wF = new ApplicationData(id, type);

        assertThat(wF.getFigureNote()).isNotNull();
    }

    @TestWith({
            "null,     testId",
            "testName, null",
            "null,     null",
    })
    public void testNullApplicationDataCreate(String id, String type){
        assertThatThrownBy(() -> new ApplicationData(id, type)).isInstanceOf(NullPointerException.class);
    }

    @TestWith({
            ",        testType",
            "testId, ",
            ",        "
    })
    public void testInvalidApplicationDataCreate(String id, String type){
        ApplicationData wf = new ApplicationData(id, type);
        Set<ConstraintViolation<ApplicationData>> constraintViolations =
                validator.validate( wf );
        assertThat( constraintViolations.size() ).isGreaterThan(0);
    }
}
