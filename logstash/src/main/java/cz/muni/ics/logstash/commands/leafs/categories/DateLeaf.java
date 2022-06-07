package cz.muni.ics.logstash.commands.leafs.categories;

import cz.muni.ics.logstash.commands.leafs.GenericLeafCommand;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.Leaf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Date Options
 * <p>
 * https://www.elastic.co/guide/en/logstash/current/plugins-filters-date.html
 */
public abstract class DateLeaf extends GenericLeafCommand {

    public static final DateLeaf LOCALE = new DateLeaf("source#date", LType.LSTRING, false) {
    };
    public static final DateLeaf MATCH = new DateLeaf("match#date", LType.LTIMESTAMP_ARRAY, false) {
    };
    public static final DateLeaf TAG_ON_FAILURE = new DateLeaf("tag_on_failure#date", LType.LARRAY, false) {
    };
    public static final DateLeaf TARGET = new DateLeaf("target#date", LType.LSTRING, false) {
    };
    public static final DateLeaf TIMEZONE = new DateLeaf("timezone#date", LType.LSTRING, false) {
    };

    private static final DateLeaf[] _ALL_VALUES = {LOCALE, MATCH, TAG_ON_FAILURE, TARGET, TIMEZONE};
    private static final List<DateLeaf> ALL_VALUES = Collections.unmodifiableList(Arrays.asList(_ALL_VALUES));

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    private DateLeaf(String commandName, LType inputType, boolean required) {
        super(commandName, inputType, Arrays.asList(NodeEnumImpl.DATE, NodeEnumImpl.TIMESTAMP), required);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Static

    public static List<DateLeaf> values() {
        return ALL_VALUES;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public Leaf getCommandInstance() throws IllegalAccessException, InstantiationException {
        return super.getCommandInstance(this);
    }

}
