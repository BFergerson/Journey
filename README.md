# Journey

[![Build Status](https://travis-ci.com/CodeBrig/Journey.svg?branch=master)](https://travis-ci.com/CodeBrig/Journey)
[![Build status](https://ci.appveyor.com/api/projects/status/w6yr31klo9uwo1mb/branch/master?svg=true)](https://ci.appveyor.com/project/BFergerson/journey/branch/master)

Journey is a JCEF-powered cross-platform web browser created to address a need for an open-source JxBrowser alternative.
No attempt has been made to go feature-to-feature with JxBrowser. Journey namely aims to provide the same level of cross-platform capability. This is accomplished by providing a reliable means of building and distributing the CEF native files with Travis-CI/AppVeyor, JitPack, and GitHub.

Issues/PRs are welcome for increasing the capability of Journey to be on par with that of JxBrowser.

### Features
 - Linux (64bit) support
 - macOS (64bit) support
 - Windows (32bit/64bit) support
 - Release distributions (CDN via GitHub)
 - Online/offline CEF native file access

## Installation

### Gradle

```groovy
repositories {
     jcenter()
     maven { url "https://jitpack.io" }
}

dependencies {
      compile 'com.github.codebrig:journey:0.2.16-online'

      //or use the offline version (includes native CEF files for all platforms; ~300MB)
      //compile 'com.github.codebrig:journey:0.2.16-offline'
}
```

### Maven

```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
	<groupId>com.github.codebrig</groupId>
	<artifactId>journey</artifactId>
	<version>0.2.16-online</version>

	<!-- or use the offline version (includes native CEF files for all platforms; ~300MB) -->
	<!-- <version>0.2.16-offline</version> -->
</dependency>
```

## Usage

```java
import com.codebrig.journey.JourneyBrowserView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JourneyBrowser {

    public static void main(String[] args) throws Exception {
        JourneyBrowserView browser = new JourneyBrowserView("https://google.com");
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
```

![](journey-linux.png)

## Version Matrix

| Build Date | Journey Version  | JCEF Version (Linux) | JCEF Version (macOS) | JCEF Version (Windows) |
|------------|------------------|----------------------|----------------------|----------------------|
| 2019-06-10 | [0.2.16](https://github.com/CodeBrig/Journey/releases/tag/0.2.16-online) | [73.1.11.215](https://bitbucket.org/chromiumembedded/java-cef/commits/d348788e3347fa4d2a421773463f7dd62da60991) | [69.0.3497.100](https://bitbucket.org/chromiumembedded/java-cef/commits/235e3a844380b72761643324e1d9b7713cae3b63) | [73.1.11.215](https://bitbucket.org/chromiumembedded/java-cef/commits/d348788e3347fa4d2a421773463f7dd62da60991) |
| 2019-05-21 | 0.1.1 | [73.1.11.215](https://bitbucket.org/chromiumembedded/java-cef/commits/d348788e3347fa4d2a421773463f7dd62da60991) | n/a | [73.1.11.215](https://bitbucket.org/chromiumembedded/java-cef/commits/d348788e3347fa4d2a421773463f7dd62da60991) |
