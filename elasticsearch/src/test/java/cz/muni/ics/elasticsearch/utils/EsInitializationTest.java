package cz.muni.ics.elasticsearch.utils;

import cz.muni.ics.elasticsearch.ElasticsearchApplication;
import cz.muni.ics.elasticsearch.exceptions.ElasticsearchInitializationException;
import cz.muni.ics.elasticsearch.interfaces.ElasticsearchHealthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class)
public class EsInitializationTest {

    @Mock
    private ElasticsearchHealthService healthService;

    @InjectMocks
    private InitializationService service;

    @Test
    public void checkElasticsearchInitTest() throws ElasticsearchInitializationException {
        Health.Builder h = Health.up();
        when(healthService.checkElasticsearchHealth()).thenReturn(h.build());
        service.initialize();
    }

    @Test
    public void checkElasticsearchBadHealthTest() {
        Health.Builder h = Health.down();
        when(healthService.checkElasticsearchHealth()).thenReturn(h.build());
        assertThatThrownBy(() -> service.initialize()).isInstanceOf(ElasticsearchInitializationException.class);
    }
}
