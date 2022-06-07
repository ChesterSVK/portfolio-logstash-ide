package cz.muni.ics.logstash.types;

import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.enums.TimestampFormat;
import cz.muni.ics.logstash.interfaces.InputType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LTimestampArray extends LArray implements InputType {

    //Used in parsing
    public LTimestampArray(Collection<LString> strings){
        super(strings.stream().map(LString::getLString).collect(Collectors.toList()));
    }

    @Override
    public LType getType() {
        return LType.LTIMESTAMP_ARRAY;
    }

    @Override
    public String toString(){return super.toString();}
}
