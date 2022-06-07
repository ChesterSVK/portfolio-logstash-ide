package cz.muni.ics.logstash.commands.nodes.plugins;

import cz.muni.ics.logstash.commands.leafs.categories.CommonsLeaf;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.NodeEnum;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class DnsNodeCommandTest {

    @Test
    public void testCreate() throws LogstashException {
        Node dns = NodeEnumImpl.DNS.getCommandInstance();
        assertThat(dns).isNotNull();
        assertThat(dns.toLogstashString(0)).isNotEmpty();
        assertThat(dns.toJsonString()).isNotEmpty();
        assertThat(dns.size()).isEqualTo(0);
        assertThat(dns.getCommandName()).isNotEmpty();
    }

    @Test
    public void testEquals() throws LogstashException {
        Node dns1 = NodeEnumImpl.DNS.getCommandInstance();
        assertThat(dns1).isNotNull();
        Node dns2 = NodeEnumImpl.DNS.getCommandInstance();
        assertThat(dns2).isNotNull();
        assertThat(dns1.equals(dns2)).isTrue();

    }

    @Test
    public void testSpecificBehaviour() throws LogstashException, InstantiationException, IllegalAccessException {
        Node dns = NodeEnumImpl.DNS.getCommandInstance();
        assertThat(dns).isNotNull();
        Leaf l = CommonsLeaf.ID.getCommandInstance();
        assertThat(dns.add(l)).isFalse();
        assertThat(dns.size()).isEqualTo(0);
        dns.add(0, l);
        assertThat(dns.size()).isEqualTo(0);
        assertThatThrownBy( () -> dns.set(0, l)).isInstanceOf(UnsupportedOperationException.class);
        assertThat(dns.size()).isEqualTo(0);
        assertThat(dns.remove(l)).isFalse();
        assertThat(dns.size()).isEqualTo(0);
        assertThatThrownBy( () -> dns.remove(0)).isInstanceOf(UnsupportedOperationException.class);
        assertThat(dns.size()).isEqualTo(0);
        assertThat(dns.removeAll(Collections.singleton(l))).isFalse();
        assertThat(dns.size()).isEqualTo(0);
        assertThat(dns.addAll(Collections.singleton(l))).isFalse();
        assertThat(dns.size()).isEqualTo(0);
        assertThat(dns.addAll(0, Collections.singleton(l))).isFalse();
        assertThat(dns.size()).isEqualTo(0);
    }
}
