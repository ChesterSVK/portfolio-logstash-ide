package cz.muni.ics.web.controllers;

import cz.muni.ics.web.utils.ViewLinks;
import cz.muni.ics.web.utils.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(ViewLinks.HOME)
public class HomeController {

    @RequestMapping("")
    public String home() {
        return ViewNames.HOME;
    }
}
