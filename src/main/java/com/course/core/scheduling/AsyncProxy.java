package com.course.core.scheduling;

import com.course.core.bean.proxy.Proxy;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;

@Slf4j
public class AsyncProxy implements Proxy {

    private final ExecutorService executor;

    public AsyncProxy(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createProxy(Object target, Class<T> interfaceType) {
         if (!interfaceType.isInstance(target)){
             throw new IllegalArgumentException("Target does not implement the specified interface: " + interfaceType.getName());
         }
        return (T) java.lang.reflect.Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class[]{interfaceType},
                new AsyncInvocationHandler(target, executor)
        );
    }
}
