package com.fabriceci.fmc.util;

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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageUtils {

    public static Dimension getImageSize(String path){

        BufferedImage readImage = null;
        Dimension dim = new Dimension();
        try {
            readImage = ImageIO.read(new File(path));
            dim.height = readImage.getHeight();
            dim.width = readImage.getWidth();
        } catch (Exception e) {
            readImage = null;
        }
        return dim;
    }

    public static Dimension getImageSize(File file){

        BufferedImage readImage = null;
        Dimension dim = new Dimension();
        try {
            readImage = ImageIO.read(file);
            dim.height = readImage.getHeight();
            dim.width = readImage.getWidth();
        } catch (Exception e) {
            readImage = null;
        }
        return dim;
    }
}
