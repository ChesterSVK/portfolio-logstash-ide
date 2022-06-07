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

public class FileAttributes {

    private String name;
    private String path;
    private int readable = 1;
    private int writable = 1;
    private Long created;
    private Long modified;
    private Integer height;
    private Integer width;
    private Long size;
    private String content;
    private Long files;

    private Long folders;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getModified() {
        return modified;
    }

    public void setModified(Long modified) {
        this.modified = modified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public int getReadable() {
        return readable;
    }

    public void setReadable(int readable) {
        this.readable = readable;
    }

    public int getWritable() {
        return writable;
    }

    public void setWritable(int writable) {
        this.writable = writable;
    }

    public boolean isReadable(){
        return this.readable == 1;
    }

    public boolean isWritable(){
        return this.writable == 1;
    }

    public Long getFiles() {
        return files;
    }

    public void setFiles(Long files) {
        this.files = files;
    }

    public Long getFolders() {
        return folders;
    }

    public void setFolders(Long folders) {
        this.folders = folders;
    }
}
