package cz.muni.ics.logstash.enums;

/**
 * Enumeration represents time formats available for logstash commands
 *
 * @author Jozef Cibík
 */
public enum TimestampFormat {

    ISO8601("ISO8601"),
    UNIX("UNIX"),
    UNIX_MS("UNIX_MS"),
    TAI64N("TAI64N");

    protected String format;

    TimestampFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }
}
