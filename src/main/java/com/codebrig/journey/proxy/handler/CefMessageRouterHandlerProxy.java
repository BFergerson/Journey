package com.codebrig.journey.proxy.handler;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.codebrig.journey.JourneyLoader;
import com.codebrig.journey.proxy.CefBrowserProxy;
import com.codebrig.journey.proxy.browser.CefFrameProxy;
import com.codebrig.journey.proxy.callback.CefQueryCallbackProxy;

import org.joor.Reflect;

/**
 * Journey local proxy for CefMessageRouterHandlerProxy.
 * <p>
 * Javadoc taken from: https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:dhruvit.raithatha@gmail.com">Dhruvit Raithatha</a>
 * @version 0.3.4
 * @since 0.3.4
 */
public interface CefMessageRouterHandlerProxy extends Reflect.ProxyObject {

    Reflect.ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {
        if ("onQuery".equals(methodName)) {
            args[0] = Reflect.on(args[0]).as(CefBrowserProxy.class);
            args[1] = Reflect.on(args[1]).as(CefFrameProxy.class);
            args[5] = Reflect.on(args[5]).as(CefQueryCallbackProxy.class);
        } else if ("onQueryCanceled".equals(methodName)) {
            args[0] = Reflect.on(args[0]).as(CefBrowserProxy.class);
            args[1] = Reflect.on(args[1]).as(CefFrameProxy.class);
        }
    };

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (methodName, returnValue) -> returnValue;

    boolean onQuery(CefBrowserProxy browser, CefFrameProxy frame, long query_id, String request,
            boolean persistent, CefQueryCallbackProxy callback);

    void onQueryCanceled(CefBrowserProxy browser, CefFrameProxy frame, long query_id);

    void setNativeRef(String identifer, long nativeRef);

    long getNativeRef(String identifer);

    static CefMessageRouterHandlerProxy createHandler(CefMessageRouterHandlerProxy handler) {
        Object instance = Proxy.newProxyInstance(JourneyLoader.getJourneyClassLoader(),
                new Class<?>[] { JourneyLoader.getJourneyClassLoader()
                        .loadClass("org.cef.handler.CefMessageRouterHandler") },
                new Reflect.ProxyInvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) {
                        ((Reflect.ProxyArgumentsConverter) Reflect.on(handler)
                                .field("PROXY_ARGUMENTS_CONVERTER").get()).convertArguments(method.getName(),
                                        args);
                        return ((Reflect.ProxyValueConverter) Reflect.on(handler).field("PROXY_VALUE_CONVERTER")
                                .get()).convertValue(method.getName(),
                                        Reflect.on(handler).call(method.getName(), args).get());
                    }
                });
        return Reflect.on(instance).as(CefMessageRouterHandlerProxy.class);
    }
}
