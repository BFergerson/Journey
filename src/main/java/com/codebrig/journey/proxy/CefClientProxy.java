package com.codebrig.journey.proxy;

import org.joor.Reflect;

/**
 * Journey local proxy for CefClient.
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.2.16
 * @since 0.2.0
 */
@SuppressWarnings("unused")
public interface CefClientProxy extends Reflect.ProxyObject {

    Reflect.ProxyValueConverter PROXY_VALUE_CONVERTER = (name, o) -> {
        if ("createBrowser".equals(name)) {
            return Reflect.on(o).as(CefBrowserProxy.class);
        }
        return o;
    };

    CefBrowserProxy createBrowser(String var1, boolean var2, boolean var3);
}
