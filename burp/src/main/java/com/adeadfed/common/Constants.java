package com.adeadfed.common;

public class Constants {
    // names of extension settings to be stored in the persistent Montoya's Persistence API
    public static String PERSISTENT_CHROMIUM_PATH = "PwnChromeExePath";
    public static String PERSISTENT_PROFILES_DIR = "PwnChromeProfilesPath";
    // hard-wired names of required directories in the PwnChrome extensions directory
    // need this to validate that the user gave us a valid path to the directory with
    // all required Chrome extensions they downloaded from GitHub
    public static String BROWSER_EXTENSIONS_PREFIX = "browser-extensions";
    public static String BROWSER_DATA_PREFIX = "browser-data";
    public static String THEME_DIR = "themes";
    public static String HEADER_EXTENSION_DIR = "header-extension";
    public static String PROXY_EXTENSION_DIR = "proxy-extension";
    // taken from args passed to the built-in BurpSuite Chromium browser
    public static String[] PWNCHROME_DEFAULT_ARGS = {
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
    // name of the HTTP PwnFox header
    public static String PWNFOX_HEADER = "X-Pwnfox-Color";
}
