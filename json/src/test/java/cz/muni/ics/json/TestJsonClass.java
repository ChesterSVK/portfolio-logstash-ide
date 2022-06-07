package cz.muni.ics.json;

public class TestJsonClass {

    private String a;
    private String c;

    //Used for Jackson, do not delete
    public TestJsonClass(){}
    public TestJsonClass(String a, String c){
        this.a = a;
        this.c = c;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }
}
