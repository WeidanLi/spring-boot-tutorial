package cn.liweidan.springboot.swagger2.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Description：Swagger2 配置
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/22 5:50 PM
 * @email toweidan@126.com
 */
@Configuration
public class Swagger2Config {

    @Bean
    public Docket swaggerSpringMvcPlugin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo())
                .select()
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("WEIDAN 的 SpringBoot 示例")
                .description("主要讲解了如何使用 Swagger2 整合 SpringBoot")
                .version("2.0")
                .build();
    }

}
