package cz.muni.ics.logstash.types;

import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.serialization.types.InputTypeGsonHelper;
import cz.muni.ics.logstash.utils.LogstashTypePrettyPrinter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LArray extends ArrayList<LString> implements InputType {

    public LArray(Collection<String> strings) {
        this.addAll(strings.stream().map(LString::new).collect(Collectors.toList()));
    }

    @Override
    public String toLogstashString(int level) {
        return LogstashTypePrettyPrinter.prettyPrintArray(level, this);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public LType getType() {
        return LType.LARRAY;
    }

    @Override
    public boolean isInitialised() {
        return !this.isEmpty();
    }


    @Override
    public String toJsonString() {
        return InputTypeGsonHelper.serialize(this);
    }
}
