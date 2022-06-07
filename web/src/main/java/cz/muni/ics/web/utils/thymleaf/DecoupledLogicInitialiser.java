package cz.muni.ics.web.utils.thymleaf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class DecoupledLogicInitialiser {

    private final SpringResourceTemplateResolver templateResolver;

    public DecoupledLogicInitialiser(SpringResourceTemplateResolver templateResolver) {
        this.templateResolver = templateResolver;
    }

    @PostConstruct
    public void initialise(){
        templateResolver.setUseDecoupledLogic(true);
        log.info("Decoupled template logic enabled.");
    }
}
