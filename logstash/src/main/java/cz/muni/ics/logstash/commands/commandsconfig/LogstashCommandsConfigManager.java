package cz.muni.ics.logstash.commands.commandsconfig;

import cz.muni.ics.logstash.interfaces.Root;

import java.util.Collection;
import java.util.List;

public interface LogstashCommandsConfigManager {
    void addRootCommand(Root newRootCommand, LogstashCommandsConfig commandsConfig);
    LogstashCommandsConfig getCopASLogstashConfiguration();
    Collection<Root> getUsedCommands(LogstashCommandsConfig configuration);
    void deleteRootCommand(Root root, LogstashCommandsConfig originalConfiguration);
    LogstashCommandsConfig createLogstashConfigFromCommands(List<Root> roots);
}
