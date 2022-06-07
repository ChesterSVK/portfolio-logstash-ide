package cz.muni.ics.logstash.types;

import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.serialization.types.InputTypeGsonHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@EqualsAndHashCode(callSuper = true)
public class LUri extends LString implements InputType {

    //Used in parsing
    public LUri() {
        super();
    }

    public LUri(@NotEmpty String lString) {
        super(lString);
    }

    @Override
    public LType getType() {
        return LType.LURI;
    }

    @Override
    public String toString(){
        return super.getLString();
    }
}
