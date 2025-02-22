package com.course.core.scheduling.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scheduled {
    long fixedDelay() default 5000;
    long initialDelay() default 0;
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
