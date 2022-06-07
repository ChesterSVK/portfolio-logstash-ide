//package cz.muni.ics.web.exceptions;
//
//import cz.muni.ics.web.exceptions.WebException;
//import org.junit.Test;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//public class WebExceptionTest {
//
//    @Test
//    public void testExceptionCreateWithMessage(){
//        String message = "message";
//        assertThatThrownBy(() -> {
//            throw new WebException(message);
//        }).hasMessage(message).hasNoCause();
//    }
//
//}
