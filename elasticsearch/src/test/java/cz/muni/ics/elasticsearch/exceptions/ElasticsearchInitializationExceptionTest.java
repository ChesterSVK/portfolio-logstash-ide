package cz.muni.ics.elasticsearch.exceptions;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ElasticsearchInitializationExceptionTest {

    @Test
    public void testExceptionCreateWithMessage(){
        String message = "message";
        assertThatThrownBy(() -> {
            throw new ElasticsearchInitializationException(message);
        }).hasMessage(message).hasNoCause();
    }
}
