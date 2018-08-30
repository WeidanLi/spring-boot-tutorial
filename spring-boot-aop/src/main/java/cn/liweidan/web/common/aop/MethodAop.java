package cn.liweidan.web.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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