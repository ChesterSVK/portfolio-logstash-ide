package cz.muni.ics.logstash.utils;

import cz.muni.ics.logstash.types.LArray;
import cz.muni.ics.logstash.types.LHash;
import cz.muni.ics.logstash.types.LString;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class LogstashTypePrettyPrinterTest {

    @Test
    public void testHash(){
        LHash lHash = new LHash();
        assertThat(LogstashTypePrettyPrinter.prettyPrintHash(0, lHash)).isEqualTo("{}");
        lHash.put(new LString("key"), new LString("val"));
        assertThat(LogstashTypePrettyPrinter.prettyPrintHash(0, lHash)).isEqualTo("{\n" +
                "\t\"key\" => \"val\"\n" +
                "}");
    }

    @Test
    public void testArray(){
        LArray lArray = new LArray();
        assertThat(LogstashTypePrettyPrinter.prettyPrintArray(0, lArray)).isEqualTo("[]");
        lArray.add(new LString("arr"));
        assertThat(LogstashTypePrettyPrinter.prettyPrintArray(0, lArray)).isEqualTo("\t[\"arr\"]");
    }

    @Test
    public void testString(){
        LString lString = new LString();
        assertThat(LogstashTypePrettyPrinter.prettyPrintString(lString)).isEqualTo("\"\"");
        lString = new LString("test");
        assertThat(LogstashTypePrettyPrinter.prettyPrintString(lString)).isEqualTo("\"test\"");
    }
}
