package cn.liweidan.springboot.tomcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Description：
 *
 * @author liweidan
 * @version 1.0
 * @date 2019/2/5 10:56 PM
 * @email toweidan@126.com
 */
@SpringBootApplication
public class InnertomcatApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(InnertomcatApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(InnertomcatApplication.class, args);
    }

}
