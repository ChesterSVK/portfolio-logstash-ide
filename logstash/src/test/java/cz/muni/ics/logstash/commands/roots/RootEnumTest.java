package cz.muni.ics.logstash.commands.roots;

import cz.muni.ics.logstash.interfaces.Root;
import cz.muni.ics.logstash.interfaces.RootEnum;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class RootEnumTest {

    @Test
    public void testInstantiateAllRoots() {
        for (RootEnum r : RootEnumImpl.values()) {
            assertThat(r.getCommandName()).isNotEmpty();
            assertThat(r.getCommandInstance()).isNotNull();
            assertThat(r.getCommandInstance()).isInstanceOf(Root.class);
        }
    }

    @Test
    public void testEquality() {
        for (RootEnum r1 : RootEnumImpl.values()) {
            for (RootEnum r2 : RootEnumImpl.values()) {
                if (r1.getCommandName().equals(r2.getCommandName())) {
                    assertThat(r1.equals(r2)).isTrue();
                } else assertThat(r1.equals(r2)).isFalse();
            }
        }
    }
}
