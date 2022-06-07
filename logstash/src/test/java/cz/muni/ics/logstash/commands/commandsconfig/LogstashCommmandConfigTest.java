package cz.muni.ics.logstash.commands.commandsconfig;

import cz.muni.ics.logstash.commands.roots.RootEnumImpl;
import org.junit.Test;

import java.util.Collections;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class LogstashCommmandConfigTest {

    @Test
    public void testCreate() {
        assertThat(new LogstashCommandsConfig()).isNotNull();
        assertThat(new LogstashCommandsConfig()).isEmpty();
        assertThat(new LogstashCommandsConfig().toString()).isNotEmpty();
    }

    @Test
    public void testEquals() {
        LogstashCommandsConfig commandsConfig1 = new LogstashCommandsConfig();
        LogstashCommandsConfig commandsConfig2 = new LogstashCommandsConfig();

        assertThat(commandsConfig1.equals(commandsConfig2)).isTrue();
        commandsConfig2.addAll(Collections.singletonList(RootEnumImpl.FILTER.getCommandInstance()));
        assertThat(commandsConfig1.equals(commandsConfig2)).isFalse();
    }

    @Test
    public void testToLogstash() {
        LogstashCommandsConfig commandsConfig = new LogstashCommandsConfig();
        assertThat(commandsConfig.toLogstashString(0)).isEmpty();
        commandsConfig.addAll(Collections.singletonList(RootEnumImpl.FILTER.getCommandInstance()));
        assertThat(commandsConfig.toLogstashString(0)).isNotEmpty();
    }
}