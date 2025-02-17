package com.course.core.bean.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <p>
 * For example:
 * </p>
 * <pre>
 * &#064;Bean
 * public class Service {
 *
 * 	public Service(@Qualifier(TestRepositoryImpl.class) final TestRepository repository) {
 * 	}
 * }
 * </pre>
 */

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RUNTIME)
public @interface Qualifier {
    Class<?> value();
}
