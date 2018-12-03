package cn.liweidan.springboot.webinterceptor.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Description：web 配置
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/22 1:35 AM
 * @email toweidan@126.com
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注入拦截器并设置拦截路径
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**");
    }

}
