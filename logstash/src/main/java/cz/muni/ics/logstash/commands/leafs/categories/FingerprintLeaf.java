package cz.muni.ics.logstash.commands.leafs.categories;

import cz.muni.ics.logstash.commands.leafs.GenericLeafCommand;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.Leaf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Fingerprint Options
 * <p>
 * https://www.elastic.co/guide/en/logstash/current/plugins-filters-fingerprint.html
 */
public abstract class FingerprintLeaf extends GenericLeafCommand {

    public static final FingerprintLeaf SOURCE = new FingerprintLeaf("source#fingerprint", LType.LARRAY, false) {
    };
    public static final FingerprintLeaf TARGET = new FingerprintLeaf("target#fingerprint", LType.LSTRING, false) {
    };
    public static final FingerprintLeaf METHOD = new FingerprintLeaf("method#fingerprint", LType.LANONYMIZE_ALGO, true) {
    };
    public static final FingerprintLeaf KEY = new FingerprintLeaf("key#fingerprint", LType.LSTRING, false) {
    };

    private static final FingerprintLeaf[] _ALL_VALUES = {KEY, METHOD, SOURCE, TARGET};
    private static final List<FingerprintLeaf> ALL_VALUES = Collections.unmodifiableList(Arrays.asList(_ALL_VALUES));

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    private FingerprintLeaf(String commandName, LType inputType, boolean required) {
        super(commandName, inputType, Arrays.asList(NodeEnumImpl.FINGERPRINT, NodeEnumImpl.ANONYMIZE), required);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Static

    public static List<FingerprintLeaf> values() {
        return ALL_VALUES;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public Leaf getCommandInstance() throws IllegalAccessException, InstantiationException {
        return super.getCommandInstance(this);
    }
}
