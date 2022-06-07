package cz.muni.ics.services.implementations.filesystem;

import com.fabriceci.fmc.IFileManager;
import com.fabriceci.fmc.error.FMInitializationException;
import com.fabriceci.fmc.impl.LocalFileManager;
import cz.muni.ics.services.interfaces.filesystem.FileManagerService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class FileManagerServiceImpl implements FileManagerService {

    private IFileManager fileManager = new LocalFileManager();

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    public FileManagerServiceImpl() throws FMInitializationException {}

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Overrides

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) {
        fileManager.handleRequest(request, response);
    }
}
