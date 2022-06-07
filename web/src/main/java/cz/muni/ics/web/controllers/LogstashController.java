package cz.muni.ics.web.controllers;

import cz.muni.ics.services.interfaces.ImportService;
import cz.muni.ics.web.utils.AjaxResponseBody;
import cz.muni.ics.web.utils.ViewLinks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@RequestMapping(ViewLinks.IMPORT + ViewLinks.TRANSFORM_LINK)
public class LogstashController {

    private final ImportService importService;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public LogstashController(ImportService importService) {
        this.importService = importService;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Mappings

    @ResponseBody
    @GetMapping(value = ViewLinks.API_LINK)
    public AjaxResponseBody transformApiGet() {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setCode(HttpStatus.OK);
        responseBody.setData(importService.getParsedAttributes());
        return responseBody;
    }

    @ResponseBody
    @PostMapping(value = ViewLinks.API_LINK)
    public void transformApiPost(HttpServletRequest request, HttpServletResponse response) {
        importService.handleRequest(request, response);
    }
}
