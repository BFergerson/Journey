package com.codebrig.journey.proxy.handler;

import com.codebrig.journey.JourneyLoader;
import com.codebrig.journey.proxy.CefBrowserProxy;
import com.codebrig.journey.proxy.browser.CefFrameProxy;
import com.codebrig.journey.proxy.callback.CefNativeProxy;
import com.codebrig.journey.proxy.callback.CefQueryCallbackProxy;
import org.joor.Reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Journey local proxy for CefMessageRouterHandler.
 * <p>
 * Javadoc taken from: https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:dhruvit.raithatha@gmail.com">Dhruvit Raithatha</a>
 * @version 0.4.0
 * @since 0.4.0
 */
public interface CefMessageRouterHandlerProxy extends CefNativeProxy {

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

    /**
     * Called when the browser receives a JavaScript query.
     *
     * @param browser    The corresponding browser.
     * @param frame      The frame generating the event. Instance only valid within the scope of this
     *                   method.
     * @param queryId    The unique ID for the query.
     * @param persistent True if the query is persistent.
     * @param callback   Object used to continue or cancel the query asynchronously.
     * @return True to handle the query or false to propagate the query to other registered
     * handlers, if any. If no handlers return true from this method then the query will be
     * automatically canceled with an error code of -1 delivered to the JavaScript onFailure
     * callback.
     */
    boolean onQuery(CefBrowserProxy browser, CefFrameProxy frame, long queryId, String request,
                    boolean persistent, CefQueryCallbackProxy callback);

    /**
     * Called when a pending JavaScript query is canceled.
     *
     * @param browser The corresponding browser.
     * @param frame   The frame generating the event. Instance only valid within the scope of this
     *                method.
     * @param queryId The unique ID for the query.
     */
    void onQueryCanceled(CefBrowserProxy browser, CefFrameProxy frame, long queryId);

    static CefMessageRouterHandlerProxy createHandler(CefMessageRouterHandlerProxy handler) {
        Object instance = Proxy.newProxyInstance(JourneyLoader.getJourneyClassLoader(),
                new Class<?>[]{JourneyLoader.getJourneyClassLoader()
                        .loadClass("org.cef.handler.CefMessageRouterHandler")},
                new Reflect.ProxyInvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) {
                        ((Reflect.ProxyArgumentsConverter) Reflect.on(handler).field("PROXY_ARGUMENTS_CONVERTER").get())
                                .convertArguments(method.getName(), args);
                        return ((Reflect.ProxyValueConverter) Reflect.on(handler).field("PROXY_VALUE_CONVERTER").get())
                                .convertValue(method.getName(), Reflect.on(handler).call(method.getName(), args).get());
                    }
                });
        return Reflect.on(instance).as(CefMessageRouterHandlerProxy.class);
    }
}
