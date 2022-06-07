package cz.muni.ics.core.os;

import cz.muni.ics.core.TemporaryTest;
import cz.muni.ics.core.dataclasses.ExecutorData;
import cz.muni.ics.core.validators.ExecutorDataValidator;
import org.apache.commons.exec.DefaultExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SystemCommanderComponentTest extends TemporaryTest {

    private ExecutorData testData = new ExecutorData("testCommnad");
    private TemporaryFolder temporaryFolder = new TemporaryFolder(PROJECT_TEST_DIR);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private DefaultExecutor executor;

    @Mock
    private ExecutorDataValidator validator;

    @InjectMocks
    private SystemCommanderComponent commander;

    @Before
    public void before() throws IOException {
        testData = new ExecutorData("testCommand");
        temporaryFolder.create();
    }

    @After
    public void after() {
        temporaryFolder.delete();
    }

    @Test
    public void testExecuteCommandString() throws IOException {
        when(executor.execute(any())).thenReturn(0);
        assertThat(commander.executeWithOutput(testData))
                .isEmpty();
    }

    @Test
    public void testExecuteCommandStringWithInput() throws IOException {
        when(executor.execute(any())).thenReturn(0);
        testData.setInput("input");
        assertThat(commander.executeWithOutput(testData))
                .isEmpty();
    }

    @Test
    public void testExecuteCommandStringInvalidWorkingDir() throws IOException {
        when(executor.execute(any())).thenReturn(0);
        testData.setWorkingDirectory(new File("invalid"));
        assertThat(commander.executeWithOutput(testData))
                .isEmpty();
    }

    @Test
    public void testExecuteCommandInt() throws IOException {
        when(executor.execute(any())).thenReturn(0);
        assertThat(commander.executeWithExitCode(testData))
                .isEqualTo(0);
    }

    @Test
    public void testExecuteCommandIntWithInput() throws IOException {
        when(executor.execute(any())).thenReturn(0);
        testData.setInput("input");
        assertThat(commander.executeWithExitCode(testData))
                .isEqualTo(0);
    }

    @Test
    public void testExecuteCommandIntInvalidWorkingDir() throws IOException {
        when(executor.execute(any())).thenReturn(0);
        testData.setWorkingDirectory(new File("invalid"));
        assertThat(commander.executeWithExitCode(testData))
                .isEqualTo(0);
    }
}