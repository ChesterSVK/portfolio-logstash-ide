package cz.muni.ics.watchdog.integration;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.file.Paths;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class WatchdogIT {

    @Value("${copas.watchdog.script.path}")
    private String watchdogScriptPath;

    @Test
    public void testLogstashBinary() throws IOException {
        CommandLine commandLine = CommandLine.parse(Paths.get(watchdogScriptPath).normalize().toString() + " -h");
        DefaultExecutor executor = new DefaultExecutor();
        assertThat(executor.execute(commandLine)).isEqualTo(1);
    }
}
