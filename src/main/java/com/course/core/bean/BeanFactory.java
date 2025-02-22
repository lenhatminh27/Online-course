package com.course.core.bean;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.StringUtils;
import com.course.core.bean.proxy.Proxy;
import com.course.core.bean.annotations.Bean;
import com.course.core.bean.annotations.Qualifier;
import com.course.core.bean.annotations.Repository;
import com.course.core.bean.annotations.Service;
import com.course.core.scheduling.AsyncProxy;
import com.course.core.scheduling.annotations.Async;
import com.course.core.scheduling.annotations.EnableAsync;
import jakarta.servlet.ServletContext;
import lombok.*;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@RequiredArgsConstructor
public class BeanFactory {

    @Setter
    protected ServletContext servletContext;
    protected final Reflections reflections;

    @SuppressWarnings("unchecked")
    private Set<Class<?>> getImplementations(final Class<?> clazz){
        return (Set<Class<?>>) reflections.getSubTypesOf(clazz);
    }

    @NonNull
    protected Boolean isBean(final Class<?> clazz) {
        return clazz.isAnnotationPresent(Bean.class) ||
                clazz.isAnnotationPresent(Service.class) ||
                clazz.isAnnotationPresent(Repository.class);
    }

    @SuppressWarnings("unchecked")
    private <T> T getBeanFromContextOrCreate(final Class<?> clazz){
        final String beanName = getBeanName(clazz);
        // Lấy bean từ ServletContext
        Object existBean = servletContext.getAttribute(beanName);
        if(ObjectUtils.isEmpty(existBean)){
            BeanRegister<?> beanRegister = getInstanceService(clazz);
            servletContext.setAttribute(beanRegister.beanName, beanRegister.bean);
            return (T) beanRegister.bean;
        } else {
            return (T) existBean;
        }

    }

    protected String getBeanName(Class<?> clazz) {
        String name = StringUtils.uncapitalize(clazz.getSimpleName());
        if(clazz.isAnnotationPresent(Bean.class) || clazz.isAnnotationPresent(Service.class) ||
            clazz.isAnnotationPresent(Repository.class)){
            Service service = clazz.getAnnotation(Service.class);
            Repository repository = clazz.getAnnotation(Repository.class);
            Bean bean = clazz.getAnnotation(Bean.class);
            if (!ObjectUtils.isEmpty(service) && !ObjectUtils.isEmpty(service.name())) {
                name = service.name();
            } else if (!ObjectUtils.isEmpty(repository) && !ObjectUtils.isEmpty(repository.name())) {
                name = repository.name();
            } else if (!ObjectUtils.isEmpty(bean) && !ObjectUtils.isEmpty(bean.name())) {
                name = bean.name();
            }

        }
        return name;
    }

    private Object getBeanByName(Class<?> implClass, Class<?> parameterType) {
        String beanName = getBeanName(implClass);
        var bean = servletContext.getAttribute(beanName);
        if (ObjectUtils.isEmpty(bean)) {
            bean = getInstanceService(implClass);
        }
        if (!parameterType.isInstance(bean)) {
            throw new IllegalArgumentException("Bean with name '" + beanName + "' not found or is not of type " + parameterType.getName());
        }
        return bean;
    }

    protected <T> BeanRegister<T> getInstanceService(final Class<T> clazz){
        T obj = null;
        boolean replace = false;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (final var constructor : constructors) {
            try {
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] paramBeans  = new Object[parameterTypes.length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    // Kiểm tra có phải interface hay không
                    if (parameterType.isInterface()) {
                        // Lấy ra impl của interface
                        Set<Class<?>> implementations = getImplementations(parameterType);
                        if (implementations.size() == 1) {
                            Class<?> implClass = implementations.iterator().next();
                            paramBeans[i] = getBeanFromContextOrCreate(implClass);
                        } else {
                            // Có nhiều implementation, kiểm tra @Qualifier
                            Qualifier qualifier = getQualifierAnnotation(constructor, i);
                            if (qualifier == null) {
                                throw new IllegalArgumentException("Multiple implementations found for interface: " + parameterType.getName()
                                        + ". Please use @Qualifier to specify the bean name.");
                            }
                            Class<?> implClass = qualifier.value();
                            paramBeans[i] = getBeanByName(implClass, parameterType);
                        }
                    }
                    // Kiểm tra có phải 1 class đánh dấu là bean hay không
                    else if (isBean(parameterType)) {
                        paramBeans[i] = getBeanFromContextOrCreate(parameterType);
                    }
                }
                obj = clazz.getDeclaredConstructor(parameterTypes).newInstance(paramBeans);
                if(isEnabled(EnableAsync.class) && isEnabled(Async.class, obj)){
                    ThreadPoolExecutor executorServiceAsync =  (ThreadPoolExecutor) servletContext.getAttribute(getBeanName(ExecutorService.class));
                    executorServiceAsync = (ThreadPoolExecutor) (ObjectUtils.isEmpty(executorServiceAsync)
                            ? Executors.newFixedThreadPool(5) : executorServiceAsync);
                    obj = wrapProxy(obj, new AsyncProxy(executorServiceAsync));
                    replace = true;
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to initialize bean for class: " + clazz.getName(), e);
            }
        }
        return new BeanRegister<>(getBeanName(clazz), obj, replace);
    }

    @SuppressWarnings("unchecked")
    private <T, P extends Proxy> T wrapProxy(T obj, P proxy) {
        Class<?>[] interfaces = obj.getClass().getInterfaces();
        if (interfaces.length > 0) {
            final var interfaceType = interfaces[0]; // Lấy interface đầu tiên
            // Kiểm tra nếu instance thực sự implement interface này
            if (!interfaceType.isInstance(obj)) {
                throw new IllegalArgumentException("Instance does not implement the specified interface: " + interfaceType.getName());
            }
            return (T) proxy.createProxy(obj, interfaceType);
        }
        throw new IllegalArgumentException("Class does not implement any interface!");
    }

    protected String getBeanName(final Method method) {
        final var name = method.getAnnotation(Bean.class).name();
        return ObjectUtils.isEmpty(name) ? method.getName() : name;
    }

    private Qualifier getQualifierAnnotation(Constructor<?> constructor, int paramIndex) {
        Annotation[] parameterAnnotations = constructor.getParameterAnnotations()[paramIndex];
        for (Annotation annotation : parameterAnnotations) {
            if (annotation instanceof Qualifier q) {
                return q;
            }
        }
        return null;
    }

    protected boolean isEnabled(Class<? extends Annotation> annotation) {
        return !new Reflections(
                new ConfigurationBuilder()
                        .forPackage("com.course")
                        .setScanners(
                                Scanners.TypesAnnotated
                        )
        ).getTypesAnnotatedWith(annotation).isEmpty();
    }

    protected boolean isEnabled(Class<? extends Annotation> annotation, Object obj) {
        if (obj.getClass().isAnnotationPresent(annotation)) {
            return true;
        }
        // Lấy tất cả các phương thức từ class của đối tượng
        Method[] methods = obj.getClass().getDeclaredMethods();
        // Kiểm tra từng phương thức xem có annotation không
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {
                return true; // Nếu tìm thấy ít nhất 1 phương thức có annotation
            }
        }
        return false; // Không tìm thấy phương thức nào có annotation
    }


    @Data
    @AllArgsConstructor
    public static class BeanRegister<T> {
        private String beanName;
        private T bean;
        private boolean replace;
    }

}
