package com.codebrig.journey.proxy.handler;

import com.codebrig.journey.JourneyLoader;
import com.codebrig.journey.proxy.CefBrowserProxy;
import com.codebrig.journey.proxy.browser.CefFrameProxy;
import org.joor.Reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Journey local proxy for CefLifeSpanHandler.
 * <p>
 * Javadoc taken from: https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.2.18
 * @since 0.2.17
 */
@SuppressWarnings("unused")
public interface CefLifeSpanHandlerProxy extends Reflect.ProxyObject {

    Reflect.ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {
        if ("onBeforePopup".equals(methodName)) {
            args[0] = Reflect.on(args[0]).as(CefBrowserProxy.class);
            args[1] = Reflect.on(args[1]).as(CefFrameProxy.class);
        } else if ("onAfterCreated".equals(methodName)) {
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

    /**
     * Called on the IO thread before a new popup window is created. The |browser|
     * and |frame| parameters represent the source of the popup request. The
     * |targetUrl| and |targetFrameName| values may be empty if none were
     * specified with the request. The |popupFeatures| structure contains
     * information about the requested popup window. To allow creation of the
     * popup window optionally modify |windowInfo|, |client|, |settings| and
     * |no_javascript_access| and return false. To cancel creation of the popup
     * window return true. The |client| and |settings| values will default to the
     * source browser's values. The |no_javascript_access| value indicates whether
     * the new browser window should be scriptable and in the same process as the
     * source browser.
     *
     * @param browser         The source of the popup request.
     * @param frame           The source of the popup request.
     * @param targetUrl       May be empty if none is specified with the request.
     * @param targetFrameName May be empty if none is specified with the request.
     * @return To cancel creation of the popup window return true.
     */
    boolean onBeforePopup(CefBrowserProxy browser, CefFrameProxy frame, String targetUrl, String targetFrameName);

    /**
     * Handle creation of a new browser window.
     *
     * @param browser The browser generating the event.
     */
    void onAfterCreated(CefBrowserProxy browser);

    /**
     * Called after a browser's native parent window has changed.
     *
     * @param browser The browser generating the event.
     */
    void onAfterParentChanged(CefBrowserProxy browser);

    /**
     * Called when a browser has received a request to close.
     * <p>
     * If CEF created an OS window for the browser returning false will send an OS
     * close notification to the browser window's top-level owner (e.g. WM_CLOSE
     * on Windows, performClose: on OS-X and "delete_event" on Linux). If no OS
     * window exists (window rendering disabled) returning false will cause the
     * browser object to be destroyed immediately. Return true if the browser is
     * parented to another window and that other window needs to receive close
     * notification via some non-standard technique.
     *
     * @param browser The browser generating the event.
     * @return false will send an OS close notification to the browser window's top-level owner.
     */
    boolean doClose(CefBrowserProxy browser);

    /**
     * Called just before a browser is destroyed.
     * <p>
     * Release all references to the
     * browser object and do not attempt to execute any methods on the browser
     * object after this callback returns. If this is a modal window and a custom
     * modal loop implementation was provided in runModal() this callback should
     * be used to exit the custom modal loop. See doClose() documentation for
     * additional usage information.
     *
     * @param browser The browser generating the event.
     */
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
