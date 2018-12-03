# Table of Contents

* [springboot-web 拦截器的使用](#springboot-web-拦截器的使用)
  * [一. 简述](#一-简述)
  * [二. 开发](#二-开发)
    * [1. `mvn` 新增 `web-starter` 的依赖](#1-`mvn`-新增-`web-starter`-的依赖)
    * [2. 设置一个资源控制器](#2-设置一个资源控制器)
    * [3. 开发拦截器](#3-开发拦截器)
    * [4. 配置拦截器注入项目](#4-配置拦截器注入项目)
    * [5. 接口测试](#5-接口测试)
  * [三. 总结](#三-总结)


# springboot-web 拦截器的使用

## 一. 简述

用过 `SpringMVC` 的应该都知道拦截器，拦截器可以设置在 `SpringMVC` 接收请求之前处理（返回 `true` 继续执行或 `false` 拒绝执行），方法处理请求之后，以及处理完整个请求之后的操作。例如：单体项目的登陆拦截、在进入处理器之前先给线程栈设置一个公用的 `UserThreadLocal` 等等。

**项目示例：`web-interceptor`**

## 二. 开发

### 1. `mvn` 新增 `web-starter` 的依赖

```xml
<dependencies>
    <!-- 引入 web-starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

拦截器依然默认是存在 `web-starter` 里面的。

### 2. 设置一个资源控制器

```java
package cn.liweidan.springboot.simpleweb.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Description：HelloWorld 控制器
 * @author liweidan
 * @version 1.0
 * @date 2018/11/19 12:14 PM
 * @email toweidan@126.com
 */
@RestController // 使用 RestController，指定该控制器输出都是 json 对象
public class HelloEndpoint {

    @GetMapping // 使用 GetMapping 代替 RequestMapping、同理还有 PostMapping PutMapping DeleteMapping
    public Map<String, String> helloWorld() {
        Map<String, String> helloMap = new HashMap<>(1);
        helloMap.put("hello", "world");
        return helloMap;
    }

}

```

### 3. 开发拦截器

```JAVA
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

```

`Spring` 5.0 之前是需要重写所有方法的，现在都是默认接口方法了，要靠自己重写所需要的。

一般重写 `preHandle` 方法，但这里需要注意的是，需要设置不通过后的响应问，不然会出现状态码 `200` 但是没有响应体的情况，我这里返回 `401`

### 4. 配置拦截器注入项目

```java
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
@Configuration // 配置类必备
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注入拦截器并设置拦截路径
        registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/**");
    }

}
```

注意两点：

1. 同样，`WebMvcConfigurer` 都是默认方法的形式，只需要重写所需要的即可。

2. `@Configuration` 是编写配置类必备的，不要忘记。

### 5. 接口测试

```
// 传递错误的 Authorization 头
GET http://localhost:8080
Accept: application/json
Authorization: weidan0

HTTP/1.1 401 
Content-Length: 0
Date: Wed, 21 Nov 2018 17:44:01 GMT

<Response body is empty>

Response code: 401; Time: 29ms; Content length: 0 bytes

---------------------------------------------------------
// 正确的姿势传递
GET http://localhost:8080
Accept: application/json
Authorization: weidan

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Wed, 21 Nov 2018 17:50:32 GMT

{
  "hello": "world"
}

Response code: 200; Time: 189ms; Content length: 17 bytes
```

## 三. 总结

使用拦截器来开发登陆拦截，权限是可行的，就是需要自己编写较多的逻辑如查询数据库啦，判断啦。

所以如果权限模型简单的话，是没有问题的，如果权限问题比较复杂可以结合著名框架 `shrio` `spring-security` 等协助开发，当然像我们公司，权限模型自定义程度高，框架是解决不了的，也只能手撸了。

在日常开发中，一般 `Authorization` 存放的是 `jwt` 验证字符串，具体后面再细说。在微服务架构中，拦截器可就没什么用场了，我能想到的就是在当前线程栈中设置用户信息这个需求了。一般微服务架构权限的实现会交给路由来解决。

































