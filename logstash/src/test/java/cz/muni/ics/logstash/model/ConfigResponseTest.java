package cz.muni.ics.logstash.model;

import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ConfigResponseTest {

    @Test
    public void testCreate(){
        assertThat(new ConfigResponse("test", Collections.emptyList())).isNotNull();
    }

    @Test
    public void testGetters(){
        ConfigResponse response = new ConfigResponse("test", Collections.emptyList());
        assertThat(response.getCommands()).isEmpty();
        assertThat(response.getConfigString()).isEqualTo("test");
    }

    @Test
    public void testSetters(){
        ConfigResponse response = new ConfigResponse("test", Collections.emptyList());

        response.setConfigString("test");
        response.setCommands(Collections.emptyList());

        assertThat(response.getCommands()).isEmpty();
        assertThat(response.getConfigString()).isEqualTo("test");
    }

    @Test
    public void testEquals(){
        ConfigResponse response1 = new ConfigResponse("test1", Collections.emptyList());
        ConfigResponse response2 = new ConfigResponse("test2", Collections.emptyList());
        assertThat(response1.equals(response2)).isFalse();
    }

}
