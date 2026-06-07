package com.xu.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Predicates;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * swagger 配置
 *
 * @author AITIAN
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        // OAS（OpenAPI标准）
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 过滤条件，扫描指定路径下的文件
                .apis(RequestHandlerSelectors.any())
                // 指定路径处理，PathSelectors.any()代表不过滤任何路径
                //错误路径不监控
                .paths(Predicates.negate(PathSelectors.regex("/error.*")))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        //标题
        String title = "AITIAN 商城后台管理系统接口文档";
        //简单描述
        String description = "一个简单明了的接口信息文档--AITIAN";
        //版本
        String version = "V1.0.0";
        // url接口路径前缀
        String termsOfServiceUrl = "/";
        //作者信息
        //协议
        String license = "The Apache License 2.0";
        //协议url
        String licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0";

        /*作者信息*/
        Contact contact = new Contact("AITIAN", "https://gitee.com/xustudy01/study1", "1968215915@qq.com");
        return new ApiInfo(title, description, version, termsOfServiceUrl, contact, license, licenseUrl, new ArrayList<>());
    }

}