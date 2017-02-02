package com.yakuza.swagger.springmvc.example.configuration;

import com.yakuza.swagger.springmvc.example.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;

/**
 * Sample of application initializer.
 */
public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    //Define logger.
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationInitializer.class);

    //Define mapping template.
    private static final String MAPPING_TEMPLATE = "%s/*";

    /**
     * Constructor.
     */
    public ApplicationInitializer() {
        LOG.info("Created.");
    }

    /**
     * Get root application context classes.
     *
     * @return
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    /**
     * Get servlet application context classes.
     *
     * @return
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    /**
     * Get servlet mappings.
     *
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{String.format(MAPPING_TEMPLATE, Constants.SERVICE_URL_V1)};
    }

    /**
     * Get servlet filters.
     *
     * @return
     */
    @Override
    protected Filter[] getServletFilters() {
        //Define character encoding filter.
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        return new Filter[]{characterEncodingFilter};
    }
}