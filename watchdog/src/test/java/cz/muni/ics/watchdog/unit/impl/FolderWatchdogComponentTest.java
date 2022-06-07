package cz.muni.ics.watchdog.unit.impl;

import cz.muni.ics.core.exceptions.ApplicationException;
import cz.muni.ics.core.os.SystemCommandExecutor;
import cz.muni.ics.watchdog.dataclasses.WatchdogConfig;
import cz.muni.ics.watchdog.dataclasses.WatchdogData;
import cz.muni.ics.watchdog.exceptions.WatchdogException;
import cz.muni.ics.watchdog.impl.WatchdogComponent;
import cz.muni.ics.watchdog.impl.WatchdogConfigCreatorComponent;
import cz.muni.ics.watchdog.unit.TemporaryTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FolderWatchdogComponentTest extends TemporaryTest {

    private TemporaryFolder temporaryFolder = new TemporaryFolder(PROJECT_TEST_DIR);
    private Path tempResultFile;
    private Set<Path> testFolders = new HashSet<>();
    private WatchdogConfig watchdogConfig = new WatchdogConfig("9200");


    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SystemCommandExecutor executor;

    @Mock
    private WatchdogConfigCreatorComponent configCreator;

    @InjectMocks
    private WatchdogComponent folderWatchdogService = new WatchdogComponent();

    @Before
    public void before() throws IOException {
        temporaryFolder.create();
        tempResultFile = temporaryFolder.newFolder().toPath();
        testFolders.add(tempResultFile.toFile().toPath());
        ReflectionTestUtils.setField(folderWatchdogService, "scriptPath", "autowiredValue");
    }

    @After
    public void after() {
        temporaryFolder.delete();
        testFolders.clear();
    }

//    @Test
//    public void testCreateConfig() throws WatchdogException {
//        when(configCreator.createConfig(any(), any())).thenReturn(tempResultFile);
//        assertThat(Collections.singleton(folderWatchdogService.createWatchdogConfig(watchdogConfig))).isNotNull();
//    }
//
//    @Test
//    public void testCreateConfigBadParent() throws WatchdogException {
//        temporaryFolder.delete();
//        when(configCreator.createConfig(any(), any())).thenThrow(WatchdogException.class);
//        assertThatThrownBy(() -> folderWatchdogService.createWatchdogConfig(watchdogConfig)).isInstanceOf(WatchdogException.class);
//    }

    @Test
    public void testExecuteCommand() throws IOException {
        File existingScript = temporaryFolder.newFile();
        File existingConfig = temporaryFolder.newFile();
        WatchdogData config = new WatchdogData(existingScript.getAbsolutePath(), existingConfig.getAbsolutePath());
        config.setFoldersToWatch(testFolders);
        when(executor.executeWithExitCode(any())).thenReturn(0);
        assertThat(folderWatchdogService.executeWatchdog(config)).isTrue();
    }

    @Test
    public void testExecuteCommandBadResult() throws IOException {
        File existingScript = temporaryFolder.newFile();
        File existingConfig = temporaryFolder.newFile();
        WatchdogData config = new WatchdogData(existingScript.getAbsolutePath(), existingConfig.getAbsolutePath());
        config.setFoldersToWatch(testFolders);
        when(executor.executeWithExitCode(any())).thenReturn(-1);
        assertThat(folderWatchdogService.executeWatchdog(config)).isFalse();
    }

    @Test
    public void testExecuteCommandNonExistingConfig() throws IOException {
        File existingScript = temporaryFolder.newFile();
        File nonExistingConfig = temporaryFolder.newFile();
        nonExistingConfig.delete();
        WatchdogData config = new WatchdogData(existingScript.getAbsolutePath(), nonExistingConfig.getAbsolutePath());
        config.setFoldersToWatch(testFolders);
        assertThat(folderWatchdogService.executeWatchdog(config)).isFalse();
    }

    @Test
    public void testExecuteCommandNonExistingScript() throws IOException {
        File nonExistingScript = temporaryFolder.newFile();
        File existingConfig = temporaryFolder.newFile();
        nonExistingScript.delete();
        WatchdogData config = new WatchdogData(nonExistingScript.getAbsolutePath(), existingConfig.getAbsolutePath());
        config.setFoldersToWatch(testFolders);
        assertThat(folderWatchdogService.executeWatchdog(config)).isFalse();
    }

    @Test
    public void testExecuteCommandNonExistingOutputFolder() throws IOException {
        File nonExistingScript = temporaryFolder.newFile();
        File existingConfig = temporaryFolder.newFile();
        nonExistingScript.delete();
        WatchdogData config = new WatchdogData(nonExistingScript.getAbsolutePath(), existingConfig.getAbsolutePath());
        config.setFoldersToWatch(testFolders);
        assertThat(folderWatchdogService.executeWatchdog(config)).isFalse();
    }

    @Test
    public void testExecuteCommandInvalidConfigPath() throws IOException {
        File existingScript = temporaryFolder.newFile();
        WatchdogData config = new WatchdogData(existingScript.getAbsolutePath(), "");
        config.setFoldersToWatch(testFolders);
        assertThat(folderWatchdogService.executeWatchdog(config)).isFalse();
    }

    @Test
    public void testExecuteCommandInvalidScriptPath() throws IOException {
        File existingConfig = temporaryFolder.newFile();
        WatchdogData config = new WatchdogData("", existingConfig.getAbsolutePath());
        config.setFoldersToWatch(testFolders);
        assertThat(folderWatchdogService.executeWatchdog(config)).isFalse();
    }

    @Test
    public void testExecuteCommandNoFolders() throws IOException {
        File existingScript = temporaryFolder.newFile();
        File existingConfig = temporaryFolder.newFile();
        WatchdogData config = new WatchdogData(existingScript.getAbsolutePath(), existingConfig.getAbsolutePath());
        ReflectionTestUtils.setField(config, "scriptPath", existingScript.getAbsolutePath());
        assertThat(folderWatchdogService.executeWatchdog(config)).isFalse();
    }

    @Test
    public void testExecuteCommandWatchFiles() throws IOException {
        File existingScript = temporaryFolder.newFile();
        File existingConfig = temporaryFolder.newFile();
        WatchdogData config = new WatchdogData(existingScript.getAbsolutePath(), existingConfig.getAbsolutePath());
        testFolders.clear();
        testFolders.add(temporaryFolder.newFile().toPath());
        config.setFoldersToWatch(testFolders);
        ReflectionTestUtils.setField(config, "scriptPath", existingScript.getAbsolutePath());
        assertThat(folderWatchdogService.executeWatchdog(config)).isFalse();
    }

    @Test
    public void testGetScriptPath() {
        assertThat(folderWatchdogService.getWatchdogScriptPath()).isNotNull();
        assertThat(folderWatchdogService.getWatchdogScriptPath()).isNotEmpty();
    }
}
