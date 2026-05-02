package com.daijia.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class Knife4jConfig {

    @Bean
    public Docket driverApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("司机端接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.daijia.controller.driver"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket clientApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("客户端接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.daijia.controller.client"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket commonApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("公共接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.daijia.controller.common"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket testApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("测试接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.daijia.controller.test"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("代驾系统API接口文档")
                .description("代驾系统后端API接口文档，包含司机端、客户端、公共接口等")
                .contact(new Contact("代驾系统开发团队", "", ""))
                .version("1.0.0")
                .build();
    }
}
