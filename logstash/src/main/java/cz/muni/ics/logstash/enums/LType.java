package cz.muni.ics.logstash.enums;

import cz.muni.ics.logstash.interfaces.InputType;
import cz.muni.ics.logstash.types.*;

public enum LType {
    LARRAY(LArray.class),
    LSTRING(LString.class),
    LHASH_MAP(LHash.class),
    LBOOL(LBool.class),
    LURI(LUri.class),
    LTIMESTAMP_ARRAY(LTimestampArray.class),
    LANONYMIZE_ALGO(LAnonymizeType.class);

    private Class aClass;

    <T extends InputType> LType(Class<T> clazz) {
        this.aClass = clazz;
    }

    public InputType getTypeInstance() throws IllegalAccessException, InstantiationException {
        return (InputType) this.aClass.newInstance();
    }
}
