package cz.muni.ics.web.controllers;

import cz.muni.ics.services.implementations.web.dto.ApplicationDataDto;
import cz.muni.ics.services.interfaces.HistoryService;
import cz.muni.ics.web.utils.AjaxResponseBody;
import cz.muni.ics.web.utils.ViewLinks;
import cz.muni.ics.web.utils.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
@RequestMapping(ViewLinks.HISTORY)
public class HistoryController {

    private final HistoryService historyService;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Mappings

    @RequestMapping("")
    public String history() {
        return ViewNames.HISTORY;
    }

    @RequestMapping("/api")
    @ResponseBody
    public AjaxResponseBody api() {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setCode(HttpStatus.OK);
        responseBody.setData("OK");
        return responseBody;
    }

    @GetMapping("/api/getAll")
    @ResponseBody
    public AjaxResponseBody getAllSavedConfigurations() {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setCode(HttpStatus.OK);
        responseBody.setData(historyService.getAllSavedApplicationData());
        return responseBody;
    }

    @PostMapping("/api/deleteItem")
    @ResponseBody
    public AjaxResponseBody deleteConfiguration(@RequestBody ApplicationDataDto applicationDataDto) {
        AjaxResponseBody responseBody = new AjaxResponseBody();
        responseBody.setCode(HttpStatus.OK);
        historyService.deleteApplicationData(applicationDataDto);
        responseBody.setData("OK");
        return responseBody;
    }

    @GetMapping("/api/downloadCommands")
    @ResponseBody
    public void downloadLogstashCommands(@RequestBody ApplicationDataDto applicationDataDto,
                                         HttpServletResponse response) throws IOException {
        File file = historyService.downloadLogstashCommands(applicationDataDto);
        InputStream in = new FileInputStream(file);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setHeader("Content-Length", String.valueOf(file.length()));
        FileCopyUtils.copy(in, response.getOutputStream());
    }
}
