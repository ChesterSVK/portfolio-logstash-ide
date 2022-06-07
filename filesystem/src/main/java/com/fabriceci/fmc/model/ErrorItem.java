package com.fabriceci.fmc.model;

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

import java.util.List;

public class ErrorItem {

    private String id;
    private int code;
    private String title;
    private ErrorMeta meta;

    public ErrorItem(String title) {
        this(title, null);
    }

    public ErrorItem(String message, List<String> arguments) {
        this.id = "server";
        this.code = 500;
        this.title = message;
        this.meta = new ErrorMeta();
        if(arguments!= null) {
            this.meta.setArguments(arguments);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String message) {
        this.title = message;
    }
}
