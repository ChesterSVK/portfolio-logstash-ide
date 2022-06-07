package cz.muni.ics.logstash.model;

import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ErrorItemTest {



    @Test
    public void testCreate(){
        assertThat(new ErrorItem("message", Collections.singletonList("arg"))).isNotNull();
    }

    @Test
    public void testGetters(){
        ErrorItem item = new ErrorItem("message", Collections.singletonList("arg"));
        assertThat(item.getCode()).isEqualTo(500);
        assertThat(item.getId()).isEqualTo("server");
        assertThat(item.getMeta()).isNotEmpty();
        assertThat(item.getTitle()).isNotEmpty();
        assertThat(item.getTitle()).isEqualTo("message");
    }

    @Test
    public void testSetters(){
        ErrorItem item = new ErrorItem("message", Collections.singletonList("arg"));

        item.setCode(400);
        assertThat(item.getCode()).isEqualTo(400);

        item.setId("test");
        assertThat(item.getId()).isEqualTo("test");

        item.setMeta(Collections.emptyList());
        assertThat(item.getMeta()).isEmpty();

        item.setTitle("title");
        assertThat(item.getTitle()).isNotEmpty();
        assertThat(item.getTitle()).isEqualTo("title");
    }

    @Test
    public void testEquals(){
        ErrorItem item1 = new ErrorItem("message1", Collections.singletonList("arg"));
        ErrorItem item2 = new ErrorItem("message2", Collections.singletonList("arg"));

        assertThat(item1.equals(item1)).isTrue();
        assertThat(item1.equals(item2)).isFalse();
    }
}
