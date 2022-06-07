package cz.muni.ics.services.implementations;

import cz.muni.ics.services.interfaces.PathsCollection;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Hash Set collection used for paths handling.
 *
 * @author Jozef Cibík
 */
public class FilePathsHashSet extends HashSet<Path> implements PathsCollection {

    public FilePathsHashSet() {
        super();
    }

    public FilePathsHashSet(Collection<? extends Path> c) {
        super(c);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////OVERRIDES

    @Override
    public boolean contains(Path path) {return super.contains(path);}

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean addAsFile(Path filePath) {
        return Files.isRegularFile(filePath) && this.add(filePath);
    }

    @Override
    public boolean addAllAsFile(List<Path> filePaths) {
        int size = this.size();
        filePaths.forEach(this::addAsFile);
        return size != this.size();
    }

    @Override
    public boolean addAsDirectory(Path directoryPath) {
        int size = this.size();

        if (!Files.isDirectory(directoryPath)){
            return this.addAsFile(directoryPath);
        }

        File dir = directoryPath.toFile();

        //Do not allow hidden files in this implementation
        if (dir.isHidden()){
            return false;
        }

        File[] files = dir.listFiles();
        for (File f : files){
            if (f.isHidden()){
                continue;
            }
            if (f.isDirectory()){
                this.addAsDirectory(f.toPath());
            }
            if (f.isFile()){
                this.addAsFile(f.toPath());
            }
        }
        return size != this.size();
    }

    @Override
    public boolean addAllAsDirectory(List<Path> directoryPaths) {
        int result = this.size();
        for (Path p : directoryPaths){
            this.addAsDirectory(p);
        }
        return result != this.size();
    }

    @Override
    public boolean removeAsFile(Path filePath) {
        return Files.isRegularFile(filePath) && this.remove(filePath);
    }

    @Override
    public boolean removeAllAsFile(List<Path> filePaths) {
        int size = this.size();
        filePaths.forEach(this::removeAsFile);
        return size != this.size();
    }

    @Override
    public boolean removeAsDirectory(Path filePath) {
        return this.removeAll(this.stream().filter(path -> path.toAbsolutePath().startsWith(filePath)).collect(Collectors.toList()));
    }

    @Override
    public boolean removeAllAsDirectory(List<Path> filePaths) {
        int result = this.size();
        for (Path p: filePaths){
           this.removeAsDirectory(p);
        }
        return result != this.size();
    }

    @Override
    public boolean containsExtension(String extension) {
        return this.getPathsWithExtension(extension).size() != 0;
    }

    @Override
    public List<Path> getPathsWithExtension(String extension) {
        return this.stream().filter(path -> FilenameUtils.getExtension(path.toString()).equals(extension)).collect(Collectors.toList());
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void clearPaths() {
        this.clear();
    }

    @Override
    public void clearPaths(Collection<Path> paths) {
        this.clear();
        this.addAllAsDirectory(paths.stream().map(Path::toAbsolutePath).collect(Collectors.toList()));
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return super.removeAll(c);
    }

    @Override
    public Iterator<Path> iterator() {
        return super.iterator();
    }

    @Override
    public Spliterator<Path> spliterator() {
        return super.spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super Path> filter) {
        return super.removeIf(filter);
    }

}
