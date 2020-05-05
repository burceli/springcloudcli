package me.bruceli.log4j2.listener;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.Map;

public class Log4j2GetApplicationPropertiesListener implements GenericApplicationListener {

    public static final int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE + 10;

    private static Class<?>[] EVENT_TYPES = { ApplicationStartingEvent.class,
            ApplicationEnvironmentPreparedEvent.class, ApplicationPreparedEvent.class,
            ContextClosedEvent.class, ApplicationFailedEvent.class };

    private static Class<?>[] SOURCE_TYPES = { SpringApplication.class,
            ApplicationContext.class };

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {

            ConfigurableEnvironment envi = ((ApplicationEnvironmentPreparedEvent) event).getEnvironment();
            MutablePropertySources mutablePropertySources = envi.getPropertySources();

            PropertySource<?> propertySource = mutablePropertySources.get("applicationConfig: [classpath:/application.yml]");

            if (propertySource != null) {
                if (propertySource.containsProperty("spring.application.name")) {
                    String applicationName = (String) propertySource.getProperty("spring.application.name");
                    MDC.put("applicationName", applicationName);
                    Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
                    System.out.println(copyOfContextMap);
                    if (propertySource.containsProperty("log.path")) {
                        String logPath = (String) propertySource.getProperty("log.path");
                        MDC.put("logPath", logPath);
                        copyOfContextMap = MDC.getCopyOfContextMap();
                        System.out.println(copyOfContextMap);
                    }else {
                        throw new RuntimeException("log.path 未配置");
                    }
                }else {
                    throw new RuntimeException("spring.application.name 未配置");
                }

            } else {
                throw new RuntimeException("找不到配置文件");
            }

        }

    }


    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }


    @Override
    public boolean supportsEventType(ResolvableType resolvableType) {
        return isAssignableFrom(resolvableType.getRawClass(), EVENT_TYPES);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return isAssignableFrom(sourceType, SOURCE_TYPES);
    }

    private boolean isAssignableFrom(Class<?> type, Class<?>... supportedTypes) {
        if (type != null) {
            for (Class<?> supportedType : supportedTypes) {
                if (supportedType.isAssignableFrom(type)) {
                    return true;
                }
            }
        }
        return false;
    }
}
