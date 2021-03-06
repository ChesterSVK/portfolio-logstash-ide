package com.fabriceci.fmc.impl;

/*-
 * #%L
 * Neck-Filemanager
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

import com.fabriceci.StubServletOutputStream;
import com.fabriceci.fmc.error.ClientErrorMessage;
import com.fabriceci.fmc.error.FMInitializationException;
import com.fabriceci.fmc.error.FileManagerException;
import com.fabriceci.fmc.model.FileData;
import com.fabriceci.fmc.model.SuccessResponse;
import com.fabriceci.fmc.util.ImageUtils;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class LocalFileManagerTest {

    private final static String FILE_ROOT = "userfiles";
    private final static String PRINT_FILE_NAME = "output.txt";
    private final static String PARAM_PATH = "path";
    private final static String PARAM_MODE = "mode";
    private final static String PARAM_NAME = "name";
    private final static String PARAM_NEW = "new";
    private final static String PARAM_OLD = "old";
    private final static String PARAM_THUMBNAIL = "thumbnail";

    private final static String THUMBNAIL_DIR = "_thumbs";

    private final static String SAMPLE_IMAGE_PATH = "test/sample/sample.jpg";
    private final static String SAMPLE_TXT_PATH = "test/sample/sample.txt";
    private final static String EXPECTED_RESULT_GET_INITIATE = "./test/result/getInitiate.json";
    private final static String EXPECTED_RESULT_GET_FILE_TXT_PATH = "test/result/getFileTxt.json";
    private final static String EXPECTED_RESULT_GET_FILE_JPG_PATH = "test/result/getFileImage.json";
    private final static String EXPECTED_RESULT_GET_FOLDER_ROOT_PATH = "test/result/getFolderRoot.json";
    private final static String EXPECTED_RESULT_GET_FOLDER_FILES_PATH = "test/result/getFolderFiles.json";
    private final static String EXPECTED_RESULT_GET_FOLDER_EMPTY_PATH = "test/result/getFolderEmpty.json";

    private File sampleImageFile;
    private File sampleTxtFile;
    private String outputFilePath;
    private ClassLoader classLoader;
    private JsonParser parser;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @ClassRule
    public static TemporaryFolder temporaryFolder = new TemporaryFolder();

    @ClassRule
    public static TemporaryFolder temporaryOutputFolder = new TemporaryFolder();


    @Before
    public void initialize() throws URISyntaxException {
        classLoader = getClass().getClassLoader();
        sampleImageFile = new File(classLoader.getResource(SAMPLE_IMAGE_PATH).toURI().normalize().getPath());
        sampleTxtFile = new File(classLoader.getResource(SAMPLE_TXT_PATH).toURI().normalize().getPath());

        outputFilePath = temporaryOutputFolder.getRoot().getAbsolutePath() + File.separatorChar + PRINT_FILE_NAME;

        parser = new JsonParser();
    }

    @AfterClass
    public static void clean(){
        temporaryFolder.delete();
        temporaryOutputFolder.delete();
    }

    private LocalFileManager initFileManager() throws IOException, FMInitializationException {
        return initFileManager(null, true);
    }

    private LocalFileManager initFileManager(Map<String, String> extraOptions, boolean delete) throws IOException, FMInitializationException {

        if (delete) {
            temporaryFolder.delete();
            temporaryFolder.create();
            temporaryFolder.newFolder(FILE_ROOT);
        }
        Map<String, String> options = new HashMap<>();
        options.put("fileRoot", temporaryFolder.getRoot().getAbsolutePath() + File.separatorChar + FILE_ROOT);
        if (extraOptions != null) {
            options.putAll(extraOptions);
        }
        return new LocalFileManager(options);
    }

    @Test
    public void getRelativePathTest() throws IOException, FMInitializationException {
        final LocalFileManager localFileManager = initFileManager();
        final String temporaryFolderPath = temporaryFolder.getRoot().getAbsolutePath() + File.separatorChar + FILE_ROOT;

        File test = new File(temporaryFolderPath + File.separatorChar + "test" + File.separatorChar + "test2" + File.separatorChar + "monfichier.zip");

        String relativePath = localFileManager.getRelativePath(test);

        assertEquals(File.separatorChar + "test" + File.separatorChar + "test2" + File.separatorChar + "monfichier.zip", relativePath);
    }

    @Test
    public void actionIntiate() throws IOException, FMInitializationException {

        final LocalFileManager localFileManager = initFileManager();

        PrintWriter writer = new PrintWriter(outputFilePath);

        // mock the request
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        given(resp.getWriter()).willReturn(writer);
        given(req.getParameter(PARAM_MODE)).willReturn("initiate");
        given(req.getMethod()).willReturn("GET");
        localFileManager.handleRequest(req, resp);
        writer.flush();

        JsonElement actual = parser.parse(new String(Files.readAllBytes(Paths.get(outputFilePath))));
        InputStream iS = this.classLoader.getResourceAsStream(EXPECTED_RESULT_GET_INITIATE);
        JsonElement jsonExpectation = parser.parse(new String(ByteStreams.toByteArray(iS)));
        assertEquals(jsonExpectation, actual);
    }

    @Test
    public void actionGetFileTest() throws IOException, FileManagerException {

        final LocalFileManager localFileManager = initFileManager();
        final String temporaryFolderPath = temporaryFolder.getRoot().getAbsolutePath() + File.separatorChar + FILE_ROOT;

        PrintWriter writer = new PrintWriter(outputFilePath);
        // mock the request
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        // Add the sample host
        File sampleTextTemp = new File(temporaryFolderPath + File.separatorChar + sampleTxtFile.getName());
        Files.copy(sampleTxtFile.toPath(), sampleTextTemp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        File sampleImageTemp = new File(temporaryFolderPath + File.separatorChar + sampleImageFile.getName());
        Files.copy(sampleImageFile.toPath(), sampleImageTemp.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // test sample txt
        given(resp.getWriter()).willReturn(writer);
        given(req.getParameter(PARAM_MODE)).willReturn("getinfo");
        given(req.getParameter(PARAM_PATH)).willReturn("/sample.txt");
        given(req.getMethod()).willReturn("GET");
        localFileManager.handleRequest(req, resp);
        writer.flush();

        JsonElement jsonResult = parser.parse(cleanJsonApiResponse(new String(Files.readAllBytes(Paths.get(outputFilePath)))));
        InputStream isTxt = this.classLoader.getResourceAsStream(EXPECTED_RESULT_GET_FILE_TXT_PATH);
        JsonElement jsonExpectation = parser.parse(cleanJsonApiResponse(new String(ByteStreams.toByteArray(isTxt))));
        assertEquals(jsonExpectation, jsonResult);

        // test sample image
        writer = new PrintWriter(outputFilePath);
        given(resp.getWriter()).willReturn(writer);
        given(req.getParameter(PARAM_PATH)).willReturn("/sample.jpg");
        localFileManager.handleRequest(req, resp);
        writer.flush();

        jsonResult = parser.parse(cleanJsonApiResponse(new String(Files.readAllBytes(Paths.get(outputFilePath)))));
        InputStream isJpg = this.classLoader.getResourceAsStream(EXPECTED_RESULT_GET_FILE_JPG_PATH);
        jsonExpectation = parser.parse(cleanJsonApiResponse(new String(ByteStreams.toByteArray(isJpg))));
        assertEquals(jsonExpectation, jsonResult);
    }

    @Test
    public void actionGetFolderTest() throws IOException, FileManagerException {

        // reset the folder
        final LocalFileManager localFileManager = initFileManager();
        final String temporaryFolderPath = temporaryFolder.getRoot().getAbsolutePath() + File.separatorChar + FILE_ROOT;

        PrintWriter writer = new PrintWriter(outputFilePath);
        // mock the request
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        // Add the sample host
        final String folderFiles = "Folder1";
        final String folderEmpty = "EmptyFolder";

        Files.createDirectory(new File(temporaryFolderPath + File.separatorChar + folderFiles).toPath());
        Files.createDirectory(new File(temporaryFolderPath + File.separatorChar + folderEmpty).toPath());

        File sampleTextTemp = new File(temporaryFolderPath + File.separatorChar + folderFiles + File.separatorChar + sampleTxtFile.getName());
        Files.copy(sampleTxtFile.toPath(), sampleTextTemp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        File sampleImageTemp = new File(temporaryFolderPath + File.separatorChar + folderFiles + File.separatorChar + sampleImageFile.getName());
        Files.copy(sampleImageFile.toPath(), sampleImageTemp.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // test root folder
        given(resp.getWriter()).willReturn(writer);
        given(req.getParameter(PARAM_MODE)).willReturn("readfolder");
        given(req.getParameter(PARAM_PATH)).willReturn(String.valueOf(File.separatorChar));
        given(req.getMethod()).willReturn("GET");
        localFileManager.handleRequest(req, resp);
        writer.flush();

        JsonElement jsonResult = parser.parse(cleanJsonApiResponse(new String(Files.readAllBytes(Paths.get(outputFilePath)))));
        InputStream iS1 = this.classLoader.getResourceAsStream(EXPECTED_RESULT_GET_FOLDER_ROOT_PATH);
        JsonElement jsonExpectation = parser.parse(cleanJsonApiResponse(new String(ByteStreams.toByteArray(iS1))));
        assertEquals(jsonExpectation, jsonResult);

        // test empty folder
        writer = new PrintWriter(outputFilePath);
        given(resp.getWriter()).willReturn(writer);
        given(req.getParameter(PARAM_PATH)).willReturn(File.separatorChar + folderEmpty + File.separatorChar);
        given(req.getMethod()).willReturn("GET");
        localFileManager.handleRequest(req, resp);
        writer.flush();

        jsonResult = parser.parse(cleanJsonApiResponse(new String(Files.readAllBytes(Paths.get(outputFilePath)))));
        InputStream iS2 = this.classLoader.getResourceAsStream(EXPECTED_RESULT_GET_FOLDER_EMPTY_PATH);
        jsonExpectation = parser.parse(cleanJsonApiResponse(new String(ByteStreams.toByteArray(iS2))));
        assertEquals(jsonExpectation, jsonResult);

        // test folder with files
        writer = new PrintWriter(outputFilePath);
        given(resp.getWriter()).willReturn(writer);
        given(req.getParameter(PARAM_PATH)).willReturn(File.separatorChar + folderFiles + File.separatorChar);
        given(req.getMethod()).willReturn("GET");
        localFileManager.handleRequest(req, resp);
        writer.flush();

        JsonParser parser = new JsonParser();
        jsonExpectation = parser.parse(cleanJsonApiResponse(new String(Files.readAllBytes(Paths.get(outputFilePath)))));
        InputStream iS3 = this.classLoader.getResourceAsStream(EXPECTED_RESULT_GET_FOLDER_FILES_PATH);
        jsonResult = parser.parse(cleanJsonApiResponse(new String(ByteStreams.toByteArray(iS3))));
        assertEquals(jsonExpectation, jsonResult);
    }

    @Test
    public void actionAddFolderTest() throws IOException, FMInitializationException {
        final LocalFileManager localFileManager = initFileManager();
        final String newFolderName = "my_new_folder";
        final String newFolderPath = temporaryFolder.getRoot().getAbsolutePath() + File.separatorChar + FILE_ROOT + File.separatorChar + newFolderName;
        File newFolderFile = new File(newFolderPath);

        assertFalse(newFolderFile.exists());

        PrintWriter writer = new PrintWriter(outputFilePath);
        // mock the request
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        given(resp.getWriter()).willReturn(writer);
        given(req.getParameter(PARAM_MODE)).willReturn("addfolder");
        given(req.getParameter(PARAM_PATH)).willReturn(String.valueOf(File.separatorChar));
        given(req.getParameter(PARAM_NAME)).willReturn(newFolderName);
        given(req.getMethod()).willReturn("GET");
        localFileManager.handleRequest(req, resp);
        writer.flush();

        assertTrue(newFolderFile.exists());
        JsonElement jsonActual = parser.parse(new String(Files.readAllBytes(Paths.get(outputFilePath))));

        // check if the method return the good file info
        FileData folderInfo = null;
        try {
            folderInfo = localFileManager.getFileInfo(File.separatorChar + newFolderName + File.separatorChar);
        } catch (FileManagerException ignore) {
        }

        Gson gson = new Gson();
        JsonElement jsonExpected = parser.parse(gson.toJson(new SuccessResponse(folderInfo)));

        assertEquals(jsonExpected, jsonActual);

        // security test with readonly
        Map<String, String> map = new HashMap<>();
        map.put("readOnly", "true");
        writer = new PrintWriter(outputFilePath);
        given(resp.getWriter()).willReturn(writer);
        given(req.getParameter(PARAM_NAME)).willReturn(newFolderName + "2");
        final LocalFileManager localFileManagerReadOnly = initFileManager(map, false);
        localFileManagerReadOnly.handleRequest(req, resp);
        writer.flush();
        JsonElement parse = parser.parse(new String(Files.readAllBytes(Paths.get(outputFilePath))));

        assertTrue(parse.toString().contains(ClientErrorMessage.NOT_ALLOWED));
    }

    @Test
    public void actionMoveTest() throws IOException, FileManagerException {

        final LocalFileManager localFileManager = initFileManager();
        final String temporaryFolderPath = temporaryFolder.getRoot().getAbsolutePath() + File.separatorChar + FILE_ROOT;

        final String movedDirName = "movedDir";
        final String movedDirPath = temporaryFolderPath + File.separatorChar + movedDirName;
        File movedDirFile = new File(movedDirPath);
        Files.createDirectory(movedDirFile.toPath());

        assertTrue(movedDirFile.exists());

        PrintWriter writer = new PrintWriter(outputFilePath);

        // mock the request
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        // Add the sample host
        File sampleImageTemp = new File(temporaryFolderPath + File.separatorChar + sampleImageFile.getName());
        File f = new File("D:\\Projects\\ICS\\Bachelor Thesis\\Neck-Web-Application\\filesystem\\target\\test-classes\\test\\sample\\sample.jpg");
        boolean a = sampleImageFile.exists();
        boolean b = Files.exists(f.toPath());
        Files.copy(sampleImageFile.toPath(), sampleImageTemp.toPath(), StandardCopyOption.REPLACE_EXISTING);

        try {
            // create a thumbnail
            localFileManager.getThumbnail(File.separatorChar + sampleImageFile.getName(), true);
        } catch (FileManagerException ignore) {
            fail();
        }

        File oldThumbnail = new File(localFileManager.getThumbnailPath(File.separatorChar + sampleImageFile.getName()));
        File newThumbnail = new File(localFileManager.getThumbnailPath(File.separatorChar + movedDirName + File.separatorChar + sampleImageFile.getName()));

        assertTrue(oldThumbnail.exists());
        assertFalse(newThumbnail.exists());

        assertTrue(sampleImageTemp.exists());

        given(resp.getWriter()).willReturn(writer);
        given(req.getParameter(PARAM_MODE)).willReturn("move");
        given(req.getParameter(PARAM_OLD)).willReturn(File.separatorChar + sampleImageFile.getName());
        given(req.getParameter(PARAM_NEW)).willReturn(File.separatorChar + movedDirName + File.separatorChar);
        given(req.getMethod()).willReturn("GET");
        localFileManager.handleRequest(req, resp);
        writer.flush();

        assertFalse(sampleImageTemp.exists());
        assertFalse(oldThumbnail.exists());
        assertTrue(newThumbnail.exists());

        JsonElement jsonActual = parser.parse(new String(Files.readAllBytes(Paths.get(outputFilePath))));

        // check if the method return the good file info
        FileData sampleInfo = localFileManager.getFileInfo(File.separatorChar + movedDirName + File.separatorChar + sampleImageFile.getName());

        Gson gson = new Gson();
        JsonElement jsonExpected = parser.parse(gson.toJson(new SuccessResponse(sampleInfo)));

        assertEquals(jsonExpected, jsonActual);

        // security test with readonly
        Map<String, String> map = new HashMap<>();
        map.put("readOnly", "true");
        writer = new PrintWriter(outputFilePath);
        given(resp.getWriter()).willReturn(writer);
        given(req.getParameter(PARAM_OLD)).willReturn(File.separatorChar + movedDirName + File.separatorChar + sampleImageFile.getName());
        given(req.getParameter(PARAM_NEW)).willReturn(String.valueOf(File.separatorChar));
        final LocalFileManager localFileManagerReadOnly = initFileManager(map, false);
        localFileManagerReadOnly.handleRequest(req, resp);
        writer.flush();

        JsonElement parse = parser.parse(new String(Files.readAllBytes(Paths.get(outputFilePath))));
        assertTrue(parse.toString().contains(ClientErrorMessage.NOT_ALLOWED));
    }

    @Test
    public void actionDeleteTest() throws IOException, FileManagerException {
        final LocalFileManager localFileManager = initFileManager();
        final String temporaryFolderPath = temporaryFolder.getRoot().getAbsolutePath() + File.separatorChar + FILE_ROOT;

        final String toBeDeleteDirName = "deleteMe";
        final String toBeDeleteDirPath = temporaryFolderPath + File.separatorChar + toBeDeleteDirName;

        File toBeDeleteFile = new File(toBeDeleteDirPath);
        Files.createDirectory(toBeDeleteFile.toPath());

        assertTrue(toBeDeleteFile.exists());

        PrintWriter writer = new PrintWriter(outputFilePath);

        // mock the request
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        // Add the sample host
        File sampleImageTemp = new File(temporaryFolderPath + File.separatorChar + sampleImageFile.getName());
        File sampleImageTemp2 = new File(toBeDeleteDirPath + File.separatorChar + sampleImageFile.getName());
        File sampleImageTemp3 = new File(temporaryFolderPath + File.separatorChar + "foo.jpg");
        Files.copy(sampleImageFile.toPath(), sampleImageTemp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(sampleImageFile.toPath(), sampleImageTemp2.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(sampleImageFile.toPath(), sampleImageTemp3.toPath(), StandardCopyOption.REPLACE_EXISTING);

        try {
            // create a thumbnail
            localFileManager.getThumbnail(File.separatorChar + sampleImageFile.getName(), true);
            localFileManager.getThumbnail(File.separatorChar + toBeDeleteDirName + File.separatorChar + sampleImageFile.getName(), true);
        } catch (FileManagerException ignore) {
            fail();
        }

        File thumbnail1 = new File(localFileManager.getThumbnailPath(File.separatorChar + sampleImageFile.getName()));
        File thumbnail2 = new File(localFileManager.getThumbnailPath(File.separatorChar + toBeDeleteDirName + File.separatorChar + sampleImageFile.getName()));

        assertTrue(sampleImageTemp.exists());
        assertTrue(sampleImageTemp2.exists());
        assertTrue(thumbnail1.exists());
        assertTrue(thumbnail2.exists());

        // test delete a file
        FileData fileInfo = localFileManager.getFileInfo(File.separatorChar + sampleImageFile.getName());
        given(resp.getWriter()).willReturn(writer);
        given(req.getParameter(PARAM_MODE)).willReturn("delete");
        given(req.getParameter(PARAM_PATH)).willReturn(File.separatorChar + sampleImageFile.getName());
        given(req.getMethod()).willReturn("GET");
        localFileManager.handleRequest(req, resp);
        writer.flush();

        assertFalse(sampleImageTemp.exists());
        assertTrue(sampleImageTemp2.exists());
        assertFalse(thumbnail1.exists());
        assertTrue(thumbnail2.exists());

        JsonElement jsonActual = parser.parse(new String(Files.readAllBytes(Paths.get(outputFilePath))));

        Gson gson = new Gson();
        JsonElement jsonExpected = parser.parse(gson.toJson(new SuccessResponse(fileInfo)));

        // check the response
        assertEquals(jsonExpected, jsonActual);

        // test delete a folder
        writer = new PrintWriter(outputFilePath);
        given(resp.getWriter()).willReturn(writer);
        given(resp.getWriter()).willReturn(writer);
        given(req.getParameter(PARAM_MODE)).willReturn("delete");
        given(req.getParameter(PARAM_PATH)).willReturn(File.separatorChar + toBeDeleteDirName + File.separatorChar);
        given(req.getMethod()).willReturn("GET");
        localFileManager.handleRequest(req, resp);
        writer.flush();

        assertFalse(sampleImageTemp.exists());
        assertFalse(sampleImageTemp2.exists());
        assertFalse(thumbnail1.exists());
        assertFalse(thumbnail2.exists());
        assertFalse(toBeDeleteFile.exists());

        // security test with readonly
        Map<String, String> map = new HashMap<>();
        map.put("readOnly", "true");
        writer = new PrintWriter(outputFilePath);
        given(resp.getWriter()).willReturn(writer);
        given(req.getParameter(PARAM_PATH)).willReturn(File.separatorChar + "foo.jpg");
        final LocalFileManager localFileManagerReadOnly = initFileManager(map, false);
        localFileManagerReadOnly.handleRequest(req, resp);
        writer.flush();

        JsonElement parse = parser.parse(new String(Files.readAllBytes(Paths.get(outputFilePath))));
        assertTrue(parse.toString().contains(ClientErrorMessage.NOT_ALLOWED));

    }

    @Test
    public void actionGetImageTest() throws IOException, FMInitializationException {
        final LocalFileManager localFileManager = initFileManager();
        final String temporaryFolderPath = temporaryFolder.getRoot().getAbsolutePath() + File.separatorChar + FILE_ROOT;

        PrintWriter writer = new PrintWriter(outputFilePath);

        // mock the request
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);

        // Add the sample host
        File sampleImageTemp = new File(temporaryFolderPath + File.separatorChar + sampleImageFile.getName());
        Files.copy(sampleImageFile.toPath(), sampleImageTemp.toPath(), StandardCopyOption.REPLACE_EXISTING);

        // normal file
        File resultFile = new File(temporaryOutputFolder.getRoot().getAbsolutePath() + File.separatorChar + "foo.jpg");
        StubServletOutputStream servletOutputStream = new StubServletOutputStream(resultFile);

        given(resp.getWriter()).willReturn(writer);
        given(resp.getOutputStream()).willReturn(servletOutputStream);
        given(req.getParameter(PARAM_MODE)).willReturn("getimage");
        given(req.getParameter(PARAM_PATH)).willReturn(File.separatorChar + sampleImageFile.getName());
        given(req.getMethod()).willReturn("GET");
        localFileManager.handleRequest(req, resp);
        writer.flush();
        servletOutputStream.flush();

        assertTrue(resultFile.exists());
        Dimension dim = ImageUtils.getImageSize(resultFile);
        assertTrue(dim.getHeight() == 162);
        assertTrue(dim.getWidth() == 200);

        // thumbnail file
        resultFile.delete();
        writer = new PrintWriter(outputFilePath);
        servletOutputStream = new StubServletOutputStream(resultFile);

        given(resp.getWriter()).willReturn(writer);
        given(resp.getOutputStream()).willReturn(servletOutputStream);
        given(req.getParameter(PARAM_THUMBNAIL)).willReturn("true");
        given(req.getParameter(PARAM_PATH)).willReturn(File.separatorChar + sampleImageFile.getName());
        given(req.getMethod()).willReturn("GET");
        localFileManager.handleRequest(req, resp);
        writer.flush();
        servletOutputStream.flush();

        dim = ImageUtils.getImageSize(resultFile);
        double i = dim.getHeight();
        assertTrue(dim.getHeight() == 810.0);
        assertTrue(dim.getWidth() == 100);

    }

    /**
     * @param json A JSON API String response
     * @return The String without path/created/modified values
     */
    private String cleanJsonApiResponse(String json) throws IOException {

        Pattern p1 = Pattern.compile("(created\":)\\d+");
        Pattern p2 = Pattern.compile("(modified\":)\\d+");
        Pattern p3 = Pattern.compile("(path\":\")[0-9A-Za-z-:.\\/ ]+(\")");

        String output = "";

        Matcher m1 = p1.matcher(json);
        if (m1.find()) {
            output = m1.replaceAll("$10");
        }

        Matcher m2 = p2.matcher(output);
        if (m2.find()) {
            output = m2.replaceAll("$10");
        }

        Matcher m3 = p3.matcher(output);
        if (m3.find()) {
            output = m3.replaceAll("$1$2");  // number 46
        }

        return output;
    }

}
