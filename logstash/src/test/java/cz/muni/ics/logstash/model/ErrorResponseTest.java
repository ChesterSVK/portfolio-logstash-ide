package cz.muni.ics.logstash.model;

import com.googlecode.zohhak.api.TestWith;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ErrorResponseTest {


    private ErrorItem item = new ErrorItem("test",Collections.emptyList());

    @Test
    public void testCreate(){
        assertThat(item).isNotNull();
        assertThat(new ErrorResponse(Collections.singletonList(item))).isNotNull();
    }

    @Test
    public void testGetters(){
        ErrorResponse response = new ErrorResponse(item);
        assertThat(response.getErrors()).isNotNull();
        assertThat(response.getErrors()).isNotEmpty();
        assertThat(response.getErrors()).hasSize(1);
    }

    @Test
    public void testSetters(){
        ErrorResponse response = new ErrorResponse(item);
        response.setErrors(Collections.singletonList(item));
        assertThat(response.getErrors()).isNotNull();
        assertThat(response.getErrors()).isNotEmpty();
        assertThat(response.getErrors()).hasSize(1);
    }

    @Test
    public void testEquals(){
        ErrorResponse response1 = new ErrorResponse(item);
        ErrorResponse response2 = new ErrorResponse(new ErrorItem("test1", Collections.emptyList()));

        assertThat(response1.equals(response2)).isFalse();
        assertThat(response1.equals(response1)).isTrue();
    }



}