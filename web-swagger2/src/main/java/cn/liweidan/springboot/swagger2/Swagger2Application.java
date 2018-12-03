package cn.liweidan.springboot.swagger2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Description：启动器
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/22 5:46 PM
 * @email toweidan@126.com
 */
@SpringBootApplication
@EnableSwagger2 // 启动 swagger 自动配置
public class Swagger2Application {

    public static void main(String[] args) {
        SpringApplication.run(Swagger2Application.class, args);
    }

}
