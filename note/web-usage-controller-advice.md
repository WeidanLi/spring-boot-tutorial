# springboot-web 常用异常信息返回

## 一. 简述

在日常的开发中，都会有多多少少的异常发生，如：不存在异常、参数异常等等。那这时候怎么向前端展示也是一个问题，通常的做法就是建立一个通用的数据返回类注入 `ResultDto` 然后存储是否顺利调用，数据，错误信息等信息。结合 `spring-boot-web` 的监听器，可以让我们尽量少的关注这些错误异常的发生，只要一句话或者一个注解【需要自己封装，使用 `aop` 】，就可以实现程序给我们的自动验证。

**示例代码：`web-exception-resp`**

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

### 2. 准备一个标准输出类和一些异常

标准数据响应类：

```java
public class ResultDto<T> {

    private boolean success;
    private T data;
    private String message;

    public ResultDto(T data) {
        this.success = true;
        this.data = data;
    }

    public ResultDto(String message) {
        this.success = false;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

}
```



准备了一些异常，一个基类，两个子类：

```java
public class AbsException extends RuntimeException {

    public AbsException() {
    }

    public AbsException(String message) {
        super(message);
    }

}

public class ElementNotFoundException extends AbsException {

    public ElementNotFoundException(String elementName) {
        super(elementName + "不存在");
    }

}

public class ParamInvalidException extends AbsException {

    public ParamInvalidException() {
        super("参数错误");
    }

}
```



### 3. 资源和资源控制器

我假设，`id` 超过 `100` 的就是参数错误了，如果 `id` 是个偶数就是资源不存在

```java
public class UserDto {

    private Long id;
    private String name;
    private Integer age;

    public UserDto(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public UserDto() {
    }

    // 省略 getter 和 setter
}

@RestController
@RequestMapping("user")
public class UserEndpoint {


    @GetMapping("{id}")
    public ResultDto<UserDto> idOf(@PathVariable("id") Long id) {
        if (id > 100) {
            throw new ParamInvalidException();
        }
        if (id % 2 == 0) {
            throw new ElementNotFoundException("用户");
        }
        UserDto userDto = new UserDto(id, "狗蛋", 18);
        return new ResultDto<>(userDto);
    }

}
```



### 4. 重点：使用 `ControllerAdvice` 监听控制器调用时出现的异常

```java
@ControllerAdvice // 指定这个类是一个监听类，用于监听不同异常输出结果
@RestController
public class EndpointAdvice {

    @ExceptionHandler(ElementNotFoundException.class) // 元素未找到异常
    @ResponseStatus(HttpStatus.NOT_FOUND) // 返回 404
    public ResultDto<Void> elementNotFount(ElementNotFoundException e) {
        return new ResultDto<>(e.getMessage());
    }

    @ExceptionHandler(ParamInvalidException.class) // 参数错误异常
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 返回 400
    public ResultDto<Void> paramInvalid(ParamInvalidException e) {
        return new ResultDto<>(e.getMessage());
    }

    @ExceptionHandler(Exception.class) // 未知错误
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 返回 500
    public ResultDto<Void> other(Exception e) {
        return new ResultDto<>(e.getMessage());
    }

}
```



### 5. 接口测试

使用 `idea-2018` 的 `http` 测试工具请求：

```
GET http://localhost:8080/user/101

HTTP/1.1 400 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 19 Nov 2018 16:45:16 GMT
Connection: close

{
  "success": false,
  "data": null,
  "message": "参数错误"
}

Response code: 400; Time: 357ms; Content length: 46 bytes

---------------------------------------------------------

GET http://localhost:8080/user/100

HTTP/1.1 404 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 19 Nov 2018 16:46:46 GMT

{
  "success": false,
  "data": null,
  "message": "用户不存在"
}

Response code: 404; Time: 41ms; Content length: 47 bytes

---------------------------------------------------------

GET http://localhost:8080/user/99

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Mon, 19 Nov 2018 16:47:09 GMT

{
  "success": true,
  "data": {
    "id": 99,
    "name": "狗蛋",
    "age": 18
  },
  "message": null
}

Response code: 200; Time: 140ms; Content length: 69 bytes

```



## 三. 总结

其实 `spring-boot` 的构建大部分都是一致的，上面前几步基本和简单的 `web` 项目一致，最重要的一步就是监听到不同的异常，然后返回不同的状态码以及输出信息。

因为现在大部分项目都是微服务架构的，所以建议对自己公司内部整理一个基本的框架，包含有不同异常的，基本输出类的，以及这个 `ControllerAdvice` 的配置。在微服务中，只要对此框架进行依赖，即可拥有上面的功能。

常用状态码：

| 状态码 | 说明                                              |
| ------ | ------------------------------------------------- |
| 200    | 请求成功（多用于 `get` 请求资源、`put` 更新资源） |
| 201    | 创建成功（多用于 `post` 创建资源）                |
| 400    | 请求错误，参数格式不正确或者参数不符合验证要求    |
| 404    | 未找到，资源不存在或者                            |
| 500    | 服务器出现问题                                    |

























