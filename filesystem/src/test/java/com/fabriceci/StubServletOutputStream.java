package com.fabriceci;

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

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// import javax.servlet.WriteListener;

public class StubServletOutputStream extends ServletOutputStream {

    private FileOutputStream outputStream;

    public StubServletOutputStream(File file){
        try {
            outputStream = new FileOutputStream(file);
        } catch(IOException ignore){}
    }

    public void write(int i) throws IOException {
        outputStream.write(i);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }
}
