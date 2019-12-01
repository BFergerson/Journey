package com.codebrig.journey.proxy.callback;

import org.joor.Reflect;

/**
 * Journey local proxy for CefJSDialogCallback.
 * <p>
 * Javadoc taken from: https://bitbucket.org/chromiumembedded/java-cef
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.4.0
 * @since 0.4.0
 */
public interface CefJSDialogCallbackProxy {

    Reflect.ProxyArgumentsConverter PROXY_ARGUMENTS_CONVERTER = (methodName, args) -> {
    };

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (methodName, returnValue) -> returnValue;

    /**
     * Continue the JS dialog request.
     *
     * @param success   Set to true if the OK button was pressed.
     * @param userInput The value should be specified for prompt dialogs.
     */
    void Continue(boolean success, String userInput);
}