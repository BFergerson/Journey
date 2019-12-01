package com.codebrig.journey.proxy.browser;

import com.codebrig.journey.JourneyLoader;
import com.codebrig.journey.proxy.CefBrowserProxy;
import com.codebrig.journey.proxy.handler.CefMessageRouterHandlerProxy;
import org.joor.Reflect;
import org.joor.Reflect.ProxyArgumentsConverter;
import org.joor.Reflect.ProxyValueConverter;

import java.lang.reflect.Proxy;

/**
 * Journey local proxy for CefMessageRouter.
 * <p>
 * Javadoc taken from: https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:dhruvit.raithatha@gmail.com">Dhruvit Raithatha</a>
 * @version 0.4.0
 * @since 0.4.0
 */
public interface CefMessageRouterProxy extends Reflect.ProxyObject {

    ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {
        if ("addHandler".equals(methodName)) {
            args[0] = ((Reflect.ProxyInvocationHandler) Proxy.getInvocationHandler(args[0])).getUnderlyingObject();
        } else if ("removeHandler".equals(methodName)) {
            args[0] = ((Reflect.ProxyInvocationHandler) Proxy.getInvocationHandler(args[0])).getUnderlyingObject();
        } else if ("cancelPending".equals(methodName)) {
            args[0] = Reflect.on(args[0]).as(CefBrowserProxy.class);
            args[0] = ((Reflect.ProxyInvocationHandler) Proxy.getInvocationHandler(args[0])).getUnderlyingObject();
        } else if ("getPendingCount".equals(methodName)) {
            args[0] = Reflect.on(args[0]).as(CefBrowserProxy.class);
            args[0] = ((Reflect.ProxyInvocationHandler) Proxy.getInvocationHandler(args[0])).getUnderlyingObject();
        }
    };

    ProxyValueConverter PROXY_VALUE_CONVERTER = (methodName, returnValue) -> returnValue;

    /**
     * Must be called if the CefMessageRouterProxy instance isn't used any more
     */
    void dispose();

    /**
     * Add a new query handler. If |first| is true it will be added as the first
     * handler, otherwise it will be added as the last handler. Returns true if the
     * handler is added successfully or false if the handler has already been added.
     * Must be called on the browser process UI thread. The Handler object must
     * either outlive the router or be removed before deletion.
     *
     * @param handler the according handler to be added
     * @param first   if If set to true it will be added as the first handler
     * @return true if the handler is added successfully
     */
    boolean addHandler(CefMessageRouterHandlerProxy handler, boolean first);

    /**
     * Remove an existing query handler. Any pending queries associated with the
     * handler will be canceled. Handler.OnQueryCanceled will be called and the
     * associated JavaScript onFailure callback will be executed with an error code
     * of -1. Returns true if the handler is removed successfully or false if the
     * handler is not found. Must be called on the browser process UI thread.
     *
     * @param handler the according handler to be removed
     * @return true if the handler is removed successfully
     */
    boolean removeHandler(CefMessageRouterHandlerProxy handler);

    /**
     * Cancel all pending queries associated with either |browser| or |handler|. If
     * both |browser| and |handler| are NULL all pending queries will be canceled.
     * Handler::OnQueryCanceled will be called and the associated JavaScript
     * onFailure callback will be executed in all cases with an error code of -1.
     *
     * @param browser may be empty
     * @param handler may be empty
     */
    void cancelPending(CefBrowserProxy browser, CefMessageRouterHandlerProxy handler);

    /**
     * Returns the number of queries currently pending for the specified |browser|
     * and/or |handler|. Either or both values may be empty. Must be called on the
     * browser process UI thread.
     *
     * @param browser may be empty
     * @param handler may be empty
     * @return the number of queries currently pending
     */
    int getPendingCount(CefBrowserProxy browser, CefMessageRouterHandlerProxy handler);

    static CefMessageRouterProxy createRouter() {
        JourneyLoader classLoader = JourneyLoader.getJourneyClassLoader();
        Object realCefMessageRouter = Reflect.onClass(classLoader.loadClass("org.cef.browser.CefMessageRouter"))
                .call("create").get();
        return Reflect.on(realCefMessageRouter).as(CefMessageRouterProxy.class);
    }
}
