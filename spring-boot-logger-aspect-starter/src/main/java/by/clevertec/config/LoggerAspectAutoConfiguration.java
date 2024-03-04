package by.clevertec.config;

import by.clevertec.aspect.LoggingAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
//@EnableAspectJAutoProxy
@ConditionalOnProperty(prefix = "logger", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LoggerAspectAutoConfiguration {

//    private String controller;
//    private String service;
//    private String repository;


    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }
}
