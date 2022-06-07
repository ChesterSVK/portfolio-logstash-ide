package cz.muni.ics.logstash.commands.leafs.categories;

import cz.muni.ics.logstash.commands.leafs.GenericLeafCommand;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.Leaf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Mutate Options
 * <p>
 * https://www.elastic.co/guide/en/logstash/current/plugins-filters-mutate.html
 */
public abstract class MutateLeaf extends GenericLeafCommand {

    public static final MutateLeaf LOWERCASE = new MutateLeaf("lowercase#mutate", LType.LARRAY, false) {
    };
    public static final MutateLeaf UPPERCASE = new MutateLeaf("uppercase#mutate", LType.LARRAY, false) {
    };

    private static final MutateLeaf[] _ALL_VALUES = {LOWERCASE, UPPERCASE};
    private static final List<MutateLeaf> ALL_VALUES = Collections.unmodifiableList(Arrays.asList(_ALL_VALUES));

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    private MutateLeaf(String commandName, LType inputType, boolean required) {
        super(commandName, inputType, Arrays.asList(NodeEnumImpl.MUTATE), required);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Static

    public static List<MutateLeaf> values() {
        return ALL_VALUES;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public Leaf getCommandInstance() throws IllegalAccessException, InstantiationException {
        return super.getCommandInstance(this);
    }
}
