package cz.muni.ics.json.utils;

import cz.muni.ics.json.JsonTestPreparator;
import cz.muni.ics.json.TestJsonClass;
import cz.muni.ics.json.exception.JsonException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

public class JsonParserTest extends JsonTestPreparator {

    private final static String SAMPLE_JSON_PATH = "test/sample/testInputJson.json";
    private final static String SAMPLE_INVALID_JSON_PATH = "test/sample/invalid.txt";

    private JsonParser parser = new JacksonObjectParser();
    private ClassLoader classLoader = getClass().getClassLoader();
    private TemporaryFolder temporaryFolder = new TemporaryFolder(PROJECT_TEST_DIR);

    private final File INPUT_FILE = new File(classLoader.getResource(SAMPLE_JSON_PATH).toURI().normalize().getPath());
    private final File INVALID_INPUT_FILE = new File(classLoader.getResource(SAMPLE_INVALID_JSON_PATH).toURI().normalize().getPath());

    public JsonParserTest() throws URISyntaxException {
    }

    @Before
    public void before() throws IOException {
        temporaryFolder.create();
    }

    @After
    public void after() {
        temporaryFolder.delete();
    }

    @Test
    public void testParseJson() throws JsonException {
        TestJsonClass t = parser.parseFrom(INPUT_FILE, TestJsonClass.class);
        assertThat(t.getA()).isNotNull();
        assertThat(t.getA()).isNotEmpty();
        assertThat(t.getA()).isEqualTo("b");
        assertThat(t.getC()).isNotNull();
        assertThat(t.getC()).isNotEmpty();
        assertThat(t.getC()).isEqualTo("d");
    }

    @Test
    public void testParseInvalidJson() {
        assertThatThrownBy(() -> parser.parseFrom(INVALID_INPUT_FILE, TestJsonClass.class)).isInstanceOf(JsonException.class);
    }
}
