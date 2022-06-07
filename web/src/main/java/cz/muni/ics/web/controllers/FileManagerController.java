package cz.muni.ics.web.controllers;

import cz.muni.ics.services.interfaces.filesystem.FileManagerService;
import cz.muni.ics.web.utils.ViewLinks;
import cz.muni.ics.web.utils.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller for File Manager page
 * <p>
 * Original template from https://github.com/fabriceci/RichFilemanager-JAVA
 *
 * @author Jozef Cibík
 */
@Controller
@RequestMapping(ViewLinks.FILEMANAGER)
public class FileManagerController {

    private final FileManagerService fileManagerService;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public FileManagerController(FileManagerService fileManagerService) {
        this.fileManagerService = fileManagerService;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Mappings

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView fileManager() {
        return new ModelAndView(ViewNames.FILEMANAGER);
    }

    @RequestMapping(value = "/api")
    public void fileManagerApi(HttpServletRequest request, HttpServletResponse response) {
        fileManagerService.handleRequest(request, response);
    }
}
