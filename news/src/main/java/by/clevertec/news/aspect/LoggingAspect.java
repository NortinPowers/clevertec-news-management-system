package by.clevertec.news.aspect;//package by.clevertec.news.aspect;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Aspect
//@Component
//public class LoggingAspect {
//
//    @Before("execution(* by.clevertec.news.controller.*.*(..))")
//    public void logRequest(JoinPoint joinPoint) {
//        log.info("Request: " + joinPoint.getSignature().toShortString());
//    }
//
//    @AfterReturning(pointcut = "execution(* by.clevertec.news.controller.*.*(..))", returning = "result")
//    public void logResponse(JoinPoint joinPoint, Object result) {
//        log.info("Response: " + result);
//    }
//
//    @AfterThrowing(pointcut = "execution(* by.clevertec.news.repository.*.*(..)) || execution(* by.clevertec.news.service.*.*(..))", throwing = "exception")
//    public void logServiceOrRepositoryException(Exception exception) {
//        log.error("Unexpected error", exception);
//    }
//}
