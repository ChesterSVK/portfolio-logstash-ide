package com.fabriceci.fmc;

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

import com.fabriceci.fmc.error.ClientErrorMessage;
import com.fabriceci.fmc.error.FileManagerException;
import com.fabriceci.fmc.model.*;
import com.fabriceci.fmc.util.FileUtils;
import com.fabriceci.fmc.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.imgscalr.Scalr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public abstract class AbstractFileManager implements IFileManager {

    private final static String CONFIG_DEFAULT_PROPERTIES = "filemanager.config.default.properties";
    private final static String CONFIG_CUSTOM_PROPERTIES = "filemanager.config.properties";
    protected final static String LANG_FILE = "filemanager.lang.en.properties";
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    protected boolean readOnly = false;
    protected Properties propertiesConfig = new Properties();

    public AbstractFileManager(Map<String, String> options) {
        // load server properties
        InputStream tempLoadIS = null;

        // load default config file
        tempLoadIS = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_DEFAULT_PROPERTIES);
        try {
            propertiesConfig.load(tempLoadIS);
        } catch (IOException ignored) {
        }

        try {
            tempLoadIS.close();
        } catch (IOException ignored) {
        }

        // load custom config file if exists
        tempLoadIS = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_CUSTOM_PROPERTIES);
        if (tempLoadIS != null) {
            Properties customConfig = new Properties();
            try {
                customConfig.load(tempLoadIS);
            } catch (IOException ignored) {
            }

            propertiesConfig.putAll(customConfig);
            try {
                tempLoadIS.close();
            } catch (IOException ignored) {
            }
        }

        if (options != null && !options.isEmpty()) {
            propertiesConfig.putAll(options);
        }

        readOnly = Boolean.parseBoolean(propertiesConfig.getProperty("readOnly"));
    }

    public AbstractFileManager() {
        this(null);
    }

    private static String cleanPath(String path) {
        if (path == null) return null;
        return path.replace("//", "/").replace("..", "");
    }

    public final void handleRequest(HttpServletRequest request, HttpServletResponse response) {

        //baseUrl = ServletUtils.getBaseUrl(request);

        final String method = request.getMethod();
        final String mode = request.getParameter("mode");
        final String pathParam = cleanPath(request.getParameter("path"));
        String sourcePath = null;
        String targetPath = null;

        Object responseData = null;
        response.setStatus(200);

        try {
            if (StringUtils.isEmpty(mode)) {
                generateResponse(response, ClientErrorMessage.MODE_ERROR);
                return;
            }

            if (method.equals("GET")) {
                switch (mode) {
                    default:
                        throw new FileManagerException(ClientErrorMessage.MODE_ERROR);
                    case "initiate":
                        responseData = actionInitiate();
                        break;
                    case "getinfo":
                        if (!StringUtils.isEmpty(pathParam)) {
                            responseData = actionGetInfo(pathParam);
                        }
                        break;
                    case "readfolder":
                        final String typeParam = request.getParameter("type");
                        if (!StringUtils.isEmpty(pathParam)) {
                            responseData = actionReadFolder(pathParam, typeParam);
                        }
                        break;
                    case "seekfolder":
                        final String searchTerm = request.getParameter("string");
                        if (!StringUtils.isEmpty(pathParam) && !StringUtils.isEmpty(searchTerm)) {
                            responseData = actionSeekFolder(pathParam, searchTerm);
                        }
                        break;
                    case "rename":
                        sourcePath = cleanPath(request.getParameter("old"));
                        targetPath = cleanPath(request.getParameter("new"));
                        if (!StringUtils.isEmpty(sourcePath) && !StringUtils.isEmpty(targetPath)) {
                            responseData = actionRename(sourcePath, targetPath);
                        }
                        break;
                    case "copy":
                        sourcePath = cleanPath(request.getParameter("source"));
                        targetPath = cleanPath(request.getParameter("target"));
                        if (!StringUtils.isEmpty(sourcePath) && !StringUtils.isEmpty(targetPath)) {
                            responseData = actionCopy(sourcePath, targetPath);
                        }
                        break;
                    case "move":
                        sourcePath = cleanPath(request.getParameter("old"));
                        targetPath = cleanPath(request.getParameter("new"));
                        if (!StringUtils.isEmpty(sourcePath) && !StringUtils.isEmpty(targetPath)) {
                            responseData = actionMove(sourcePath, targetPath);
                        }
                        break;
                    case "delete":
                        if (!StringUtils.isEmpty(pathParam)) {
                            responseData = actionDelete(pathParam);
                        }
                        break;
                    case "addfolder":
                        final String name = request.getParameter("name");
                        if (!StringUtils.isEmpty(pathParam) && !StringUtils.isEmpty(name)) {
                            responseData = actionAddFolder(pathParam, name);
                        }
                        break;
                    case "download":
                        if (!StringUtils.isEmpty(pathParam)) {
                            responseData = actionDownload(response, pathParam);
                        }
                        break;
                    case "getimage":
                        if (!StringUtils.isEmpty(pathParam)) {
                            Boolean thumbnail = Boolean.parseBoolean(request.getParameter("thumbnail"));
                            responseData = actionGetImage(response, pathParam, thumbnail);
                        }
                        break;
                    case "readfile" :
                        if (!StringUtils.isEmpty(pathParam)) {
                            responseData = actionReadFile(response, pathParam);
                        }
                        break;
                    case "summarize" :
                        responseData = actionSummarize();
                    break;
                }
            } else if (method.equals("POST")) {
                switch (mode) {
                    default:
                        throw new FileManagerException(ClientErrorMessage.MODE_ERROR);
                    case "upload":
                        if (!StringUtils.isEmpty(pathParam)) {
                            responseData = actionUpload(request, pathParam);
                        }
                        break;
                    case "savefile":
                        final String contentParam = cleanPath(request.getParameter("content"));
                        if (!StringUtils.isEmpty(pathParam) && !StringUtils.isEmpty(contentParam)) {
                            responseData = actionSaveFile(pathParam, contentParam);
                        }
                        break;
                    case "extract":
                        sourcePath = cleanPath(request.getParameter("source"));
                        targetPath = cleanPath(request.getParameter("target"));
                        if (!StringUtils.isEmpty(sourcePath) && !StringUtils.isEmpty(targetPath)) {
                            responseData = actionExtract(sourcePath, targetPath);
                        }
                        break;
                }
            }

            if (responseData != null) {
                generateResponse(response, responseData);
            }

        } catch (FileManagerException e) {
//            log.info(e.getMessage(), e);
            generateErrorResponse(response, e.getMessage(), e.getArguments());
        } catch (Exception e) {
//            log.info(e.getMessage(), e);
            generateErrorResponse(response, "ERROR_SERVER", null);
        }


    }

    private void generateErrorResponse(HttpServletResponse response, String message, List<String> arguments) {
        response.setStatus(500);
        response.addHeader("Content-Type", "application/json; charset=utf-8");

        Gson gson = new GsonBuilder().create();

        ErrorItem errorItem = new ErrorItem(message, arguments);

        try {
            response.getWriter().write(gson.toJson(new ErrorResponse(errorItem)));
        } catch (IOException ignore) {}
    }

    private void generateResponse(HttpServletResponse response, Object data) throws IOException {
        response.setStatus(200);
        response.addHeader("Content-Type", "application/json; charset=utf-8");

        Gson gson = new GsonBuilder().create();

        response.getWriter().write(gson.toJson(new SuccessResponse(data)));
    }

    @Override
    public InitiateData actionInitiate() throws FileManagerException {

        ConfigUpload configUpload = new ConfigUpload();
        configUpload.setFileSizeLimit(Long.parseLong(propertiesConfig.getProperty("upload.fileSizeLimit")));


        ConfigExtensions configExtensions = new ConfigExtensions();
        boolean policyAllow = Boolean.parseBoolean(propertiesConfig.getProperty("extensions.policy.allow"));
        configExtensions.setPolicy(policyAllow ? "ALLOW_LIST" : "DISALLOW_LIST");
        configExtensions.setRestrictions(propertiesConfig.getProperty("extensions.restrictions").split(","));

        ConfigSecurity configSecurity = new ConfigSecurity();
        configSecurity.setReadOnly(readOnly);
        configSecurity.setExtensions(configExtensions);

        ConfigRoot configRoot = new ConfigRoot();
        configRoot.setSecurity(configSecurity);
        configRoot.setUpload(configUpload);

        InitiateAttributes initiateAttributes = new InitiateAttributes();
        initiateAttributes.setConfig(configRoot);


        InitiateData initiateData = new InitiateData();
        initiateData.setAttributes(initiateAttributes);

        return initiateData;
    }

    @Override
    public FileData actionGetInfo(String path) throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<FileData> actionReadFolder(String path, String type) throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileData actionAddFolder(String path, String name) throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileData actionDelete(String path) throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileData actionMove(String sourcePath, String targetPath) throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileData actionGetImage(HttpServletResponse response, String path, Boolean thumbnail) throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileData actionSummarize() throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileData actionRename(String sourcePath, String targetPath) throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileData actionCopy(String sourcePath, String targetPath) throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileData actionReadFile(HttpServletResponse response, String path) throws FileManagerException, FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileData actionDownload(HttpServletResponse response, String path) throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<FileData> actionUpload(HttpServletRequest request, String path) throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileData actionSaveFile(String pathParam, String contentParam) throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<FileData>  actionExtract(String sourcePath, String targetPath) throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object actionSeekFolder(String folderPath, String term) throws FileManagerException {
        throw new UnsupportedOperationException();
    }

    protected final boolean isAllowedImageExt(String ext) {
        return Arrays.asList(propertiesConfig.getProperty("images.extensions").split(",")).contains(ext.toLowerCase());
    }

    protected final boolean isAllowedFileExtension(String file) {
        String extension = FileUtils.getExtension(file).toLowerCase();

        boolean policyAllow = Boolean.parseBoolean(propertiesConfig.getProperty("extensions.policy.allow"));
        List<String> restrictions = Arrays.asList(propertiesConfig.getProperty("extensions.restrictions").split(","));

        if (policyAllow) {
            return restrictions.contains(extension);
        } else {
            return !restrictions.contains(extension);
        }
    }

    protected final boolean isAllowedPattern(String name, boolean isDir) throws FileManagerException {

        boolean policyAllow = Boolean.parseBoolean(propertiesConfig.getProperty("patterns.policy.allow"));

        if (!Boolean.parseBoolean(propertiesConfig.getProperty("allowHidden", "false"))){
            if (name.startsWith(".")){
                return false;
            }
        }

        try {
            if (isDir) {
                List<String> restrictionsFolder = Arrays.asList(propertiesConfig.getProperty("patterns.restrictions.folder").split(","));
                boolean isMatch = false;
                for (String regex : restrictionsFolder) {
                    if (name.matches(regex)) isMatch = true;
                }

                return policyAllow == isMatch;

            } else {
                List<String> restrictionsFile = Arrays.asList((propertiesConfig.getProperty("patterns.restrictions.file").split(",")));
                boolean isMatch = false;
                for (String regex : restrictionsFile) {
                    if (name.matches(regex)) isMatch = true;
                }

                return policyAllow == isMatch;
            }
        } catch (PatternSyntaxException e) {
//            log.error("Regex Dir Syntax Exception : " + propertiesConfig.getProperty("excluded_dirs_REGEXP"), e);
            throw new FileManagerException(ClientErrorMessage.ERROR_SERVER);
        }
    }

    protected final BufferedImage generateThumbnail(BufferedImage source) {
        return Scalr.resize(source, Scalr.Method.AUTOMATIC, Scalr.Mode.FIT_TO_WIDTH, Integer.parseInt(propertiesConfig.getProperty("images.thumbnail.maxWidth")), Integer.parseInt(propertiesConfig.getProperty("images.thumbnail.maxHeight")), Scalr.OP_ANTIALIAS);
    }

    protected String normalizeName(String input) {

        boolean normalizeFilename = Boolean.parseBoolean(propertiesConfig.getProperty("normalizeFilename"));

        if (!normalizeFilename) return input;

        boolean charsLatinOnly = Boolean.parseBoolean(propertiesConfig.getProperty("charsLatinOnly"));

        String nowhitespace = WHITESPACE.matcher(input).replaceAll("_");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);

        return charsLatinOnly ? NONLATIN.matcher(normalized).replaceAll("") : normalized;
    }

    public Properties getPropertiesConfig() {
        return propertiesConfig;
    }

    public void setPropertiesConfig(Properties propertiesConfig) {
        this.propertiesConfig = propertiesConfig;
    }
}
