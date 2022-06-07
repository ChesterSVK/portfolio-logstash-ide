package cz.muni.ics.logstash.commands.roots;

import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Root;
import cz.muni.ics.logstash.interfaces.RootEnum;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class RootCommandInstanceTest {

    @Test
    public void testCreateAny(){
        for (RootEnum rEnum : RootEnumImpl.values()){
            Root r = rEnum.getCommandInstance();
            assertThat(r).isNotNull();
            assertThat(r).isInstanceOf(Root.class);
            assertThat(r.getCommandArgument()).isEmpty();
            assertThat(r.toJsonString()).isNotEmpty();
            System.out.println(r.toLogstashString(0));
            assertThat(r.toLogstashString(0)).isEmpty();
        }
    }

    @Test
    public void testEqualsOnEqualItems(){
        for (RootEnum rEnum1 : RootEnumImpl.values()){
            for (RootEnum rEnum2 : RootEnumImpl.values()){
                if (rEnum1.getCommandName().equals(rEnum2.getCommandName()))
                    assertThat(rEnum1.getCommandInstance().equals(rEnum2.getCommandInstance())).isTrue();
                else
                    assertThat(rEnum1.getCommandInstance().equals(rEnum2.getCommandInstance())).isFalse();

            }
        }
    }

    @Test
    public void testEqualsOnSimilarItems() throws LogstashException {
        for (RootEnum rEnum1 : RootEnumImpl.values()){
            for (RootEnum rEnum2 : RootEnumImpl.values()){
                if (rEnum1.getCommandName().equals(rEnum2.getCommandName())){
                    Root r1 = rEnum1.getCommandInstance();
                    Root r2 = rEnum2.getCommandInstance();
                    r2.add(NodeEnumImpl.JSON.getCommandInstance());
                    assertThat(r2.size()).isEqualTo(1);
                    assertThat(r1.equals(r2)).isTrue();
                }
            }
        }
    }
}
