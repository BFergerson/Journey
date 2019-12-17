package com.codebrig.journey.proxy.handler;

import com.codebrig.journey.JourneyLoader;
import com.codebrig.journey.proxy.CefBrowserProxy;
import com.codebrig.journey.proxy.misc.CefKeyEventWrapper;
import com.codebrig.journey.proxy.misc.BoolRefProxy;
import org.joor.Reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Journey local proxy for CefKeyboardHandler.
 * <p>
 * Javadoc taken from https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:matyas.mazzag@gmail.com">Matyas Mazzag</a>
 */
@SuppressWarnings("unused")
public interface CefKeyboardHandlerProxy extends Reflect.ProxyObject {

    Reflect.ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {
        if ("onPreKeyEvent".equals(methodName)) {
            args[0] = Reflect.on(args[0]).as(CefBrowserProxy.class);
            args[1] = new CefKeyEventWrapper(args[1]);
            args[2] = Reflect.on(args[2]).as(BoolRefProxy.class);
        } else if ("onKeyEvent".equals(methodName)) {
            args[0] = Reflect.on(args[0]).as(CefBrowserProxy.class);
            args[1] = new CefKeyEventWrapper(args[1]);
        }
    };

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (methodName, returnValue) -> returnValue;

    /**
     * Called before a keyboard event is sent to the renderer.
     *
     * @param browser            the corresponding browser.
     * @param event              contains information about the keyboard event.
     * @param isKeyboardShortcut set to true and return false, if the event will be handled in OnKeyEvent() as a keyboard shortcut.
     * @return true if the event was handled or false otherwise.
     */
    boolean onPreKeyEvent(CefBrowserProxy browser, CefKeyEventWrapper event, BoolRefProxy isKeyboardShortcut);

    /**
     * Called after the renderer and JavaScript in the page has had a chance to handle the event.
     *
     * @param browser the corresponding browser.
     * @param event   contains information about the keyboard event.
     * @return true if the keyboard event was handled or false otherwise.
     */
    default boolean onKeyEvent(CefBrowserProxy browser, CefKeyEventWrapper event) {
        return false;
    }

    /**
     * Use this to create a Journey browser specific keyboard handler to receive browser keyboard events.
     *
     * @param handler
     * @return
     */
    static CefKeyboardHandlerProxy createHandler(CefKeyboardHandlerProxy handler) {
        Object instance = Proxy.newProxyInstance(JourneyLoader.getJourneyClassLoader(),
                new Class<?>[]{JourneyLoader.getJourneyClassLoader().loadClass("org.cef.handler.CefKeyboardHandler")},
                new Reflect.ProxyInvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) {
                        ((Reflect.ProxyArgumentsConverter) Reflect.on(handler).field("PROXY_ARGUMENTS_CONVERTER").get())
                                .convertArguments(method.getName(), args);
                        return ((Reflect.ProxyValueConverter) Reflect.on(handler).field("PROXY_VALUE_CONVERTER").get())
                                .convertValue(method.getName(), Reflect.on(handler).call(method.getName(), args).get());
                    }
                });
        return Reflect.on(instance).as(CefKeyboardHandlerProxy.class);
    }
}
