package cz.muni.ics.logstash.types;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.logstash.enums.LType;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(ZohhakRunner.class)
public class LTimestampArrayTest {

    @Test
    public void testEmptyArray() {
        LTimestampArray array = new LTimestampArray();
        assertThat(array.toLogstashString(0)).isEqualTo("[]");
        assertThat(array.isInitialised()).isFalse();
        assertThat(array.getType()).isEqualTo(LType.LTIMESTAMP_ARRAY);
        assertThat(array.toString()).isNotEmpty();
    }

    @TestWith({
            "a",
    })
    public void testArraySingleElement(String key) {
        LTimestampArray array = new LTimestampArray();
        array.add(new LString(key));
        assertThat(array.toLogstashString(0)).isEqualTo("\t[\"a\"]");
        assertThat(array.isInitialised()).isTrue();
        assertThat(array.getType()).isEqualTo(LType.LTIMESTAMP_ARRAY);
        assertThat(array.toString()).isNotEmpty();
    }

    @TestWith({
            "a",
    })
    public void testArrayMultipleElements(String key) {
        LTimestampArray array = new LTimestampArray();
        array.add(new LString(key));
        array.add(new LString(key));
        assertThat(array.toLogstashString(0)).isEqualTo("\t[\"a\", \"a\"]");
        assertThat(array.isInitialised()).isTrue();
        assertThat(array.getType()).isEqualTo(LType.LTIMESTAMP_ARRAY);
        assertThat(array.toString()).isNotEmpty();
    }
}
