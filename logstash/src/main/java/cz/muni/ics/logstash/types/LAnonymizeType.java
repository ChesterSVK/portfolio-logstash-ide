package cz.muni.ics.logstash.types;

import cz.muni.ics.logstash.enums.AnonymizeAlgorithm;
import cz.muni.ics.logstash.enums.LType;
import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.serialization.types.InputTypeGsonHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class LAnonymizeType extends LString implements InputType {
    //Used in parsing
    public LAnonymizeType() {
        super();
    }

    public LAnonymizeType(AnonymizeAlgorithm algorithm) {
        super(algorithm.getAlgo());
    }

    @Override
    public LType getType() {
        return LType.LANONYMIZE_ALGO;
    }

    @Override
    public String toString(){
        return super.getLString();
    }
}
