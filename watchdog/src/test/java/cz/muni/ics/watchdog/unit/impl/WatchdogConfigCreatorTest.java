package cz.muni.ics.watchdog.unit.impl;

import cz.muni.ics.watchdog.exceptions.WatchdogException;
import cz.muni.ics.watchdog.dataclasses.WatchdogConfig;
import cz.muni.ics.watchdog.impl.WatchdogConfigCreatorComponent;
import cz.muni.ics.watchdog.unit.TemporaryTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class WatchdogConfigCreatorTest extends TemporaryTest {

    private TemporaryFolder temporaryFolder = new TemporaryFolder(PROJECT_TEST_DIR);

    private WatchdogConfigCreatorComponent configCreator = new WatchdogConfigCreatorComponent();
    private WatchdogConfig watchdogConfig = new WatchdogConfig("9200");

    @Before
    public void before() throws IOException {
        temporaryFolder.create();
    }

    @After
    public void after() {
        temporaryFolder.delete();
    }

    @Test
    public void testCreateConfig() throws IOException, WatchdogException {
        Path config = configCreator.createConfig(watchdogConfig, temporaryFolder.getRoot().toPath());
        assertThat(config.toFile()).exists();
        assertThat(Files.readAllLines(config).size()).isGreaterThan(0);
    }

    @Test
    public void testCreateConfigNonExistingParent(){
        temporaryFolder.delete();
        assertThatThrownBy( () -> configCreator.createConfig(watchdogConfig, temporaryFolder.getRoot().toPath())).isInstanceOf(WatchdogException.class);
    }
}
