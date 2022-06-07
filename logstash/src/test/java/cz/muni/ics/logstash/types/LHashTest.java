package cz.muni.ics.logstash.types;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.logstash.enums.LType;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(ZohhakRunner.class)
public class LHashTest {

    @Test
    public void testEmptyHash() {
        LHash hash = new LHash();
        assertThat(hash.toLogstashString(0)).isEqualTo("{}");
        assertThat(hash.isInitialised()).isFalse();
        assertThat(hash.getType()).isEqualTo(LType.LHASH_MAP);
        assertThat(hash.toString()).isNotEmpty();
    }

    @TestWith({
            "a, b",
    })
    public void testHash(String key, String value) {
        LHash hash = new LHash();
        hash.put(new LString(key), new LString(value));
        assertThat(hash.toLogstashString(0)).isEqualTo("{\n\t\"a\" => \"b\"\n}");
        assertThat(hash.isInitialised()).isTrue();
        assertThat(hash.getType()).isEqualTo(LType.LHASH_MAP);
        assertThat(hash.toString()).isNotEmpty();
    }
}
