package com.codebrig.journey.proxy.handler;

import com.codebrig.journey.JourneyLoader;
import com.codebrig.journey.proxy.CefBrowserProxy;
import org.joor.Reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Journey local proxy for CefLifeSpanHandler.
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.2.17
 * @since 0.2.17
 */
@SuppressWarnings("unused")
public interface CefLifeSpanHandlerProxy extends Reflect.ProxyObject {

    Reflect.ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {
        if ("onAfterCreated".equals(methodName)) {
            args[0] = Reflect.on(args[0]).as(CefBrowserProxy.class);
        } else if ("onAfterParentChanged".equals(methodName)) {
            args[0] = Reflect.on(args[0]).as(CefBrowserProxy.class);
        } else if ("doClose".equals(methodName)) {
            args[0] = Reflect.on(args[0]).as(CefBrowserProxy.class);
        } else if ("onBeforeClose".equals(methodName)) {
            args[0] = Reflect.on(args[0]).as(CefBrowserProxy.class);
        }
    };

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (methodName, returnValue) -> returnValue;

    void onAfterCreated(CefBrowserProxy browser);

    void onAfterParentChanged(CefBrowserProxy browser);

    boolean doClose(CefBrowserProxy browser);

    void onBeforeClose(CefBrowserProxy browser);

    static CefLifeSpanHandlerProxy createHandler(CefLifeSpanHandlerProxy handler) {
        Object instance = Proxy.newProxyInstance(JourneyLoader.getJourneyClassLoader(),
                new Class<?>[]{JourneyLoader.getJourneyClassLoader().loadClass("org.cef.handler.CefLifeSpanHandler")},
                new Reflect.ProxyInvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) {
                        ((Reflect.ProxyArgumentsConverter) Reflect.on(handler).field("PROXY_ARGUMENTS_CONVERTER").get())
                                .convertArguments(method.getName(), args);
                        return ((Reflect.ProxyValueConverter) Reflect.on(handler).field("PROXY_VALUE_CONVERTER").get())
                                .convertValue(method.getName(), Reflect.on(handler).call(method.getName(), args).get());
                    }
                });
        return Reflect.on(instance).as(CefLifeSpanHandlerProxy.class);
    }
}
