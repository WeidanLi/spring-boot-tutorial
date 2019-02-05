# springboot 的打包与单独部署 部署到 tomcat

* [springboot 的打包与单独部署 部署到 tomcat](#springboot-的打包与单独部署-部署到-tomcat)
  * [一.简述](#一简述)
  * [二.打包的两种方式](#二打包的两种方式)
    * [（一）直接打包成jar包进行运行](#（一）直接打包成jar包进行运行)
    * [（二）打包成war包放入web容器运行](#（二）打包成war包放入web容器运行)

## 一.简述

项目开发完成，就需要上线部署了，不然开发了那么久的代码也没什么存在的意义。

说到上线部署，那么以往的部署方式都是使用 `servlet` 项目，整合一些中间件使用，配置好配置文件。当需要上线部署的时候，就打包成 `war` 格式的压缩包，放在一个 `web` 容器中，然后启动容器，让容器去启动我们的项目。

但是在现在的 `springboot` 时代，官方已经很贴心的给我们提供一个内置的 `tomcat` 容器了，当然如果我们需要更换成其他的内置容器也完全可行。这样子能够让部署显得更加简单便捷，也不需要像以前一样，当需要多个微服务项目的时候，我们又不想一个 `tomcat` 运行多个项目让他互相影响，然而开启了很多 `tomcat` 容器，造成了资源的浪费。`springboot` 内置的容器咧，也经过官方修改让他更加吻合我们的项目了，我们完全可以在项目的配置文件中配置允许 `tomcat` 使用的资源信息。

## 二.打包的两种方式

### （一）直接打包成jar包进行运行

直接打包 `jar` 包的方式就很简单了，我们可以使用命令行或者 `IDE` 去到我们项目的 `pom` 所在的目录。

运行 `mvn package`。稍等片刻，如果没有遇到错误，那么 `mvn` 会帮助我们在 `target` 文件夹生成一个 `.jar` 结尾的压缩包，这个就是我们项目的所有信息了。

我们只需要通过 `FTP` 或者其他的方式将这个项目包放在一个地方，然后使用 `java -jar xxx.jar` 的形式进行运行就可以启动项目了。过程还是十分轻量级的。

当然以上那种方式，运行完成以后，如果退出了，那么项目也就退出了，这并不是我们想看到的。所以在 `Linux` 下，我们可以使用 `nohup` 命令进行启动，将启动打印出来的日志定位到一个文件里面去，例如 `nohup java -jar xxx.jar >xxx.out 2>&1 &` 这样子，项目运行过程中的情况，会跟我们使用 `IDE` 一样把日志写到指定的文件里面去，当然如果不想要这个文件毕竟自己一般都实现了日志了的。那么可以使用这个命令，将日志导入到一个黑洞里面去：`nohup java -jar /xxx/xxx/xxx.jar >/dev/null 2>&1 &`

### （二）打包成war包放入web容器运行

将项目打成 `jar` 包容易，毕竟是标配了，但是如果因为业务需要需要放进一个 `tomcat` 里面去运行的话，那么久需要反其道而行把一些配置给去掉。

`pom.xml` 需要加入一些配置：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>spring-boot-tutorial</artifactId>
        <groupId>cn.liweidan.springboot</groupId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <!-- 指定搜索不到 web.xml 不报错，继续构建 -->
    <properties>
        <failOnMissingWebXml>false</failOnMissingWebXml>
    </properties>

    <groupId>cn.liweidan.springboot.tomcat</groupId>
    <artifactId>global-inner-tomcat</artifactId>

    <!-- 指定需要构建的格式 -->
    <packaging>war</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 指定Tomcat只是编译时期加入，打包不加入 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>

</project>
```

然后运行 `mvn package` 即可打包成 `war` 包了，这时将 `war` 包放入 `WEB` 容器即可运行起来。























