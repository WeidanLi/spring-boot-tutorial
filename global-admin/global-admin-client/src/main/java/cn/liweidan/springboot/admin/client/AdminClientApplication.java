package cn.liweidan.springboot.admin.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Description：
 *
 * @author liweidan
 * @version 1.0
 * @date 2019/2/4 6:25 PM
 * @email toweidan@126.com
 */
@SpringBootApplication
public class AdminClientApplication {

    @Configuration
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests().anyRequest().permitAll()
                    .and().csrf().disable();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(AdminClientApplication.class, args);
    }


}
