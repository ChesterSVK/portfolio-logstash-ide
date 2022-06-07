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


import com.fabriceci.fmc.error.FileManagerException;
import com.fabriceci.fmc.model.FileData;
import com.fabriceci.fmc.model.InitiateData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IFileManager {

    void handleRequest(HttpServletRequest request, HttpServletResponse response);

    // GET

    InitiateData actionInitiate() throws FileManagerException;

    FileData actionGetInfo(String path) throws FileManagerException;

    List<FileData> actionReadFolder(String path, String type) throws FileManagerException;

    FileData actionMove(String sourcePath, String targetPath) throws FileManagerException;

    FileData actionDelete(String path) throws FileManagerException;

    FileData actionAddFolder(String path, String name) throws FileManagerException;

    FileData actionGetImage(HttpServletResponse response, String path, Boolean thumbnail) throws FileManagerException;

    // TO test :

    Object actionSeekFolder(String folderPath, String term) throws FileManagerException;

    FileData actionCopy(String sourcePath, String targetPath) throws FileManagerException;

    FileData actionRename(String sourcePath, String targetPath) throws FileManagerException;

    FileData actionReadFile(HttpServletResponse response, String path) throws FileManagerException;

    FileData actionSummarize() throws FileManagerException;

    FileData actionDownload(HttpServletResponse response, String path) throws FileManagerException;

    List<FileData> actionUpload(HttpServletRequest request, String path) throws FileManagerException;

    FileData actionSaveFile(String pathParam, String contentParam) throws FileManagerException;

    List<FileData> actionExtract(String sourcePath, String targetPath) throws FileManagerException;


}
