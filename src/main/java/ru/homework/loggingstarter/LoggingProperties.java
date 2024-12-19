package ru.homework.loggingstarter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "logging.aspect")
public class LoggingProperties {
    public enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR
    }

    private boolean enabled = true;

    private LogLevel logLevel = LogLevel.INFO;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }
}
