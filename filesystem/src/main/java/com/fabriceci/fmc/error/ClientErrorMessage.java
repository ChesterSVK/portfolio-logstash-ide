package com.fabriceci.fmc.error;

import java.util.ResourceBundle;

public final class ClientErrorMessage {

    private ClientErrorMessage(){}

    private static final ResourceBundle managerBundle = ResourceBundle.getBundle("manager");

//    public static final String ALLOWED_FILE_TYPE = managerBundle.getString("ALLOWED_FILE_TYPE");
//    public static final String AUTHORIZATION_REQUIRED = managerBundle.getString("AUTHORIZATION_REQUIRED");
    public static final String DIRECTORY_ALREADY_EXISTS = managerBundle.getString("DIRECTORY_ALREADY_EXISTS");
    public static final String DIRECTORY_EMPTY = managerBundle.getString("DIRECTORY_EMPTY");
    public static final String DIRECTORY_NOT_EXIST = managerBundle.getString("DIRECTORY_NOT_EXIST");
//    public static final String DISALLOWED_FILE_TYPE = managerBundle.getString("DISALLOWED_FILE_TYPE");
//    public static final String ERROR_CONFIG_FILE = managerBundle.getString("ERROR_CONFIG_FILE");
    public static final String ERROR_COPYING_DIRECTORY = managerBundle.getString("ERROR_COPYING_DIRECTORY");
    public static final String ERROR_COPYING_FILE = managerBundle.getString("ERROR_COPYING_FILE");
    public static final String ERROR_CREATING_ZIP = managerBundle.getString("ERROR_CREATING_ZIP");
//    public static final String ERROR_EXTRACTING_FILE = managerBundle.getString("ERROR_EXTRACTING_FILE");
//    public static final String ERROR_OPENING_FILE = managerBundle.getString("ERROR_OPENING_FILE");
//    public static final String ERROR_MOVING_DIRECTORY = managerBundle.getString("ERROR_MOVING_DIRECTORY");
//    public static final String ERROR_MOVING_FILE = managerBundle.getString("ERROR_MOVING_FILE");
    public static final String ERROR_RENAMING_DIRECTORY = managerBundle.getString("ERROR_RENAMING_DIRECTORY");
    public static final String ERROR_RENAMING_FILE = managerBundle.getString("ERROR_RENAMING_FILE");
//    public static final String ERROR_REPLACING_FILE = managerBundle.getString("ERROR_REPLACING_FILE");
    public static final String ERROR_SAVING_FILE = managerBundle.getString("ERROR_SAVING_FILE");
    public static final String ERROR_SERVER = managerBundle.getString("ERROR_SERVER");
//    public static final String ERROR_UPLOADING_FILE = managerBundle.getString("ERROR_UPLOADING_FILE");
//    public static final String ERROR_WRITING_PERM = managerBundle.getString("ERROR_WRITING_PERM");
    public static final String FILE_ALREADY_EXISTS = managerBundle.getString("FILE_ALREADY_EXISTS");
    public static final String FILE_DOES_NOT_EXIST = managerBundle.getString("FILE_DOES_NOT_EXIST");
    public static final String FILE_EMPTY = managerBundle.getString("FILE_EMPTY");
    public static final String FORBIDDEN_ACTION_DIR = managerBundle.getString("FORBIDDEN_ACTION_DIR");
//    public static final String FORBIDDEN_ACTION_FILE = managerBundle.getString("FORBIDDEN_ACTION_FILE");
    public static final String FORBIDDEN_CHAR_SLASH = managerBundle.getString("FORBIDDEN_CHAR_SLASH");
//    public static final String FORBIDDEN_CHANGE_EXTENSION = managerBundle.getString("FORBIDDEN_CHANGE_EXTENSION");
    public static final String FORBIDDEN_NAME = managerBundle.getString("FORBIDDEN_NAME");
//    public static final String INVALID_ACTION = managerBundle.getString("INVALID_ACTION");
//    public static final String INVALID_DIRECTORY_OR_FILE = managerBundle.getString("INVALID_DIRECTORY_OR_FILE");
    public static final String INVALID_FILE_TYPE = managerBundle.getString("INVALID_FILE_TYPE");
//    public static final String INVALID_FILE_UPLOAD = managerBundle.getString("INVALID_FILE_UPLOAD");
//    public static final String INVALID_VAR = managerBundle.getString("INVALID_VAR");
//    public static final String NOT_FOUND_LANGUAGE_FILE = managerBundle.getString("NOT_FOUND_LANGUAGE_FILE");
//    public static final String NOT_FOUND_SYSTEM_MODULE = managerBundle.getString("NOT_FOUND_SYSTEM_MODULE");
    public static final String MODE_ERROR = managerBundle.getString("MODE_ERROR");
    public static final String NOT_ALLOWED = managerBundle.getString("NOT_ALLOWED");
    public static final String NOT_ALLOWED_SYSTEM = managerBundle.getString("NOT_ALLOWED_SYSTEM");
//    public static final String STORAGE_SIZE_EXCEED = managerBundle.getString("STORAGE_SIZE_EXCEED");
    public static final String UNABLE_TO_CREATE_DIRECTORY = managerBundle.getString("UNABLE_TO_CREATE_DIRECTORY");
    public static final String UNABLE_TO_OPEN_DIRECTORY = managerBundle.getString("UNABLE_TO_OPEN_DIRECTORY");
    public static final String UPLOAD_FILES_SMALLER_THAN = managerBundle.getString("UPLOAD_FILES_SMALLER_THAN");
//    public static final String UPLOAD_IMAGES_ONLY = managerBundle.getString("UPLOAD_IMAGES_ONLY");
//    public static final String UPLOAD_IMAGES_TYPE_JPEG_GIF_PNG = managerBundle.getString("UPLOAD_IMAGES_TYPE_JPEG_GIF_PNG");
}
