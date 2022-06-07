package cz.muni.ics.watchdog.unit;

import cz.muni.ics.watchdog.WatchdogApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WatchdogApp.class)
public class WatchdogAppTest implements UnitTest {

    @Test
    public void test(){ }
}
