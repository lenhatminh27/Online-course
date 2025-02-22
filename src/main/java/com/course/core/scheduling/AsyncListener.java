package com.course.core.scheduling;

import com.course.common.utils.ObjectUtils;
import com.course.core.bean.BeanFactory;
import com.course.core.scheduling.annotations.EnableScheduling;
import com.course.core.scheduling.annotations.Scheduled;
import com.course.core.utils.ReflectUtils;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class AsyncListener extends BeanFactory implements ServletContextListener {

    private ScheduledExecutorService executorScheduled; // Thread Pool với 5 luồng

    public AsyncListener() {
       super(
               new Reflections(
                       new ConfigurationBuilder()
                               .forPackage("com.course")
                               .setScanners(
                                       Scanners.MethodsAnnotated
                               )
               )
       );
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Dynamically add entity classes using Reflections
        super.setServletContext(sce.getServletContext());
        if (isEnabled(EnableScheduling.class)) {
            var bean = (ScheduledExecutorService) getBean(ExecutorService.class.getSimpleName());
            executorScheduled = (ObjectUtils.isEmpty(bean) ? Executors.newScheduledThreadPool(5) : bean);
            handleScheduling();
        }
    }


    @SuppressWarnings("unchecked")
    public <T> T getBean(String name) {
        if (null != servletContext) {
            return (T) servletContext.getAttribute(name);
        }
        return null;
    }

    private void handleScheduling() {
        reflections.getMethodsAnnotatedWith(Scheduled.class)
                .forEach(method -> {
                    Scheduled annotation = method.getAnnotation(Scheduled.class);
                    var delay = annotation.fixedDelay();
                    var initialDelay = annotation.initialDelay();
                    var timeUnit = annotation.timeUnit();
                    // Đăng ký phương thức vào ScheduledExecutorService
                    executorScheduled.scheduleAtFixedRate(() -> {
                        try {
                            // Gọi phương thức (cần đảm bảo phương thức không có tham số)
                            BeanRegister<?> beanRegister = getInstanceService(method.getDeclaringClass());
                            ReflectUtils.invoke(beanRegister.getBean(), method);
                        } catch (Exception e) {
                            throw new IllegalStateException("Error executing scheduled method: " + method.getName());
                        }
                    }, initialDelay, delay, timeUnit);
                });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (null != executorScheduled) {
            executorScheduled.shutdown();
            try {
                if (!executorScheduled.awaitTermination(1, TimeUnit.SECONDS)) {
                    executorScheduled.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorScheduled.shutdownNow();
            }
        }
    }
}
