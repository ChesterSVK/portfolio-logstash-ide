package cz.muni.ics.elasticsearch.acutate;

import cz.muni.ics.elasticsearch.ElasticsearchApplication;
import cz.muni.ics.elasticsearch.implementations.ElasticsearchHealthCheckService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ElasticsearchApplication.class)
public class HealthCheckTest {

    @Autowired
    ElasticsearchHealthCheckService service;

    @Test
    public void checkElasticsearchStatusTest() {
        Health h = service.checkElasticsearchHealth();
        assertThat(h).isNotNull();
        assertThat(h.getDetails()).isNotEmpty();
        assertThat(h.getStatus().getCode()).isEqualTo(Status.UP.toString());
    }
}
