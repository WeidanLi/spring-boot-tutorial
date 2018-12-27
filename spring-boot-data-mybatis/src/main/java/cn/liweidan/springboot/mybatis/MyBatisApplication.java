package cn.liweidan.springboot.mybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Description：MyBatis项目启动器
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/12/27 7:01 PM
 * @email toweidan@126.com
 */
@SpringBootApplication
@MapperScan(basePackages = "cn.liweidan.springboot.mybatis.mapper")
public class MyBatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyBatisApplication.class, args);
    }

}
