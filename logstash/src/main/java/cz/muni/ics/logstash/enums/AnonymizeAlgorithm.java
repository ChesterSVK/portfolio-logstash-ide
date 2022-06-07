package cz.muni.ics.logstash.enums;

/**
 * Enumeration represents algorithm names available for logstash commands
 *
 * @author Jozef Cibík
 */
public enum AnonymizeAlgorithm {

    SHA1("SHA1"),
    SHA256("SHA256"),
    SHA348("SHA384"),
    SHA512("SHA512"),
    MD5("MD5"),
    MURMUR3("MURMUR3"),
    PUNCTUATION("PUNCTUATION"),
    IPV4_NETWORK("IPV4_NETWORK");

    protected String algo;

    AnonymizeAlgorithm(String algo) {
        this.algo = algo;
    }

    public String getAlgo() {
        return this.algo;
    }
}
