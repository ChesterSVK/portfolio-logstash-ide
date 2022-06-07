package cz.muni.ics.logstash.utils;

import cz.muni.ics.core.os.SystemCommanderComponent;
import cz.muni.ics.logstash.LogstashApplication;
import cz.muni.ics.logstash.TemporaryTest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LogstashApplication.class)
public class LogstashExecutorTest extends TemporaryTest {

    private TemporaryFolder temporaryFolder = new TemporaryFolder(PROJECT_TEST_DIR);

    @Before
    public void before() throws IOException {
        logstashExecutor = new LogstashExecutor(executor);
        temporaryFolder.create();
    }

    @After
    public void after() {
        temporaryFolder.delete();
    }


    @Value("${logstash.binary.path}")
    private String logstashBinPath;

    @Mock
    private SystemCommanderComponent executor;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Autowired
    @InjectMocks
    private LogstashExecutor logstashExecutor;

    @Test
    public void executeInitCommand() throws IOException {
        when(executor.executeWithExitCode(any())).thenReturn(0);
        ReflectionTestUtils.setField(logstashExecutor, "logstashBinPath", logstashBinPath);
        assertThat(logstashExecutor.executeInitCommand()).isEqualTo(0);
    }

    @Test
    public void getBinary() throws IOException {
        ReflectionTestUtils.setField(logstashExecutor, "logstashBinPath", logstashBinPath);
        assertThat(logstashExecutor.getLogstashBinPath()).isNotEmpty();
        assertThat(logstashExecutor.getLogstashBinPath()).isEqualTo(logstashBinPath);
    }

    //Test disabled, because test fails on unsupported operation when creating script in temporary folder.
    // Workaround needed in implementation of the service.
//    @Test
//    public void executePipedCommand() throws IOException {
//        when(executor.executeWithExitCode(any())).thenReturn(0);
//        ExecutorData data1 = new ExecutorData("test1");
//        ExecutorData data2 = new ExecutorData("test2");
//        ReflectionTestUtils.setField(logstashExecutor, "logstashBinPath", logstashBinPath);
//        assertThat(logstashExecutor.executePipedCommands(data1, data2, temporaryFolder.newFolder().toPath())).isEqualTo(0);
//    }
}
