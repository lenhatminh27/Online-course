package com.course.core.bean;

import com.course.common.utils.ObjectUtils;
import com.course.common.utils.StringUtils;
import com.course.core.bean.annotations.Bean;
import com.course.core.bean.annotations.Repository;
import com.course.core.bean.annotations.Service;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;
import jakarta.servlet.ServletContext;

import java.lang.reflect.Constructor;
import java.util.Collections;

@WebListener
public class BeanListener extends BeanFactory implements ServletContextListener {

    private static final String beanNameException = "exceptionHandler_";

    public BeanListener() {
        super(
                new Reflections(
                        new ConfigurationBuilder()
                                .forPackage("com.course")
                                .setScanners(
                                        Scanners.ConstructorsAnnotated,
                                        Scanners.FieldsAnnotated,
                                        Scanners.TypesAnnotated,
                                        Scanners.MethodsAnnotated,
                                        Scanners.SubTypes
                                )
                )
        );
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Dynamically add entity classes using Reflections
        super.setServletContext(sce.getServletContext());
        BeanContext.setServletContext(sce.getServletContext());

        // Xử lý @Bean
        handleConfigurationBean();

        // Xử lý @Repository
        handleRepository();

        // Xử lý @Service
        handleService();

    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        final var attributeNames = servletContext.getAttributeNames();
        // Xóa bean
        Collections.list(attributeNames)
                .forEach(servletContext::removeAttribute);
    }

    private void handleRepository() {
        reflections.getTypesAnnotatedWith(Repository.class)
                .forEach(clazz -> registerBean(getBeanName(clazz), getInstanceRepository(clazz)));
    }


    /**
     * Scan @Service và tạo bean add vào context
     */
    private void handleService() {
        reflections.getTypesAnnotatedWith(Service.class, true)
                .forEach(clazz -> {
                    try {
                        final BeanRegister<?> beanRegister = getInstanceService(clazz);
                        if (beanRegister.isReplace()) {
                            registerBean(beanRegister.getBeanName(), beanRegister.getBean());
                        } else {
                            if (ObjectUtils.isEmpty(BeanContext.getBean(String.valueOf(beanRegister.getBean().getClass())))) {
                                registerBean(beanRegister.getBeanName(), beanRegister.getBean());
                            }
                        }
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Failed to initialize bean for class: " + clazz.getName(), e);
                    }
                });

    }

    /**
     * Sử lý add @Bean vào context
     */
    private void handleConfigurationBean() {
        reflections.getMethodsAnnotatedWith(Bean.class)
                .forEach(method -> {
                    try {
                        // Lấy đối tượng chứa method (class) mà method này thuộc về
                        var declaringObject = getInstanceService(method.getDeclaringClass());

                        // Lấy danh sách các tham số của method
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        Object[] paramBeans = new Object[parameterTypes.length];

                        // Duyệt qua từng tham số để khởi tạo
                        for (int i = 0; i < parameterTypes.length; i++) {
                            Class<?> parameterType = parameterTypes[i];

                            // Kiểm tra nếu tham số là bean
                            if (isBean(parameterType) || hasBean(parameterType)) {
                                String beanName = getBeanName(parameterType);
                                Object existingBean = servletContext.getAttribute(beanName);

                                // Nếu bean chưa tồn tại trong context, khởi tạo mới
                                if (ObjectUtils.isEmpty(existingBean)) {
                                    var beanRegister = getInstanceService(parameterType);
                                    servletContext.setAttribute(beanRegister.getBeanName(), beanRegister.getBean());
                                    paramBeans[i] = beanRegister.getBean();
                                } else {
                                    paramBeans[i] = existingBean;
                                }
                            }
                        }

                        // Gọi method và lưu kết quả vào ServletContext
                        Object bean = method.invoke(declaringObject.getBean(), paramBeans);
                        String beanName = getBeanName(method);
                        servletContext.setAttribute(beanName, bean);

                    } catch (Exception e) {
                        throw new IllegalArgumentException(
                                "Failed to initialize bean for method: " + method.getName(), e);
                    }
                });
    }

    private boolean hasBean(Class<?> clazz) {
        return !ObjectUtils.isEmpty(servletContext.getAttribute(getBeanName(clazz))) ||
                !ObjectUtils.isEmpty(servletContext.getAttribute(StringUtils.uncapitalize(clazz.getSimpleName())));
    }

    private <T> T getInstanceRepository(Class<T> clazz) {
        T obj = null;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            try {
                // Lấy danh sách các tham số của method
                Class<?>[] parameterTypes = constructor.getParameterTypes();
                Object[] paramBeans = new Object[parameterTypes.length];

                // Duyệt qua từng tham số để khởi tạo
                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> parameterType = parameterTypes[i];
                    paramBeans[i] = servletContext.getAttribute(StringUtils.uncapitalize(parameterType.getSimpleName()));
                }

                obj = clazz.getDeclaredConstructor(parameterTypes).newInstance(paramBeans);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Failed to initialize bean for class: " + clazz.getName(), ex);
            }
        }
        return obj;
    }

    protected void registerBean(String name, Object bean) {
        if (ObjectUtils.isEmpty(servletContext.getAttribute(name))) {
            servletContext.setAttribute(name, bean);
        }
    }


    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class BeanContext {
        static final BeanContext INSTANCE = new BeanContext();

        @Setter
        private static ServletContext servletContext;

        @SuppressWarnings("unchecked")
        public static <T> T getBean(String name) {
            if (null != servletContext) {
                Object bean = servletContext.getAttribute(StringUtils.uncapitalize(name));
                if (null == bean) {
                    return null;
                }
                return (T) bean;
            }
            return null;
        }
    }
}
