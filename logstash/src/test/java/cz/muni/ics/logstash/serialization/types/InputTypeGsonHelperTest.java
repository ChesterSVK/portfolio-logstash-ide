package cz.muni.ics.logstash.serialization.types;

import com.google.gson.JsonParser;
import cz.muni.ics.logstash.enums.AnonymizeAlgorithm;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.types.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class InputTypeGsonHelperTest {

    private JsonParser parser = new JsonParser();
    private LString testTypeString = new LString("testString");
    private LArray testTypeArray = new LArray(Collections.singletonList("testArray"));
    private LBool testTypeBool = new LBool(false);
    private LHash testTypeHash = new LHash();
    private LUri testTypeUri = new LUri("testUri");
    private LTimestampArray testTypeTimpestampArray = new LTimestampArray(Collections.singletonList(new LString("testTArray")));
    private LAnonymizeType testTypeAnonymize = new LAnonymizeType(AnonymizeAlgorithm.IPV4_NETWORK);

    @Before
    public void before(){
        testTypeHash.put(new LString("testKey"), new LString("testVal"));
    }

    @After
    public void after(){
        testTypeHash.clear();
    }

    @Test
    public void testSerialize(){
        assertThat(InputTypeGsonHelper.serialize(testTypeString)).isNotEmpty();
        assertThat(InputTypeGsonHelper.serialize(testTypeString)).contains(testTypeString.getLString());

        assertThat(InputTypeGsonHelper.serialize(testTypeArray)).isNotEmpty();
        assertThat(InputTypeGsonHelper.serialize(testTypeArray)).isEqualTo("[\n" +
                "  \"testArray\"\n" +
                "]");

        assertThat(InputTypeGsonHelper.serialize(testTypeBool)).isNotEmpty();
        assertThat(InputTypeGsonHelper.serialize(testTypeBool)).isEqualTo("false");

        assertThat(InputTypeGsonHelper.serialize(testTypeHash)).isNotEmpty();
        assertThat(InputTypeGsonHelper.serialize(testTypeHash)).isEqualTo("{\n" +
                "  \"testKey\": \"testVal\"\n" +
                "}");

        assertThat(InputTypeGsonHelper.serialize(testTypeTimpestampArray)).isNotEmpty();
        assertThat(InputTypeGsonHelper.serialize(testTypeTimpestampArray)).isEqualTo("[\n" +
                "  \"testTArray\"\n" +
                "]");

        assertThat(InputTypeGsonHelper.serialize(testTypeAnonymize)).isNotEmpty();
        assertThat(InputTypeGsonHelper.serialize(testTypeAnonymize)).contains(AnonymizeAlgorithm.IPV4_NETWORK.getAlgo());

        assertThat(InputTypeGsonHelper.serialize(testTypeUri)).isNotEmpty();
        assertThat(InputTypeGsonHelper.serialize(testTypeUri)).contains("testUri");
    }

    @Test
    public void testDeserialize(){
        assertThat(InputTypeGsonHelper.deserialize(parser.parse("\"testString\""), LType.LSTRING)).isNotNull();
        assertThat(InputTypeGsonHelper.deserialize(parser.parse("\"testString\""), LType.LSTRING)).isEqualTo(testTypeString);

        assertThat(InputTypeGsonHelper.deserialize(parser.parse("[\n" +
                "  \"\\\"testArray\\\"\"\n" +
                "]"), LType.LARRAY)).isNotNull();
        assertThat(InputTypeGsonHelper.deserialize(parser.parse("[\n" +
                "  \"\\\"testArray\\\"\"\n" +
                "]"), LType.LARRAY)).isEqualTo(testTypeArray);

        assertThat(InputTypeGsonHelper.deserialize(parser.parse("false"), LType.LBOOL)).isNotNull();
        assertThat(InputTypeGsonHelper.deserialize(parser.parse("false"), LType.LBOOL)).isEqualTo(testTypeBool);

        assertThat(InputTypeGsonHelper.deserialize(parser.parse("{\n" +
                "  \"testKey\": \"testVal\"\n" +
                "}"), LType.LHASH_MAP)).isNotNull();
        assertThat(InputTypeGsonHelper.deserialize(parser.parse("{\n" +
                "  \"testKey\": \"testVal\"\n" +
                "}"), LType.LHASH_MAP)).isEqualTo(testTypeHash);

        assertThat(InputTypeGsonHelper.deserialize(parser.parse("[\n" +
                "  \"\\\"testTArray\\\"\"\n" +
                "]"), LType.LTIMESTAMP_ARRAY)).isNotNull();
        assertThat(InputTypeGsonHelper.deserialize(parser.parse("[\n" +
                "  \"\\\"testTArray\\\"\"\n" +
                "]"), LType.LTIMESTAMP_ARRAY)).isEqualTo(testTypeTimpestampArray);

        assertThat(InputTypeGsonHelper.deserialize(parser.parse("IPV4_NETWORK"), LType.LANONYMIZE_ALGO)).isNotNull();
        assertThat(InputTypeGsonHelper.deserialize(parser.parse("IPV4_NETWORK"), LType.LANONYMIZE_ALGO)).isEqualTo(testTypeAnonymize);

        assertThat(InputTypeGsonHelper.deserialize(parser.parse("\"testUri\""), LType.LURI)).isNotNull();
        assertThat(InputTypeGsonHelper.deserialize(parser.parse("\"testUri\""), LType.LURI)).isEqualTo(testTypeUri);
    }
}
