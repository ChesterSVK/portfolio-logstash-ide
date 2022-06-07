package cz.muni.ics.logstash.commands.nodes.plugins;

import cz.muni.ics.logstash.commands.nodes.NodeCommandInstance;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.serialization.nodes.NodeCommandGsonHelper;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
public class DnsResolvingCommand extends NodeCommandInstance implements Node {

    public DnsResolvingCommand() {
        super();
        super.setCommandEnum(NodeEnumImpl.DNS);
    }

    @Override
    public String toJsonString() {
        return NodeCommandGsonHelper.serialize(this);
    }

    @Override
    public String toLogstashString(int level) {
        return StringUtils.repeat('\t', level) + "if [id.orig_h] {\n" +
                StringUtils.repeat('\t', level + 1) + "if ![id.orig_h-resolved] {\n" +
                StringUtils.repeat('\t', level + 2) + "mutate {\n" +
                StringUtils.repeat('\t', level + 3) + "add_field => [ \"id.orig_h-resolved\", \"%{id.orig_h}\" ]\n" +
                StringUtils.repeat('\t', level + 2) + "}\n" +
                StringUtils.repeat('\t', level + 2) + "dns {\n" +
                StringUtils.repeat('\t', level + 3) + "reverse => [ \"id.orig_h-resolved\" ]\n" +
                StringUtils.repeat('\t', level + 3) + "action => \"replace\"\n" +
                StringUtils.repeat('\t', level + 2) + "}\n" +
                StringUtils.repeat('\t', level + 1) + "}\n" +
                StringUtils.repeat('\t', level) + "}\n" +
                "\n" +
                StringUtils.repeat('\t', level) + "if [id.resp_h] {\n" +
                StringUtils.repeat('\t', level + 1) + "if ![id.resp_h-resolved] {\n" +
                StringUtils.repeat('\t', level + 2) + "mutate {\n" +
                StringUtils.repeat('\t', level + 3) + "add_field => [ \"id.resp_h-resolved\", \"%{id.resp_h}\" ]\n" +
                StringUtils.repeat('\t', level + 2) + "}\n" +
                StringUtils.repeat('\t', level + 2) + "dns {\n" +
                StringUtils.repeat('\t', level + 3) + "reverse => [ \"id.resp_h-resolved\" ]\n" +
                StringUtils.repeat('\t', level + 3) + "action => \"replace\"\n" +
                StringUtils.repeat('\t', level + 2) + "}\n" +
                StringUtils.repeat('\t', level + 1) + "}\n" +
                StringUtils.repeat('\t', level) + "}\n";
    }

    @Override
    public Leaf set(int index, Leaf element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Leaf leaf) {
        return false;
    }

    @Override
    public void add(int index, Leaf element) {
    }

    @Override
    public Leaf remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Leaf> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Leaf> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }
}
