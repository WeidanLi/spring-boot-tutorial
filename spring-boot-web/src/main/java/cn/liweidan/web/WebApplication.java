package cn.liweidan.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Web项目启动器
 * @author liweidan
 * @date 2018/8/22 上午9:59
 * @email toweidan@126.com
 */
@SpringBootApplication // #1
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args); // #2
    }

}
