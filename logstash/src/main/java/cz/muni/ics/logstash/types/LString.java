package cz.muni.ics.logstash.types;

import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.serialization.types.InputTypeGsonHelper;
import cz.muni.ics.logstash.utils.LogstashTypePrettyPrinter;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@RequiredArgsConstructor
public class LString implements InputType {

    @NonNull
    @NotEmpty
    private String lString = "";

    public LString(@NonNull String s) {
        this.lString = s;
    }

    @Override
    public String toLogstashString(int level) {
        return LogstashTypePrettyPrinter.prettyPrintString(this);
    }

    @Override
    public LType getType() {
        return LType.LSTRING;
    }

    @Override
    public String toJsonString() {
        return InputTypeGsonHelper.serialize(this);
    }

    @Override
    public String toString() {
        return lString;
    }

    @Override
    public boolean isInitialised() {
        return !lString.isEmpty();
    }
}
