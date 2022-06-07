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

import com.fabriceci.fmc.error.FMInitializationException;
import com.fabriceci.fmc.error.FileManagerException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Properties;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AbstractFileManagerTest {

    private Properties propertiesConfig = new Properties();
    private AbstractFileManager abstractFileManager;

    @Before
    public void initialize() throws FMInitializationException, IOException {
        abstractFileManager = Mockito.mock(AbstractFileManager.class, Mockito.CALLS_REAL_METHODS);
        propertiesConfig.setProperty("images.extensions", "jpg,jpe,jpeg,gif,png,svg,bmp");
        propertiesConfig.setProperty("extensions.restrictions", ",jpg,jpe,jpeg,gif,png,svg,txt,pdf,odp,ods,odt,rtf,doc,docx,xls,xlsx,ppt,pptx,csv,ogv,avi,mkv,mp4,webm,m4v,ogg,mp3,wav,zip,md");
        propertiesConfig.setProperty("patterns.restrictions.file", ".htaccess,web.config,.DS_Store");
        propertiesConfig.setProperty("patterns.restrictions.folder", "_thumbs,.CDN_ACCESS_LOGS");
        propertiesConfig.setProperty("allowHidden", "true");

        abstractFileManager.setPropertiesConfig(propertiesConfig);
    }

    @Test
    public void isAllowedImageExtTest() {

        assertTrue(abstractFileManager.isAllowedImageExt("jpg"));
        assertTrue(abstractFileManager.isAllowedImageExt("jpeg"));
        assertTrue(abstractFileManager.isAllowedImageExt("png"));
        assertTrue(abstractFileManager.isAllowedImageExt("svg"));
        assertFalse(abstractFileManager.isAllowedImageExt("zip"));
        assertFalse(abstractFileManager.isAllowedImageExt("jpgg"));
        assertFalse(abstractFileManager.isAllowedImageExt("jpge"));
    }

    @Test
    public void isAllowedFileExtensionTest() {

        propertiesConfig.setProperty("extensions.policy.allow", "true");
        assertTrue(abstractFileManager.isAllowedFileExtension(""));
        assertTrue(abstractFileManager.isAllowedFileExtension("image.jpg"));

        propertiesConfig.setProperty("extensions.policy.allow", "false");
        assertFalse(abstractFileManager.isAllowedFileExtension(""));
        assertFalse(abstractFileManager.isAllowedFileExtension("image.jpg"));
    }

    @Test
    public void isAllowedPatternTest() throws FileManagerException {

        propertiesConfig.setProperty("patterns.policy.allow", "false");
        assertTrue(abstractFileManager.isAllowedPattern(".htaccess", true));
        assertTrue(abstractFileManager.isAllowedPattern("image.jpg", false));
        assertFalse(abstractFileManager.isAllowedPattern(".htaccess", false));
        assertFalse(abstractFileManager.isAllowedPattern("_thumbs", true));
        assertFalse(abstractFileManager.isAllowedPattern(".CDN_ACCESS_LOGS", true));
        assertTrue(abstractFileManager.isAllowedPattern(".CDN_ACCESS_LOGS", false));


        propertiesConfig.setProperty("patterns.policy.allow", "true");
        assertFalse(abstractFileManager.isAllowedPattern(".htaccess", true));
        assertFalse(abstractFileManager.isAllowedPattern("image.jpg", false));
        assertTrue(abstractFileManager.isAllowedPattern(".htaccess", false));
        assertTrue(abstractFileManager.isAllowedPattern("_thumbs", true));
        assertTrue(abstractFileManager.isAllowedPattern(".CDN_ACCESS_LOGS", true));
        assertFalse(abstractFileManager.isAllowedPattern(".CDN_ACCESS_LOGS", false));
    }

    @Test
    public void normalizeTest() {

        propertiesConfig.setProperty("normalizeFilename", "false");
        assertEquals("My folder ê 2", abstractFileManager.normalizeName("My folder ê 2"));

        propertiesConfig.setProperty("normalizeFilename", "true");
        propertiesConfig.setProperty("charsLatinOnly", "false");

        assertEquals("My_folder_ê_2", abstractFileManager.normalizeName("My folder ê 2"));
        assertEquals("過每頁左上角的連結隨時", abstractFileManager.normalizeName("過每頁左上角的連結隨時"));

        propertiesConfig.setProperty("charsLatinOnly", "true");
        assertEquals("My_folder_e_2", abstractFileManager.normalizeName("My folder ê 2"));
        assertEquals("", abstractFileManager.normalizeName("過每頁左上角的連結隨時"));
    }
}
