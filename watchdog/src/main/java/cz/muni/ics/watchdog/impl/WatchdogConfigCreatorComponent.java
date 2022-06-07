package cz.muni.ics.watchdog.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import cz.muni.ics.watchdog.dataclasses.WatchdogConfig;
import cz.muni.ics.watchdog.exceptions.WatchdogException;
import cz.muni.ics.watchdog.exceptions.WatchdogMessages;
import cz.muni.ics.watchdog.interfaces.WatchdogConfigCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Jozef Cibík
 */

@Slf4j
@Component
public class WatchdogConfigCreatorComponent implements WatchdogConfigCreator {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public Path createConfig(WatchdogConfig config, Path parent) throws WatchdogException {
        String finalFileName = createConfigName();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Path configPath = Files.createFile(new File(parent.toFile(), finalFileName).toPath());
            try (Writer writer = new FileWriter(configPath.toFile())) {
                gson.toJson(config, writer);
            }
            log.debug("Created new watchdog config file: " + configPath.toAbsolutePath().toString());
            return configPath;
        } catch (IOException e) {
            throw new WatchdogException(WatchdogMessages.ERROR_CREATING_CONFIG + e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private static String createConfigName() {
        return "watchdog-config-" + new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date()) + ".json";
    }
}
