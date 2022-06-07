package cz.muni.ics.logstash.commands.leafs.categories;

import cz.muni.ics.logstash.commands.leafs.GenericLeafCommand;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.Leaf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Elasticsearch Options
 * <p>
 * https://www.elastic.co/guide/en/logstash/current/plugins-outputs-elasticsearch.html
 */
public abstract class ElasticsearchLeaf extends GenericLeafCommand {

    public static final ElasticsearchLeaf HOSTS = new ElasticsearchLeaf("hosts#elasticsearch", LType.LARRAY, false) {
    };

    private static final ElasticsearchLeaf[] _ALL_VALUES = {HOSTS};
    private static final List<ElasticsearchLeaf> ALL_VALUES = Collections.unmodifiableList(Arrays.asList(_ALL_VALUES));

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    private ElasticsearchLeaf(String commandName, LType inputType, boolean required) {
        super(commandName, inputType, Arrays.asList(NodeEnumImpl.ELASTICSEARCH), required);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Static

    public static List<ElasticsearchLeaf> values() {
        return ALL_VALUES;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public Leaf getCommandInstance() throws IllegalAccessException, InstantiationException {
        return super.getCommandInstance(this);
    }
}
