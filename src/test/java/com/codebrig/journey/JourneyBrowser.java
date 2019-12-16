package com.codebrig.journey;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.codebrig.journey.proxy.CefBrowserProxy;
import com.codebrig.journey.proxy.browser.CefFrameProxy;
import com.codebrig.journey.proxy.browser.CefMessageRouterProxy;
import com.codebrig.journey.proxy.callback.CefQueryCallbackProxy;
import com.codebrig.journey.proxy.handler.CefMessageRouterHandlerProxy;

public class JourneyBrowser {

    public static void main(String[] args) {
        File jsTest = new File("src/test/resources/JSTest.html");
        JourneyBrowserView browser = new JourneyBrowserView("file:///" + jsTest.getAbsolutePath());

        CefMessageRouterProxy messageRouter = CefMessageRouterProxy.createRouter();
        messageRouter.addHandler(CefMessageRouterHandlerProxy.createHandler(new CefMessageRouterHandlerProxy() {

            private long N_CefHandle = 0;

            @Override
            public boolean onQuery(CefBrowserProxy browser, CefFrameProxy frame, long queryId, String request,
                                   boolean persistent, CefQueryCallbackProxy callback) {
                if ("javaVersion".equals(request)) {
                    callback.success(System.getProperty("java.version"));
                    return true;
                } else if ("showDevTools".equals(request)) {
                    CefBrowserProxy devTools = browser.getDevTools();
                    Component uiComponent = devTools.getUIComponent();
                    JFrame devFrame = new JFrame("DevToolsWindow");
                    devFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    devFrame.setSize(1000, 600);
                    devFrame.getContentPane().add(BorderLayout.CENTER, uiComponent);
                    devFrame.setVisible(true);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onQueryCanceled(CefBrowserProxy browser, CefFrameProxy frame, long queryId) {
            }

            @Override
            public void setNativeRef(String identifer, long nativeRef) {
                N_CefHandle = nativeRef;
            }

            @Override
            public long getNativeRef(String identifer) {
                return N_CefHandle;
            }
        }), true);
        browser.getCefClient().addMessageRouter(messageRouter);
        
        browser.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("keyPressed: " + e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
            }

            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("keyTyped: " + e);
            }
            
        });

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