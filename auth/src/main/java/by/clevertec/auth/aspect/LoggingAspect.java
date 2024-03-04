package by.clevertec.auth.aspect;//package by.clevertec.auth.aspect;
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
//    @Before("execution(* by.clevertec.auth.controller.*.*(..))")
//    public void logRequest(JoinPoint joinPoint) {
//        log.info("Request: " + joinPoint.getSignature().toShortString());
//    }
//
//    @AfterReturning(pointcut = "execution(* by.clevertec.auth.controller.*.*(..))", returning = "result")
//    public void logResponse(JoinPoint joinPoint, Object result) {
//        log.info("Response: " + result);
//    }
//
//    @AfterThrowing(pointcut = "execution(* by.clevertec.auth.repository.*.*(..)) || execution(* by.clevertec.auth.service.*.*(..))", throwing = "exception")
//    public void logServiceOrRepositoryException(Exception exception) {
//        log.error("Unexpected error", exception);
//    }
//}
