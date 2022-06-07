package cz.muni.ics.web.utils.thymleaf;

public final class GlobalPageVariables {
    private GlobalPageVariables(){}
    //Invoked by th.xml files
    public static final String getCopasImageVersion(){
        return System.getenv().getOrDefault("COPAS_IMAGE_VERSION", "N/A");
    }
    public static final String getCopasVersion(){
        return System.getenv().getOrDefault("COPAS_VERSION", "N/A");
    }
    public static final String getContainerId(){
        return System.getenv().getOrDefault("COPAS_ID", "");
    }
    public static final String getKibanaPort(){
        return System.getenv().getOrDefault("KIBANA_PORT", "");
    }
    public static final String getAppBuildVersion() { return ""; }
    public static final String getSupervisorMail(){
        return "mailto:rebok@ics.muni.cz?subject=Neck%20mail";
    }
}
