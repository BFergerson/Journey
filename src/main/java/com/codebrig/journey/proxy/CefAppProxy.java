package com.codebrig.journey.proxy;

import org.joor.Reflect;

/**
 * Journey local proxy for CefApp.
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.2.17
 * @since 0.2.0
 */
@SuppressWarnings("unused")
public interface CefAppProxy extends Reflect.ProxyObject {

    Reflect.ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {
    };

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (methodName, returnValue) -> {
        if ("createClient".equals(methodName)) {
            return Reflect.on(returnValue).as(CefClientProxy.class);
        }
        return returnValue;
    };

    void dispose();

    CefClientProxy createClient();

    boolean clearSchemeHandlerFactories();

    void doMessageLoopWork(long delayMs);
}
