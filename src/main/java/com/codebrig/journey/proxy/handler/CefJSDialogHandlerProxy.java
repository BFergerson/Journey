package com.codebrig.journey.proxy.handler;

import com.codebrig.journey.JourneyLoader;
import com.codebrig.journey.proxy.CefBrowserProxy;
import com.codebrig.journey.proxy.callback.CefJSDialogCallbackProxy;
import com.codebrig.journey.proxy.misc.BoolRefProxy;
import org.joor.Reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Journey local proxy for CefJSDialogHandler.
 * <p>
 * Javadoc taken from: https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.4.1
 * @since 0.4.0
 */
public interface CefJSDialogHandlerProxy {

    Reflect.ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {
        if ("onJSDialog".equals(methodName)) {
            args[0] = Reflect.on(args[0]).as(CefBrowserProxy.class);
            args[2] = JSDialogType.valueOf(args[2].toString());
            args[5] = Reflect.on(args[5]).as(CefJSDialogCallbackProxy.class);
            args[6] = Reflect.on(args[6]).as(BoolRefProxy.class);
        }
    };

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (methodName, returnValue) -> returnValue;

    /**
     * Supported JavaScript dialog types.
     */
    enum JSDialogType {
        JSDIALOGTYPE_ALERT,
        JSDIALOGTYPE_CONFIRM,
        JSDIALOGTYPE_PROMPT,
    }

    /**
     * Called to run a JavaScript dialog. Set suppressMessage to true and
     * return false to suppress the message (suppressing messages is preferable
     * to immediately executing the callback as this is used to detect presumably
     * malicious behavior like spamming alert messages in onbeforeunload). Set
     * suppressMessage to false and return false to use the default
     * implementation (the default implementation will show one modal dialog at a
     * time and suppress any additional dialog requests until the displayed dialog
     * is dismissed). Return true if the application will use a custom dialog or
     * if the callback has been executed immediately. Custom dialogs may be either
     * modal or modeless. If a custom dialog is used the application must execute
     * callback once the custom dialog is dismissed.
     *
     * @param browser           The corresponding browser.
     * @param originUrl         The originating url.
     * @param dialogType        the dialog type.
     * @param messageText       the text to be displayed.
     * @param defaultPromptText value will be specified for prompt dialogs only.
     * @param callback          execute callback once the custom dialog is dismissed.
     * @param suppressMessage   set to true to suppress displaying the message.
     * @return false to use the default dialog implementation. Return true if the
     * application will use a custom dialog.
     */
    boolean onJSDialog(CefBrowserProxy browser, String originUrl, JSDialogType dialogType,
                       String messageText, String defaultPromptText, CefJSDialogCallbackProxy callback,
                       BoolRefProxy suppressMessage);

    /**
     * Called to run a dialog asking the user if they want to leave a page. Return
     * false to use the default dialog implementation. Return true if the
     * application will use a custom dialog or if the callback has been executed
     * immediately. Custom dialogs may be either modal or modeless. If a custom
     * dialog is used the application must execute callback once the custom
     * dialog is dismissed.
     *
     * @param browser     The corresponding browser.
     * @param messageText The text to be displayed.
     * @param isReload    true if the page is reloaded.
     * @param callback    execute callback once the custom dialog is dismissed.
     * @return false to use the default dialog implementation. Return true if the
     * application will use a custom dialog.
     */
    boolean onBeforeUnloadDialog(CefBrowserProxy browser, String messageText, boolean isReload,
                                 CefJSDialogCallbackProxy callback);

    /**
     * Called to cancel any pending dialogs and reset any saved dialog state. Will
     * be called due to events like page navigation irregardless of whether any
     * dialogs are currently pending.
     */
    void onResetDialogState(CefBrowserProxy browser);

    /**
     * Called when the default implementation dialog is closed.
     */
    void onDialogClosed(CefBrowserProxy browser);

    static CefJSDialogHandlerProxy createHandler(CefJSDialogHandlerProxy handler) {
        Object instance = Proxy.newProxyInstance(JourneyLoader.getJourneyClassLoader(),
                new Class<?>[]{JourneyLoader.getJourneyClassLoader().loadClass("org.cef.handler.CefJSDialogHandler")},
                new Reflect.ProxyInvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) {
                        ((Reflect.ProxyArgumentsConverter) Reflect.on(handler).field("PROXY_ARGUMENTS_CONVERTER").get())
                                .convertArguments(method.getName(), args);
                        return ((Reflect.ProxyValueConverter) Reflect.on(handler).field("PROXY_VALUE_CONVERTER").get())
                                .convertValue(method.getName(), Reflect.on(handler).call(method.getName(), args).get());
                    }
                });
        return Reflect.on(instance).as(CefJSDialogHandlerProxy.class);
    }
}
