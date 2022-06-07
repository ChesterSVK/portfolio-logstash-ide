package cz.muni.ics.watchdog.enums;

/**
 * Actions supported by watchdog script
 *
 * @author Jozef Cibík
 */

public enum WatchdogAction {
    REGISTER("register"),
    UNREGISTER("unregister"),
    CLEAN("clean");

    protected String action;

    WatchdogAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }
}
