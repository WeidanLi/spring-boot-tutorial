package cn.liweidan.web;

import cn.liweidan.web.dbo.UserDo;
import cn.liweidan.web.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Web项目启动器
 * @author liweidan
 * @date 2018/8/22 上午9:59
 * @email toweidan@126.com
 */
@SpringBootApplication // #1
@ServletComponentScan
public class DataApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class, args); // #2
    }

    @Bean
    public CommandLineRunner demo(UserRepository userRepository) {
        return (args) -> {
            // 启动时进行初始化
            if (userRepository.findAll().size() < 1) {
                userRepository.save(new UserDo("Jane", 18));
                userRepository.save(new UserDo("Tony", 22));
                userRepository.save(new UserDo("Jenny", 31));
                userRepository.save(new UserDo("Gogo", 54));
            }
        };
    }

}
