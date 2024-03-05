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

    /**
     * Определяет точку среза для аспекта, связанного с аннотацией {@link ControllerAspectLogger}.
     */
    @Pointcut("@annotation(ControllerAspectLogger)")
    public void callControllerAspectLoggerAnnotation() {
    }

    /**
     * Логирует запрос перед выполнением метода контроллера.
     *
     * @param joinPoint Точка соединения, представляющая вызов метода.
     */
    @Before("callControllerAspectLoggerAnnotation()")
    public void logRequest(JoinPoint joinPoint) {
        log.info("Request: " + joinPoint.getSignature().toShortString());
    }

    /**
     * Логирует ответ после успешного выполнения метода контроллера.
     *
     * @param result Результат выполнения метода.
     */
    @AfterReturning(pointcut = "callControllerAspectLoggerAnnotation()", returning = "result")
    public void afterReturningAdvice(Object result) {
        log.info("Response: " + result);
    }

    /**
     * Определяет точку среза для аспекта, связанного с аннотацией {@link ServiceAspectLogger}.
     */
    @Pointcut("@annotation(ServiceAspectLogger)")
    public void callServiceAspectLoggerAnnotation() {
    }

    /**
     * Логирует исключение, возникшее в сервисном методе.
     *
     * @param exception Исключение, которое было выброшено.
     */
    @AfterThrowing(pointcut = "callServiceAspectLoggerAnnotation()", throwing = "exception")
    public void logServiceException(Exception exception) {
        log.error("Unexpected error", exception);
    }

    /**
     * Определяет точку среза для аспекта, связанного с аннотацией {@link RepositoryAspectLogger}.
     */
    @Pointcut("@annotation(RepositoryAspectLogger)")
    public void callRepositoryAspectLoggerAnnotation() {
    }

    /**
     * Логирует исключение, возникшее в методе репозитория.
     *
     * @param exception Исключение, которое было выброшено.
     */
    @AfterThrowing(pointcut = "callRepositoryAspectLoggerAnnotation()", throwing = "exception")
    public void logRepositoryException(Exception exception) {
        log.error("Unexpected error", exception);
    }
}
