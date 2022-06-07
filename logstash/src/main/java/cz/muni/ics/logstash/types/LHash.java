package cz.muni.ics.logstash.types;

import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.serialization.types.InputTypeGsonHelper;
import cz.muni.ics.logstash.utils.LogstashTypePrettyPrinter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;

@Data
@EqualsAndHashCode(callSuper = true)
public class LHash extends HashMap<LString, LString> implements InputType {

    @Override
    public String toLogstashString(int level) {
        return LogstashTypePrettyPrinter.prettyPrintHash(level, this);
    }

    @Override
    public String toJsonString() {
        return InputTypeGsonHelper.serialize(this);
    }

    @Override
    public LType getType() {
        return LType.LHASH_MAP;
    }

    @Override
    public boolean isInitialised() {
        return !this.isEmpty();
    }
}
