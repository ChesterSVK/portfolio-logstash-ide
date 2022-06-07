package cz.muni.ics.web.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import cz.muni.ics.logstash.commands.roots.RootCommandInstance;
import cz.muni.ics.logstash.interfaces.Root;
import cz.muni.ics.logstash.serialization.roots.RootDeserializer;
import cz.muni.ics.logstash.serialization.roots.RootSerializer;
import cz.muni.ics.services.config.ServiceConfiguration;
import cz.muni.ics.web.utils.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.SessionTrackingMode;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

/**
 * Spring configuration class for Web module
 *
 * @author Jozef Cibík
 */

@Configuration
@EnableWebMvc
@Import(ServiceConfiguration.class)
@PropertySource("classpath:application-web.properties")
@ComponentScan(basePackages = "cz.muni.ics.web")
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.mvc.locale}")
    private String userLocale;

    //For locale resolution
    @Bean
    public LocaleResolver localeResolver() {
        LocaleResolver resolver = new CookieLocaleResolver();
        ((CookieLocaleResolver) resolver).setDefaultLocale(Locale.forLanguageTag(userLocale));
        return new CookieLocaleResolver();
    }

    @Bean
//    public MultipartResolver multipartResolver() {
//        CommonsMultipartResolver multipartResolver
//                = new CommonsMultipartResolver();
//        multipartResolver.setMaxUploadSize(5000000000L);
//        return multipartResolver;
//    }
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setCacheSeconds(3600);
        //messageSource.setFileEncodings(UTF);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

//    @Bean
//    public ServletRegistrationBean adminServletRegistrationBean() {
//        return new ServletRegistrationBean(new UploadServlet(), "/uploadManager/upload");
//    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }


    //    Servlet
    @Bean
    public ServletContextInitializer initializer() {
        return servletContext -> {
            //Store sessionID in a cookie
            servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
//                default html escaping
            servletContext.setInitParameter("defaultHtmlEscape", "true");
        };
    }

//    @Bean
//    public SpringSecurityDialect springSecurityDialect(){
//        return new SpringSecurityDialect();
//    }

    //    ValiadtionMessages Encoding
    @Bean(name = "validationMessageSource")
    public MessageSource validationMessageSource() {
        ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
        bean.setBasename("classpath:ValidationMessages");
        bean.setDefaultEncoding("UTF-8");
        return bean;
    }

    @Bean(name = "validator")
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(validationMessageSource());
        return bean;
    }

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public Validator getValidator() {
        return validator();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Overrides
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/about").setViewName(ViewNames.ABOUT);
        registry.addViewController("/login").setViewName("admin/login");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
//                "/webjars/**"
//                for General purpose
                "/img/**",
                "/css/**",
                "/js/**",
                "/libs/**",
                "/plug-in/**"
        )
                .addResourceLocations(
//                        "classpath:/META-INF/resources/webjars/",
//                for General purpose
                        "classpath:/static/img/",
                        "classpath:/static/css/",
                        "classpath:/static/js/",
                        "classpath:/static/libs/",
                        "classpath:/plug-in/"
                );
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor =
                new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("langCode");
        registry.addInterceptor(localeChangeInterceptor);
    }
}
