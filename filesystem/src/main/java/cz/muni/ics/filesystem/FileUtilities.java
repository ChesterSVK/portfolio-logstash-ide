package cz.muni.ics.filesystem;


import com.fabriceci.fmc.error.ClientErrorMessage;
import com.fabriceci.fmc.error.FileManagerException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public final class FileUtilities {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Public

    public static void checkPath(File fileToCheck, Boolean isDirectory) throws FileManagerException {
        if (!fileToCheck.exists()) {
            throw new FileManagerException(
                    ClientErrorMessage.FILE_DOES_NOT_EXIST, Collections.singletonList(fileToCheck.getName()));
        }

        if (isDirectory != null) {
            if (fileToCheck.isDirectory() && !isDirectory) {
                throw new FileManagerException(
                        ClientErrorMessage.INVALID_FILE_TYPE, Collections.singletonList(fileToCheck.getName()));
            }
            if (!fileToCheck.isDirectory() && isDirectory) {
                throw new FileManagerException(
                        ClientErrorMessage.INVALID_FILE_TYPE, Collections.singletonList(fileToCheck.getName()));
            }
        }
    }

    public static void checkWritePermission(File fileToCheck) throws FileManagerException {
        if (!fileToCheck.canWrite()) {
            throw new FileManagerException(ClientErrorMessage.NOT_ALLOWED_SYSTEM);
        }
    }

    public static void renameFile(File oldFile, String newFileName) throws FileManagerException {
        checkFileName(newFileName);
        File newFile = new File(oldFile.getParent(), newFileName);
        try {
            FileUtils.moveFile(oldFile, newFile);
        } catch (IOException e) {
            throw new FileManagerException("Error renaming file", e);
        }
    }

    public static Path createFile(String fileContent, String fileName, Path parentPath, boolean timestamp) throws IOException {
        String finalFileName = timestamp ? createConfigName(fileName) : fileName;
        Path newFile = Files.createFile(new File(parentPath.toFile(), finalFileName).toPath());
        Files.write(newFile, fileContent.getBytes());
        return newFile;
    }

    static void checkFileName(String nameToCheck) throws FileManagerException {
        if (nameToCheck.length() == 0) {
            throw new FileManagerException(ClientErrorMessage.FORBIDDEN_NAME, Collections.singletonList(nameToCheck));
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private static String createConfigName(String prefix) {
        return prefix + "_" + new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date());
    }
}
