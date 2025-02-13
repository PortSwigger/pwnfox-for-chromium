package com.adeadfed.validators;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.adeadfed.common.Constants.*;

public class FsValidator {
    public static boolean isDirValid(String directory) {
        return Files.isDirectory(Paths.get(directory));
    }

    public static boolean isChromiumExecutableValid(String pwnChromeExePath) {
        // check if executable is named Chrome or Chromium
        String chromeExeName = Paths.get(pwnChromeExePath).getFileName().toString().toLowerCase();
        if (!(chromeExeName.contains("chrom"))) {
            return false;
        }
        // check if the --version flag of the chromium executable returns Chrome or Chromium in the output
        // a more robust check if this is actually a Chromium exe
        if (System.getProperty("os.name").startsWith("Windows")) {
            // skip this check on Windows cause apparently Chrome.exe does not have --version flag
            // why?
            // cause fuck you, that's why
            return true;
        }
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(pwnChromeExePath, "--version");
            Process process = processBuilder.start();
            String output = new String(process.getInputStream().readAllBytes());
            // check for a substring of "Chrom" in either "Chrome" or "Chromium"
            // if that's the case, then we should be good
            return output.contains("Chrom");
        } catch (IOException e) {
            return false;
        }
    }
}
