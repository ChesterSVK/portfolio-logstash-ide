package cz.muni.ics.logstash.commands.leafs.categories;

import cz.muni.ics.logstash.commands.leafs.GenericLeafCommand;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.Leaf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Commons Options
 */
public abstract class CommonsLeaf extends GenericLeafCommand {

    public static final CommonsLeaf ADD_FIELD = new CommonsLeaf("add_field#common", LType.LHASH_MAP, false) {
    };
    public static final CommonsLeaf ADD_TAG = new CommonsLeaf("add_tag#common", LType.LARRAY, false) {
    };
    public static final CommonsLeaf ENABLE_METRIC = new CommonsLeaf("enable_metric#common", LType.LBOOL, false) {
    };
    public static final CommonsLeaf ID = new CommonsLeaf("id#common", LType.LSTRING, false) {
    };
    public static final CommonsLeaf PERIODIC_FLUSH = new CommonsLeaf("periodic_flush#common", LType.LBOOL, false) {
    };
    public static final CommonsLeaf REMOVE_FIELD = new CommonsLeaf("remove_field#common", LType.LARRAY, false) {
    };
    public static final CommonsLeaf REMOVE_TAG = new CommonsLeaf("remove_tag#common", LType.LARRAY, false) {
    };

    private static final CommonsLeaf[] _ALL_VALUES = {ADD_FIELD, ADD_TAG, ENABLE_METRIC, ID, PERIODIC_FLUSH, REMOVE_FIELD, REMOVE_TAG};
    private static final List<CommonsLeaf> ALL_VALUES = Collections.unmodifiableList(Arrays.asList(_ALL_VALUES));

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    private CommonsLeaf(String logstashLeaf, LType inputType, boolean required) {
        super(logstashLeaf, inputType, Arrays.asList(NodeEnumImpl.values()), required);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Static

    public static List<CommonsLeaf> values() {
        return ALL_VALUES;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public Leaf getCommandInstance() throws IllegalAccessException, InstantiationException {
        return super.getCommandInstance(this);
    }
}
