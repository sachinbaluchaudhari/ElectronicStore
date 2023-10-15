package com.electonic.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket docket()
    {
        Docket docket=new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(getApiInfo());
        docket.securityContexts(Arrays.asList(getSecurityContext()));
        docket.securitySchemes(Arrays.asList(apiKey()));

        ApiSelectorBuilder select = docket.select();
        select.apis(RequestHandlerSelectors.any());
        select.paths(PathSelectors.any());
        docket = select.build();

        return docket;
    }
    public ApiInfo getApiInfo() {
        ApiInfo apiInfo=new ApiInfo(
                "Electronic store: Backend",
                "This is electronic store project created by Sach",
                "1.0.0v",
                "https://instagram.com/_.sachin_chaudhari._?igshid=MzRlODBiNWFlZA==",
                new Contact("Sachin Chaudhari","https://instagram.com/_.sachin_chaudhari._?igshid=MzRlODBiNWFlZA==","sachinbaluchaudhari@gmail.com"),
                "Licence of APIs",
                "https://instagram.com/_.sachin_chaudhari._?igshid=MzRlODBiNWFlZA==",
                new ArrayList()

        );
        return apiInfo;
    }


    private SecurityContext getSecurityContext() {
        SecurityContext context=SecurityContext
                .builder()
                .securityReferences(getSecurityReferences())
                .build();
return context;
    }

    private List<SecurityReference> getSecurityReferences() {
        AuthorizationScope[] scopes={new AuthorizationScope("Global","Access Every Thing")};
        return Arrays.asList(new SecurityReference("jwtToken",scopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("jwtToken","Authorization","header");
    }

}
