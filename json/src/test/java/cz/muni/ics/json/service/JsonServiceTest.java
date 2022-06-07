package cz.muni.ics.json.service;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import cz.muni.ics.json.JsonTestPreparator;
import cz.muni.ics.json.TestJsonClass;
import cz.muni.ics.json.exception.JsonException;
import cz.muni.ics.json.utils.JsonMapper;
import cz.muni.ics.json.utils.JsonParser;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(ZohhakRunner.class)
public class JsonServiceTest extends JsonTestPreparator {

    private final static String SAMPLE_JSON_PATH = "test/sample/testInputJson.json";
    private final static String SAMPLE_INVALID_JSON_PATH = "test/sample/invalid.txt";

    private ClassLoader classLoader = getClass().getClassLoader();

    private final File INPUT_FILE = new File(classLoader.getResource(SAMPLE_JSON_PATH).toURI().normalize().getPath());
    private final File INVALID_INPUT_FILE = new File(classLoader.getResource(SAMPLE_INVALID_JSON_PATH).toURI().normalize().getPath());

    private TemporaryFolder temporaryFolder = new TemporaryFolder(PROJECT_TEST_DIR);


    private JsonService jsonService;


    public JsonServiceTest() throws URISyntaxException {
    }

    @Mock
    private JsonParser parser;

    @Mock
    private JsonMapper mapper;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void before() throws IOException {
        temporaryFolder.create();
        jsonService = new JsonServiceImpl(parser, mapper);
    }

    @After
    public void after() {
        temporaryFolder.delete();
    }

    @Test
    public void testServiceMappingCreateDefaultConstructor() {
        jsonService = new JsonServiceImpl();
        assertThat(jsonService).isNotNull();
    }


    @TestWith({
            "b, d, filename"
    })
    public void testServiceMapping(String a, String c, String fileName) throws JsonException {
        TestJsonClass t = new TestJsonClass(a, c);
        when(mapper.mapToJson(any(), any(), anyString())).thenReturn(INPUT_FILE);
        assertThat(jsonService.mapJsonObject(t, temporaryFolder.getRoot(), fileName)).isNotNull();
    }

    @TestWith({
            "b, d"
    })
    public void testServiceMappingBadFilename(String a, String c) throws JsonException {
        TestJsonClass t = new TestJsonClass(a, c);
        when(mapper.mapToJson(any(), any(), anyString())).thenReturn(INPUT_FILE);
        File res = jsonService.mapJsonObject(t, temporaryFolder.getRoot(), "");
        assertThat(res).isNotNull();
        assertThat(res.getName()).isNotNull();
        assertThat(res.getName()).isNotEmpty();
        assertThat(FilenameUtils.getExtension(res.getAbsolutePath())).endsWith("json");
    }

    @TestWith({
            "b, d, filename"
    })
    public void testServiceMappingErrorWhenParsing(String a, String c, String fileName) throws JsonException {
        TestJsonClass t = new TestJsonClass(a, c);
        when(mapper.mapToJson(any(), any(), any())).thenThrow(new JsonException("Test Message"));
        assertThatThrownBy(() -> jsonService.mapJsonObject(t, temporaryFolder.getRoot(), fileName)).isInstanceOf(JsonException.class);
    }

    @TestWith({
            "filename"
    })
    public void testServiceMappingNullObject(String fileName) throws JsonException {
        when(mapper.mapToJson(any(), any(), anyString())).thenThrow(new JsonException("Test Message"));
        assertThatThrownBy(() -> jsonService.mapJsonObject(null, temporaryFolder.getRoot(), fileName)).isInstanceOf(JsonException.class);
    }

    @TestWith({
            "b, d, filename"
    })
    public void testServiceMappingInvalidFolder(String a, String c, String fileName) throws JsonException {
        TestJsonClass t = new TestJsonClass(a, c);
        when(mapper.mapToJson(any(), any(), anyString())).thenThrow(new JsonException("Test Message"));
        assertThatThrownBy(() -> jsonService.mapJsonObject(t, temporaryFolder.newFile(), fileName)).isInstanceOf(JsonException.class);
    }

    @TestWith({
            "b, d, filename"
    })
    public void testServiceMappingNonExistingFolder(String a, String c, String fileName) throws JsonException {
        TestJsonClass t = new TestJsonClass(a, c);
        when(mapper.mapToJson(any(), any(), anyString())).thenThrow(new JsonException("Test Message"));
        temporaryFolder.delete();
        assertThatThrownBy(() -> jsonService.mapJsonObject(t, temporaryFolder.getRoot(), fileName)).isInstanceOf(JsonException.class);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Parsing
    @TestWith({
            "b, d"
    })
    public void testServiceParsing(String a, String c) throws JsonException {
        TestJsonClass t = new TestJsonClass(a, c);
        when(parser.parseFrom(any(), any())).thenReturn(t);
        assertThat(jsonService.parseJsonFile(INPUT_FILE, TestJsonClass.class)).isNotNull();
    }

    @TestWith({
            "b, d"
    })
    public void testServiceParsingNonExistingFile(String a, String c) throws JsonException {
        TestJsonClass t = new TestJsonClass(a, c);
        when(parser.parseFrom(any(), any())).thenReturn(t);
        assertThatThrownBy(() -> jsonService.parseJsonFile(new File("noFile"), TestJsonClass.class)).isInstanceOf(JsonException.class);
    }

    @TestWith({
            "b, d"
    })
    public void testServiceParsingInvalidFile(String a, String c) throws JsonException {
        TestJsonClass t = new TestJsonClass(a, c);
        when(parser.parseFrom(any(), any())).thenReturn(t);
        assertThatThrownBy(() -> jsonService.parseJsonFile(temporaryFolder.newFolder(), TestJsonClass.class)).isInstanceOf(JsonException.class);
    }

    @TestWith({
            "b, d"
    })
    public void testServiceParsingHiddenFile(String a, String c) throws IOException, JsonException {
        TestJsonClass t = new TestJsonClass(a, c);
        File f = temporaryFolder.newFile();
        Files.setAttribute(f.toPath(), "dos:hidden", true);
        assertThatThrownBy(() -> jsonService.parseJsonFile(f, TestJsonClass.class)).isInstanceOf(JsonException.class);
    }

}
