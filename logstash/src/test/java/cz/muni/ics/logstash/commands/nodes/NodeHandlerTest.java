package cz.muni.ics.logstash.commands.nodes;

import cz.muni.ics.logstash.commands.leafs.LeafCommandInstance;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.NodeEnum;
import cz.muni.ics.logstash.types.LString;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.NotEmpty;
import java.util.*;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class NodeHandlerTest {

    private NodeHandler nodeHandler = new NodeHandlerImpl();
    private TestLeaf testLeaf;
    private List<NodeEnum> testNodes;

    public NodeHandlerTest() {
        testNodes = new ArrayList<>();
        for (NodeEnum nodeEnum : NodeEnumImpl.values()) {
            List<NodeEnum> excludedNodes = Arrays.asList(NodeEnumImpl.ANONYMIZE, NodeEnumImpl.TIMESTAMP, NodeEnumImpl.DNS);
            if (!excludedNodes.contains(nodeEnum)) {
                testNodes.add(nodeEnum);
            }
        }
    }

    @Before
    public void before(){
        testLeaf = new TestLeaf("test", new LString("testS"), false, testNodes);
        testLeaf.setAllowedParents(Arrays.asList(NodeEnumImpl.values()));
    }

    @Test
    public void testGetInstances() throws LogstashException {
        for (NodeEnum nodeEnum : testNodes) {
            Node n = nodeHandler.getNodeInstance((Class<Node>) nodeEnum.getCommandInstance().getClass(), nodeEnum, Collections.emptyList());
            assertThat(n).isNotNull();
        }
    }

    @Test
    public void testGetInstancesParents() throws LogstashException {
        for (NodeEnum nodeEnum : testNodes) {
            Node n = nodeHandler.getNodeInstance((Class<Node>) nodeEnum.getCommandInstance().getClass(), nodeEnum, Collections.emptyList());
            assertThat(nodeHandler.getAllowedParentRoots(n)).isNotEmpty();
        }
    }


    @Test
    public void testAddNotInitialisedItem() throws LogstashException {
        for (NodeEnum nodeEnum : testNodes) {
            Node n = nodeHandler.getNodeInstance((Class<Node>) nodeEnum.getCommandInstance().getClass(), nodeEnum, Collections.emptyList());
            testLeaf.setCommandArgument(new LString());
            nodeHandler.addOption(n, testLeaf);
            assertThat(n.size()).isEqualTo(0);
        }
    }

    @Test
    public void testAddInitialisedItem() throws LogstashException {
        for (NodeEnum nodeEnum : testNodes) {
            Node n = nodeHandler.getNodeInstance((Class<Node>) nodeEnum.getCommandInstance().getClass(), nodeEnum, Collections.emptyList());
            nodeHandler.addOption(n, testLeaf);
            assertThat(n.size()).isEqualTo(1);
        }
    }

    @Test
    public void testAddInvalidItem() throws LogstashException {
        for (NodeEnum nodeEnum : testNodes) {
            Node n = nodeHandler.getNodeInstance((Class<Node>) nodeEnum.getCommandInstance().getClass(), nodeEnum, Collections.emptyList());
            testLeaf.setAllowedParents(Collections.EMPTY_LIST);
            assertThatThrownBy( () -> nodeHandler.addOption(n, testLeaf)).isInstanceOf(IllegalArgumentException.class);
            assertThat(n.size()).isEqualTo(0);
        }
    }

    @Test
    public void testAddItems() throws LogstashException {
        for (NodeEnum nodeEnum : testNodes) {
            Node n = nodeHandler.getNodeInstance((Class<Node>) nodeEnum.getCommandInstance().getClass(), nodeEnum, Collections.emptyList());
            nodeHandler.addOptions(n, Collections.singleton(testLeaf));
            assertThat(n.size()).isEqualTo(1);
        }
    }

    @Test
    public void testAddInvalidItems() throws LogstashException {
        for (NodeEnum nodeEnum : testNodes) {
            Node n = nodeHandler.getNodeInstance((Class<Node>) nodeEnum.getCommandInstance().getClass(), nodeEnum, Collections.emptyList());
            testLeaf.setAllowedParents(Collections.EMPTY_LIST);
            assertThatThrownBy( () -> nodeHandler.addOptions(n, Collections.singleton(testLeaf))).isInstanceOf(IllegalArgumentException.class);
            assertThat(n.size()).isEqualTo(0);
        }
    }

    @Test
    public void testRemoveItem() throws LogstashException {
        for (NodeEnum nodeEnum : testNodes) {
            Node n = nodeHandler.getNodeInstance((Class<Node>) nodeEnum.getCommandInstance().getClass(), nodeEnum, Collections.emptyList());
            nodeHandler.addOptions(n, Collections.singleton(testLeaf));
            assertThat(n.size()).isEqualTo(1);
            nodeHandler.removeLeaf(n, testLeaf);
            assertThat(n.size()).isEqualTo(0);
        }
    }

    @Test
    public void testRemoveNonExistingItem() throws LogstashException {
        for (NodeEnum nodeEnum : testNodes) {
            Node n = nodeHandler.getNodeInstance((Class<Node>) nodeEnum.getCommandInstance().getClass(), nodeEnum, Collections.emptyList());
            nodeHandler.addOptions(n, Collections.singleton(testLeaf));
            assertThat(n.size()).isEqualTo(1);
            Leaf testLeaf2 = new TestLeaf("test2", new LString(), false, testNodes);
            nodeHandler.removeLeaf(n, testLeaf2);
            assertThat(n.size()).isEqualTo(1);
        }
    }

    @Test
    public void testRemoveSimilarItem() throws LogstashException {
        for (NodeEnum nodeEnum : testNodes) {
            Node n = nodeHandler.getNodeInstance((Class<Node>) nodeEnum.getCommandInstance().getClass(), nodeEnum, Collections.emptyList());
            nodeHandler.addOption(n, testLeaf);
            assertThat(n.size()).isEqualTo(1);
            nodeHandler.removeLeaf(n, new TestLeaf("test", new LString(), false, testNodes));
            assertThat(n.size()).isEqualTo(0);
        }
    }

    @Test
    public void testRemoveItems() throws LogstashException {
        for (NodeEnum nodeEnum : testNodes) {
            Node n = nodeHandler.getNodeInstance((Class<Node>) nodeEnum.getCommandInstance().getClass(), nodeEnum, Collections.emptyList());
            nodeHandler.addOptions(n, Collections.singleton(testLeaf));
            assertThat(n.size()).isEqualTo(1);
            nodeHandler.removeAllLeaves(n);
            assertThat(n.size()).isEqualTo(0);
        }
    }


    private class TestLeaf extends LeafCommandInstance implements Leaf{

        TestLeaf(@NotEmpty String commandName, InputType commandArgument, boolean required, Collection<NodeEnum> allowedParentNodes) {
            super(commandName, commandArgument, required, allowedParentNodes);
        }

        @Override
        public Collection<NodeEnum> getAllowedParentNodes() {
            return super.getAllowedParentNodes();
        }

        @Override
        public InputType getCommandArgument() {
            return super.getCommandArgument();
        }

        @Override
        public void setCommandArgument(InputType argument) {super.setCommandArgument(argument);}

        @Override
        public LType getCommandArgumentInputType() {
            return super.getCommandArgumentInputType();
        }

        @Override
        public String getCommandName() {
            return super.getCommandName();
        }

        @Override
        public String toLogstashString(int level) {
            return "logstashString";
        }

        @Override
        public String toJsonString() {
            return "jsonString";
        }

        void setAllowedParents(Collection<NodeEnum> parents){
            super.setAllowedParentNodes(parents);
        }
    }
}
