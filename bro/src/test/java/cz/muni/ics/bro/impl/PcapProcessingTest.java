package cz.muni.ics.bro.impl;

import cz.muni.ics.bro.TemporaryTest;
import cz.muni.ics.bro.exceptions.BroException;
import cz.muni.ics.bro.utils.BroExecutor;
import cz.muni.ics.core.exceptions.ApplicationException;
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
import java.nio.file.Paths;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PcapProcessingTest extends TemporaryTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BroExecutor broExecutor;

    @InjectMocks
    private BroPcapProcessing broProcessing;

    private TemporaryFolder temporaryFolder = new TemporaryFolder(PROJECT_TEST_DIR);
    private File temporaryInput;
    private File temporaryConfig;

    @Before
    public void before() throws IOException {
        temporaryFolder.create();
        temporaryInput = temporaryFolder.newFile("test.pcap");
        temporaryConfig = temporaryFolder.newFile();
    }

    @After
    public void after() {
        temporaryFolder.delete();
    }

    @Test
    public void testBroProcess() throws IOException, ApplicationException, BroException {
        when(broExecutor.executeCommand(any())).thenReturn(0);
        when(broExecutor.getBroBinPath()).thenReturn("bin");
        assertThat(broProcessing.processInputFile(temporaryInput.toPath(), temporaryFolder.getRoot().toPath(), temporaryConfig.toPath())).isEqualTo(0);
    }

    @Test
    public void testBroProcessNonExistingInput() {
        temporaryInput.delete();
        assertThatThrownBy(() -> broProcessing.processInputFile(temporaryInput.toPath(), temporaryFolder.getRoot().toPath(), temporaryConfig.toPath())).isInstanceOf(BroException.class);
    }

    @Test
    public void testBroProcessWrongExtension() throws IOException {
        temporaryInput.delete();
        temporaryInput = temporaryFolder.newFile("test.txt");
        assertThatThrownBy(() -> broProcessing.processInputFile(temporaryInput.toPath(), temporaryFolder.getRoot().toPath(), temporaryConfig.toPath())).isInstanceOf(BroException.class);
    }

    @Test
    public void testBroProcessHidden() {
        File file = new File(Paths.get(temporaryFolder.toString()).toFile(), ".hidden");
        assertThatThrownBy(() -> broProcessing.processInputFile(file.toPath(), temporaryFolder.getRoot().toPath(), temporaryConfig.toPath())).isInstanceOf(BroException.class);
    }

    @Test
    public void testBroProcessNonExistingParent() {
        temporaryFolder.delete();
        assertThatThrownBy(() -> broProcessing.processInputFile(temporaryInput.toPath(), temporaryFolder.getRoot().toPath(), temporaryConfig.toPath())).isInstanceOf(BroException.class);
    }

    @Test
    public void testBroProcessNonExistingConfig() {
        temporaryConfig.delete();
        assertThatThrownBy(() -> broProcessing.processInputFile(temporaryInput.toPath(), temporaryFolder.getRoot().toPath(), temporaryConfig.toPath())).isInstanceOf(BroException.class);
    }

    @Test
    public void testBroProcessNullExistingConfig() {
        assertThatThrownBy(() -> broProcessing.processInputFile(temporaryInput.toPath(), temporaryFolder.getRoot().toPath(), null)).isInstanceOf(BroException.class);
    }

    @Test
    public void testBroProcessInvalidParent() {
        assertThatThrownBy(() -> broProcessing.processInputFile(temporaryInput.toPath(), temporaryFolder.newFile().toPath(), temporaryConfig.toPath())).isInstanceOf(BroException.class);
    }
}
