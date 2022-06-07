package cz.muni.ics.logstash.utils;

import cz.muni.ics.logstash.types.LArray;
import cz.muni.ics.logstash.types.LHash;
import cz.muni.ics.logstash.types.LString;
import org.apache.commons.lang3.StringUtils;

public class LogstashTypePrettyPrinter {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Public

    public static String prettyPrintString(LString lString) {
        return "\"" + lString + "\"";
    }

    public static String prettyPrintHash(int level, LHash lHash) {
        if (lHash.isEmpty()) {
            return "{}";
        }
        return printHash(level, lHash);
    }

    public static String prettyPrintArray(int level, LArray lArray) {
        if (lArray.isEmpty()) {
            return "[]";
        }
        return printArray(level, lArray);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private static String printHash(int level, LHash lHash) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        lHash.forEach((key, value) -> sb
                .append(StringUtils.repeat("\t", level + 1))
                .append(key.toLogstashString(level))
                .append(" => ")
                .append(value.toLogstashString(level))
                .append("\n"));
        sb.append(StringUtils.repeat("\t", level));
        sb.append("}");
        return sb.toString();
    }

    private static String printArray(int level, LArray lArray) {
        StringBuilder sb = new StringBuilder();
        sb.append("\t[");
        printArrayValues(sb, level, lArray);
        sb.append("]");
        return sb.toString();
    }

    private static void printArrayValues(StringBuilder sb, int level, LArray lArray) {
        boolean first = true;
        for (LString s : lArray) {
            if (first) {
                first = false;
                sb.append(s.toLogstashString(level));
            } else {
                sb.append(", ")
                        .append(s.toLogstashString(level));
            }
        }
    }
}
