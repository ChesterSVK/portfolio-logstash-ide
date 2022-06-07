package cz.muni.ics.logstash.utils;

import cz.muni.ics.logstash.commands.leafs.categories.CommonsLeaf;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.commands.roots.RootEnumImpl;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.Root;
import cz.muni.ics.logstash.types.LString;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class LogstashCommandPrettyPrinterTest {

    @Test
    public void testNode() throws LogstashException, InstantiationException, IllegalAccessException {
        Node n = NodeEnumImpl.JSON.getCommandInstance();
        assertThat(LogstashCommandPrettyPrinter.prettyPrintNode(0, n.getCommandName(), n)).isNotEmpty();
        assertThat(LogstashCommandPrettyPrinter.prettyPrintNode(0, n.getCommandName(), n).contains(NodeEnumImpl.JSON.getCommandName())).isTrue();
        n.add(CommonsLeaf.ID.getCommandInstance());
        assertThat(LogstashCommandPrettyPrinter.prettyPrintNode(0, n.getCommandName(), n)).isNotEmpty();
        assertThat(LogstashCommandPrettyPrinter.prettyPrintNode(0, n.getCommandName(), n).contains(NodeEnumImpl.JSON.getCommandName())).isTrue();
        assertThat(LogstashCommandPrettyPrinter.prettyPrintNode(0, n.getCommandName(), n).contains("id")).isTrue();

    }

    @Test
    public void testRoot() throws LogstashException {
        Root r = RootEnumImpl.FILTER.getCommandInstance();
        assertThat(LogstashCommandPrettyPrinter.prettyPrintRoot(0, r.getCommandName(), r)).isEmpty();
        r.add(NodeEnumImpl.JSON.getCommandInstance());
        assertThat(LogstashCommandPrettyPrinter.prettyPrintRoot(0, r.getCommandName(), r)).isNotEmpty();
        assertThat(LogstashCommandPrettyPrinter.prettyPrintRoot(0, r.getCommandName(), r).contains(RootEnumImpl.FILTER.getCommandName())).isTrue();
        assertThat(LogstashCommandPrettyPrinter.prettyPrintRoot(0, r.getCommandName(), r).contains(NodeEnumImpl.JSON.getCommandName())).isTrue();
    }

    @Test
    public void testLeaf() throws InstantiationException, IllegalAccessException {
        Leaf leaf = CommonsLeaf.ID.getCommandInstance();
        assertThat(LogstashCommandPrettyPrinter.prettyPrintLeaf(0, leaf.getCommandName(), leaf.getCommandArgument())).isNotEmpty();
        assertThat(LogstashCommandPrettyPrinter.prettyPrintLeaf(0, leaf.getCommandName(), leaf.getCommandArgument()).contains(CommonsLeaf.ID.getCommandName())).isTrue();
        leaf.setCommandArgument(new LString("test"));
        assertThat(LogstashCommandPrettyPrinter.prettyPrintLeaf(0, leaf.getCommandName(), leaf.getCommandArgument())).isNotEmpty();
        assertThat(LogstashCommandPrettyPrinter.prettyPrintLeaf(0, leaf.getCommandName(), leaf.getCommandArgument()).contains(CommonsLeaf.ID.getCommandName())).isTrue();
        assertThat(LogstashCommandPrettyPrinter.prettyPrintLeaf(0, leaf.getCommandName(), leaf.getCommandArgument()).contains("test")).isTrue();
    }

}
