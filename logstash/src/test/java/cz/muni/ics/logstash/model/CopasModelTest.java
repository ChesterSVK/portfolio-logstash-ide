package cz.muni.ics.logstash.model;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CopasModelTest {

    @Test
    public void testCreate(){
        assertThat(new CopasTransformationsModel()).isNotNull();
    }
}
