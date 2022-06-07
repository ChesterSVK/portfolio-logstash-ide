package cz.muni.ics.logstash.types;

import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.serialization.types.InputTypeGsonHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LBool implements InputType {

    @Getter
    private boolean value = false;

    @Override
    public String toLogstashString(int level) {
        return String.valueOf(value);
    }

    @Override
    public LType getType() {
        return LType.LBOOL;
    }

    @Override
    public boolean isInitialised() {
        return true;
    }

    @Override
    public String toJsonString() {
        return InputTypeGsonHelper.serialize(this);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
