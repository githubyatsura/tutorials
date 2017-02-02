package com.yakuza.swagger.springmvc.example.v1.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Controller
@RequestMapping(path = "/ping/error500")
public class TestErrorController {

    private static final Logger LOG = LoggerFactory.getLogger(TestErrorController.class);

    @Autowired
    private ReloadableResourceBundleMessageSource messageSource;

    @PostConstruct
    private void init() {
        LOG.info("Initialized.");
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Exception> generate500() {

        //Get localized message.
        String message = messageSource.getMessage("http.error.500", null, Locale.getDefault());

        return new ResponseEntity<Exception>(new Exception(message), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
