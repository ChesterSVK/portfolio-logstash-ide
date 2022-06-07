package cz.muni.ics.web.controllers;

/*-
 * #%L
 * Neck-Web
 * %%
 * Copyright (C) 2018 Masaryk University, Faculty of Informatics
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import cz.muni.ics.web.utils.ViewLinks;
import cz.muni.ics.web.utils.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Error page controller
 *
 * @author Jozef Cibík
 */

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping(ViewLinks.ERROR)
    public ModelAndView handleError(HttpServletRequest request, Model model, Exception e) {

        model.addAttribute("request", request);

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return new ModelAndView(getErrorPath(ViewNames.ERROR_404), model.asMap());
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return new ModelAndView(getErrorPath(ViewNames.ERROR_500), model.asMap());
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return new ModelAndView(getErrorPath(ViewNames.ERROR_403), model.asMap());
            }

        }
        return new ModelAndView(getErrorPath(ViewNames.ERROR), model.asMap());
    }

    @Override
    public String getErrorPath() {
        return getErrorPath(ViewNames.ERROR);
    }

    private String getErrorPath(String name) {
        return "error/" + name;
    }
}
