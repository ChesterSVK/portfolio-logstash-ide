package cz.muni.ics.core;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class TemporaryTest {

    protected static final File PROJECT_TEST_DIR =
            new File(System.getProperty("user.dir") + File.separatorChar + "testOutput");

    @BeforeClass
    public static void prepare() throws IOException {
        if (!PROJECT_TEST_DIR.exists()) {
            Files.createDirectory(PROJECT_TEST_DIR.toPath());
        }
    }

    @AfterClass
    public static void clean() throws IOException {
        Files.delete(PROJECT_TEST_DIR.toPath());
    }
}