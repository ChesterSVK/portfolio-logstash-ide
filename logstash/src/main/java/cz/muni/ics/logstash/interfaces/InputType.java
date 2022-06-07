package cz.muni.ics.logstash.interfaces;

import cz.muni.ics.logstash.enums.LType;

public interface InputType extends LogstashSerializable {
    String toLogstashString(int level);
    LType getType();
    boolean isInitialised();
}
