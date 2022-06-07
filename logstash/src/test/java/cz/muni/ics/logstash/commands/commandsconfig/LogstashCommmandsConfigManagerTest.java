package cz.muni.ics.logstash.commands.commandsconfig;

import cz.muni.ics.logstash.LogstashApplication;
import cz.muni.ics.logstash.commands.nodes.NodeEnumImpl;
import cz.muni.ics.logstash.commands.roots.RootEnumImpl;
import cz.muni.ics.logstash.exception.LogstashException;
import cz.muni.ics.logstash.interfaces.Root;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = LogstashApplication.class)
public class LogstashCommmandsConfigManagerTest {

    @Autowired
    private LogstashCommandsConfigManager manager;


    private Root testCommand = RootEnumImpl.FILTER.getCommandInstance();

    @Test
    public void testGetCopasConfig() {
        LogstashCommandsConfig commandsConfig = manager.getCopASLogstashConfiguration();
        assertThat(commandsConfig).isNotNull();
        assertThat(commandsConfig.size()).isNotEqualTo(0);
        assertThat(commandsConfig.contains(RootEnumImpl.FILTER.getCommandInstance())).isTrue();
        assertThat(commandsConfig.contains(RootEnumImpl.INPUT.getCommandInstance())).isTrue();
        assertThat(commandsConfig.contains(RootEnumImpl.OUTPUT.getCommandInstance())).isTrue();
    }


    @Test
    public void testCreateFromCommands() {
        LogstashCommandsConfig commandsConfig = manager.createLogstashConfigFromCommands(Collections.emptyList());
        assertThat(commandsConfig).isNotNull();
        assertThat(commandsConfig.size()).isEqualTo(0);

        commandsConfig = manager.createLogstashConfigFromCommands(Collections.singletonList(testCommand));
        assertThat(commandsConfig).isNotNull();
        assertThat(commandsConfig.size()).isEqualTo(1);
        assertThat(commandsConfig.contains(testCommand)).isTrue();
    }

    @Test
    public void testGetUsedCommands() {
        LogstashCommandsConfig commandsConfig = new LogstashCommandsConfig();
        assertThat(manager.getUsedCommands(commandsConfig)).isEmpty();

        commandsConfig.add(testCommand);
        assertThat(commandsConfig.size()).isEqualTo(1);
        assertThat(commandsConfig.contains(testCommand)).isTrue();
        assertThat(manager.getUsedCommands(commandsConfig)).isNotEmpty();
        assertThat(manager.getUsedCommands(commandsConfig).size()).isEqualTo(1);
        assertThat(manager.getUsedCommands(commandsConfig).contains(testCommand)).isTrue();
    }


    @Test
    public void testAddEqualCommands() {
        LogstashCommandsConfig commandsConfig = new LogstashCommandsConfig();
        manager.addRootCommand(testCommand, commandsConfig);
        assertThat(commandsConfig.contains(testCommand)).isTrue();
        assertThat(commandsConfig.size()).isEqualTo(1);
        assertThat(commandsConfig.get(0)).isEqualTo(testCommand);

        manager.addRootCommand(testCommand, commandsConfig);
        assertThat(commandsConfig.contains(testCommand)).isTrue();
        assertThat(commandsConfig.size()).isEqualTo(1);
        assertThat(commandsConfig.get(0)).isEqualTo(testCommand);
    }

    @Test
    public void testAddSimilarCommands() throws LogstashException {
        LogstashCommandsConfig commandsConfig = new LogstashCommandsConfig();
        manager.addRootCommand(testCommand, commandsConfig);
        assertThat(commandsConfig.contains(testCommand)).isTrue();
        assertThat(commandsConfig.size()).isEqualTo(1);
        assertThat(commandsConfig.get(0)).isEqualTo(testCommand);

        Root newRoot1 = RootEnumImpl.FILTER.getCommandInstance();
        newRoot1.add(NodeEnumImpl.JSON.getCommandInstance());
        manager.addRootCommand(newRoot1, commandsConfig);
        assertThat(commandsConfig.contains(testCommand)).isTrue();
        assertThat(commandsConfig.size()).isEqualTo(1);
        assertThat(commandsConfig.get(0)).isEqualTo(newRoot1);
        assertThat(commandsConfig.get(0).getCommandArgument()).isNotNull();
        assertThat(commandsConfig.get(0).getCommandArgument()).isNotEmpty();
    }

    @Test
    public void testAddDifferentCommands() throws LogstashException {
        LogstashCommandsConfig commandsConfig = new LogstashCommandsConfig();

        Root newRoot1 = RootEnumImpl.INPUT.getCommandInstance();
        newRoot1.add(NodeEnumImpl.JSON.getCommandInstance());

        manager.addRootCommand(testCommand, commandsConfig);
        manager.addRootCommand(newRoot1, commandsConfig);

        assertThat(commandsConfig.contains(testCommand)).isTrue();
        assertThat(commandsConfig.contains(newRoot1)).isTrue();
        assertThat(commandsConfig.size()).isEqualTo(2);
    }

    @Test
    public void testAddCommand() {
        LogstashCommandsConfig commandsConfig = new LogstashCommandsConfig();
        manager.addRootCommand(testCommand, commandsConfig);
        assertThat(commandsConfig.contains(testCommand)).isTrue();
        assertThat(commandsConfig.size()).isEqualTo(1);
        assertThat(commandsConfig.get(0)).isEqualTo(testCommand);
    }

    @Test
    public void testRemoveCommand() throws LogstashException {
        LogstashCommandsConfig commandsConfig = new LogstashCommandsConfig();
        manager.addRootCommand(testCommand, commandsConfig);
        manager.deleteRootCommand(testCommand, commandsConfig);
        assertThat(commandsConfig.contains(testCommand)).isFalse();
        assertThat(commandsConfig.size()).isEqualTo(0);
    }

    @Test
    public void testRemoveNonexistingCommand() throws LogstashException {
        LogstashCommandsConfig commandsConfig = new LogstashCommandsConfig();
        manager.addRootCommand(testCommand, commandsConfig);

        //Check if nonexisting / different changes anything
        Root newRoot1 = RootEnumImpl.INPUT.getCommandInstance();
        newRoot1.add(NodeEnumImpl.JSON.getCommandInstance());
        manager.addRootCommand(newRoot1, commandsConfig);
        manager.deleteRootCommand(testCommand, commandsConfig);
        assertThat(commandsConfig.size()).isEqualTo(1);
    }

    @Test
    public void testRemoveEqualCommandDifferentArgs() throws LogstashException {
        LogstashCommandsConfig commandsConfig = new LogstashCommandsConfig();

        Root newRoot1 = RootEnumImpl.INPUT.getCommandInstance();
        newRoot1.add(NodeEnumImpl.JSON.getCommandInstance());
        newRoot1.add(NodeEnumImpl.DATE.getCommandInstance());
        assertThat(newRoot1.getCommandArgument().size()).isEqualTo(2);

        manager.addRootCommand(newRoot1, commandsConfig);
        assertThat(commandsConfig.size()).isEqualTo(1);
        assertThat(commandsConfig.contains(newRoot1)).isTrue();

        Root newRoot2 = RootEnumImpl.INPUT.getCommandInstance();
        newRoot2.add(NodeEnumImpl.JSON.getCommandInstance());
        assertThat(newRoot2.getCommandArgument().size()).isEqualTo(1);

        manager.deleteRootCommand(newRoot2, commandsConfig);
        assertThat(commandsConfig.size()).isEqualTo(1);
        assertThat(commandsConfig.get(0).getCommandArgument().size()).isEqualTo(1);
    }

    @Test
    public void testRemoveCommandsEmptyArgs() throws LogstashException {
        LogstashCommandsConfig commandsConfig = new LogstashCommandsConfig();

        Root newRoot1 = RootEnumImpl.INPUT.getCommandInstance();
        newRoot1.add(NodeEnumImpl.JSON.getCommandInstance());
        newRoot1.add(NodeEnumImpl.DATE.getCommandInstance());
        assertThat(newRoot1.getCommandArgument().size()).isEqualTo(2);

        manager.addRootCommand(newRoot1, commandsConfig);
        assertThat(commandsConfig.size()).isEqualTo(1);
        assertThat(commandsConfig.contains(newRoot1)).isTrue();

        Root newRoot2 = RootEnumImpl.INPUT.getCommandInstance();
        newRoot2.add(NodeEnumImpl.JSON.getCommandInstance());
        newRoot2.add(NodeEnumImpl.DATE.getCommandInstance());
        assertThat(newRoot2.getCommandArgument().size()).isEqualTo(2);

        manager.deleteRootCommand(newRoot2, commandsConfig);
        assertThat(commandsConfig.size()).isEqualTo(0);
    }
}