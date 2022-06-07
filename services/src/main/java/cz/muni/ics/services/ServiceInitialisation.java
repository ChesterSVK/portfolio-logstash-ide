package cz.muni.ics.services;

import com.fabriceci.fmc.error.FileManagerException;
import cz.muni.ics.filesystem.FolderUtilities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ServiceInitialisation {

    @Value("${services.root.output.directory.home}")
    private String workspacePath;
    @Value("${services.logstash.output.directory.home}")
    private String logstashDirName;
    @Value("${services.watchdog.output.directory.home}")
    private String watchdogDirName;
    @Value("${services.bro.output.directory.home}")
    private String broDirName;
    @Value("${services.json.output.directory.home}")
    private String jsonDirName;


    public void initialize() throws FileManagerException {
        Path workspaceRoot = Paths.get(workspacePath);
        if (!workspaceRoot.toFile().exists()){
            workspaceRoot = FolderUtilities.createFolder("application-workspace", workspaceRoot.getParent(), true);
        }
        if (!new File(workspaceRoot.toFile(), broDirName).exists()) FolderUtilities.createFolder(broDirName, workspaceRoot);
        if (!new File(workspaceRoot.toFile(), jsonDirName).exists()) FolderUtilities.createFolder(jsonDirName, workspaceRoot);
        if (!new File(workspaceRoot.toFile(), watchdogDirName).exists()) FolderUtilities.createFolder(watchdogDirName, workspaceRoot);
        if (!new File(workspaceRoot.toFile(), logstashDirName).exists()) FolderUtilities.createFolder(logstashDirName, workspaceRoot);
    }
}
