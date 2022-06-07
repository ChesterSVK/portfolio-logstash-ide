package cz.muni.ics.logstash.commands.leafs.categories;

import cz.muni.ics.logstash.commands.leafs.GenericLeafCommand;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.Leaf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Ruby Options
 * <p>
 * https://www.elastic.co/guide/en/logstash/current/plugins-filters-ruby.html
 */
public abstract class RubyLeaf extends GenericLeafCommand {

    public static final RubyLeaf CODE = new RubyLeaf("code#ruby", LType.LSTRING, false) {
    };
    public static final RubyLeaf INIT = new RubyLeaf("init#ruby", LType.LSTRING, false) {
    };
    public static final RubyLeaf PATH = new RubyLeaf("path#ruby", LType.LSTRING, false) {
    };
    public static final RubyLeaf SCRIPT_PARAMS = new RubyLeaf("script_params#ruby", LType.LHASH_MAP, false) {
    };
    public static final RubyLeaf TAG_ON_EXCEPTION = new RubyLeaf("tag_on_exception#ruby", LType.LSTRING, false) {
    };

    private static final RubyLeaf[] _ALL_VALUES = {CODE, INIT, PATH, SCRIPT_PARAMS, TAG_ON_EXCEPTION};
    private static final List<RubyLeaf> ALL_VALUES = Collections.unmodifiableList(Arrays.asList(_ALL_VALUES));

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    private RubyLeaf(String commandName, LType inputType, boolean required) {
        super(commandName, inputType, Arrays.asList(NodeEnumImpl.RUBY), required);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Static

    public static List<RubyLeaf> values() {
        return ALL_VALUES;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public Leaf getCommandInstance() throws IllegalAccessException, InstantiationException {
        return super.getCommandInstance(this);
    }
}
