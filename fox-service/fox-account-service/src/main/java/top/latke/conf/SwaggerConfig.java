package top.latke.conf;

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

/**
 * Swagger 配置类
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * Swagger 实例 Bean 是 Docket，所以通过配置 Docket 来实例配置 Swagger
     *
     * @return
     */
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("top.latke"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Swagger 的描述信息
     *
     * @return
     */
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("fox-micro-service")
                .description("微服务实例工程")
                .contact(new Contact("fox", "www.latke.top", "fox@qq.com"))
                .version("1.0")
                .build();
    }
}
