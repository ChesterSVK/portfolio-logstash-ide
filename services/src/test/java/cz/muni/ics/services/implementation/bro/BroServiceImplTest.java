package cz.muni.ics.services.implementation.bro;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.bro.exceptions.BroException;
import cz.muni.ics.bro.impl.BroJsonConfigCreator;
import cz.muni.ics.bro.impl.BroPcapProcessing;
import cz.muni.ics.core.exceptions.ApplicationException;
import cz.muni.ics.services.TemporaryTest;
import cz.muni.ics.services.implementations.bro.BroServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(ZohhakRunner.class)
public class BroServiceImplTest extends TemporaryTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private BroPcapProcessing pcapProcessing;

    @Mock
    private BroJsonConfigCreator broConfigCreator;

    @InjectMocks
    private BroServiceImpl broService;

    private TemporaryFolder temporaryFolder = new TemporaryFolder(PROJECT_TEST_DIR);
    private File temporaryInput1;
    private File temporaryInput2;

    private File defaultConfig;

    @Before
    public void before() throws IOException {
        temporaryFolder.create();
        temporaryInput1 = temporaryFolder.newFile("test1.pcap");
        temporaryInput2 = temporaryFolder.newFile("test2.pcap");
        defaultConfig = temporaryFolder.newFile("default_config.bro");
    }

    @After
    public void after() {
        temporaryFolder.delete();
    }

    @TestWith({
            "testCommand"
    })
    public void testGetCustomConfig(String command) throws IOException, BroException {
        when(broConfigCreator.createBroConfig(any(), any())).thenReturn(temporaryFolder.newFile().toPath());
        Path config = broService.createCustomConfig(command, temporaryFolder.getRoot().toPath());
        assertThat(config.toFile()).exists();
    }

    @Test
    public void testGetCustomConfigEmptyCommand() throws BroException {
        when(broConfigCreator.createBroConfig(any(), any())).thenThrow(BroException.class);
        assertThatThrownBy(() -> broService.createCustomConfig("", temporaryFolder.getRoot().toPath())).isInstanceOf(BroException.class);
    }

    @TestWith({
            "testCommand"
    })
    public void testGetCustomConfigNonExistingDir(String command) {
        temporaryFolder.delete();
        assertThatThrownBy(() -> broService.createCustomConfig(command, temporaryFolder.getRoot().toPath())).isInstanceOf(BroException.class);
    }

    @TestWith({
            "testCommand"
    })
    public void testGetCustomConfigInvalidDir(String command) {
        assertThatThrownBy(() -> broService.createCustomConfig(command, temporaryFolder.newFile().toPath())).isInstanceOf(BroException.class);
    }

    @Test
    public void testProcessFile() throws ApplicationException, BroException, IOException {
        when(pcapProcessing.processInputFile(any(), any(), any())).thenReturn(0);
        System.out.println(defaultConfig);
        assertThat(
                broService.broProcessPcap(
                        temporaryInput1.toPath(),
                        temporaryFolder.getRoot().toPath(),
                        defaultConfig.toPath())).isTrue();
    }


    @Test
    public void testProcessFiles() throws ApplicationException, BroException, IOException {
        List<Path> pathList = new ArrayList<>();
        pathList.add(temporaryInput1.toPath());
        pathList.add(temporaryInput2.toPath());
        when(pcapProcessing.processInputFile(any(), any(), any())).thenReturn(0);
        assertThat(broService.broProcessPcap(pathList, temporaryFolder.getRoot().toPath(), defaultConfig.toPath())).isTrue();
    }
}
