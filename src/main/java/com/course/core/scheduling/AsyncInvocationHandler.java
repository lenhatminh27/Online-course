package com.course.core.scheduling;

import com.course.core.scheduling.annotations.Async;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;

@Slf4j
public class AsyncInvocationHandler implements InvocationHandler {

    private final Object target;

    private final ExecutorService executor;

    public AsyncInvocationHandler(final Object target, final ExecutorService executor) {
        this.target = target;
        this.executor = executor;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method methodImpl = target.getClass()
                .getMethod(method.getName(), method.getParameterTypes());
        if (methodImpl.isAnnotationPresent(Async.class)) {
            try{
                executor.execute(() -> {
                    try {
                        method.invoke(target, args);
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                });
            }catch (Exception e){
                log.error("Run proxy executor error", e);
            }
        }
        return null;
    }
}
