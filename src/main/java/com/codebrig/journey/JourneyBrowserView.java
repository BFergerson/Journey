package com.codebrig.journey;

import static org.joor.Reflect.on;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.codebrig.journey.proxy.CefAppProxy;
import com.codebrig.journey.proxy.CefBrowserProxy;
import com.codebrig.journey.proxy.CefClientProxy;
import com.codebrig.journey.proxy.browser.CefFrameProxy;
import com.codebrig.journey.proxy.browser.CefKeyEventWrapper;
import com.codebrig.journey.proxy.handler.CefKeyboardHandlerProxy;
import com.codebrig.journey.proxy.handler.CefLifeSpanHandlerProxy;

/**
 * Wraps CefApp/CefClient/CefBrowser and extends JComponent for ease of implementation.
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.4.0
 * @since 0.1.1
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class JourneyBrowserView extends JComponent {

    public static final String ABOUT_BLANK = "about:blank";
    public static final JourneySettings DEFAULT_SETTINGS;

    static {
        JourneyLoader.setup();
        DEFAULT_SETTINGS = new JourneySettings();
        DEFAULT_SETTINGS.setWindowlessRenderingEnabled(false);
        DEFAULT_SETTINGS.setLogFile(new File(JourneyLoader.NATIVE_DIRECTORY, "debug.log").getAbsolutePath());
    }

    private static JourneySettings journeySettings;
    private static CefAppProxy cefApp;
    private CefClientProxy cefClient;
    private CefBrowserProxy cefBrowser;

    public JourneyBrowserView(CefBrowserProxy browser) {
        this.cefBrowser = Objects.requireNonNull(browser);
        this.cefClient = browser.getClient();

        setLayout(new BorderLayout());
        if (SwingUtilities.isEventDispatchThread()) {
            add(cefBrowser.getUIComponent(), "Center");
        } else {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    add(cefBrowser.getUIComponent(), "Center");
                });
            } catch (InterruptedException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public JourneyBrowserView() {
        this(getDefaultCEFArguments(), DEFAULT_SETTINGS, ABOUT_BLANK);
    }

    public JourneyBrowserView(JourneySettings journeySettings, String initialUrl) {
        this(getDefaultCEFArguments(), journeySettings, initialUrl);
    }

    public JourneyBrowserView(String initialUrl) {
        this(getDefaultCEFArguments(), DEFAULT_SETTINGS, initialUrl);
    }

    public JourneyBrowserView(String[] args, JourneySettings journeySettings, String initialUrl) {
        JourneyBrowserView.journeySettings = journeySettings;

        setLayout(new BorderLayout());
        if (SwingUtilities.isEventDispatchThread()) {
            if (cefApp == null) {
                Object realCefApp = on(JourneyLoader.getJourneyClassLoader().loadClass("org.cef.CefApp"))
                        .call("getInstance", args, journeySettings.asCefSettings()).get();
                cefApp = on(realCefApp).as(CefAppProxy.class, JourneyLoader.getJourneyClassLoader());
            }
            cefClient = cefApp.createClient();
            cefBrowser = cefClient.createBrowser(initialUrl, false, false);
            add(cefBrowser.getUIComponent(), "Center");
        } else {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    if (cefApp == null) {
                        Object realCefApp = on(JourneyLoader.getJourneyClassLoader().loadClass("org.cef.CefApp"))
                                .call("getInstance", args, journeySettings.asCefSettings()).get();
                        cefApp = on(realCefApp).as(CefAppProxy.class, JourneyLoader.getJourneyClassLoader());
                    }
                    cefClient = cefApp.createClient();
                    cefBrowser = cefClient.createBrowser(initialUrl, false, false);
                    add(cefBrowser.getUIComponent(), "Center");
                });
            } catch (InterruptedException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }

        //https://github.com/CodeBrig/Journey/issues/13
        if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            cefClient.addLifeSpanHandler(CefLifeSpanHandlerProxy.createHandler(new CefLifeSpanHandlerProxy() {
                @Override
                public boolean onBeforePopup(CefBrowserProxy browser, CefFrameProxy frame, String targetUrl, String targetFrameName) {
                    return false;
                }

                @Override
                public void onAfterCreated(CefBrowserProxy browser) {
                    new Timer().schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    if (browser.getZoomLevel() != -1.5) {
                                        browser.setZoomLevel(-1.5);
                                    }
                                }
                            }, 0, 50
                    );
                }

                @Override
                public void onAfterParentChanged(CefBrowserProxy browser) {
                }

                @Override
                public boolean doClose(CefBrowserProxy browser) {
                    return false;
                }

                @Override
                public void onBeforeClose(CefBrowserProxy browser) {
                }
            }));
        }
        
        cefClient.addKeyboardHandler(CefKeyboardHandlerProxy.createHandler(new CefKeyboardHandlerProxy() {
            
            @Override
            public boolean onPreKeyEvent(CefBrowserProxy browser, CefKeyEventWrapper event, boolean isKeyboardShortcut) {
                KeyEvent ke = new KeyEvent(JourneyBrowserView.this, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), event.getModifiers(), event.getWindowsKeyCode(), event.getCharacter());
                for(KeyListener keyListener : getKeyListeners()) {
                    keyListener.keyPressed(ke);
                }
                return ke.isConsumed();
            }

            @Override
            public boolean onKeyEvent(CefBrowserProxy browser, CefKeyEventWrapper event) {
                KeyEvent ke = new KeyEvent(JourneyBrowserView.this, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), event.getModifiers(), event.getWindowsKeyCode(), event.getCharacter());
                for(KeyListener keyListener : getKeyListeners()) {
                    keyListener.keyTyped(ke);
                }
                return ke.isConsumed();
            }

        }));
    }

    public JourneySettings getJourneySettings() {
        return journeySettings;
    }

    public CefAppProxy getCefApp() {
        return cefApp;
    }

    public CefClientProxy getCefClient() {
        return cefClient;
    }

    public CefBrowserProxy getCefBrowser() {
        return cefBrowser;
    }

    private static String[] getDefaultCEFArguments() {
        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            File frameworkDirPath = new File(JourneyLoader.NATIVE_DIRECTORY,
                    "jcef_app.app/Contents/Frameworks/Chromium Embedded Framework.framework");
            File browserSubprocessPath = new File(JourneyLoader.NATIVE_DIRECTORY,
                    "jcef_app.app/Contents/Frameworks/jcef Helper.app/Contents/MacOS/jcef Helper");
            File resourcesDirPath = new File(JourneyLoader.NATIVE_DIRECTORY,
                    "jcef_app.app/Contents/Frameworks/Chromium Embedded Framework.framework/Resources");
            return new String[]{
                    "--framework-dir-path=" + frameworkDirPath.getAbsolutePath(),
                    "--browser-subprocess-path=" + browserSubprocessPath.getAbsolutePath(),
                    "--resources-dir-path=" + resourcesDirPath.getAbsolutePath(),
                    "--disable-gpu"
            };
        } else {
            return new String[0];
        }
    }
    
}
