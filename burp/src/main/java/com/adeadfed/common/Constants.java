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
    // name of the HTTP PwnFox header
    public static String PWNFOX_HEADER = "X-Pwnfox-Color";
}
