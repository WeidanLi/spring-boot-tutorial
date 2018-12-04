package cn.liweidan.springboot.logback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description：启动器
 *
 * @author liweidan
 * @version 1.0
 * @date 2018-12-03 17:39
 * @email toweidan@126.com
 */
@SpringBootApplication
public class LogBackApplication {

    public static void main(String[] args) {
        System.setProperty("log.path", "./debug");
        SpringApplication.run(LogBackApplication.class, args);
    }

}
