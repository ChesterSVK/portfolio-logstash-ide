package cz.muni.ics.logstash.commands.nodes;

import cz.muni.ics.logstash.commands.leafs.categories.CommonsLeaf;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Node;
import cz.muni.ics.logstash.interfaces.NodeEnum;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class NodeCommandInstanceTest {

    private List<NodeEnum> testNodes;

    public NodeCommandInstanceTest() {
        testNodes = new ArrayList<>();
        for (NodeEnum nodeEnum : NodeEnumImpl.values()) {
            List<NodeEnum> excludedNodes = Arrays.asList(NodeEnumImpl.ANONYMIZE, NodeEnumImpl.TIMESTAMP, NodeEnumImpl.DNS);
            if (!excludedNodes.contains(nodeEnum)) {
                testNodes.add(nodeEnum);
            }
        }
    }


    @Test
    public void testCreateAny() throws LogstashException {
        for (NodeEnum nEnum : testNodes) {
            Node n = nEnum.getCommandInstance();
            assertThat(n).isNotNull();
            assertThat(n).isInstanceOf(Node.class);
            assertThat(n.size()).isEqualTo(0);
            assertThat(n.getCommandName()).isNotEmpty();
            assertThat(n.toJsonString()).isNotEmpty();
            assertThat(n.toLogstashString(0)).isNotEmpty();
        }
    }

    @Test
    public void testEqualsOnEqualItems() throws LogstashException {
        for (NodeEnum nEnum1 : testNodes) {
            for (NodeEnum nEnum2 : testNodes) {
                if (nEnum1.getCommandName().equals(nEnum2.getCommandName()))
                    assertThat(nEnum1.getCommandInstance().equals(nEnum2.getCommandInstance())).isTrue();
                else
                    assertThat(nEnum1.getCommandInstance().equals(nEnum2.getCommandInstance())).isFalse();

            }
        }
    }

    @Test
    public void testEqualsOnSimilarItems() throws LogstashException, InstantiationException, IllegalAccessException {
        for (NodeEnum nEnum1 : testNodes) {
            for (NodeEnum nEnum2 : testNodes) {
                if (nEnum1.getCommandName().equals(nEnum2.getCommandName())) {
                    Node n1 = nEnum1.getCommandInstance();
                    Node n2 = nEnum2.getCommandInstance();
                    n2.add(CommonsLeaf.ID.getCommandInstance());
                    assertThat(n2.size()).isEqualTo(1);
                    assertThat(n1.equals(n2)).isTrue();
                }
            }
        }
    }
}
