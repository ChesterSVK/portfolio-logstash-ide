package cz.muni.ics.logstash.commands.nodes.plugins;

import cz.muni.ics.logstash.commands.leafs.categories.CommonsLeaf;
import cz.muni.ics.logstash.commands.leafs.categories.DateLeaf;
import cz.muni.ics.logstash.commands.leafs.categories.FingerprintLeaf;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.types.LString;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class AnonymizeNodeCommandTest {

    @Test
    public void testCreate() throws LogstashException {
        Node ann = NodeEnumImpl.ANONYMIZE.getCommandInstance();
        assertThat(ann).isNotNull();
        assertThat(ann.toLogstashString(0)).isNotEmpty();
        assertThat(ann.toJsonString()).isNotEmpty();
        assertThat(ann.size()).isEqualTo(3);
        assertThat(ann.getCommandName()).isNotEmpty();
    }

    @Test
    public void testAddInvalid() throws InstantiationException, IllegalAccessException, LogstashException {
        Node ann = NodeEnumImpl.ANONYMIZE.getCommandInstance();
        assertThat(ann).isNotNull();
        Leaf l = CommonsLeaf.ID.getCommandInstance();
        l.setCommandArgument(new LString("test"));
        assertThat(ann.add(l)).isFalse();
        assertThat(ann.size()).isEqualTo(3);
    }

    @Test
    public void testAddValidMethod() throws InstantiationException, IllegalAccessException, LogstashException {
        Node ann = NodeEnumImpl.ANONYMIZE.getCommandInstance();
        assertThat(ann).isNotNull();
        Leaf l = FingerprintLeaf.METHOD.getCommandInstance();
        l.setCommandArgument(new LString("test"));
        assertThat(ann.add(l)).isTrue();
        assertThat(ann.size()).isEqualTo(3);
        ann.forEach(item ->{
            if (item.getCommandName().equals(l.getCommandName())){
                assertThat(item.getCommandArgument()).isEqualTo(l.getCommandArgument());
            }
        });
    }

    @Test
    public void testRemoveInvalid() throws InstantiationException, IllegalAccessException, LogstashException {
        Node ann = NodeEnumImpl.ANONYMIZE.getCommandInstance();
        assertThat(ann).isNotNull();
        Leaf l = CommonsLeaf.ID.getCommandInstance();
        l.setCommandArgument(new LString("test"));
        assertThat(ann.remove(l)).isFalse();
        assertThat(ann.size()).isEqualTo(3);
    }

    @Test
    public void testRemoveValid() throws InstantiationException, IllegalAccessException, LogstashException {
        Node ann = NodeEnumImpl.ANONYMIZE.getCommandInstance();
        assertThat(ann).isNotNull();
        Leaf l = FingerprintLeaf.METHOD.getCommandInstance();
        assertThat(ann.remove(l)).isTrue();
        assertThat(ann.size()).isEqualTo(3);
    }

    @Test
    public void testRemoveValidAndInitialised() throws InstantiationException, IllegalAccessException, LogstashException {
        Node ann = NodeEnumImpl.ANONYMIZE.getCommandInstance();
        assertThat(ann).isNotNull();
        Leaf l = FingerprintLeaf.METHOD.getCommandInstance();
        l.setCommandArgument(new LString("test"));
        assertThat(ann.add(l)).isTrue();
        ann.forEach(item ->{
            if (item.getCommandName().equals(l.getCommandName())){
                assertThat(item.getCommandArgument()).isEqualTo(l.getCommandArgument());
            }
        });
        assertThat(ann.remove(l)).isTrue();
        assertThat(ann.size()).isEqualTo(3);
        ann.forEach(item ->{
            if (item.getCommandName().equals(l.getCommandName())){
                assertThat(item.getCommandArgument()).isNotEqualTo(l.getCommandArgument());
            }
        });
    }

    @Test
    public void testAddValidKey() throws InstantiationException, IllegalAccessException, LogstashException {
        Node ann = NodeEnumImpl.ANONYMIZE.getCommandInstance();
        assertThat(ann).isNotNull();
        Leaf l = FingerprintLeaf.KEY.getCommandInstance();
        l.setCommandArgument(new LString("test"));
        assertThat(ann.add(l)).isTrue();
        assertThat(ann.size()).isEqualTo(3);
        ann.forEach(item ->{
            if (item.getCommandName().equals(l.getCommandName())){
                assertThat(item.getCommandArgument()).isEqualTo(l.getCommandArgument());
            }
        });
    }

    @Test
    public void testAddValidSource() throws InstantiationException, IllegalAccessException, LogstashException {
        Node ann = NodeEnumImpl.ANONYMIZE.getCommandInstance();
        assertThat(ann).isNotNull();
        Leaf l = FingerprintLeaf.SOURCE.getCommandInstance();
        l.setCommandArgument(new LString("test"));
        assertThat(ann.add(l)).isTrue();
        assertThat(ann.size()).isEqualTo(3);
        ann.forEach(item ->{
            if (item.getCommandName().equals(l.getCommandName())){
                assertThat(item.getCommandArgument()).isEqualTo(l.getCommandArgument());
            }
        });
    }

    @Test
    public void testSpecificBehaviour() throws LogstashException, InstantiationException, IllegalAccessException {
        Node ann = NodeEnumImpl.ANONYMIZE.getCommandInstance();
        assertThat(ann).isNotNull();
        Leaf l = FingerprintLeaf.METHOD.getCommandInstance();
        assertThatThrownBy( () -> ann.set(0, l)).isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy( () -> ann.remove(0)).isInstanceOf(UnsupportedOperationException.class);
        assertThat(ann.removeAll(Collections.singleton(l))).isFalse();
        assertThat(ann.size()).isEqualTo(3);
        ann.add(0, l);
        assertThat(ann.size()).isEqualTo(3);
        assertThat(ann.addAll(Collections.singleton(l))).isFalse();
        assertThat(ann.size()).isEqualTo(3);
        assertThat(ann.addAll(0, Collections.singleton(l))).isFalse();
        assertThat(ann.size()).isEqualTo(3);
    }
}
