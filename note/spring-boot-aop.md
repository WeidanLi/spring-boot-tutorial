# spring-boot 使用 aop

## 一. 引入 aop 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

## 二. 使用注解切入

- `@Pointcut`： 切入点
- `@Around("aopPoint()")`：环绕切入，可以在方法执行之前之后切入，用于记录请求
- `@AfterThrowing(throwing = "ex", value = "aopPoint()")`：抛出异常之后切入


```java
@Aspect
@Component
public class MethodAop {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(* cn.liweidan.web.service.UserService.*(..))")
    public void aopPoint() {}

    @Around("aopPoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        long start = System.currentTimeMillis();
        logger.info("-----" + className + "." + methodName + "----");
        Object proceed = joinPoint.proceed();
        logger.info("-----" + className + "." + methodName + "End, doing in " + (System.currentTimeMillis() - start) + "ms----");
        return proceed;
    }


    @AfterThrowing(throwing = "ex", value = "aopPoint()")
    public void errorAop(JoinPoint joinPoint, Throwable ex) {
        logger.error(ex.toString());
    }

}
```