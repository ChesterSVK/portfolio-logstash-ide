package cz.muni.ics.logstash.model;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class SuccessResponseTest {

    @Test
    public void testCreate(){
        Object o = new Object();
        assertThat(new SuccessResponse(o)).isNotNull();
    }

    @Test
    public void testGetters(){
        Object o = new Object();
        SuccessResponse response = new SuccessResponse(o);
        assertThat(response.getData()).isEqualTo(o);
    }

    @Test
    public void testSetters(){
        Object o = new Object();
        SuccessResponse response = new SuccessResponse(o);
        response.setData(o);
        assertThat(response.getData()).isEqualTo(o);
    }

    @Test
    public void testNullCreate(){
        assertThatThrownBy( () -> new SuccessResponse(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testInvalidSetters(){
        Object o = new Object();
        SuccessResponse response = new SuccessResponse(o);
        assertThatThrownBy( () -> response.setData(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testEquals(){
        Object o1 = new Object();
        SuccessResponse response1 = new SuccessResponse(o1);

        Object o2 = new Object();
        SuccessResponse response2 = new SuccessResponse(o2);

        assertThat(response1.equals(response2)).isFalse();
    }

}
