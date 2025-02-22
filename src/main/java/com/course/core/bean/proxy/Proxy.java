package com.course.core.bean.proxy;

public interface Proxy {

    <T> T createProxy(Object target, Class<T> interfaceType);

}
