package cz.muni.ics.services.interfaces;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

/**
 * This collection is used for paths manipulation representing system file paths
 *
 * @author Jozef Cibík
 */
public interface PathsCollection extends Collection<Path> {

    /**
     * This method will add path to the collection treating it as a regular file path
     *
     * @param filePath file to add
     * @return true on success, false otherwise
     */
    boolean addAsFile(Path filePath);

    /**
     * This method will add all paths to the collection treating them as a regular file path
     *
     * @param filePaths file to add
     * @return true on success, false otherwise
     */
    boolean addAllAsFile(List<Path> filePaths);

    /**
     * This method will remove path from the collection treating it as a regular file path
     *
     * @param filePath file to remove
     * @return true on success, false otherwise
     */
    boolean removeAsFile(Path filePath);

    /**
     * This method will remove paths from the collection treating them as a regular file path
     *
     * @param filePaths file to remove
     * @return true on success, false otherwise
     */
    boolean removeAllAsFile(List<Path> filePaths);

    /**
     * This method will treat path as a directory file path, which means all children of this directory will be
     * added to this collection.
     *
     * @param directoryPath directory to add
     * @return true on success, false otherwise
     */
    boolean addAsDirectory(Path directoryPath);

    /**
     * This method will treat paths as a directories file paths, which means all children of this directories will be
     * added to this collection.
     *
     * @param directoryPaths directory to add
     * @return true on success, false otherwise
     */
    boolean addAllAsDirectory(List<Path> directoryPaths);

    /**
     * This method will treat path as a directory file path, which means all children of this directory will be
     * removed from this collection.
     *
     * @param directoryPath directory to add
     * @return true on success, false otherwise
     */
    boolean removeAsDirectory(Path directoryPath);

    /**
     * This method will treat paths as a directories file paths, which means all children of this directories will be
     * removed from this collection.
     *
     * @param directoryPaths directory to add
     * @return true on success, false otherwise
     */
    boolean removeAllAsDirectory(List<Path> directoryPaths);

    /**
     * Checks whether path with given extension is in collection
     * @param extension to find
     * @return true if found, false otherwise
     */
    boolean containsExtension(String extension);

    /**
     * Checks whether collection contains path
     * @param path to find
     * @return true if found, false otherwise
     */
    boolean contains(Path path);

    /**
     * Retrieves paths with given extension from collection
     * @param extension to find
     * @return List of paths if found, empty list otherwise
     */
    List<Path> getPathsWithExtension(String extension);

    /**
     * Clears this collection
     */
    void clearPaths();

    /**
     * Clear this collection and initializes it with given new List of paths
     * @param paths to add after clean
     */
    void clearPaths(Collection<Path> paths);
}
