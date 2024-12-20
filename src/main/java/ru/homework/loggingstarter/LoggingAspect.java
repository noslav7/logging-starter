package ru.homework.loggingstarter;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final LoggingProperties properties;

    public LoggingAspect(LoggingProperties properties) {
        this.properties = properties;
    }

    @Pointcut("execution(* com.example.aspect_oriented_programming.controller..*(..))")
    public void controllerMethods() {
    }

    @Before("controllerMethods()")
    public void logBeforeMethod(JoinPoint joinPoint) {
        log("Before advice: начало выполнения метода " + joinPoint.getSignature().getName(), null);

        Object[] args = joinPoint.getArgs();
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();

        if (args.length > 0) {
            StringBuilder sb = new StringBuilder("Параметры метода: ");
            for (int i = 0; i < args.length; i++) {
                sb.append(parameterNames[i]).append("=").append(args[i]);
                if (i < args.length - 1) {
                    sb.append(", ");
                }
            }
            log(sb.toString(), null);
        } else {
            log("Метод не принимает параметров.", null);
        }
    }

    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log("AfterThrowing advice: исключение в методе " + joinPoint.getSignature().getName() + ": "
                + exception.getMessage(), exception);
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturningResult(JoinPoint joinPoint, Object result) {
        log("AfterReturning advice: метод " + joinPoint.getSignature().getName() + " вернул результат: "
                + result, null);
    }

    @Around("controllerMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        log("Around advice: метод " + joinPoint.getSignature().getName() + " начинается...", null);
        Object result;

        try {
            result = joinPoint.proceed();
        } catch (Throwable exception) {
            log("Метод " + joinPoint.getSignature().getName() + " выбросил исключение: "
                    + exception.getMessage(), exception);
            throw new RuntimeException(exception);
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        log("Метод " + joinPoint.getSignature().getName() + " завершился за " + elapsedTime + " мс.",
                null);
        return result;
    }

    private void log(String message, Throwable throwable) {
        if (!properties.isEnabled()) return;

        switch (properties.getLogLevel()) {
            case TRACE -> logger.trace(message);
            case DEBUG -> logger.debug(message);
            case INFO -> logger.info(message);
            case WARN -> logger.warn(message);
            case ERROR -> {
                if (throwable != null) {
                    logger.error(message, throwable);
                } else {
                    logger.error(message);
                }
            }
        }
    }
}
