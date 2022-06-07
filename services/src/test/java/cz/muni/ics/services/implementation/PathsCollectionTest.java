package cz.muni.ics.services.implementation;

/*-
 * #%L
 * Neck-Service
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

import cz.muni.ics.services.TemporaryTest;
import cz.muni.ics.services.implementations.FilePathsHashSet;
import cz.muni.ics.services.interfaces.PathsCollection;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author Jozef Cibík
 */
public class PathsCollectionTest extends TemporaryTest {

    private TemporaryFolder temporaryFolder = new TemporaryFolder(PROJECT_TEST_DIR);

    private File testingFile;
    private File testingDir;
    private File testingSubFile1;
    private File testingSubFile2;
    PathsCollection emptyTestingPaths;
    PathsCollection populatedTestingPaths;
    Path existingFilePath;
    Path existingDirPath;
    Path nonExistingFilePath = Paths.get("nonExistingFilePath.tmp");
    Path nonExistingDirPath = Paths.get("nonExistingDirPath");
    String testingExtension = "tmp";
    String testingFakeExtension = "noTmp";

    @Before
    public void before() throws IOException {
        temporaryFolder.create();
        testingFile = temporaryFolder.newFile();
        testingDir = temporaryFolder.newFolder();
        testingSubFile1 = new File(testingDir.getAbsolutePath(), "file1.tmp");
        testingSubFile1.createNewFile();
        testingSubFile2 = new File(testingDir.getAbsolutePath(), "file2.tmp");
        testingSubFile2.createNewFile();

        existingFilePath = Paths.get(testingFile.getAbsolutePath());
        existingDirPath = Paths.get(testingDir.getAbsolutePath());
        emptyTestingPaths = new FilePathsHashSet();
        populatedTestingPaths = new FilePathsHashSet();
        populatedTestingPaths.addAsFile(existingFilePath);
        populatedTestingPaths.addAsDirectory(existingDirPath);
    }

    @After
    public void after(){
        temporaryFolder.delete();
    }


    @Test
    public void testCreateWithCollection(){
        assertThat(new FilePathsHashSet(populatedTestingPaths)).isEqualTo(populatedTestingPaths);
    }

    @Test
    public void testContains(){
        assertThat(populatedTestingPaths.contains(existingFilePath)).isTrue();
    }


    @Test
    public void testEmpty(){
        assertThat(emptyTestingPaths.isEmpty()).isTrue();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////ADD

    @Test
    public void testAddExistingFilePathAsFile() {
        assertThat(emptyTestingPaths.addAsFile(existingFilePath)).isTrue();
        assertThat(emptyTestingPaths.size()).isEqualTo(1);
        assertThat(emptyTestingPaths.contains(existingFilePath.toAbsolutePath())).isTrue();
    }

    @Test
    public void testAddNonExistingFilePathAsFile() {
        assertThat(emptyTestingPaths.addAsFile(nonExistingFilePath)).isFalse();
        assertThat(emptyTestingPaths.size()).isEqualTo(0);
    }

    @Test
    public void testAddExistingFilePathAsDirectory() {
        assertThat(emptyTestingPaths.addAsDirectory(existingFilePath)).isTrue();
        assertThat(emptyTestingPaths.size()).isEqualTo(1);
    }

    @Test
    public void testAddNonExistingFilePathAsDirectory() {
        assertThat(emptyTestingPaths.addAsDirectory(nonExistingFilePath)).isFalse();
        assertThat(emptyTestingPaths.size()).isEqualTo(0);
    }

    @Test
    public void testAddExistingDirectoryPathAsDirectory() {
        assertThat(emptyTestingPaths.addAsDirectory(existingDirPath)).isTrue();
        assertThat(emptyTestingPaths.size()).isEqualTo(2);
    }

    @Test
    public void testAddNonExistingDirectoryPathAsDirectory() {
        assertThat(emptyTestingPaths.addAsDirectory(nonExistingDirPath)).isFalse();
        assertThat(emptyTestingPaths.size()).isEqualTo(0);
    }

    @Test
    public void testAddExistingDirectoryPathAsFile() {
        assertThat(emptyTestingPaths.addAsFile(existingDirPath)).isFalse();
    }

    @Test
    public void testAddNonExistingDirectoryPathAsFile() {
        assertThat(emptyTestingPaths.addAsFile(nonExistingDirPath)).isFalse();
    }

    @Test
    public void testAddAllExistingFilePathsAsFile() {
        List<Path> list = new ArrayList<>();
        list.add(existingFilePath);
        assertThat(emptyTestingPaths.addAllAsFile(list)).isTrue();
        assertThat(emptyTestingPaths.size()).isEqualTo(1);
    }

    @Test
    public void testAddAllNonExistingFilePathsAsFile() {
        List<Path> list = new ArrayList<>();
        list.add(nonExistingFilePath);
        assertThat(emptyTestingPaths.addAllAsFile(list)).isFalse();
        assertThat(emptyTestingPaths.size()).isEqualTo(0);
    }

    @Test
    public void testAddAllMixedExistenceFilePathsAsFile() {
        List<Path> list = new ArrayList<>();
        list.add(existingFilePath);
        list.add(nonExistingFilePath);
        assertThat(emptyTestingPaths.addAllAsFile(list)).isTrue();
        assertThat(emptyTestingPaths.size()).isEqualTo(1);
    }

    @Test
    public void testAddAllExistingDirPathsAsDirectory() {
        List<Path> list = new ArrayList<>();
        list.add(existingDirPath);
        assertThat(emptyTestingPaths.addAllAsDirectory(list)).isTrue();
        assertThat(emptyTestingPaths.size()).isEqualTo(2);
    }

    @Test
    public void testAddAllNonExistingDirPathsAsDirectory() {
        List<Path> list = new ArrayList<>();
        list.add(nonExistingDirPath);
        assertThat(emptyTestingPaths.addAllAsDirectory(list)).isFalse();
        assertThat(emptyTestingPaths.size()).isEqualTo(0);
    }

    @Test
    public void testAddAllMixedExistenceDirPathsAsDirectory() {
        List<Path> list = new ArrayList<>();
        list.add(existingDirPath);
        list.add(nonExistingDirPath);
        assertThat(emptyTestingPaths.addAllAsDirectory(list)).isTrue();
        assertThat(emptyTestingPaths.size()).isEqualTo(2);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////REMOVE

    @Test
    public void testRemoveIfPredicate(){
        populatedTestingPaths.removeIf(item -> { return item.toString().equals(testingFile.getAbsolutePath()); });
        assertThat(populatedTestingPaths.size()).isEqualTo(2);
    }

    @Test
    public void testRemoveExistingFilePathAsFile() {
        assertThat(populatedTestingPaths.removeAsFile(existingFilePath)).isTrue();
        assertThat(populatedTestingPaths.size()).isEqualTo(2);
    }

    @Test
    public void testRemoveNonExistingFilePathAsFile() {
        assertThat(populatedTestingPaths.removeAsFile(nonExistingFilePath)).isFalse();
        assertThat(populatedTestingPaths.size()).isEqualTo(3);
    }

    @Test
    public void testRemoveExistingFilePathAsDirectory() {
        assertThat(populatedTestingPaths.removeAsDirectory(existingFilePath));
        assertThat(populatedTestingPaths.size()).isEqualTo(2);
    }

    @Test
    public void testRemoveNonExistingFilePathAsDirectory() {
        assertThat(populatedTestingPaths.removeAsDirectory(nonExistingFilePath)).isFalse();
        assertThat(populatedTestingPaths.size()).isEqualTo(3);
    }

    @Test
    public void testRemoveExistingDirectoryPathAsDirectory() {
        assertThat(populatedTestingPaths.removeAsDirectory(existingDirPath)).isTrue();
        assertThat(populatedTestingPaths.size()).isEqualTo(1);
    }

    @Test
    public void testRemoveNonExistingDirectoryPathAsDirectory() {
        assertThat(populatedTestingPaths.removeAsDirectory(nonExistingFilePath)).isFalse();
        assertThat(populatedTestingPaths.size()).isEqualTo(3);
    }

    @Test
    public void testRemoveExistingDirectoryPathAsFile() {
        assertThat(populatedTestingPaths.removeAsFile(existingDirPath)).isFalse();
    }

    @Test
    public void testRemoveNonExistingDirectoryPathAsFile() {
        assertThat(populatedTestingPaths.removeAsFile(nonExistingDirPath)).isFalse();
    }

    @Test
    public void testRemoveAllExistingFilePathsAsFile() {
        List<Path> list = new ArrayList<>();
        list.add(existingFilePath);
        list.add(Paths.get(testingSubFile1.getAbsolutePath()));
        list.add(Paths.get(testingSubFile2.getAbsolutePath()));
        assertThat(populatedTestingPaths.removeAllAsFile(list)).isTrue();
        assertThat(populatedTestingPaths.size()).isEqualTo(0);
    }

    @Test
    public void testRemoveAllNonExistingFilePathsAsFile() {
        List<Path> list = new ArrayList<>();
        list.add(nonExistingFilePath);
        assertThat(populatedTestingPaths.removeAllAsFile(list)).isFalse();
        assertThat(populatedTestingPaths.size()).isEqualTo(3);
    }

    @Test
    public void testRemoveAllMixedExistenceFilePathsAsFile() {
        List<Path> list = new ArrayList<>();
        list.add(existingFilePath);
        list.add(nonExistingFilePath);
        assertThat(populatedTestingPaths.removeAllAsFile(list)).isTrue();
        assertThat(populatedTestingPaths.size()).isEqualTo(2);
    }

    @Test
    public void testRemoveAllExistingFilePathsAsDirectory() {
        List<Path> list = new ArrayList<>();
        list.add(existingFilePath);
        list.add(Paths.get(testingSubFile1.getAbsolutePath()));
        list.add(Paths.get(testingSubFile2.getAbsolutePath()));
        assertThat(populatedTestingPaths.removeAllAsDirectory(list)).isTrue();
        assertThat(populatedTestingPaths.size()).isEqualTo(0);
    }

    @Test
    public void testRemoveAllNonExistingFilePathsAsDirectory() {
        List<Path> list = new ArrayList<>();
        list.add(nonExistingDirPath);
        assertThat(populatedTestingPaths.removeAllAsDirectory(list)).isFalse();
        assertThat(populatedTestingPaths.size()).isEqualTo(3);
    }

    @Test
    public void testRemoveAllMixedExistenceFilePathsAsDirectory() {
        List<Path> list = new ArrayList<>();
        list.add(existingDirPath);
        list.add(nonExistingDirPath);
        assertThat(populatedTestingPaths.removeAllAsDirectory(list)).isTrue();
        assertThat(populatedTestingPaths.size()).isEqualTo(1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////CLEAR
    @Test
    public void testClearPathsInEmptyCollection() {
        int size = emptyTestingPaths.size();
        emptyTestingPaths.clearPaths();
        assertThat(emptyTestingPaths.size()).isEqualTo(size);
    }

    @Test
    public void testClearPathsInNonEmptyCollection() {
        populatedTestingPaths.clearPaths();
        assertThat(emptyTestingPaths.size()).isEqualTo(0);
    }

    @Test
    public void testClearPathsAndInitializeInEmptyCollection() {
        List<Path> list = new ArrayList<>();
        list.add(existingFilePath);
        emptyTestingPaths.clearPaths(list);
        assertThat(emptyTestingPaths.size()).isEqualTo(1);
    }

    @Test
    public void testClearPathsAndInitializeInNonEmptyCollection() {
        List<Path> list = new ArrayList<>();
        list.add(existingFilePath);
        populatedTestingPaths.clearPaths(list);
        assertThat(populatedTestingPaths.size()).isEqualTo(1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////EXTENSIONS

    @Test
    public void testContainsExtensionInEmptyCollection() {
        assertThat(emptyTestingPaths.containsExtension(testingExtension)).isFalse();
    }

    @Test
    public void testContainsExistingMixedCaseExtensionInPopulatedCollection() {
        assertThat(populatedTestingPaths.containsExtension(testingExtension.toUpperCase())).isFalse();
    }

    @Test
    public void testContainsExistingExtensionInPopulatedCollection() {
        assertThat(populatedTestingPaths.containsExtension(testingExtension)).isTrue();
    }

    @Test
    public void testContainsNonExistingExtensionInPopulatedCollection() {
        assertThat(populatedTestingPaths.containsExtension(testingFakeExtension)).isFalse();
    }

    @Test
    public void testGetFilesWithExtensionInEmptyCollection() {
        assertThat(emptyTestingPaths.getPathsWithExtension(testingExtension).size()).isEqualTo(0);
    }

    @Test
    public void testGetFilesWithExistingExtensionInPopulatedCollection() {
        assertThat(populatedTestingPaths.getPathsWithExtension(testingExtension).size()).isEqualTo(3);
    }

    @Test
    public void testGetFilesWithNonExistingExtensionInPopulatedCollection() {
        assertThat(populatedTestingPaths.getPathsWithExtension(testingFakeExtension).size()).isEqualTo(0);
    }
}

