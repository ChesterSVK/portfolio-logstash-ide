package cz.muni.ics.logstash.serialization.nodes;

import cz.muni.ics.logstash.commands.leafs.GenericLeafCommand;
import cz.muni.ics.logstash.commands.leafs.categories.CommonsLeaf;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.NodeEnum;
import cz.muni.ics.logstash.serialization.leaves.LeafCommandGsonHelper;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class NodeGsonHelperTest {

    private Node testNode = NodeEnumImpl.JSON.getCommandInstance();

    public NodeGsonHelperTest() throws LogstashException {
    }

    @Before
    public void before() throws InstantiationException, IllegalAccessException {
        testNode.clear();
        testNode.add(CommonsLeaf.ID.getCommandInstance());
    }
    @Test
    public void testSerialize(){
        assertThat(NodeCommandGsonHelper.serialize(testNode)).isNotEmpty();
        assertThat(NodeCommandGsonHelper.serialize(testNode)).isEqualTo("{\n" +
                "  \"commandEnum\": \"json\",\n" +
                "  \"commandArgument\": [\n" +
                "    {\n" +
                "      \"commandEnum\": \"id#common\",\n" +
                "      \"commandInputType\": \"LSTRING\",\n" +
                "      \"commandArgument\": \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}");
    }

    @Test
    public void testDeserialize(){
        assertThat(NodeCommandGsonHelper.deserialize("{\n" +
                "  \"commandEnum\": \"json\",\n" +
                "  \"commandArgument\": [\n" +
                "    {\n" +
                "      \"commandEnum\": \"id#common\",\n" +
                "      \"commandInputType\": \"LSTRING\",\n" +
                "      \"commandArgument\": \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}")).isNotNull();
        assertThat(NodeCommandGsonHelper.deserialize("{\n" +
                "  \"commandEnum\": \"json\",\n" +
                "  \"commandArgument\": [\n" +
                "    {\n" +
                "      \"commandEnum\": \"id#common\",\n" +
                "      \"commandInputType\": \"LSTRING\",\n" +
                "      \"commandArgument\": \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}")).isEqualTo(testNode);
    }

    @Test
    public void testEquality(){
        assertThat(NodeCommandGsonHelper.deserialize(NodeCommandGsonHelper.serialize(testNode))).isEqualTo(testNode);
        assertThat(NodeCommandGsonHelper.serialize(NodeCommandGsonHelper.deserialize("{\n" +
                "  \"commandEnum\": \"json\",\n" +
                "  \"commandArgument\": [\n" +
                "    {\n" +
                "      \"commandEnum\": \"id#common\",\n" +
                "      \"commandInputType\": \"LSTRING\",\n" +
                "      \"commandArgument\": \"\"\n" +
                "    }\n" +
                "  ]\n" +
                "}")));
    }

    @Test
    public void testAllNodes(){
        for (NodeEnum nodeEnum : NodeEnumImpl.values()) {
            try {
                Node n = nodeEnum.getCommandInstance();
                assertThat(NodeCommandGsonHelper.deserialize(NodeCommandGsonHelper.serialize(n))).isEqualTo(n);
            } catch (LogstashException e) {
                System.err.println(e.toString());
            }
        }
    }
}
