package cz.muni.ics.web.controllers;

import cz.muni.ics.services.enums.ImportState;
import cz.muni.ics.services.exceptions.ImportException;
import cz.muni.ics.services.implementations.web.dto.ApplicationDataDto;
import cz.muni.ics.services.interfaces.ImportService;
import cz.muni.ics.web.utils.ViewAttributes;
import cz.muni.ics.web.utils.ViewLinks;
import cz.muni.ics.web.utils.ViewMessages;
import cz.muni.ics.web.utils.ViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Slf4j
@Controller
@RequestMapping(ViewLinks.IMPORT)
public class ImportController {

    private final ImportService importService;
    private final MessageSource messageSource;
    private final String stateAttributeName = "importState";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    @Autowired
    public ImportController(ImportService importService, MessageSource messageSource) {
        this.importService = importService;
        this.messageSource = messageSource;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Mappings

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////Start

    @RequestMapping(ViewLinks.START)
    public String home(Model model) {
        importService.resetImport();
        model.addAttribute(this.stateAttributeName, importService.getState().ordinal());
        return ViewNames.IMPORT;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////Pick

    @GetMapping(value = ViewLinks.PICK_FILES_LINK)
    public String pickGet(HttpServletRequest request, RedirectAttributes attributes, Model model, Locale locale) {
//        If direct access

        if (request.getHeader("referer") == null) {
            return redirectToStart(attributes, locale);
        }

        importService.resetToImportFiles();
        model.addAttribute(this.stateAttributeName, importService.getState().ordinal());
        return ViewNames.IMPORT;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////Convert
    @GetMapping(value = ViewLinks.CONVERT_LINK)
    public String convertGet(HttpServletRequest request, RedirectAttributes attributes, Model model, Locale locale) throws ImportException {

//        If direct access
        if (request.getHeader("referer") == null) {
            return redirectToStart(attributes, locale);
        }

        generateConvertFlashMessage(model, locale);
        importService.resetToConvert();
        model.addAttribute(this.stateAttributeName, importService.getState().ordinal());
        return ViewNames.IMPORT;
    }

    private void generateConvertFlashMessage(Model model, Locale locale) throws ImportException {
        if (importService.getState().ordinal() <= ImportState.FILES_CONVERTING.ordinal()) {
            if (!importService.getWorkingFiles().isEmpty()) {
                model.addAttribute(ViewAttributes.ALERT_SUCC, messageSource.getMessage(ViewMessages.SUCC_FILES_IMPORTED, null, locale));
            } else {
                model.addAttribute(ViewAttributes.ALERT_ERRO, messageSource.getMessage(ViewMessages.ERR_FILES_NOT_IMPORTED, null, locale));
            }
        }
        String type = importService.getConvertType().name().toLowerCase();
        model.addAttribute(type + "Settings", true);
    }

    private void generateTransformFlashMessage(Model model, Locale locale) {
        if (importService.getState().ordinal() <= ImportState.TRANSFORMING.ordinal()) {
            String finalMessage = "";
            if (!importService.getProcessedFiles().isEmpty()) {
                finalMessage += messageSource.getMessage(ViewMessages.SUCC_FILES_CONVERTED, null, locale);
            } else {
                finalMessage += messageSource.getMessage(ViewMessages.ERR_FILES_NOT_CONVERTED, null, locale);
            }
            if (!importService.getParsedAttributes().isEmpty()) {
                finalMessage += " " + messageSource.getMessage(ViewMessages.SUCC_ATTRIBUTES_CONVERTED, null, locale);
            } else {
                finalMessage += " " + messageSource.getMessage(ViewMessages.ERRO_ATTRIBUTES_NOT_CONVERTED, null, locale);
            }
            model.addAttribute(ViewAttributes.ALERT_INFO, finalMessage);
        }
    }

    private void generateSummaryFlashMessage(Model model, Locale locale) {
        if (importService.getState().ordinal() <= ImportState.SUMMARY.ordinal()) {
            if (!importService.getLogstashConfigurationAsJson().isEmpty()) {
                model.addAttribute(ViewAttributes.ALERT_SUCC, messageSource.getMessage(ViewMessages.SUCC_TRANSFORMATIONS, null, locale));
            } else {
                model.addAttribute(ViewAttributes.ALERT_ERRO, messageSource.getMessage(ViewMessages.ERRO_TRANSFORMATIONS, null, locale));
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////Transform
    @GetMapping(value = ViewLinks.TRANSFORM_LINK)
    public String transformGet(HttpServletRequest request, RedirectAttributes attributes, Model model, Locale locale) {
//        If direct access
        if (request.getHeader("referer") == null) {
            return redirectToStart(attributes, locale);
        }

        generateTransformFlashMessage(model, locale);
        importService.resetToTransform();

        model.addAttribute(this.stateAttributeName, importService.getState().ordinal());
        return ViewNames.IMPORT;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////Summary
    @GetMapping(value = ViewLinks.SUMMARY_LINK)
    public String summaryGet(HttpServletRequest request, RedirectAttributes attributes, Model model, Locale locale) throws ImportException {
//        If direct access
        if (request.getHeader("referer") == null) {
            return redirectToStart(attributes, locale);
        }

        generateSummaryFlashMessage(model, locale);
        importService.resetToSummary();

        model.addAttribute(this.stateAttributeName, importService.getState().ordinal());
        model.addAttribute("pickedFilesList", importService.getProcessedFiles());
        model.addAttribute("convertType", importService.getConvertType());
        model.addAttribute("transformationsJson", importService.getLogstashConfigurationAsJson());
        model.addAttribute("watchFoldersList", importService.getFoldersToWatch());
        model.addAttribute("applicationDataDto", new ApplicationDataDto(importService.getConvertType()));
        return ViewNames.IMPORT;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////Upload

    @PostMapping(value = ViewLinks.UPLOAD_TO_ES_LINK)
    public String summaryUpload(@ModelAttribute(value = "applicationDataDto") ApplicationDataDto dataDto,
                                RedirectAttributes attributes,
                                Locale locale) {
        try {
            if (importService.uploadToEs(dataDto)) {
                attributes.addFlashAttribute(ViewAttributes.ALERT_SUCC, messageSource.getMessage(ViewMessages.SUCC_UPLOADED_TO_ES, null, locale));
            } else {
                attributes.addFlashAttribute(ViewAttributes.ALERT_ERRO, messageSource.getMessage(ViewMessages.ERR_UPLOAD_FAILED, null, locale));
            }
        } catch (Exception e) {
            attributes.addFlashAttribute(ViewAttributes.ALERT_ERRO, messageSource.getMessage(ViewMessages.ERR_UPLOAD_ERROR, null, locale));
        } finally {
            importService.resetImport();
        }
        return "redirect:" + ViewLinks.IMPORT_START;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private String redirectToStart(RedirectAttributes attributes, Locale locale) {
        importService.resetToImportFiles();
        attributes.addFlashAttribute(ViewAttributes.ALERT_WARN, messageSource.getMessage(ViewMessages.WARN_DIRECT_ACCESS_NOT_AllOWED, null, locale));
        return "redirect:" + ViewLinks.IMPORT_START;
    }
}
