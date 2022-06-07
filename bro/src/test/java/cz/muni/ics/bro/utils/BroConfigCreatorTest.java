package cz.muni.ics.bro.utils;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.bro.TemporaryTest;
import cz.muni.ics.bro.exceptions.BroException;
import cz.muni.ics.bro.impl.BroJsonConfigCreator;
import org.junit.After;
import org.junit.Before;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

@RunWith(ZohhakRunner.class)
public class BroConfigCreatorTest extends TemporaryTest {

    private TemporaryFolder temporaryFolder = new TemporaryFolder(PROJECT_TEST_DIR);
    private BroJsonConfigCreator configCreator = new BroJsonConfigCreator();

    @Before
    public void before() throws IOException {
        temporaryFolder.create();
    }

    @After
    public void after() {
        temporaryFolder.delete();
    }

    @TestWith({
            "testCommand"
    })
    public void testCreateConfig(String command) throws IOException, BroException {
        Path config = configCreator.createBroConfig(command, temporaryFolder.getRoot().toPath());
        assertThat(config.toFile()).exists();
        assertThat(Files.readAllLines(config).size()).isEqualTo(1);
        assertThat(Files.readAllLines(config).get(0)).isEqualTo(command);
    }

    @TestWith({
            "testCommand"
    })
    public void testCreateConfigNonExistingParent(String command) {
        temporaryFolder.delete();
        assertThatThrownBy(() -> configCreator.createBroConfig(command, temporaryFolder.getRoot().toPath())).isInstanceOf(BroException.class);
    }

    @TestWith({
            ""
    })
    public void testCreateConfigEmptyCommand(String command) {
        assertThatThrownBy(() -> configCreator.createBroConfig(command, temporaryFolder.getRoot().toPath())).isInstanceOf(BroException.class);
    }
}
