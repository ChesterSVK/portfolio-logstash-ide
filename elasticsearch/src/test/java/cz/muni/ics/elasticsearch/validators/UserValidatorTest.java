//package cz.muni.ics.elasticsearch.validators;
//
//import com.googlecode.zohhak.api.TestWith;
//import com.googlecode.zohhak.api.runners.ZohhakRunner;
//import cz.muni.ics.elasticsearch.dataclasses.UserData;
//import cz.muni.ics.elasticsearch.exceptions.ValidationException;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
//
//@RunWith(ZohhakRunner.class)
//public class UserValidatorTest {
//
//    private UserValidator userValidator = new UserValidator();
//
//    @TestWith({
//            "name, surname, email@email.test, Pass123"
//    })
//    public void testUserValidation(String name,
//                                   String surname,
//                                   String email,
//                                   String pass){
//
//        UserData user = new UserData(name, surname, email, pass);
//
//        userValidator.validate(user);
//    }
//
//    @TestWith({
//            ", surname, email@email.test, Pass123",
//            "name, , email@email.test, Pass123",
//            "name, surname, , Pass123",
//            "name, surname, email, Pass123",
//            "name, surname, email@, Pass123",
//            "name, surname, email@email.test, ",
//            "name, surname, email@email.test, pass123",
//    })
//    public void testInvalidUserValidation(String name,
//                                   String surname,
//                                   String email,
//                                   String pass){
//        UserData user = new UserData(name, surname, email, pass);
//        assertThatThrownBy(() -> userValidator.validate(user)).isInstanceOf(ValidationException.class);
//    }
//
//    @Test
//    public void testNullUser(){
//        assertThatThrownBy(() -> userValidator.validate(null)).isInstanceOf(ValidationException.class);
//    }
//}
