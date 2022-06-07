package cz.muni.ics.logstash.serialization.roots;

import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.commands.roots.RootEnumImpl;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Root;
import cz.muni.ics.logstash.interfaces.RootEnum;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class RootGsonHelperTest {

    private Root testRoot = RootEnumImpl.FILTER.getCommandInstance();

    @Before
    public void before() throws LogstashException {
        testRoot.clear();
        testRoot.add(NodeEnumImpl.JSON.getCommandInstance());
    }

    @Test
    public void testSerialize(){
        assertThat(RootCommandGsonHelper.serialize(testRoot)).isNotEmpty();
        assertThat(RootCommandGsonHelper.serialize(testRoot)).isEqualTo("{\n" +
                "  \"commandEnum\": \"filter\",\n" +
                "  \"commandArgument\": [\n" +
                "    {\n" +
                "      \"commandEnum\": \"json\",\n" +
                "      \"commandArgument\": []\n" +
                "    }\n" +
                "  ]\n" +
                "}");
    }

    @Test
    public void testDeserialize(){
        assertThat(RootCommandGsonHelper.deserialize("{\n" +
                "  \"commandEnum\": \"filter\",\n" +
                "  \"commandArgument\": [\n" +
                "    {\n" +
                "      \"commandEnum\": \"json\",\n" +
                "      \"commandArgument\": []\n" +
                "    }\n" +
                "  ]\n" +
                "}")).isNotNull();
        assertThat(RootCommandGsonHelper.deserialize("{\n" +
                "  \"commandEnum\": \"filter\",\n" +
                "  \"commandArgument\": [\n" +
                "    {\n" +
                "      \"commandEnum\": \"json\",\n" +
                "      \"commandArgument\": []\n" +
                "    }\n" +
                "  ]\n" +
                "}")).isEqualTo(testRoot);
    }

    @Test
    public void testEquality(){
        assertThat(RootCommandGsonHelper.deserialize(RootCommandGsonHelper.serialize(testRoot))).isEqualTo(testRoot);
        assertThat(RootCommandGsonHelper.serialize(RootCommandGsonHelper.deserialize("{\n" +
                "  \"commandEnum\": \"filter\",\n" +
                "  \"commandArgument\": [\n" +
                "    {\n" +
                "      \"commandEnum\": \"json\",\n" +
                "      \"commandArgument\": []\n" +
                "    }\n" +
                "  ]\n" +
                "}")));
    }

    @Test
    public void testAllRoots(){
        for (RootEnum rootEnum : RootEnumImpl.values()){
            Root r = rootEnum.getCommandInstance();
            assertThat(RootCommandGsonHelper.deserialize(RootCommandGsonHelper.serialize(r))).isEqualTo(r);
        }
    }
}
