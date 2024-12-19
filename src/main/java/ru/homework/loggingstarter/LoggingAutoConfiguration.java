package ru.homework.loggingstarter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@EnableAspectJAutoProxy
public class LoggingAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "logging.aspect", name = "enabled", havingValue = "true", matchIfMissing = true)
    public LoggingAspect loggingAspect(LoggingProperties properties) {
        return new LoggingAspect(properties);
    }
}
