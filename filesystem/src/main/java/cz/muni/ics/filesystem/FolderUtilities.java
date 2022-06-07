package cz.muni.ics.filesystem;

import com.fabriceci.fmc.error.ClientErrorMessage;
import com.fabriceci.fmc.error.FileManagerException;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FolderUtilities {

    public static boolean folderExists(String folderPathString) throws FileManagerException {
        FileUtilities.checkPath(new File(folderPathString), true);
        return true;
    }

    public static Path createFolder(String folderName, Path parentPath) throws FileManagerException {
        FileUtilities.checkPath(parentPath.toFile(), true);
        FileUtilities.checkWritePermission(parentPath.toFile());
        return createFolder(folderName, parentPath, false);
    }

    public static Path createFolder(String folderName, Path parentPath, boolean createParentsIfNotExist)
            throws FileManagerException {
        FileUtilities.checkFileName(folderName);
        try {
            return (createParentsIfNotExist) ?
                    Files.createDirectories(new File(parentPath.toFile(), folderName).toPath()) :
                    Files.createDirectory(new File(parentPath.toFile(), folderName).toPath());
        } catch (IOException e) {
            throw new FileManagerException("Error creating directory: ", e); }
    }
}
