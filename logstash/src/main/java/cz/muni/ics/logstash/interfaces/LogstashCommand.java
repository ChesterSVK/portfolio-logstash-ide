package cz.muni.ics.logstash.interfaces;

public interface LogstashCommand extends LogstashPrintable, Cloneable, LogstashSerializable {
    String getCommandName();
}
