package cn.liweidan.springboot.webinterceptor.simpleweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description：启动器
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/19 12:24 PM
 * @email toweidan@126.com
 */
@SpringBootApplication(scanBasePackages = {"cn.liweidan.springboot"}) // 使用注解指定这是 spring-boot 项目的启动类
public class SimpleWebApplication {

    public static void main(String[] args) {
        // 固定写法，传入本类以及参数 该参数是可以在启动的时候指定覆盖配置的
        SpringApplication.run(SimpleWebApplication.class, args);
    }

}
