package cz.muni.ics.services.implementation.elasticsearch;

import cz.muni.ics.elasticsearch.dataclasses.LogstashCommandData;
import cz.muni.ics.elasticsearch.exceptions.ValidationException;
import cz.muni.ics.services.ServiceApp;
import cz.muni.ics.services.implementations.web.dto.LogstashCommandDataDto;
import cz.muni.ics.services.interfaces.elasticsearch.LogstashCommandDataService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        LogstashCommandDataServiceTest.class})
@TestPropertySource("classpath:application-elasticsearch-test.properties")
@SpringBootTest(classes = ServiceApp.class)
public class LogstashCommandDataServiceTest extends AbstractTestExecutionListener {

    private static final LogstashCommandDataDto ORIGINAL_DATA = new LogstashCommandDataDto(Arrays.asList("test"));
    private static final LogstashCommandDataDto TEST_DATA = new LogstashCommandDataDto();

    @Autowired
    private LogstashCommandDataService logstashCommandDataService;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public void beforeTestClass(TestContext testContext) {
        this.esTemplate = testContext.getApplicationContext().getBean(ElasticsearchTemplate.class);
        esTemplate.deleteIndex(LogstashCommandData.class);
        esTemplate.createIndex(LogstashCommandData.class);
        esTemplate.putMapping(LogstashCommandData.class);
        esTemplate.refresh(LogstashCommandData.class);
    }

    @Before
    public void before(){
        logstashCommandDataService.deleteAllLogstashCommandData();

        TEST_DATA.setId(ORIGINAL_DATA.getId());
        TEST_DATA.setLogstashRootCommands(ORIGINAL_DATA.getLogstashRootCommands());
    }

    @After
    public void resetData(){
        logstashCommandDataService.deleteAllLogstashCommandData();

        TEST_DATA.setId(ORIGINAL_DATA.getId());
        TEST_DATA.setLogstashRootCommands(ORIGINAL_DATA.getLogstashRootCommands());
    }

    @Override
    public void afterTestClass(TestContext testContext) {
        esTemplate.deleteIndex(LogstashCommandData.class);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Save

    @Test
    public void testSaveLogstashCommandData() {
        LogstashCommandData data = logstashCommandDataService.saveLogstashCommandData(TEST_DATA);

        assertThat(data).isNotNull();
        assertThat(data.getId()).isNotNull();
//        assertThat(data.getId()).isEqualTo(TEST_DATA.getId());
        assertThat(data.getLogstashRootCommands().size()).isEqualTo(TEST_DATA.getLogstashRootCommands().size());
        assertThat(logstashCommandDataService.findAllLogstashCommandData().iterator().hasNext()).isTrue();
    }

    @Test
    public void testSaveEmptyLogstashCommandData() {
        TEST_DATA.setLogstashRootCommands(new ArrayList<>());
        assertThatThrownBy(() -> logstashCommandDataService.saveLogstashCommandData(TEST_DATA)).isInstanceOf(ValidationException.class);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Find

    @Test
    public void testFindLogstashCommandDataById() {
        LogstashCommandData data = logstashCommandDataService.saveLogstashCommandData(TEST_DATA);
        TEST_DATA.setId(data.getId());
        LogstashCommandData foundData = logstashCommandDataService.findLogstashDataById(TEST_DATA);
        assertThat(foundData).isNotNull();
        assertThat(foundData.getId()).isNotNull();
//        assertThat(foundData.getId()).isEqualTo(TEST_DATA.getId());
        assertThat(foundData.getLogstashRootCommands().size()).isEqualTo(TEST_DATA.getLogstashRootCommands().size());
    }

    @Test
    public void testFindAllLogstashCommandDataById() {
        LogstashCommandData data = logstashCommandDataService.saveLogstashCommandData(TEST_DATA);
        Iterable<LogstashCommandData> dataIterable = logstashCommandDataService.findAllLogstashCommandData();
        assertThat(dataIterable.iterator().hasNext()).isTrue();
        assertThat(dataIterable.iterator().next()).isEqualTo(data);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Delete

    @Test
    public void testDeleteLogstashCommandData() {
        LogstashCommandData data = logstashCommandDataService.saveLogstashCommandData(TEST_DATA);
        logstashCommandDataService.deleteLogstashCommandData(data.getId());
        Iterable<LogstashCommandData> allLogstashCommandData = logstashCommandDataService.findAllLogstashCommandData();
        assertThat(logstashCommandDataService.findAllLogstashCommandData().iterator().hasNext()).isFalse();
    }

}
