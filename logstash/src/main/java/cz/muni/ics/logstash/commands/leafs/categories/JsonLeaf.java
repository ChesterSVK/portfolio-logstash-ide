package cz.muni.ics.logstash.commands.leafs.categories;

import cz.muni.ics.logstash.commands.leafs.GenericLeafCommand;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.Leaf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Json Options
 * <p>
 * https://www.elastic.co/guide/en/logstash/current/plugins-filters-json.html
 */
public abstract class JsonLeaf extends GenericLeafCommand {

    public static final JsonLeaf SOURCE = new JsonLeaf("source#json", LType.LSTRING, true) {
    };
    public static final JsonLeaf SKIP_ON_INVALID_JSON = new JsonLeaf("skip_on_invalid_json#json", LType.LBOOL, false) {
    };
    public static final JsonLeaf TAG_ON_FAILURE = new JsonLeaf("tag_on_failure#json", LType.LARRAY, false) {
    };
    public static final JsonLeaf TARGET = new JsonLeaf("target#json", LType.LSTRING, false) {
    };

    private static final JsonLeaf[] _ALL_VALUES = {SOURCE, SKIP_ON_INVALID_JSON, TAG_ON_FAILURE, TARGET};
    private static final List<JsonLeaf> ALL_VALUES = Collections.unmodifiableList(Arrays.asList(_ALL_VALUES));

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    private JsonLeaf(String commandName, LType inputType, boolean required) {
        super(commandName, inputType, Arrays.asList(NodeEnumImpl.JSON), required);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Static

    public static List<JsonLeaf> values() {
        return ALL_VALUES;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public Leaf getCommandInstance() throws IllegalAccessException, InstantiationException {
        return super.getCommandInstance(this);
    }
}
