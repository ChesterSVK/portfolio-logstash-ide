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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;


/**
 * @author Jozef Cibík
 */
public class FolderUtilitiesTest extends FileManagerTestPreparator {

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
    public void testCreateDirectory() throws FileManagerException {
        Path resFile = FolderUtilities.createFolder("testFolder", PROJECT_TEST_DIR.toPath());
        assertThat(resFile.toFile().exists()).isTrue();
        assertThat(resFile.toFile().isDirectory()).isTrue();
    }

    @Test
    public void testCreateDirectoriesTrue() throws FileManagerException, IOException {
        File parent = temporaryFolder.newFolder();
        parent.delete();
        Path resFile = FolderUtilities.createFolder("testFolder", parent.toPath(), true);
        assertThat(resFile.toFile().exists()).isTrue();
        assertThat(resFile.toFile().isDirectory()).isTrue();
    }

    @Test
    public void testCreateDirectoriesFalse() throws IOException {
        File parent = temporaryFolder.newFolder();
        parent.delete();
        assertThatThrownBy( () -> FolderUtilities.createFolder("testFolder", parent.toPath(), false)).isInstanceOf(FileManagerException.class);
    }

    @Test
    public void testCreateDirectoryInFile() throws IOException {
        File parent = temporaryFolder.newFile();
        assertThatThrownBy( () -> FolderUtilities.createFolder("testFolder", parent.toPath())).isInstanceOf(FileManagerException.class);
    }

    @Test
    public void testCreateExistingDirectory() throws IOException {
        File folder = temporaryFolder.newFolder();
        assertThatThrownBy( () -> FolderUtilities.createFolder(folder.getName(), temporaryFolder.getRoot().toPath())).isInstanceOf(FileManagerException.class);
    }

    @Test
    public void testCreateDirectoryNameLengthZero() {
        assertThatThrownBy(() -> FolderUtilities.createFolder("", temporaryFolder.getRoot().toPath())).isInstanceOf(FileManagerException.class);
    }

    @Test
    public void testCreateDirectoryNonExistingParent() {
        temporaryFolder.delete();
        assertThatThrownBy(() -> FolderUtilities.createFolder("testDir", temporaryFolder.getRoot().toPath())).isInstanceOf(FileManagerException.class);
    }

    @Test
    public void testDirectoryExists() throws FileManagerException, IOException {
        File f = temporaryFolder.newFolder();
        assertThat(FolderUtilities.folderExists(f.getAbsolutePath())).isTrue();
    }

    @Test
    public void testDirectoryFileExists() {
        assertThatThrownBy(() -> FolderUtilities.folderExists(temporaryFolder.newFile().getAbsolutePath())).isInstanceOf(FileManagerException.class);
    }

    @Test
    public void testNonExistingDirectoryExists(){
        assertThatThrownBy(() -> FolderUtilities.folderExists("someDir")).isInstanceOf(FileManagerException.class);
    }
}
