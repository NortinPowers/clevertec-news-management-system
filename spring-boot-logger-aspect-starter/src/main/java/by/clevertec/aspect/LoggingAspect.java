package by.clevertec.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@annotation(ControllerAspectLogger)")
    public void callControllerAspectLoggerAnnotation() {
    }

    @Before("callControllerAspectLoggerAnnotation()")
    public void logRequest(JoinPoint joinPoint) {
        log.info("Request: " + joinPoint.getSignature().toShortString());
    }

    @AfterReturning(pointcut = "callControllerAspectLoggerAnnotation()", returning = "result")
    public void afterReturningAdvice(Object result) {
        log.info("Response: " + result);
    }

    @Pointcut("@annotation(ServiceAspectLogger)")
    public void callServiceAspectLoggerAnnotation() {
    }

    @AfterThrowing(pointcut = "callServiceAspectLoggerAnnotation()", throwing = "exception")
    public void logServiceException(Exception exception) {
        log.error("Unexpected error", exception);
    }

    @Pointcut("@annotation(RepositoryAspectLogger)")
    public void callRepositoryAspectLoggerAnnotation() {
    }

    @AfterThrowing(pointcut = "callRepositoryAspectLoggerAnnotation()", throwing = "exception")
    public void logRepositoryException(Exception exception) {
        log.error("Unexpected error", exception);
    }
}
