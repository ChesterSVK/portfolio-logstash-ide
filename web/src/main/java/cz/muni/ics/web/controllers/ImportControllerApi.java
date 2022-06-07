package cz.muni.ics.web.controllers;

import cz.muni.ics.services.exceptions.ImportException;
import cz.muni.ics.services.implementations.web.dto.ImportFileDto;
import cz.muni.ics.services.interfaces.ImportService;
import cz.muni.ics.web.utils.AjaxResponseBody;
import cz.muni.ics.web.utils.ViewLinks;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(ViewLinks.IMPORT)
public class ImportControllerApi {

    private final ImportService importService;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public ImportControllerApi(ImportService importService) {
        this.importService = importService;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    API Mappings

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////    Pick

    @ResponseBody
    @GetMapping(value = ViewLinks.PICK_FILES_LINK + ViewLinks.API_LINK)
    public AjaxResponseBody listOriginalFiles() {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setCode(HttpStatus.OK);
        responseBody.setData(importService.getOriginalFiles());
        return responseBody;
    }

    @ResponseBody
    @PostMapping(value = ViewLinks.PICK_FILES_LINK + ViewLinks.API_LINK)
    public AjaxResponseBody importFiles(@RequestBody List<ImportFileDto> files) {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setCode(HttpStatus.OK);
        responseBody.setData(importService.importFiles(files));
        responseBody.setRedirectURL(ViewLinks.IMPORT + ViewLinks.CONVERT_LINK);
        return responseBody;
    }

    @ResponseBody
    @PostMapping(value = ViewLinks.PICK_FILES_LINK + ViewLinks.API_LINK + "/type")
    public AjaxResponseBody importChooseType(@RequestBody String type) {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setCode(HttpStatus.OK);
        importService.chooseType(type);
        responseBody.setData("OK");
        responseBody.setRedirectURL(ViewLinks.IMPORT + ViewLinks.CONVERT_LINK);
        return responseBody;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////    Convert

    @ResponseBody
    @GetMapping(value = ViewLinks.CONVERT_LINK + ViewLinks.API_LINK)
    public AjaxResponseBody convertApiListWorkingFiles() {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setCode(HttpStatus.OK);
        responseBody.setData(importService.getWorkingFiles());
        return responseBody;
    }

    @ResponseBody
    @GetMapping(value = ViewLinks.CONVERT_LINK + ViewLinks.API_LINK + "/getSavedApplicationData")
    public AjaxResponseBody convertApiListSavedApplicationData() throws ImportException {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setCode(HttpStatus.OK);
        responseBody.setData(importService.getSavedApplicationData(importService.getConvertType()));
        return responseBody;
    }

    //Used after convertApiPost in front-end when only loading configuration is required.
    @ResponseBody
    @PostMapping(value = ViewLinks.CONVERT_LINK + ViewLinks.API_LINK + "/useSavedApplicationData")
    public AjaxResponseBody convertApiUseSavedApplicationData(@RequestParam("logstashCommandId") String id) {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setCode(HttpStatus.OK);
        importService.loadLogstashTransformation(id);
        responseBody.setRedirectURL(ViewLinks.IMPORT + ViewLinks.TRANSFORM_LINK);
        return responseBody;
    }

    @ResponseBody
    @PostMapping(value = ViewLinks.CONVERT_LINK + ViewLinks.API_LINK)
    public AjaxResponseBody convertApiPost(@RequestBody List<String> paths) throws ImportException {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setCode(HttpStatus.OK);
        importService.convertPaths(paths);
        responseBody.setData("OK");
        responseBody.setRedirectURL(ViewLinks.IMPORT + ViewLinks.TRANSFORM_LINK);
        return responseBody;
    }

    //    Transformation API in LogstashController
}
