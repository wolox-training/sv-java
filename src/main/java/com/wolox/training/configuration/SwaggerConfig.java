package com.wolox.training.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build().apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo(){
        return new ApiInfoBuilder().title("Training")
                .description("Training app")
                .contact(new Contact("Author", "https://github.com/susanavarela", "email@gmail.com"))
                .license("Apache 2.0")
                .licenseUrl("https://www.apache.org")
                .version("1.0.0")
                .build();
    }
}
