package cz.muni.ics.filesystem;

/*-
 * #%L
 * Neck-Application
 * %%
 * Copyright (C) 2018 Masaryk University, Faculty of Informatics
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fabriceci.fmc.error.FileManagerException;
import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import org.apache.commons.io.monitor.FileEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;


/**
 * @author Jozef Cibík
 */
@RunWith(ZohhakRunner.class)
public class FileUtilitiesTest extends FileManagerTestPreparator {

    private TemporaryFolder temporaryFolder = new TemporaryFolder(PROJECT_TEST_DIR);

    @Before
    public void before() throws IOException {
        temporaryFolder.create();
    }

    @After
    public void after() {
        temporaryFolder.delete();
    }

    @Test
    public void testCheckFilePath() throws IOException, FileManagerException {
        File f = temporaryFolder.newFile();
        FileUtilities.checkPath(f, null);
        FileUtilities.checkPath(f, false);
    }

    @TestWith({
            "content, filename, false"
    })
    public void testCreateFileWithContent(String content, String fileName, boolean timestamp) throws IOException {
        Path f = FileUtilities.createFile(content, fileName, temporaryFolder.getRoot().toPath(), timestamp);
        assertThat(f.toFile().exists()).isTrue();
        assertThat(f.toFile().getName()).isEqualTo(fileName);
        assertThat(Files.readAllLines(f).size()).isEqualTo(1);
        assertThat(Files.readAllLines(f).get(0)).isEqualTo(content);
    }

    @TestWith({
            "content, filename, true"
    })
    public void testCreateFileWithContentAndTimestamp(String content, String fileName, boolean timestamp) throws IOException {
        Path f = FileUtilities.createFile(content, fileName, temporaryFolder.getRoot().toPath(), timestamp);
        assertThat(f.toFile().exists()).isTrue();
        assertThat(f.toFile().getName()).startsWith(fileName);
        assertThat(Files.readAllLines(f).size()).isEqualTo(1);
        assertThat(Files.readAllLines(f).get(0)).isEqualTo(content);
    }

    @Test
    public void testInvalidRenameFile() throws IOException, FileManagerException {
        File f = temporaryFolder.newFile();
        assertThatThrownBy( () -> FileUtilities.renameFile(f, "")).isInstanceOf(FileManagerException.class);
    }

    @Test
    public void testRenameNonExistingFile() throws IOException, FileManagerException {
        File f = temporaryFolder.newFile();
        f.delete();
        assertThatThrownBy( () -> FileUtilities.renameFile(f, "test.tmp")).isInstanceOf(FileManagerException.class);
    }

    @Test
    public void testCheckNonExistingFilePath() throws IOException, FileManagerException {
        File f = temporaryFolder.newFile();
        f.delete();
        assertThatThrownBy( () -> FileUtilities.checkPath(f, null)).isInstanceOf(FileManagerException.class);
    }

    @Test
    public void testCheckFileName() throws IOException, FileManagerException {
        assertThatThrownBy( () -> FileUtilities.checkPath(new File(""), null)).isInstanceOf(FileManagerException.class);
    }

    @Test
    public void testCheckFolderPath() throws IOException, FileManagerException {
        File f = temporaryFolder.newFolder();
        FileUtilities.checkPath(f, true);
    }

    @Test
    public void testCheckExistingFolderPathAsFile() throws IOException {
        File f = temporaryFolder.newFolder();
        assertThatThrownBy(() -> FileUtilities.checkPath(f, false)).isInstanceOf(FileManagerException.class);
    }

    @Test
    public void testCheckExistingFilePathAsFolder() throws IOException {
        File f = temporaryFolder.newFile();
        assertThatThrownBy(() -> FileUtilities.checkPath(f, true)).isInstanceOf(FileManagerException.class);
    }

    @Test
    public void testCheckWritePermissions() throws IOException {
        File f = temporaryFolder.newFile();
        if (f.setReadOnly()){
            assertThatThrownBy(() -> FileUtilities.checkWritePermission(f)).isInstanceOf(FileManagerException.class);
        }
    }
}
