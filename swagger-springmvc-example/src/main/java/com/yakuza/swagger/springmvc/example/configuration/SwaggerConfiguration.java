package com.yakuza.swagger.springmvc.example.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Example of swagger configuration.
 */
@EnableSwagger2
public class SwaggerConfiguration {

    //Define logger.
    private static final Logger LOG = LoggerFactory.getLogger(SwaggerConfiguration.class);

    //Log the configuration initialization.
    @PostConstruct
    private void init() {
        LOG.info("Initialized.");
    }

    /**
     * Swagger bean.
     *
     * @return
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yakuza.swagger.springmvc.example.v1.controllers"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET,
                        newArrayList(
                                new ResponseMessageBuilder().code(500)
                                        .message("Upps...some server misbehaviour took place. It's implied to read log files.")
                                        .responseModel(new ModelRef("Error")).build(),
                                new ResponseMessageBuilder().code(403).message("What can I do for you?").build())
                );
    }

    /**
     * Just creates API info.
     *
     * @return
     */
    private ApiInfo apiInfo() {

        ApiInfo apiInfo = new ApiInfo(
                "My title of REST API"
                , "API for my service."
                , "0.1"
                , "Terms of service"
                , new Contact("Eugene N. Yatsura", "", "yatsura.evgeny@gmail.com")
                , "License of API"
                , "API license URL"
        );
        return apiInfo;
    }
}