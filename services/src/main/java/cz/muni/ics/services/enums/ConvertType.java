package cz.muni.ics.services.enums;

public enum ConvertType {
    JSON("json"),
    PCAP("pcap");

    private final String typeExt;

    ConvertType(String s) {
        typeExt = s;
    }

    public boolean equalsName(String otherExt) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return typeExt.equals(otherExt);
    }

    public String toString() {
        return this.typeExt;
    }
}
