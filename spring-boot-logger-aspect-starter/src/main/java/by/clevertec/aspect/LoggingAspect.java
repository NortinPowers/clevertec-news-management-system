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
//@RequiredArgsConstructor
public class LoggingAspect {

//    @Value("${logger.package.controller}")
//    private String controllerPackage;

//    @Value("${logger.package.service}")
//    private String servicePackage;
//
//    @Value("${logger.package.repository}")
//    private String repositoryPackage;

//    @Pointcut("execution(* " + controllerPackage + ".*.*(..))")
//    public void controllerMethods() {}
//
//
////    @Before("execution(* *.controller.*.*(..))")
//    @Before("execution(* " + controllerPackage + ".*.*(..))")
////    @Before("execution(* *.*(..)) && within(controllerPackage)")
//    public void logRequest(JoinPoint joinPoint) {
//        System.out.println("QQQQ");
//        log.info("Request: " + joinPoint.getSignature().toShortString());
//    }

//    @AfterReturning(pointcut = "execution(* controllerPackage.*.*(..))", returning = "result")
//    public void logResponse(JoinPoint joinPoint, Object result) {
//        log.info("Response: " + result);
//    }

//    @AfterThrowing(pointcut = "execution(* servicePackage.*.*(..)) || execution(* repositoryPackage.*.*(..))", throwing = "exception")
//    public void logServiceOrRepositoryException(Exception exception) {
//        log.error("Unexpected error", exception);
//    }

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


    //Зацикливание и ресурсожор!

//    @Around("execution(* *.*(..))")
//    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
//        if (joinPoint.getSignature().getDeclaringTypeName().startsWith(controllerPackage)) {
//            log.info("Request: " + joinPoint.getSignature().toShortString());
//        }
//        return joinPoint.proceed();
//    }
//
//    @AfterReturning(pointcut = "execution(* *.*(..))", returning = "result")
//    public void logResponse(JoinPoint joinPoint, Object result) {
//        if (joinPoint.getSignature().getDeclaringTypeName().startsWith(controllerPackage)) {
//            log.info("Response: " + result);
//        }
//    }
//
//    @AfterThrowing(pointcut = "execution(* *.*(..))", throwing = "exception")
//    public void logServiceOrRepositoryException(Exception exception) {
//        StackTraceElement[] stackTrace = exception.getStackTrace();
//        for (StackTraceElement element : stackTrace) {
//            if (element.getClassName().startsWith(servicePackage) || element.getClassName().startsWith(repositoryPackage)) {
//                log.error("Unexpected error", exception);
//                break;
//            }
//        }
//    }
}
