package cz.muni.ics.web.controllers;

import cz.muni.ics.services.interfaces.elasticsearch.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@ControllerAdvice
@RequestMapping("*")
public class GeneralController {

    @Value("${elasticsearch.port}")
    private Integer elasticsearchPort;

    @Value("${elasticsearch.cluster}")
    private String clusterName;

    private final ElasticsearchService elasticsearchService;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public GeneralController(ElasticsearchService elasticsearchService) {
        this.elasticsearchService = elasticsearchService;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Mappings

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Model

    @ModelAttribute
    public void addModelAttributes(Model model) {
        model.addAttribute("langCode", LocaleContextHolder.getLocale());
        model.addAttribute("containerID", 1);
        model.addAttribute("esHealth", elasticsearchService.getHealthService().checkElasticsearchHealth());
        model.addAttribute("esPort", elasticsearchPort);
        model.addAttribute("esClusterName", clusterName);
    }
}
