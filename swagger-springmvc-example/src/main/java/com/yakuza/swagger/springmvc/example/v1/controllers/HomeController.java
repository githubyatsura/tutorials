package com.yakuza.swagger.springmvc.example.v1.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;

@Controller
public class HomeController {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @PostConstruct
    private void init() {
        LOG.info("Initialized.");
    }

    @RequestMapping("/")
    public String index() {
        LOG.info("Redirect to swagger-ui.");
        return "redirect:swagger-ui.html";
    }

}
