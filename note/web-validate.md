# springboot 与数据验证

* [spring-boot 使用 aop](#spring-boot-使用-aop)
  * [一. 简述](#一-简述)
  * [二. 开发](#二-开发)
    * [1. 引入 aop 依赖](#1-引入-aop-依赖)
    * [2. 使用注解切入](#2-使用注解切入)
    * [3. 开发一个用户控制器以及用户传输类](#3-开发一个用户控制器以及用户传输类)
    * [4. 开发一个用户业务层](#4-开发一个用户业务层)
    * [5. 请求访问](#5-请求访问)
    * [6. 注解使用说明](#6-注解使用说明)
    * [7. 开发自定义校验器](#7-开发自定义校验器)
        * [7.1 定义校验注解](7.1-定义校验注解)
        * [7.2 定义校验器](7.2-定义校验器)
        * [7.3 测试自定义校验器](7.3-测试自定义校验器)
  * [三. 总结](#三-总结)

## 一. 简述

在项目开发中，验证参数也是最经常使用的业务需求了。通常在开发的时候都需要根据业务需求，对参数进行必要验证。

当然一堆的 `if-else` 的验证在日常开发中时常可见。这种方式非常不友好：
1. 代码太长导致阅读不友好，更改需求可能只是简单的修改但是却需要阅读几十到几百行的代码
2. 有时候业务只是一两句但是验证代码却占用了很长时间

`JSR-303` 是 `Java` 的验证规范，早期是在 `Hibernate` 框架中实现的，后面被抽取到 `Java` 体系。`Spring-Boot` 使用了 `hibernate-validator` 验证器，所以也包含了 `JSR-303` 在里面。当需要进行参数验证的时候，只需要几个注解即可实现复杂的验证。

## 二. 开发

### 1. 引入依赖

```xml
<!-- 引入 web-starter, 已经包含 hibernate-validator 验证器 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 2. 使用注解开发控制器

```java
@RestController
@Validated // 类上添加该注解，能够验证直接使用参数形式的借口
public class UserEndpoint {

    /**
     * 如果需要验证Dto类，只需要加上@Validated即可，如需分组，传入参数
     * @param userDto
     */
    @PostMapping
    public void create(@Validated @RequestBody UserDto userDto) {
        System.out.println("-----> add: " + userDto);
    }

    @GetMapping
    public UserDto queryByName(@Length(min = 1, max = 8) @RequestParam(value = "name") String name) {
        System.out.println(name);
        UserDto userDto = new UserDto();
        userDto.setId(100);
        userDto.setUserName("狗蛋");
        userDto.setAge(19);
        return userDto;
    }

}

```

PS: 
1. 第一个接口使用 `DTO` 的形式进行验证，验证注解将会被使用在 `DTO` 上，`DTO`如下
2. 第二个接口直接在接口参数进行验证

UserDto：
```java
/**
 * 用户数据交换类
 */
public class UserDto {

    private Integer id;

    @NotBlank
    private String userName;

    @NotNull
    private Integer age;
}
```

### 3. 请求两个接口

```
POST http://localhost:8080

HTTP/1.1 400 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Fri, 14 Dec 2018 01:41:46 GMT
Connection: close

{
  "timestamp": "2018-12-14T01:41:46.244+0000",
  "status": 400,
  "error": "Bad Request",
  "errors": [
  ... 这里不做详细演示因为下面要配合控制器监听把这个错误给修改掉
  ]
}

----------------------------------------------------------

GET http://localhost:8080?name=

HTTP/1.1 500 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Fri, 14 Dec 2018 01:42:55 GMT
Connection: close

{
  "timestamp": "2018-12-14T01:42:55.654+0000",
  "status": 500,
  "error": "Internal Server Error",
  "message": "queryByName.name: 长度需要在1和8之间",
  "path": "/"
}

Response code: 500; Time: 29ms; Content length: 141 bytes

```

可以看到，`Spring` 已经对数据按照我们的需求进行了验证，但是返回的错误信息并不是很友好，我们可以使用前面提到的 `controller-advice` 进行拦截返回

### 4. 定制返回验证错误信息

从控制台可以看到，验证不通过的时候抛出的异常是 `ConstraintViolationException` 和 `MethodArgumentNotValidException` 异常，所以在监听器里只需要对这个异常进行监听，获取 `message` 属性进行返回即可得知错误的原因。

```java
@ControllerAdvice
@RestController
public class EndpointAdvice {

    @ExceptionHandler(ConstraintViolationException.class) // 参数验证异常
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 返回 400
    public Map<String, String> errorParam(ConstraintViolationException e) {
        Map<String, String> error = new HashMap<>(2);
        error.put("message", e.getMessage());
        return error;
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class) // DTO 验证异常
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 返回 400
    public Map<String, String> errorDto(MethodArgumentNotValidException e) {
        Map<String, String> error = new HashMap<>(2);
        StringBuilder message = new StringBuilder();
        for (ObjectError allError : e.getBindingResult().getAllErrors()) {
            message.append(allError.getDefaultMessage()).append("; ");
        }
        error.put("message", message.toString());
        return error;
    }

}
```

重新请求：

```
POST http://localhost:8080

HTTP/1.1 400 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Fri, 14 Dec 2018 01:59:57 GMT
Connection: close

{
  "message": "名字不允许为空; 年龄不允许为空; "
}

Response code: 400; Time: 166ms; Content length: 28 bytes

---------------------------------------------------------
GET http://localhost:8080?name=

HTTP/1.1 400 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Fri, 14 Dec 2018 01:48:23 GMT
Connection: close

{
  "message": "queryByName.name: 长度需要在1和8之间"
}

Response code: 400; Time: 151ms; Content length: 42 bytes
```

### 5. 分组验证

`DTO` 如果验证不能自由的话，那么`DTO` 的复用性就变差了，总不能一个接口都要重新写属性相同但是验证不相同的 `DTO` 吧

所以，`JSR-303` 的每个验证注解上都有一个 `groups` 属性，传入 `Class` 数组（多组验证），用于定义分组验证的接口（但是这个接口没有实际实现，只是以 `Java` 类来做分组罢了）。

`DTO` 修改为更新的时候要求 `id` 不能为空，新增时名字和年龄不能为空。

```java
public class UserDto {

    @NotNull(groups = Update.class)
    private Integer id;

    @NotBlank(message = "名字不允许为空", groups = Add.class)
    private String userName;

    @NotNull(message = "年龄不允许为空", groups = Add.class)
    private Integer age;
}
```

接口修改，验证器传入分组

```java
@RestController
@Validated // 类上添加该注解，能够验证直接使用参数形式的借口
public class UserEndpoint {

    /**
     * 新增的时候验证 ADD 分组下需要验证的属性
     * @param userDto
     */
    @PostMapping
    public void create(@Validated({Add.class}) @RequestBody UserDto userDto) {
        System.out.println("-----> add: " + userDto);
    }

    /**
     * 更新的时候验证 UPDATE 分组下需要验证的属性
     * @param userDto
     */
    @PutMapping
    public void update(@Validated({Update.class}) @RequestBody UserDto userDto) {
        System.out.println("-----> update: " + userDto);
    }

}
```

重新请求接口：

```
POST http://localhost:8080

HTTP/1.1 400 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Fri, 14 Dec 2018 02:11:14 GMT
Connection: close

{
  "message": "年龄不允许为空; 名字不允许为空; "
}

Response code: 400; Time: 11ms; Content length: 32 bytes

---------------------------------------------------------

PUT http://localhost:8080

HTTP/1.1 400 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Fri, 14 Dec 2018 02:12:06 GMT
Connection: close

{
  "message": "不能为null; "
}

Response code: 400; Time: 194ms; Content length: 23 bytes

```

### 6. 注解使用说明

每个注解都拥有两个属性：
1. groups 用于定义这个注解要应用于哪些分组的
2. message 用于定义不符合要求时要输出的内容

|注解|说明|
|:--:|:--:|
|@Null|被注释的元素必须为|
|@NotNull|被注释的元素必须不为|null|
|@AssertTrue|被注释的元素必须为|true|
|@AssertFalse|被注释的元素必须为|false|
|@Min(value)|被注释的元素必须是一个数字，其值必须大于等于指定的最小值|
|@Max(value)|被注释的元素必须是一个数字，其值必须小于等于指定的最大值|
|@DecimalMin(value)|被注释的元素必须是一个数字，其值必须大于等于指定的最小值|
|@DecimalMax(value)|被注释的元素必须是一个数字，其值必须小于等于指定的最大值|
|@Size(max=,min=)|被注释的元素的大小必须在指定的范围内|
|@Digits|被注释的元素必须是一个数字，其值必须在可接受的范围内|
|@Past|被注释的元素必须是一个过去的日期|
|@Future|被注释的元素必须是一个将来的日期|
|@Pattern(regex=,flag=)|被注释的元素必须符合指定的正则表达式|
|Hibernate|Validator|
|@NotBlank|验证字符串非null，且长度必须大于0|
|@Email|被注释的元素必须是电子邮箱地址|
|@Length(min=,max=)|被注释的字符串的大小必须在指定的范围内|
|@NotEmpty|被注释的字符串的必须非空|
|@Range(min=,max=,message=)|被注释的元素必须在合适的范围内|

### 7. 开发自定义校验器

很多时候，框架自带的功能并不能或者说不方便实现我们想要的功能的时候，我们就可以开始考虑自己造轮子，开发属于我们所在领域的校验器。

简单来说，开发流程就是以下两个步骤：

1. 编写约束注解，填充需要的属性以及官方必须要求的属性；
2. 编写校验器，对目标值配合注解上的属性进行验证。

#### 7.1 定义校验注解

为了示范，我定义一个验证目标字符串需要纯中文字符的校验注解

```java
/**
 * 验证纯中文的注解
 */
@Constraint(validatedBy = { ChsValidator.class }) // 定义注解的校验器
@Target(ElementType.FIELD) // 定义该注解使用在类属性上
@Retention(RUNTIME) // 运行时有效
@Documented
public @interface Chs {

    /** 可以自定义属性，这个属性暂时没什么用 */
    String chineseName();

    /** 以下是注解必须的三个属性，分别是定义组别、错误等级以及提示信息 */
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String message() default "该字段需要纯中文";

}

```

#### 7.2 定义校验器

校验器需要去实现 `ConstraintValidator` 接口，传入两个泛型，第一个泛型是注解类型，第二个是目标值的类型。实现两个方法，校验器调用的校验在 `isValid` 中进行，如果校验通过返回 `True` 不同过则返回 `False`

```java
public class ChsValidator implements ConstraintValidator<Chs, String> { // 实现ConstraintValidator接口，第一个泛型是注解类型，第二个是目标值的类型

    private String reg = "^[\\u0391-\\uFFE5]+[\\w*[\\u0391-\\uFFE5]*]*";

    @Override
    public void initialize(Chs constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (null == value) {
            return false;
        }
        return value.matches(reg);
    }

}
```

#### 7.3 测试自定义校验器

现在需要一个控制器接口来测试

```java
@PostMapping("testChsValidator")
public void testChsValidator(@Chs @RequestParam String chsName) {

}
```

请求访问测试

```
POST http://localhost:8080/testChsValidator?chsName=888

HTTP/1.1 400 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Tue, 18 Dec 2018 01:39:40 GMT
Connection: close

{
  "message": "testChsValidator.chsName: 该字段需要纯中文"
}

Response code: 400; Time: 239ms; Content length: 48 bytes

--------------------------------------------------------

POST http://localhost:8080/testChsValidator?chsName=你好

HTTP/1.1 200 
Content-Length: 0
Date: Tue, 18 Dec 2018 01:40:11 GMT

<Response body is empty>

Response code: 200; Time: 44ms; Content length: 0 bytes
```



## 三. 总结

OK，验证器的使用到这里基本完结，一般来说基础的可以适用大部分需求，但是我建议如果某个校验规则用的不多的话，就不大需要定义自定义校验器，如果用的比较多而且是通用的话，那么自定义的注解校验无疑会带来很多便捷。校验器适用的实现是 `Hibernate Validate` 所以如若需要查询资料可直接查询`Hibernate Validate` 相关的即可。

[Hibernate-Validate](http://docs.jboss.org/hibernate/validator/4.2/reference/zh-CN/html_single/#validator-customconstraints-constraintannotation)













