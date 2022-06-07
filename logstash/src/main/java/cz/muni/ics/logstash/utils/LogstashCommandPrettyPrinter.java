package cz.muni.ics.logstash.utils;

import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.interfaces.Leaf;
import cz.muni.ics.logstash.interfaces.LogstashCommand;
import cz.muni.ics.logstash.interfaces.Node;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

public class LogstashCommandPrettyPrinter {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Public

    public static String prettyPrintRoot(int level, String name, Collection<Node> nodes) {
        if (nodes.size() != 0) {
            return StringUtils.repeat("\t", level)
                    + name
                    + " {\n"
                    + getCollectionPrint(level + 1, nodes)
                    + StringUtils.repeat("\t", level)
                    + "}\n";
        } else return "";
    }

    public static String prettyPrintNode(int level, String name, Collection<Leaf> leaves) {
        return StringUtils.repeat("\t", level)
                + name
                + " {\n"
                + getCollectionPrint(level + 1, leaves)
                + StringUtils.repeat("\t", level)
                + "}\n";
    }

    public static String prettyPrintLeaf(int level, String commandName, InputType commandArgument) {
        return StringUtils.repeat("\t", level)
                + commandName
                + " => "
                + "\n"
                + StringUtils.repeat("\t", level)
                + ((commandArgument != null) ? commandArgument.toLogstashString(level) : "")
                + StringUtils.repeat("\t", level)
                + "\n";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private static <P extends LogstashCommand> String getCollectionPrint(int level, Collection<P> items) {
        if (!items.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            items.forEach(command -> {
                sb.append(command.toLogstashString(level));
            });
            return sb.toString();
        } else return "";
    }
}
