package cz.muni.ics.logstash.types;

import cz.muni.ics.logstash.enums.AnonymizeAlgorithm;
import cz.muni.ics.logstash.enums.LType;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class LAnonymizeTest {

    @Test
    public void testLAnonymize(){
        LAnonymizeType string = new LAnonymizeType(AnonymizeAlgorithm.IPV4_NETWORK);
        assertThat(string.toLogstashString(0)).isNotNull();
        assertThat(string.toLogstashString(0)).isNotEmpty();
        assertThat(string.toLogstashString(0)).startsWith("\"");
        assertThat(string.toLogstashString(0)).endsWith("\"");
        assertThat(string.getLString()).isEqualTo(AnonymizeAlgorithm.IPV4_NETWORK.toString());
        assertThat(string.isInitialised()).isTrue();
        assertThat(string.getType()).isEqualTo(LType.LANONYMIZE_ALGO);
        assertThat(string.toString()).isNotEmpty();
    }

    @Test
    public void testNullLAnonymize(){
        assertThatThrownBy(() -> new LAnonymizeType(null)).isInstanceOf(NullPointerException.class);
    }
}
