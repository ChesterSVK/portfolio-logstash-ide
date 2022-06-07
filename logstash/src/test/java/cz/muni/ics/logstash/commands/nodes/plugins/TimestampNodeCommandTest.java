package cz.muni.ics.logstash.commands.nodes.plugins;

import cz.muni.ics.logstash.commands.leafs.categories.CommonsLeaf;
import cz.muni.ics.logstash.commands.leafs.categories.DateLeaf;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.enums.TimestampFormat;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.types.LArray;
import cz.muni.ics.logstash.types.LString;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class TimestampNodeCommandTest {

    @Test
    public void testCreate() throws LogstashException, InstantiationException, IllegalAccessException {
        Node timestamp = NodeEnumImpl.TIMESTAMP.getCommandInstance();
        Leaf l = DateLeaf.MATCH.getCommandInstance();
        l.setCommandArgument(new LArray(Collections.singletonList("test")));
        timestamp.add(l);
        assertThat(timestamp).isNotNull();
        assertThat(timestamp.toLogstashString(0)).isNotEmpty();
        assertThat(timestamp.toJsonString()).isNotEmpty();
        assertThat(timestamp.size()).isEqualTo(1);
        assertThat(timestamp.getCommandName()).isNotEmpty();
    }


    @Test
    public void testAddValid() throws LogstashException, InstantiationException, IllegalAccessException {
        Node timestamp = NodeEnumImpl.TIMESTAMP.getCommandInstance();
        assertThat(timestamp).isNotNull();
        Leaf l = DateLeaf.MATCH.getCommandInstance();
        l.setCommandArgument(new LArray(Collections.singletonList("test")));
        timestamp.add(l);
        assertThat(timestamp.size()).isEqualTo(1);
        assertThat(timestamp.get(0)).isEqualTo(l);
    }

    @Test
    public void testAddInvalid() throws LogstashException, InstantiationException, IllegalAccessException {
        Node timestamp = NodeEnumImpl.TIMESTAMP.getCommandInstance();
        assertThat(timestamp).isNotNull();
        Leaf l = CommonsLeaf.ID.getCommandInstance();
        l.setCommandArgument(new LString(TimestampFormat.values()[0].getFormat()));
        timestamp.add(l);
        assertThat(timestamp.size()).isEqualTo(1);
        assertThat(timestamp.get(0)).isNotEqualTo(l);
    }

    @Test
    public void testDeleteValid() throws LogstashException, InstantiationException, IllegalAccessException {
        Node timestamp = NodeEnumImpl.TIMESTAMP.getCommandInstance();
        assertThat(timestamp).isNotNull();
        Leaf l = DateLeaf.MATCH.getCommandInstance();
        l.setCommandArgument(new LArray(Collections.singletonList("test")));
        timestamp.remove(l);
        assertThat(timestamp.size()).isEqualTo(0);
    }

    @Test
    public void testSpecificBehaviour() throws LogstashException, InstantiationException, IllegalAccessException {
        Node timestamp = NodeEnumImpl.TIMESTAMP.getCommandInstance();
        assertThat(timestamp).isNotNull();
        Leaf l = DateLeaf.MATCH.getCommandInstance();
        assertThatThrownBy( () -> timestamp.set(0, l)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy( () -> timestamp.remove(0)).isInstanceOf(UnsupportedOperationException.class);
        assertThat(timestamp.removeAll(Collections.singleton(l))).isFalse();
        assertThat(timestamp.size()).isEqualTo(1);
        assertThat(timestamp.addAll(Collections.singleton(l))).isFalse();
        assertThat(timestamp.size()).isEqualTo(1);
        assertThat(timestamp.addAll(0, Collections.singleton(l))).isFalse();
        assertThat(timestamp.size()).isEqualTo(1);
        timestamp.add(0, l);
        assertThat(timestamp.size()).isEqualTo(1);
    }
}
