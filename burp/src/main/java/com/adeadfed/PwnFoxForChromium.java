package com.adeadfed;

import static com.adeadfed.common.Constants.*;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import com.adeadfed.browser_extensions.BrowserExtensionsLoader;
import com.adeadfed.common.ProfileColors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.nio.file.FileSystem;
import java.nio.file.PathMatcher;
import java.nio.file.FileSystems;

public class PwnFoxForChromium implements BurpExtension {
    public MontoyaApi montoyaApi;


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

    private String getPwnChromiumLoadExtensionArgs(String pwnChromeProfileDir, String themeColor) throws IOException {
        BrowserExtensionsLoader browserExtensions = new BrowserExtensionsLoader(
                pwnChromeProfileDir,
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

    private String getPwnChromiumProfileArgs(String pwnChromeProfileDir, String themeColor) {
        String pwnChromeBrowserDataDir = Paths.get(
                pwnChromeProfileDir,
                BROWSER_DATA_PREFIX,
                themeColor.toLowerCase()
        ).toString();
        return "--user-data-dir=" + pwnChromeBrowserDataDir;
    }

    private void populateDefaultSettings() {
        if (!settingExists(PERSISTENT_CHROMIUM_PATH)) {
            populateChromiumPath();
        }
        if (!settingExists(PERSISTENT_PROFILES_DIR)) {
            populateProfilesDir();
        }
    }

    private boolean settingExists(String settingName) {
        return montoyaApi.persistence().preferences().getString(settingName) != null;
    }

    private void populateChromiumPath() {
        String chromiumPath = tryGetBurpbrowserPath();
        if (chromiumPath != null) {
            montoyaApi.persistence().preferences().setString(PERSISTENT_CHROMIUM_PATH, chromiumPath);
        }
    }

    private void populateProfilesDir() {
        String profileDirPath = tryCreateProfilesDir(); 
        if (profileDirPath != null) {
            montoyaApi.persistence().preferences().setString(PERSISTENT_PROFILES_DIR, profileDirPath);
        }
    }

    private String tryGetBurpbrowserPath() {
        String chromiumGlob = "regex:[Cc]hrom(e|ium)(\\.exe)?";

        Path userDirPath = Paths.get(
                System.getProperty("user.dir"));

        FileSystem fs = FileSystems.getDefault();
        PathMatcher matcher = fs.getPathMatcher(chromiumGlob);

        try {
            String chromiumPath = Files.walk(userDirPath, 10)
                    .filter(Files::isRegularFile)
                    .filter(Files::isExecutable)
                    .filter(path -> matcher.matches(path.getFileName()))
                    .findFirst()
                    .get()
                    .toAbsolutePath()
                    .toString();

            montoyaApi.logging().logToOutput("Chromium located: " + chromiumPath);
            return chromiumPath;

        } catch (Exception e) {
            montoyaApi.logging().logToError("Failed to locate Chromium executable automatically");
            montoyaApi.logging().logToError(e);
        }
        return null;
    }

    private String tryCreateProfilesDir() {
        Path profileDirPath = Paths.get(
                System.getProperty("user.home"),
                ".PwnChromiumData");
        try {
            if (!Files.exists(profileDirPath)) {
                Files.createDirectory(profileDirPath);
                montoyaApi.logging().logToOutput("Created ~/.PwnChromiumData directory successfully...");
                return profileDirPath.toString();
            }
        } catch (IOException e) {
            montoyaApi.logging().logToError("Failed to create ~/.PwnChromiumData directory");
            montoyaApi.logging().logToError(e);
        }
        return null;
    }

    public boolean startDetachedPwnChromium(String pwnChromeExePath, String pwnChromeProfileDir, String themeColor) {
        // start Chromium with needed args and a correct profile
        try {
            String[] pwnChromiumArgs = concatAll(
                    // I just want my list expansion from Python >:(
                    new String[] { pwnChromeExePath },
                    PWNCHROME_DEFAULT_ARGS,
                    new String[] { getPwnChromiumLoadExtensionArgs(pwnChromeProfileDir, themeColor) },
                    new String[] { getPwnChromiumProfileArgs(pwnChromeProfileDir, themeColor)
            });
            ProcessBuilder processBuilder = new ProcessBuilder(pwnChromiumArgs);
            processBuilder.start();
            return true;
        } catch (Exception e) {
            montoyaApi.logging().logToError(e);
            return false;
        }
    }

    private void setMontoyaApi(MontoyaApi api) {
        this.montoyaApi = api;
    }

    @Override
    public void initialize(MontoyaApi api) {
        setMontoyaApi(api);

        montoyaApi.extension().setName("PwnFox For Chromium");
        populateDefaultSettings();
        montoyaApi.logging().logToOutput("PwnFox For Chromium - Options loaded...");
        montoyaApi.userInterface().registerSuiteTab("PwnFox For Chromium", new PwnFoxForChromiumUI(this).getUI());
        montoyaApi.logging().logToOutput("PwnFox For Chromium - Suite Tab loaded...");
        montoyaApi.proxy().registerRequestHandler(new PwnFoxForChromiumRequestHandler());
        montoyaApi.logging().logToOutput("PwnFox For Chromium - Request Interceptor loaded...");
    }
}