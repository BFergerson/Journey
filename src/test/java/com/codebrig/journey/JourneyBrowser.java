package com.codebrig.journey;

import com.codebrig.journey.JourneyBrowserView;
import com.codebrig.journey.proxy.CefBrowserProxy;
import com.codebrig.journey.proxy.browser.CefFrameProxy;
import com.codebrig.journey.proxy.browser.CefMessageRouterProxy;
import com.codebrig.journey.proxy.callback.CefQueryCallbackProxy;
import com.codebrig.journey.proxy.handler.CefMessageRouterHandlerProxy;
import com.codebrig.journey.proxy.handler.CefNativeDefault;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MessageHandler extends CefNativeDefault implements CefMessageRouterHandlerProxy {
    public void onQueryCanceled(CefBrowserProxy browser, CefFrameProxy frame, long query_id) {
        System.out.println("Query cancelled.");
    }

    public boolean onQuery(CefBrowserProxy browser, CefFrameProxy frame, long query_id, String request,
            boolean persistent, CefQueryCallbackProxy callback) {
        System.out.println("Query succeeded: " + request);
        callback.success(request);
        return true;
    }
}

public class JourneyBrowser {

    public static void main(String[] args) throws Exception {
        JourneyBrowserView browser = new JourneyBrowserView("https://google.com");
        CefMessageRouterProxy messageRouter = CefMessageRouterProxy.create();
        messageRouter.addHandler(new MessageHandler(), true);
        browser.getCefClient().addMessageRouter(messageRouter);
        JFrame frame = new JFrame();
        frame.getContentPane().add(browser, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                browser.getCefApp().dispose();
                frame.dispose();
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                browser.getCefApp().dispose();
                frame.dispose();
            }
        });

        frame.setTitle("Journey");
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }
}