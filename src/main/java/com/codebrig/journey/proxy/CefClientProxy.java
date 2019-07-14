package com.codebrig.journey.proxy;

import com.codebrig.journey.proxy.handler.CefLifeSpanHandlerProxy;
import org.joor.Reflect;

import java.lang.reflect.Proxy;

/**
 * Journey local proxy for CefClient.
 * <p>
 * Javadoc taken from: https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.3.0
 * @since 0.2.0
 */
@SuppressWarnings("unused")
public interface CefClientProxy extends Reflect.ProxyObject {

    Reflect.ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {
        if ("addLifeSpanHandler".equals(methodName)) {
            args[0] = ((Reflect.ProxyInvocationHandler) Proxy.getInvocationHandler(args[0])).getUnderlyingObject();
        }
    };

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (methodName, returnValue) -> {
        if ("createBrowser".equals(methodName)) {
            return Reflect.on(returnValue).as(CefBrowserProxy.class);
        } else if ("addLifeSpanHandler".equals(methodName)) {
            return Reflect.on(returnValue).as(CefClientProxy.class);
        }
        return returnValue;
    };

    void dispose();

    CefBrowserProxy createBrowser(String url, boolean isOffscreenRendered, boolean isTransparent);

    CefClientProxy addLifeSpanHandler(CefLifeSpanHandlerProxy handler);

    void removeContextMenuHandler();

    void removeDialogHandler();

    void removeDisplayHandler();

    void removeDownloadHandler();

    void removeDragHandler();

    void removeFocusHandler();

    void removeJSDialogHandler();

    void removeKeyboardHandler();

    void removeLifeSpanHandler();

    void removeLoadHandler();

    void removeRequestHandler();
}
