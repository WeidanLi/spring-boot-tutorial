package cn.liweidan.springboot.webinterceptor.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Description：权限拦截器
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/22 1:30 AM
 * @email toweidan@126.com
 */
public class AuthInterceptor implements HandlerInterceptor { // 1. 实现 HandlerInterceptor 接口

    // 重写 preHandle 方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 假设只有 Authorization 的值是 weidan 才给继续请求
        if (Objects.nonNull(authHeader) && !authHeader.isEmpty() && authHeader.equals("weidan")) {
            return true;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }

}
