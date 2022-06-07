package cz.muni.ics.logstash.types;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.logstash.enums.LType;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(ZohhakRunner.class)
public class LArrayTest {

    @Test
    public void testEmptyArray(){
        LArray array = new LArray();
        assertThat(array.toLogstashString(0)).isEqualTo("[]");
        assertThat(array.isInitialised()).isFalse();
        assertThat(array.getType()).isEqualTo(LType.LARRAY);
        assertThat(array.toString()).isNotEmpty();
    }

    @TestWith({
            "a",
    })
    public void testArraySingleElement(String key){
        LArray array = new LArray();
        array.add(new LString(key));
        assertThat(array.toLogstashString(0)).isEqualTo("\t[\"a\"]");
        assertThat(array.isInitialised()).isTrue();
        assertThat(array.getType()).isEqualTo(LType.LARRAY);
        assertThat(array.toString()).isNotEmpty();
    }
    @TestWith({
            "a",
    })
    public void testArrayMultipleElements(String key){
        LArray array = new LArray();
        array.add(new LString(key));
        array.add(new LString(key));
        assertThat(array.toLogstashString(0)).isEqualTo("\t[\"a\", \"a\"]");
        assertThat(array.isInitialised()).isTrue();
        assertThat(array.getType()).isEqualTo(LType.LARRAY);
        assertThat(array.toString()).isNotEmpty();
    }

}
