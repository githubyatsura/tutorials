package com.yakuza.swagger.springmvc.example.v1.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

@Controller
@RequestMapping(path = "/ping")
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @PostConstruct
    private void init() {
        LOG.info("Initialized.");
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String ping() {
        return "Ping completed with success.";
    }
}
