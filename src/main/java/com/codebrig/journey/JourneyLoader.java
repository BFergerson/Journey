package com.codebrig.journey;

import org.cef.CefApp;
import org.cef.OS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Downloads and loads the necessary CEF files for the current OS.
 *
 * @author <a href="mailto:brandon.fergerson@codebrig.com">Brandon Fergerson</a>
 * @version 0.1.1
 * @since 0.1.1
 */
public class JourneyLoader {

    private static JourneyLoaderListener JOURNEY_LOADER_LISTENER = new JourneyLoaderAdapter() {
    };
    private static final AtomicBoolean loaderSetup = new AtomicBoolean();

    public static void setup() throws Exception {
        if (loaderSetup.getAndSet(true)) {
            return;
        }
        JOURNEY_LOADER_LISTENER.journeyLoaderStarted(JourneyConstants.VERSION);
        File nativeDir = new File(System.getProperty("java.io.tmpdir"), "journey-" + JourneyConstants.VERSION);
        if (!nativeDir.exists()) nativeDir.mkdirs();
        JOURNEY_LOADER_LISTENER.usingNativeDirectory(nativeDir);

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
        } else {
            JOURNEY_LOADER_LISTENER.determinedOS("unsupported", -1);
            throw new UnsupportedOperationException("OS is not currently supported");
        }

        String jcefDistribFile = "jcef-distrib-" + providerName.replace("_", "") + ".zip";
        File localNative = new File(nativeDir, jcefDistribFile);
        if ("online".equals(JourneyConstants.MODE) && !localNative.exists()) {
            JOURNEY_LOADER_LISTENER.downloadingNativeCEFFiles();
            Files.copy(new URL(String.format("https://github.com/CodeBrig/Journey/releases/download/%s-online/%s",
                    JourneyConstants.VERSION, jcefDistribFile)).openStream(),
                    localNative.toPath(), StandardCopyOption.REPLACE_EXISTING);
            JOURNEY_LOADER_LISTENER.downloadedNativeCEFFiles();
        }

        if (!new File(nativeDir, "icudtl.dat").exists()) {
            JOURNEY_LOADER_LISTENER.extractingNativeCEFFiles();
            String libLocation = String.format("%s/bin/lib/%s/", jcefName, jcefName);
            if ("offline".equals(JourneyConstants.MODE)) {
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

                    File entryDestination = new File(nativeDir, entry.getName().replace(libLocation, ""));
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
            loadWindows(nativeDir);
        } else if (OS.isLinux()) {
            loadLinux(nativeDir);
        }
        JOURNEY_LOADER_LISTENER.loadedNativeCEFFiles();

        CefApp.startup();
        JOURNEY_LOADER_LISTENER.journeyLoaderComplete();
    }

    public static void setJourneyLoaderListener(JourneyLoaderListener listener) {
        JOURNEY_LOADER_LISTENER = Objects.requireNonNull(listener);
    }

    public static abstract class JourneyLoaderListener {
        public abstract void journeyLoaderStarted(String version);

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
        public void journeyLoaderStarted(String version) {
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

    private static void loadLinux(File nativeDir) throws Exception {
        String javaHome = System.getProperty("java.home");
        File amd64 = new File(javaHome + File.separator + "lib/amd64");

        String currentLibraryPath = System.getProperty("java.library.path");
        System.setProperty("java.library.path", currentLibraryPath + ":" + amd64.getAbsolutePath()
                + ":" + nativeDir.getAbsolutePath());
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

    private static void loadWindows(File nativeDir) throws Exception {
        System.setProperty("java.library.path", nativeDir.getAbsolutePath());
        try {
            Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
            fieldSysPath.setAccessible(true);
            fieldSysPath.set(null, null);
        } catch (NoSuchFieldException ex) {
            //ignore
        }
    }
}
