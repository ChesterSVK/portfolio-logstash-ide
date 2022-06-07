package cz.muni.ics.logstash.commands.leaves;

import cz.muni.ics.logstash.commands.leafs.GenericLeafCommand;
import cz.muni.ics.logstash.interfaces.Leaf;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class LeafCommandInstanceTest {

    @Test
    public void testCreateAny() throws InstantiationException, IllegalAccessException {
        for (GenericLeafCommand command : GenericLeafCommand.values()) {
            Leaf l = command.getCommandInstance();
            assertThat(l).isNotNull();
            assertThat(l.getCommandName()).isNotEmpty();
            assertThat(l.getCommandName().contains("#")).isTrue();
            assertThat(l.getCommandArgument()).isNotNull();
            assertThat(l.getAllowedParentNodes()).isNotNull();
            assertThat(l.getAllowedParentNodes()).isNotEmpty();
            assertThat(l.toJsonString()).isNotEmpty();
            assertThat(l.toLogstashString(0)).isNotEmpty();
        }
    }

    @Test
    public void testEqualsOnEqualItems() throws InstantiationException, IllegalAccessException {
        for (GenericLeafCommand command1 : GenericLeafCommand.values()) {
            for (GenericLeafCommand command2 : GenericLeafCommand.values()) {
                if (command1.getCommandName().equals(command2.getCommandName())
                        && command1.getArgumentInputType().equals(command2.getArgumentInputType()))
                    assertThat(command1.getCommandInstance().equals(command2.getCommandInstance())).isTrue();
                else
                    assertThat(command1.getCommandInstance().equals(command2.getCommandInstance())).isFalse();
            }
        }
    }

    @Test
    public void testEqualsOnSimilarItems() throws InstantiationException, IllegalAccessException {
        for (GenericLeafCommand command1 : GenericLeafCommand.values()) {
            for (GenericLeafCommand command2 : GenericLeafCommand.values()) {
                if (command1.getCommandName().equals(command2.getCommandName())
                        && command1.getArgumentInputType().equals(command2.getArgumentInputType())) {
                    Leaf leaf1 = command1.getCommandInstance();
                    Leaf leaf2 = command2.getCommandInstance();
                    leaf2.setCommandArgument(leaf2.getCommandArgumentInputType().getTypeInstance());
                    assertThat(leaf2.getCommandArgument()).isNotNull();
                    assertThat(leaf1.equals(leaf2)).isTrue();
                }
            }
        }
    }
}
