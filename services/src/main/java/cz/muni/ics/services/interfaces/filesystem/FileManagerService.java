package cz.muni.ics.services.interfaces.filesystem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Jozef Cibík on 21.05.2018.
 */

public interface FileManagerService {
    void handleRequest(HttpServletRequest request, HttpServletResponse response);
}
