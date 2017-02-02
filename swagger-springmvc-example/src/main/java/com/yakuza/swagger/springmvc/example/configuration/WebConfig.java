package com.yakuza.swagger.springmvc.example.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.annotation.PostConstruct;
import java.util.Locale;

/**
 * Web configuration.
 */
@EnableWebMvc
@ComponentScan(basePackages = "com.yakuza.swagger.springmvc.example.v1")
@Configuration
@Import(SwaggerConfiguration.class)
@PropertySource("classpath:application.properties")
public class WebConfig extends WebMvcConfigurerAdapter {

    //Define logger.
    private static final Logger LOG = LoggerFactory.getLogger(WebConfig.class);

    //Default locale.
    public static final Locale DEFAULT_LOCALE = StringUtils.parseLocaleString("en");

    //Log the configuration initialization.
    @PostConstruct
    private void init() {
        LOG.info("Initialized.");
    }

    /**
     * Add resources handlers.
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //Adding resource handling for SWAGGER.
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * Get locale resolver.
     *
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        //Add cookie locale resolver.
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(DEFAULT_LOCALE);
        return cookieLocaleResolver;
    }


    /**
     * Add interceptors.
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //Add parameter for locale.
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }


    /**
     * Message sources.
     *
     * @return bundle of message sources.
     */
    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource getMessageSource() {
        ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
        resource.setBasename("classpath:messages");
        resource.setDefaultEncoding("UTF-8");

        // if true, the key of the message will be displayed if the key is not
        // found, instead of throwing a NoSuchMessageException
        resource.setUseCodeAsDefaultMessage(true);

        //Set updatable
        resource.setCacheSeconds(0);

        return resource;
    }

}
