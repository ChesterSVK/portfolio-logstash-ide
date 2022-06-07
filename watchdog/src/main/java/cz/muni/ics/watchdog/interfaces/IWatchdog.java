package cz.muni.ics.watchdog.interfaces;

import cz.muni.ics.watchdog.exceptions.WatchdogException;
import cz.muni.ics.watchdog.dataclasses.WatchdogConfig;
import cz.muni.ics.watchdog.dataclasses.WatchdogData;

import java.nio.file.Path;

/**
 *
 *
 * @author Jozef Cibík
 */

public interface IWatchdog {
    String getWatchdogScriptPath();
    boolean executeWatchdog(WatchdogData config);
}
