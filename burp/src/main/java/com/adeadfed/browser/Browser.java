package com.adeadfed.browser;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Arrays;

import com.adeadfed.common.ProfileColors;
import com.adeadfed.browser_extensions.BrowserExtensionsLoader;

public class Browser {
    private String exePath;
    private String profilesPath;
    private String themeColor;

    private final String BROWSER_DATA_PREFIX = "browser-data";

    // taken from args passed to the built-in BurpSuite Chromium browser
    private final String[] PWNCHROME_DEFAULT_ARGS = {
        "--ignore-certificate-errors",
        "--disable-ipc-flooding-protection",
        "--disable-xss-auditor",
        "--disable-bundled-ppapi-flash",
        "--disable-plugins-discovery",
        "--disable-default-apps",
        "--disable-prerender-local-predictor",
        "--disable-sync",
        "--disable-breakpad",
        "--disable-crash-reporter",
        "--disable-prerender-local-predictor",
        "--disk-cache-size=0",
        "--disable-settings-window",
        "--disable-notifications",
        "--disable-speech-api",
        "--disable-file-system",
        "--disable-presentation-api",
        "--disable-permissions-api",
        "--disable-new-zip-unpacker",
        "--disable-media-session-api",
        "--no-experiments",
        "--no-events",
        "--no-first-run",
        "--no-default-browser-check",
        "--no-pings",
        "--no-service-autorun",
        "--media-cache-size=0",
        "--use-fake-device-for-media-stream",
        "--dbus-stub",
        "--disable-background-networking",
        "--disable-features=ChromeWhatsNewUI,HttpsUpgrades,ImageServiceObserveSyncDownloadStatus"
    };

    public Browser(String exePath, String profilesPath, String themeColor) {
        this.exePath = exePath;
        this.profilesPath = profilesPath;
        this.themeColor = themeColor;
    }

    public Process start() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(getArgs());
        return processBuilder.start();
    }

    private String[] getArgs() throws Exception {
        return concatAll(
                new String[] { exePath },
                PWNCHROME_DEFAULT_ARGS,
                new String[] { getExtensionsArgs() },
                new String[] { getProfileArgs() }
        );
    }

    private String getExtensionsArgs() throws IOException {
        BrowserExtensionsLoader browserExtensions = new BrowserExtensionsLoader(
                profilesPath,
                ProfileColors.valueOf(themeColor.toUpperCase())
        );
        // build --load-extension argument passed to the Chromium to hot-load proxy,
        // theme and header extensions
        return "--load-extension=" + String.join(
                ",",
                browserExtensions.getHeaderExtensionDir(),
                browserExtensions.getProxyExtensionDir(),
                browserExtensions.getThemeDir()
        );
    }

    private String getProfileArgs() throws InvalidPathException {
        String pwnChromeBrowserDataDir = Paths.get(
                profilesPath,
                BROWSER_DATA_PREFIX,
                themeColor.toLowerCase()
        ).toString();
        return "--user-data-dir=" + pwnChromeBrowserDataDir;
    }

    // https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
    // sorry if this causes heap pollution ¯\_(ツ)_/¯
    // guess it's your usual BurpSuite experience then
    @SafeVarargs
    private static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }
}