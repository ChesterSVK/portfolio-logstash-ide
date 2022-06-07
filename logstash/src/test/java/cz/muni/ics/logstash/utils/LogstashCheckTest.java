package cz.muni.ics.logstash.utils;

import cz.muni.ics.logstash.LogstashApplication;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LogstashApplication.class)
public class LogstashCheckTest {

    @Value("${logstash.binary.path}")
    private String logstashBinPath;

    @Test
    public void testLogstashBinary() throws IOException {
        CommandLine commandLine = CommandLine.parse(Paths.get(logstashBinPath).normalize().toString() + " --help");
        DefaultExecutor executor = new DefaultExecutor();
        assertThat(executor.execute(commandLine)).isEqualTo(0);
    }
}
