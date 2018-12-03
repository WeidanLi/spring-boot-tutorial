# Table of Contents

* [springboot 与数据接口](#springboot-与数据接口)
  * [一. 简述](#一-简述)
  * [二. 开发](#二-开发)
    * [1. `maven` 依赖](#1-`maven`-依赖)
    * [2. 资源控制器](#2-资源控制器)
    * [3. 接口测试](#3-接口测试)
  * [三. 总结](#三-总结)


# springboot 与数据接口

## 一. 简述

从以往的 `Spring` 项目开发经验来看，`Spring` 对 `JSON` 情有独钟，这也得益于 `JSON` 是`JS` 发明的一种轻量级的数据交换格式，因为本身 `JS` 是弱类型的语言，所以 `JSON` 便没有什么特定类型限制，使得其他各门语言都可以对 `JSON` 进行解析，从而序列化成各自的对象。

其实前面说的已经有一点半点的，基本都是返回 `JSON` 字符串，所以这里便加深一点，涉及 `SpringMVC` 可以用于开发的注解。

**项目示例：`web-restful`**

## 二. 开发

### 1. `maven` 依赖

`maven` 依赖依然只需要 `spring-boot-starter-web` 的依赖即可

```xml
<dependencies>
    <!-- 引入 web-starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

### 2. 资源控制器

我这里为了能够说明明白，先假定控制器是用户资源控制器，我可以通过公司查询，也可以通过用户的某个字段查询。

一般来说，一个资源控制器，我喜欢只关注聚合类的资源，比如用户控制器，返回的一般都是用户，操作的也一般是用户。

使用 `RESTful` 风格来编排 `URL` ，以前开发的时候因为没有关注到一些注解的用法，`RESTful` 用起来总是有点吃力，因为有些时候我是从另外一个方向去查询的，并不是 `User` 的内部属性。

由于先不接触数据库，所以先用集合模拟数据。

```java
package cn.liweidan.springboot.webrestful.endpoint;

import cn.liweidan.springboot.webrestful.dbo.UserDo;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description：用户资源控制器
 *
 * @author liweidan
 * @version 1.0
 * @date 2018/11/22 11:00 AM
 * @email toweidan@126.com
 */
@RestController
@RequestMapping("user")
public class UserEndpoint {

    /** 公司关联用户ID表 */
    Map<Long, List<Long>> orgRelationUser = new HashMap<>();
    /** 用户表 */
    List<UserDo> userDoList = new ArrayList<>();

    public UserEndpoint() {
        /**
         * 假设五个用户，隶属于两个公司，我可能从公司ID查询，也根据条件查询全部
         */
        userDoList.add(new UserDo(1L, "weidan", 6000L, 18));
        userDoList.add(new UserDo(2L, "xiaodan", 10000L, 36));
        userDoList.add(new UserDo(3L, "dadan", 9000L, 28));
        userDoList.add(new UserDo(4L, "Sally", 12000L, 24));
        userDoList.add(new UserDo(5L, "weisuodan", 20000L, 20));

        List<Long> userIds1 = new ArrayList<>(3);
        userIds1.add(1L);
        userIds1.add(3L);
        userIds1.add(4L);
        orgRelationUser.put(10L, userIds1);

        List<Long> userIds2 = new ArrayList<>(2);
        userIds2.add(2L);
        userIds2.add(5L);
        orgRelationUser.put(7L, userIds2);
    }

    @GetMapping(params = "orgId")
    public List<UserDo> selectByOrg(@RequestParam("orgId") Long orgId) {
        List<Long> userIds = orgRelationUser.get(orgId);
        if (Objects.isNull(userIds) || userIds.isEmpty()) {
            return new ArrayList<>(0);
        }
        return userDoList.stream().filter(userDo -> userIds.contains(userDo.getId())).collect(Collectors.toList());
    }

    @GetMapping(params = {"salaryMin", "salaryMax"})
    public List<UserDo> selectAllByCondition(@RequestParam(value = "salaryMin", defaultValue = "0") Long salaryMin,
                                             @RequestParam(value = "salaryMax", defaultValue = Long.MAX_VALUE + "") Long salaryMax) {
        return userDoList.stream()
                .filter(userDo -> userDo.getSalary() > salaryMin && userDo.getSalary() < salaryMax)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<UserDo> selectAll() {
        return userDoList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody UserDo userDo, @RequestParam("orgId") Long orgId) {
        Long nextId = (long) (userDoList.size() + 1);
        userDo.setId(nextId);
        userDoList.add(userDo);

        List<Long> userIds = orgRelationUser.get(orgId);
        if (Objects.isNull(userIds)) {
            userIds = new ArrayList<>();
        }
        userIds.add(nextId);
        orgRelationUser.put(orgId, userIds);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody UserDo userDo) {
        for (UserDo origin : userDoList) {
            if (origin.getId().equals(userDo.getId())) {
                origin.setName(userDo.getName());
                origin.setSalary(userDo.getSalary());
                origin.setAge(userDo.getAge());
                break;
            }
        }
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userDoList = userDoList.stream().filter(userDo -> !userDo.getId().equals(userId)).collect(Collectors.toList());
    }

}

```

### 3. 接口测试

```
# 根据公司ID查询
GET http://localhost:8080/user?orgId=10

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 22 Nov 2018 08:12:46 GMT

[
  {
    "id": 1,
    "name": "weidan",
    "salary": 6000,
    "age": 18
  },
  {
    "id": 3,
    "name": "dadan",
    "salary": 9000,
    "age": 28
  },
  {
    "id": 4,
    "name": "Sally",
    "salary": 12000,
    "age": 24
  }
]

Response code: 200; Time: 469ms; Content length: 144 bytes
---------------------------------------------------------
# 根据条件查询全部
GET http://localhost:8080/user?salaryMin=9000

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 22 Nov 2018 08:13:38 GMT

[
  {
    "id": 1,
    "name": "weidan",
    "salary": 6000,
    "age": 18
  },
  {
    "id": 2,
    "name": "xiaodan",
    "salary": 10000,
    "age": 36
  },
  {
    "id": 3,
    "name": "dadan",
    "salary": 9000,
    "age": 28
  },
  {
    "id": 4,
    "name": "Sally",
    "salary": 12000,
    "age": 24
  },
  {
    "id": 5,
    "name": "weisuodan",
    "salary": 20000,
    "age": 20
  }
]

Response code: 200; Time: 22ms; Content length: 246 bytes
---------------------------------------------------------
# 查询全部用户
GET http://localhost:8080/user

HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Thu, 22 Nov 2018 08:14:11 GMT

[
  {
    "id": 1,
    "name": "weidan",
    "salary": 6000,
    "age": 18
  },
  {
    "id": 2,
    "name": "xiaodan",
    "salary": 10000,
    "age": 36
  },
  {
    "id": 3,
    "name": "dadan",
    "salary": 9000,
    "age": 28
  },
  {
    "id": 4,
    "name": "Sally",
    "salary": 12000,
    "age": 24
  },
  {
    "id": 5,
    "name": "weisuodan",
    "salary": 20000,
    "age": 20
  }
]

Response code: 200; Time: 29ms; Content length: 246 bytes
---------------------------------------------------------
# 新增用户接口 返回 201
POST http://localhost:8080/user?orgId=10

HTTP/1.1 201 
Content-Length: 0
Date: Thu, 22 Nov 2018 08:16:23 GMT

<Response body is empty>

Response code: 201; Time: 232ms; Content length: 0 bytes
---------------------------------------------------------
# 更新用户接口 返回 204
PUT http://localhost:8080/user

HTTP/1.1 204 
Date: Thu, 22 Nov 2018 08:16:56 GMT

<Response body is empty>

Response code: 204; Time: 79ms; Content length: 0 bytes
---------------------------------------------------------
# 删除用户接口
DELETE http://localhost:8080/user/2

HTTP/1.1 200 
Content-Length: 0
Date: Thu, 22 Nov 2018 08:17:20 GMT

<Response body is empty>

Response code: 200; Time: 46ms; Content length: 0 bytes
```

## 三. 总结

`JSON` 已经成为各大语言的青睐，`SpringMVC` 还可以提供其他的数据格式交互，例如 `Hession` 。这些后面跟 `SpringCloud` 放在一起，跟 `Feign` 一起搭配使用，提升数据交互的效率。

其实做 `RESTful` 的时候，最纠结的就是资源的设计了，有的需要用这个条件获取，有的需要那个条件去获取，以前写的乱七八糟，自从知道了 `params` 以后，设计就变得简单易懂了。不仅仅 `GetMapping` 所有的请求方式都有这个参数。

以及还有 `@SessionAttribute` `@CookieValue` `@RequestHeader` 可以获取相对应区域的信息。交替使用则可以让开发更加流畅。

























