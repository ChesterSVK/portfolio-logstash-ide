package cz.muni.ics.json.utils;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.json.JsonTestPreparator;
import cz.muni.ics.json.TestJsonClass;
import cz.muni.ics.json.exception.JsonException;
import org.junit.After;
import org.junit.Before;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

@RunWith(ZohhakRunner.class)
public class JsonMapperTest extends JsonTestPreparator {

    private TemporaryFolder temporaryFolder = new TemporaryFolder(PROJECT_TEST_DIR);

    private JsonMapper mapper = new JacksonObjectMapper();

    @Before
    public void before() throws IOException {
        temporaryFolder.create();
    }

    @After
    public void after() {
        temporaryFolder.delete();
    }

    @TestWith({
            "b, d"
    })
    public void testMapObject(String a, String c) throws JsonException, IOException {
        TestJsonClass t = new TestJsonClass(a, c);
        File f = mapper.mapToJson(t, temporaryFolder.getRoot(), "testJsonOuput.json");
        assertThat(Files.exists(f.toPath())).isTrue();
        assertThat(Files.size(f.toPath())).isGreaterThan(0);
        assertThat(Files.readAllLines(f.toPath()).size()).isEqualTo(1);
        assertThat(Files.readAllLines(f.toPath()).get(0)).isEqualTo("{\"a\":\"b\",\"c\":\"d\"}");
    }

    @TestWith({
            "b, d"
    })
    public void testMapObjectToInvalidFolder(String a, String c) {
        TestJsonClass t = new TestJsonClass(a, c);
        assertThatThrownBy(() -> mapper.mapToJson(t, temporaryFolder.newFile(), "testJsonOuput.json")).isInstanceOf(JsonException.class);
    }
}
