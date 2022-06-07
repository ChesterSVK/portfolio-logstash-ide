package cz.muni.ics.logstash.commands.nodes.plugins;

import cz.muni.ics.logstash.commands.leafs.categories.CommonsLeaf;
import cz.muni.ics.logstash.commands.leafs.categories.DateLeaf;
import cz.muni.ics.logstash.commands.nodes.NodeCommandInstance;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.types.LArray;
import cz.muni.ics.logstash.utils.LogstashCommandPrettyPrinter;
import lombok.EqualsAndHashCode;

import java.util.Collection;


@EqualsAndHashCode(callSuper = false)
public class TimestampCommand extends NodeCommandInstance implements Node {

    private Leaf match = DateLeaf.MATCH.getCommandInstance();

    public TimestampCommand() throws IllegalAccessException, InstantiationException {
        super();
        super.setCommandEnum(NodeEnumImpl.TIMESTAMP);
        super.add(match);
    }

    @Override
    public String toLogstashString(int level) {
        try {
            Leaf removeLeaf = prepareRemoveField();
            super.add(removeLeaf);
            String result = LogstashCommandPrettyPrinter.prettyPrintNode(level, NodeEnumImpl.DATE.getCommandName(), this);
            super.remove(removeLeaf);
            return result;
        } catch (InstantiationException | IllegalAccessException ignored) {return "";}
    }

    private Leaf prepareRemoveField() throws InstantiationException, IllegalAccessException {
        Leaf removeField = CommonsLeaf.REMOVE_FIELD.getCommandInstance();
        LArray array = new LArray();
        if (match.getCommandArgumentInputType().equals(LType.LTIMESTAMP_ARRAY)) {
            if (((LArray) match.getCommandArgument()).size() >= 1) {
                array.add(((LArray) match.getCommandArgument()).get(0));
                removeField.setCommandArgument(array);
            }
        }
        return removeField;
    }


    @Override
    public Leaf set(int index, Leaf element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Leaf leaf) {
        if (leaf.getCommandName().equals(DateLeaf.MATCH.getCommandName())) {
            match.setCommandArgument(leaf.getCommandArgument());
            return true;
        }
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
        if (!(o instanceof Leaf)) {
            return false;
        }
        Leaf leaf = (Leaf) o;
        if (leaf.getCommandName().equals(DateLeaf.MATCH.getCommandName())
                || leaf.getCommandName().equals(CommonsLeaf.REMOVE_FIELD.getCommandName())) {
            super.clear();
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Leaf> c) {
        int s = this.size();
        c.forEach(this::add);
        return this.size() != s;
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
