package cz.muni.ics.bro.utils;

import cz.muni.ics.bro.TemporaryTest;
import cz.muni.ics.core.dataclasses.ExecutorData;
import cz.muni.ics.core.exceptions.ApplicationException;
import cz.muni.ics.core.os.SystemCommandExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BroExecutorTest extends TemporaryTest {

    private TemporaryFolder temporaryFolder = new TemporaryFolder();
    private ExecutorData testData = new ExecutorData("test");

    @Value("${bro.binary.path}")
    private String broBinPath;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private SystemCommandExecutor executor;

    @InjectMocks
    private BroExecutor broExecutor;

    @Before
    public void before() throws IOException {
        temporaryFolder.create();
    }

    @After
    public void after() {
        temporaryFolder.delete();
    }

    @Test
    public void testExecuteCommand() throws IOException, ApplicationException {
        when(executor.executeWithExitCode(any())).thenReturn(0);
        ReflectionTestUtils.setField(broExecutor, "broBinPath", "test");
        assertThat(broExecutor.executeCommand(testData)).isEqualTo(0);
    }

    @Test
    public void testExecuteCommandInInvalidWorkingDir() throws IOException, ApplicationException {
        when(executor.executeWithExitCode(any())).thenReturn(0);
        ReflectionTestUtils.setField(broExecutor, "broBinPath", "test");
        testData.setWorkingDirectory(new File("./invalid"));
        assertThat(broExecutor.executeCommand(testData)).isEqualTo(0);
    }

    @Test
    public void testExecuteCommandError() throws IOException {
        when(executor.executeWithExitCode(any())).thenThrow(IOException.class);
        ReflectionTestUtils.setField(broExecutor, "broBinPath", "test");
        assertThatThrownBy(() -> broExecutor.executeCommand(testData)).isInstanceOf(IOException.class);
    }

    @Test
    public void testExecuteInvalidBroCommand() {
        ReflectionTestUtils.setField(broExecutor, "broBinPath", "different");
        assertThatThrownBy(() -> broExecutor.executeCommand(testData)).isInstanceOf(ApplicationException.class);
    }

    @Test
    public void testGetBinary() {
        ReflectionTestUtils.setField(broExecutor, "broBinPath", "test");
        assertThat(broExecutor.getBroBinPath()).isEqualTo("test");
    }
}
