package com.codebrig.journey.example;

import com.codebrig.journey.JourneyBrowserView;
import com.codebrig.journey.proxy.CefBrowserProxy;
import com.codebrig.journey.proxy.handler.CefKeyboardHandlerProxy;
import com.codebrig.journey.proxy.misc.BoolRefProxy;
import com.codebrig.journey.proxy.misc.CefKeyEventWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class KeyboardHandlerTest {

    public static void main(String[] args) {
        JourneyBrowserView browser = new JourneyBrowserView("https://google.com");
        browser.getCefClient().addKeyboardHandler(CefKeyboardHandlerProxy.createHandler(new CefKeyboardHandlerProxy() {

            @Override
            public boolean onPreKeyEvent(CefBrowserProxy browser, CefKeyEventWrapper event, BoolRefProxy isKeyboardShortcut) {
                System.out.printf("Keyboard event - Type: %s - Native Key Code: %s - Value: %s%n",
                        event.getType(), event.getNativeKeyCode(), event.getCharacter());
                return false;
            }

            @Override
            public boolean onKeyEvent(CefBrowserProxy browser, CefKeyEventWrapper event) {
                return false;
            }
        }));

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

        frame.setTitle("Journey");
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }
}
