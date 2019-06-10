package com.codebrig.journey.proxy;

import org.joor.Reflect;

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
