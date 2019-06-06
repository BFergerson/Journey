package com.codebrig.journey;

import org.cef.CefApp;
import org.cef.OS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Downloads and loads the necessary CEF files for the current OS.
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.2.4
 * @since 0.1.1
 */
public class JourneyLoader {

    private static final ResourceBundle BUILD = ResourceBundle.getBundle("journey_build");
    public static final String VERSION = BUILD.getString("version");
    public static final String JCEF_VERSION = BUILD.getString("jcef_version");
    public static final String MODE = BUILD.getString("mode");
    public static final String PROJECT_URL = BUILD.getString("project_url");
    public static File NATIVE_DIRECTORY = new File((OS.isMacintosh()) ? "/tmp" : System.getProperty("java.io.tmpdir"),
            "journey-" + VERSION);

    private static JourneyLoaderListener JOURNEY_LOADER_LISTENER = new JourneyLoaderAdapter() {
    };
    private static final AtomicBoolean loaderSetup = new AtomicBoolean();

    public static void setup() throws RuntimeException {
        try {
            if (loaderSetup.getAndSet(true)) {
                return;
            }
            JOURNEY_LOADER_LISTENER.journeyLoaderStarted(VERSION, JCEF_VERSION);
            if (!NATIVE_DIRECTORY.exists()) NATIVE_DIRECTORY.mkdirs();
            JOURNEY_LOADER_LISTENER.usingNativeDirectory(NATIVE_DIRECTORY);

            String jcefName;
            String providerName;
            JOURNEY_LOADER_LISTENER.determiningOS();
            if (OS.isWindows()) {
                boolean is64bit;
                if (System.getProperty("os.name").contains("Windows")) {
                    is64bit = (System.getenv("ProgramFiles(x86)") != null);
                } else {
                    is64bit = (System.getProperty("os.arch").contains("64"));
                }
                if (is64bit) {
                    providerName = "windows_64";
                    jcefName = "win64";
                    JOURNEY_LOADER_LISTENER.determinedOS("windows", 32);
                } else {
                    providerName = "windows_32";
                    jcefName = "win32";
                    JOURNEY_LOADER_LISTENER.determinedOS("windows", 64);
                }
            } else if (OS.isLinux()) {
                providerName = "linux_64";
                jcefName = "linux64";
                JOURNEY_LOADER_LISTENER.determinedOS("linux", 64);
            } else if (OS.isMacintosh()) {
                providerName = "macintosh_64";
                jcefName = "macosx64";
                JOURNEY_LOADER_LISTENER.determinedOS("macintosh", 64);
            } else {
                JOURNEY_LOADER_LISTENER.determinedOS("unsupported", -1);
                throw new UnsupportedOperationException("OS is not currently supported");
            }

            int chromiumMajorVersion = Integer.parseInt(JCEF_VERSION.split("\\.")[0]);
            String jcefDistribFile = "jcef-distrib-" + providerName.replace("_", "") + ".zip";
            File localNative = new File(NATIVE_DIRECTORY, jcefDistribFile);
            if ("online".equals(MODE) && !localNative.exists()) {
                JOURNEY_LOADER_LISTENER.downloadingNativeCEFFiles();
                Files.copy(new URL(String.format("%s/releases/download/%s-%s-online/%s",
                        PROJECT_URL, VERSION, chromiumMajorVersion, jcefDistribFile)).openStream(), localNative.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                JOURNEY_LOADER_LISTENER.downloadedNativeCEFFiles();
            }

            if (!new File(NATIVE_DIRECTORY, "icudtl.dat").exists() && !new File(NATIVE_DIRECTORY, "jcef_app.app").exists()) {
                JOURNEY_LOADER_LISTENER.extractingNativeCEFFiles();
                String libLocation = String.format("%s/bin/", jcefName);
                if (OS.isLinux() || OS.isWindows()) {
                    libLocation += String.format("lib/%s/", jcefName);
                }
                if ("offline".equals(MODE)) {
                    //extract from self .jar
                    localNative = new File(JourneyLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI());
                }

                try (ZipFile zipFile = new ZipFile(localNative)) {
                    Enumeration<? extends ZipEntry> entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        if (!entry.getName().startsWith(libLocation)) {
                            continue;
                        }

                        File entryDestination = new File(NATIVE_DIRECTORY, entry.getName().replace(libLocation, ""));
                        if (entry.isDirectory()) {
                            entryDestination.mkdirs();
                        } else {
                            entryDestination.getParentFile().mkdirs();
                            InputStream in = zipFile.getInputStream(entry);
                            OutputStream out = new FileOutputStream(entryDestination);
                            int n;
                            byte[] buffer = new byte[1024];
                            while ((n = in.read(buffer)) > -1) {
                                out.write(buffer, 0, n);
                            }
                            out.close();
                        }
                    }
                }
            }
            JOURNEY_LOADER_LISTENER.extractedNativeCEFFiles();

            JOURNEY_LOADER_LISTENER.loadingNativeCEFFiles();
            if (OS.isWindows()) {
                loadWindows(NATIVE_DIRECTORY);
            } else if (OS.isLinux()) {
                loadLinux(NATIVE_DIRECTORY);
            } else if (OS.isMacintosh()) {
                loadMacintosh(NATIVE_DIRECTORY);
            }
            JOURNEY_LOADER_LISTENER.loadedNativeCEFFiles();

            if (chromiumMajorVersion >= 73) {
                Method method = CefApp.class.getMethod("startup");
                method.invoke(null);
            } else if (chromiumMajorVersion >= 69) {
                Method method = CefApp.class.getMethod("initXlibForMultithreading");
                method.invoke(null);
            }
            JOURNEY_LOADER_LISTENER.journeyLoaderComplete();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void setJourneyLoaderListener(JourneyLoaderListener listener) {
        JOURNEY_LOADER_LISTENER = Objects.requireNonNull(listener);
    }

    public static abstract class JourneyLoaderListener {
        public abstract void journeyLoaderStarted(String journeyVersion, String jcefVersion);

        public abstract void usingNativeDirectory(File nativeDir);

        public abstract void determiningOS();

        public abstract void determinedOS(String os, int bits);

        public abstract void downloadingNativeCEFFiles();

        public abstract void downloadedNativeCEFFiles();

        public abstract void extractingNativeCEFFiles();

        public abstract void extractedNativeCEFFiles();

        public abstract void loadingNativeCEFFiles();

        public abstract void loadedNativeCEFFiles();

        public abstract void journeyLoaderComplete();
    }

    public static abstract class JourneyLoaderAdapter extends JourneyLoaderListener {
        @Override
        public void journeyLoaderStarted(String journeyVersion, String jcefVersion) {
        }

        @Override
        public void usingNativeDirectory(File nativeDir) {
        }

        @Override
        public void determiningOS() {
        }

        @Override
        public void determinedOS(String os, int bits) {
        }

        @Override
        public void downloadingNativeCEFFiles() {
        }

        @Override
        public void downloadedNativeCEFFiles() {
        }

        @Override
        public void extractingNativeCEFFiles() {
        }

        @Override
        public void extractedNativeCEFFiles() {
        }

        @Override
        public void loadingNativeCEFFiles() {
        }

        @Override
        public void loadedNativeCEFFiles() {
        }

        @Override
        public void journeyLoaderComplete() {
        }
    }

    private static void loadLinux(File nativeDir) throws IllegalAccessException {
        System.setProperty("java.library.path", System.getProperty("java.library.path") + ":" +
                new File(System.getProperty("java.home"), "lib/amd64").getAbsolutePath() + ":" +
                nativeDir.getAbsolutePath());
        try {
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (NoSuchFieldException ex) {
            //ignore
        }

        if (!new File(nativeDir, "jcef_helper").setExecutable(true)) {
            throw new IllegalStateException("Failed to set jcef_helper as executable");
        }

        try {
            System.loadLibrary("jawt");
        } catch (UnsatisfiedLinkError e) {
            //ignore
        }
    }

    private static void loadWindows(File nativeDir) throws IllegalAccessException {
        System.setProperty("java.library.path", nativeDir.getAbsolutePath());
        try {
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (NoSuchFieldException ex) {
            //ignore
        }
    }

    private static void loadMacintosh(File nativeDir) throws IllegalAccessException {
        System.setProperty("java.library.path", System.getProperty("java.library.path") + ":" +
                new File(nativeDir, "jcef_app.app/Contents/Java").getAbsolutePath());
        try {
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (NoSuchFieldException ex) {
            //ignore
        }

        if (!new File(nativeDir, "jcef_app.app/Contents/Frameworks/Chromium Embedded Framework.framework").setExecutable(true)) {
            throw new IllegalStateException("Failed to set Chromium Embedded Framework as executable");
        }
        if (!new File(nativeDir, "jcef_app.app/Contents/Frameworks/jcef Helper.app/Contents/MacOS/jcef Helper").setExecutable(true)) {
            throw new IllegalStateException("Failed to set jcef Helper as executable");
        }
        if (!new File(nativeDir, "jcef_app.app/Contents/MacOS/JavaAppLauncher").setExecutable(true)) {
            throw new IllegalStateException("Failed to set JavaAppLauncher as executable");
        }
        if (!new File(nativeDir, "jcef_app.app/Contents/Java/gluegen-rt.jar").setExecutable(true)) {
            throw new IllegalStateException("Failed to set gluegen-rt as executable");
        }
        if (!new File(nativeDir, "jcef_app.app/Contents/Java/gluegen-rt-natives-macosx-universal.jar").setExecutable(true)) {
            throw new IllegalStateException("Failed to set gluegen-rt-natives-macosx-universal as executable");
        }
        if (!new File(nativeDir, "jcef_app.app/Contents/Java/jcef.jar").setExecutable(true)) {
            throw new IllegalStateException("Failed to set jcef as executable");
        }
        if (!new File(nativeDir, "jcef_app.app/Contents/Java/jcef-tests.jar").setExecutable(true)) {
            throw new IllegalStateException("Failed to set jcef-tests as executable");
        }
        if (!new File(nativeDir, "jcef_app.app/Contents/Java/jogl-all.jar").setExecutable(true)) {
            throw new IllegalStateException("Failed to set jogl-all as executable");
        }
        if (!new File(nativeDir, "jcef_app.app/Contents/Java/jogl-all-natives-macosx-universal.jar").setExecutable(true)) {
            throw new IllegalStateException("Failed to set jogl-all-natives-macosx-universal as executable");
        }
        if (!new File(nativeDir, "jcef_app.app/Contents/Java/libjcef.dylib").setExecutable(true)) {
            throw new IllegalStateException("Failed to set libjcef as executable");
        }
    }
}
