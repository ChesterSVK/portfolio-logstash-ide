package cz.muni.ics.services.implementation.elasticsearch;

import cz.muni.ics.elasticsearch.dataclasses.ApplicationData;
import cz.muni.ics.elasticsearch.dataclasses.LogstashCommandData;
import cz.muni.ics.elasticsearch.exceptions.ValidationException;
import cz.muni.ics.elasticsearch.utils.LocalDateTimeFormatterUtil;
import cz.muni.ics.services.ServiceApp;
import cz.muni.ics.services.enums.ConvertType;
import cz.muni.ics.services.implementations.web.dto.ApplicationDataDto;
import cz.muni.ics.services.implementations.elasticsearch.ApplicationDataServiceImpl;
import cz.muni.ics.services.interfaces.elasticsearch.ApplicationDataService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.time.LocalDateTime;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

@RunWith(SpringRunner.class)
@TestExecutionListeners(listeners = {
        DependencyInjectionTestExecutionListener.class,
        ApplicationDataServiceTest.class})
@SpringBootTest(classes = ServiceApp.class)
@TestPropertySource("classpath:application-elasticsearch-test.properties")
public class ApplicationDataServiceTest extends AbstractTestExecutionListener {

    private static final String BEFORE_NOW = LocalDateTimeFormatterUtil.getNormalizedDate(LocalDateTime.now().minusHours(1));
    private static final String NOW = LocalDateTimeFormatterUtil.getNormalizedDate(LocalDateTime.now());
    private static final ApplicationDataDto ORIGINAL_APP_DATA =
            new ApplicationDataDto("testCommandId", ConvertType.JSON, NOW);
    private static final ApplicationDataDto TEST_APP_DATA =
            new ApplicationDataDto("testCommandId", ConvertType.JSON, NOW);

    @Autowired
    private ApplicationDataServiceImpl applicationDataService;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public void beforeTestClass(TestContext testContext) {
        this.esTemplate = testContext.getApplicationContext().getBean(ElasticsearchTemplate.class);
        esTemplate.deleteIndex(ApplicationData.class);
        esTemplate.createIndex(ApplicationData.class);
        esTemplate.putMapping(ApplicationData.class);
        esTemplate.refresh(ApplicationData.class);
    }

    @Before
    public void before() {
        applicationDataService.deleteAllApplicationData();
    }

    @After
    public void resetData() {
        TEST_APP_DATA.setId(ORIGINAL_APP_DATA.getId());
        TEST_APP_DATA.setFigureNote(ORIGINAL_APP_DATA.getFigureNote());
        TEST_APP_DATA.setLogstashCommandId(ORIGINAL_APP_DATA.getLogstashCommandId());
        TEST_APP_DATA.setType(ORIGINAL_APP_DATA.getType());
    }

    @Override
    public void afterTestClass(TestContext testContext) {
        esTemplate.deleteIndex(ApplicationData.class);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Save
    @Test
    public void testSaveApplicationData() {
        ApplicationData savedAD = applicationDataService.saveApplicationData(TEST_APP_DATA);
        TEST_APP_DATA.setDateCreated(savedAD.getDateCreated());
        assertThat(savedAD.getId()).isNotNull();
        assertThat(savedAD.getFigureNote()).isEqualTo(TEST_APP_DATA.getFigureNote());
        assertThat(savedAD.getDateCreated()).isEqualTo(TEST_APP_DATA.getDateCreated());
        assertThat(savedAD.getLogstashCommandId()).isEqualTo(TEST_APP_DATA.getLogstashCommandId());
        assertThat(savedAD.getType()).isEqualTo(TEST_APP_DATA.getType().name().toLowerCase());
    }


    @Test
    public void testSaveInvalidApplicationDataLogstashCommandId() {
        ApplicationDataDto customAD = new ApplicationDataDto("", ConvertType.JSON, "test");
        assertThatThrownBy(() -> applicationDataService.saveApplicationData(customAD))
                .isInstanceOf(ValidationException.class);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Find
    @Test
    public void testFindApplicationDataById() {
        ApplicationData savedWf = applicationDataService.saveApplicationData(TEST_APP_DATA);

        assertThat(applicationDataService.findApplicationDataById(savedWf.getId()))
                .isEqualToComparingFieldByField(savedWf);

    }


    @Test
    public void testFindApplicationDataByLogstashCommandId() {
        ApplicationData savedWf = applicationDataService.saveApplicationData(TEST_APP_DATA);

        assertThat(applicationDataService.findApplicationDataByLogstashCommandId(savedWf.getLogstashCommandId()).getTotalElements()).isEqualTo(1);

    }

//    @Test
//    public void testFindApplicationDataByDateCreated(){
//        applicationDataService.saveApplicationData(TEST_APP_DATA);
//        assertThat(applicationDataService
//                .findApplicationDataByDateCreated(LocalDateTimeFormatterUtil.parseNormalizedDate(NOW)).getTotalElements()).isEqualTo(1);
//
//    }

    @Test
    public void testFindApplicationDataBeforeDateCreated() {
        applicationDataService.saveApplicationData(TEST_APP_DATA);
        assertThat(applicationDataService
                .findApplicationDataByDateCreatedBefore(LocalDateTimeFormatterUtil.parseNormalizedDate(NOW)).getTotalElements()).isEqualTo(1);

    }

    @Test
    public void testFindApplicationDataByAfterCreated() {
        applicationDataService.saveApplicationData(TEST_APP_DATA);
        assertThat(applicationDataService
                .findApplicationDataByDateCreatedAfter(LocalDateTimeFormatterUtil.parseNormalizedDate(BEFORE_NOW)).getTotalElements()).isEqualTo(1);

    }

    @Test
    public void testFindAllWorkingFigures() {
        ApplicationData applicationData = applicationDataService.saveApplicationData(TEST_APP_DATA);
        TEST_APP_DATA.setId(applicationData.getId());
        TEST_APP_DATA.setDateCreated(applicationData.getDateCreated());
        assertThat(applicationDataService.findAllApplicationData().iterator().hasNext()).isTrue();
    }

    @Test
    public void testFindApplicationDataByInvalidId() {
        assertThatThrownBy(() -> applicationDataService.findApplicationDataById("")).isInstanceOf(ValidationException.class);
        assertThatThrownBy(() -> applicationDataService.findApplicationDataById(null)).isInstanceOf(ValidationException.class);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Delete
    @Test
    public void testDeleteApplicationData() {
        TEST_APP_DATA.setId("1");
        ApplicationData savedWf = applicationDataService.saveApplicationData(TEST_APP_DATA);
        applicationDataService.deleteApplicationData(TEST_APP_DATA);

        assertThat(applicationDataService.findApplicationDataById(savedWf.getId())).isNull();
    }
}