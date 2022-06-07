//package cz.muni.ics.elasticsearch.dataclasses;
//
//import com.googlecode.zohhak.api.TestWith;
//import com.googlecode.zohhak.api.runners.ZohhakRunner;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validation;
//import javax.validation.Validator;
//import javax.validation.ValidatorFactory;
//import java.util.Set;
//
//import static org.assertj.core.api.Java6Assertions.assertThat;
//import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
//
//@RunWith(ZohhakRunner.class)
//public class UserDataTest {
//
//    private static Validator validator;
//
//    @BeforeClass
//    public static void setUp() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    @Test
//    public void testUserCreateNoArgs(){
//        assertThat(new UserData()).isNotNull();
//    }
//
//    @TestWith({
//            "Tom, Mot, tm@mail.com, 123asD, Tom, Mot, tm@mail.com, 123asD",
//    })
//    public void testUserEquals(String nameTest, String surnameTest, String emailTest, String passTest,
//                               String nameExpected, String surnameExpected, String emailExpected, String passExpected){
//        UserData testUser = new UserData(nameTest, surnameTest, emailTest, passTest);
//        UserData testExpe = new UserData(nameExpected, surnameExpected, emailExpected, passExpected);
//        assertThat(testUser).isEqualTo(testExpe);
//    }
//
//    @TestWith({
//            "Tom, Mot, tm@mail.com, 123asD, tom, Mot, tm@mail.com, 123asD",
//            "Tom, Mot, tm@mail.com, 123asD, Tom, mot, tm@mail.com, 123asD",
//            "Tom, Mot, tm@mail.com, 123asD, Tom, Mot, Tm@mail.com, 123asD",
//            "Tom, Mot, tm@mail.com, 123asD, Tom, Mot, tm@mail.cz, 123asD",
//            "Tom, Mot, tm@mail.com, 123asD, Tom, Mot, tm@mail.com, 123asd",
//    })
//    public void testUserNotEquals(String nameTest, String surnameTest, String emailTest, String passTest,
//                               String nameExpected, String surnameExpected, String emailExpected, String passExpected){
//        UserData testUser = new UserData(nameTest, surnameTest, emailTest, passTest);
//        UserData testExpe = new UserData(nameExpected, surnameExpected, emailExpected, passExpected);
//        assertThat(testUser).isNotEqualTo(testExpe);
//    }
//
//    @TestWith({
//            "Tom, Mot, tm@mail.com, 123asD",
//    })
//    public void testUserCreate(String name, String surname, String email, String pass){
//        UserData u = new UserData(name, surname, email, pass);
//
//        assertThat(u.getName()).isEqualTo(name);
//        assertThat(u.getSurname()).isEqualTo(surname);
//        assertThat(u.getEmail()).isEqualTo(email);
//        assertThat(u.getPassword()).isEqualTo(pass);
//    }
//
//    @TestWith({
//            "Null, Mot, tm@mail.com, 123asD",
//            "Tom, Null, tm@mail.com, 123asD",
//            "Tom, Mot, Null, 123asD",
//            "Tom, Mot, tm@mail.com, Null"
//    })
//    public void testNullUserCreate(String name, String surname, String email, String pass){
//        assertThatThrownBy(()-> new UserData(name, surname, email, pass)).isInstanceOf(NullPointerException.class);
//    }
//
//    @TestWith({
//            "*, Mot, tm@mail.com, 123asD",
//            "Tom, *, tm@mail.com, 123asD",
//            "Tom, Mot, *, 123asD",
//            "Tom, Mot, tm@mail.com, 123456"
//    })
//    public void testInvalidUserCreate(String name, String surname, String email, String pass){
//        UserData u = new UserData(name, surname, email, pass);
//        Set<ConstraintViolation<UserData>> constraintViolations =
//                validator.validate( u );
//
//        assertThat(constraintViolations.size() ).isGreaterThan(0);
//    }
//}
