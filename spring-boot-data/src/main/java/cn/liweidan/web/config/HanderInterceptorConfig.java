package cn.liweidan.web.config;

import cn.liweidan.web.common.interceptors.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 注册拦截器
 *
 * @author liweidan
 * @date 2018/8/22 下午3:02
 * @email toweidan@126.com
 */
@Configuration
public class HanderInterceptorConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/hello-springboot");
    }

}
