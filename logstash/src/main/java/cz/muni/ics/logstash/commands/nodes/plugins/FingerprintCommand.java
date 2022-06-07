package cz.muni.ics.logstash.commands.nodes.plugins;

import cz.muni.ics.logstash.commands.leafs.categories.FingerprintLeaf;
import cz.muni.ics.logstash.commands.nodes.NodeCommandInstance;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.utils.LogstashCommandPrettyPrinter;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
public class FingerprintCommand extends NodeCommandInstance implements Node {

    public FingerprintCommand() throws InstantiationException, IllegalAccessException {
        super();
        super.setCommandEnum(NodeEnumImpl.FINGERPRINT);
        Leaf key = FingerprintLeaf.KEY.getCommandInstance();
        Leaf source = FingerprintLeaf.SOURCE.getCommandInstance();
        Leaf method = FingerprintLeaf.METHOD.getCommandInstance();
        super.add(key);
        super.add(source);
        super.add(method);
    }

    @Override
    public String toLogstashString(int level) {
        return LogstashCommandPrettyPrinter.prettyPrintNode(level, NodeEnumImpl.FINGERPRINT.getCommandName(), this);
    }

    @Override
    public Leaf set(int index, Leaf element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Leaf leaf) {
        for (Leaf l : this){
            if (l.getCommandName().equals(leaf.getCommandName())
                    && leaf.getCommandArgument().isInitialised()) {
                l.setCommandArgument(leaf.getCommandArgument());
                return true;
            }
        }
        return false;
    }

    @Override
    public void add(int index, Leaf element) {
        this.add(element);
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
        for (Leaf l : this){
            if (l.getCommandName().equals(leaf.getCommandName())) {
                try {
                    l.setCommandArgument(l.getCommandArgumentInputType().getTypeInstance());
                    return true;
                } catch (IllegalAccessException | InstantiationException ignore) {}
            }
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
