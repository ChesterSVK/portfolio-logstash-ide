package cz.muni.ics.watchdog.interfaces;

import cz.muni.ics.watchdog.exceptions.WatchdogException;
import cz.muni.ics.watchdog.dataclasses.WatchdogConfig;

import java.nio.file.Path;

/**
 * Interface states method for creating configrutaion file used in watchdog script
 * Constraints: So far script accepts json file so created config file must be JSON.
 *
 * @author Jozef Cibík
 */

public interface WatchdogConfigCreator {
    /**
     * Creates json file with configuration host used in watchdog script
     * @param config host to save to file
     * @param parent to create config in
     * @return path to created config file
     * @throws WatchdogException if config, parent is invalid or something failed during file creation
     */
    Path createConfig(WatchdogConfig config, Path parent) throws WatchdogException;
}
