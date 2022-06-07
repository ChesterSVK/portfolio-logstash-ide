package cz.muni.ics.logstash.serialization.leaves;

import cz.muni.ics.logstash.commands.leafs.GenericLeafCommand;
import cz.muni.ics.logstash.commands.leafs.categories.CommonsLeaf;
import cz.muni.ics.logstash.interfaces.Leaf;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)

public class LeafGsonHelperTest {

    private Leaf testLeaf = CommonsLeaf.ID.getCommandInstance();

    public LeafGsonHelperTest() throws InstantiationException, IllegalAccessException {}

    @Test
    public void testSerialize(){
        assertThat(LeafCommandGsonHelper.serialize(testLeaf)).isNotEmpty();
    }

    @Test
    public void testDeserialize(){
        assertThat(LeafCommandGsonHelper.deserialize("{\n" +
                "  \"commandEnum\": \"id#common\",\n" +
                "  \"commandInputType\": \"LSTRING\",\n" +
                "  \"commandArgument\": \"\"\n" +
                "}")).isEqualTo(testLeaf);
    }

    @Test
    public void testEquality(){
        assertThat(LeafCommandGsonHelper.deserialize(LeafCommandGsonHelper.serialize(testLeaf))).isEqualTo(testLeaf);
        assertThat(LeafCommandGsonHelper.serialize(LeafCommandGsonHelper.deserialize("{\n" +
                "  \"commandEnum\": \"id#common\",\n" +
                "  \"commandInputType\": \"LSTRING\",\n" +
                "  \"commandArgument\": \"\"\n" +
                "}"))).isEqualTo("{\n" +
                "  \"commandEnum\": \"id#common\",\n" +
                "  \"commandInputType\": \"LSTRING\",\n" +
                "  \"commandArgument\": \"\"\n" +
                "}");
    }

    @Test
    public void testAllLeaves(){
        GenericLeafCommand.values().forEach(leaf ->{
            try {
                Leaf l = leaf.getCommandInstance();
                assertThat(LeafCommandGsonHelper.deserialize(LeafCommandGsonHelper.serialize(l))).isEqualTo(l);
            } catch (IllegalAccessException | InstantiationException e) {
                System.err.println(e);
            }
        });
    }
}
