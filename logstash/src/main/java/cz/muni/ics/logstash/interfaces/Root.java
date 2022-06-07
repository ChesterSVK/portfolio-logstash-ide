package cz.muni.ics.logstash.interfaces;

import java.util.List;

public interface Root extends List<Node>, LogstashCommand {
    List<Node> getCommandArgument();
}
