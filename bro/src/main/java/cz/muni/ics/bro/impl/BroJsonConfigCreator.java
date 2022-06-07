package cz.muni.ics.bro.impl;

import cz.muni.ics.bro.exceptions.BroException;
import cz.muni.ics.bro.exceptions.BroMessages;
import cz.muni.ics.filesystem.FileUtilities;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Implementation for the Bro config creator used for json file types
 *
 * @author Jozef Cibík
 */

@Component
public class BroJsonConfigCreator {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Public

    public Path createBroConfig(String customCommand, Path parent) throws BroException {
        checkCommand(customCommand);
        try {
            return FileUtilities.createFile(customCommand, "custom_bro_config", parent, true);
        } catch (IOException e) {
            throw new BroException(BroMessages.ERROR_CREATING_CONFIG, e);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private functions

    private void checkCommand(String customCommand) throws BroException {
        if (customCommand.isEmpty()) {
            throw new BroException(BroMessages.ERROR_EMPTY_CONFIG_COMMAND);
        }
    }
}
