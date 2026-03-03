package com.daviddev.automation.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for capturing screenshots using Selenium WebDriver.
 */
public class ScreenshotUtils {
    
    private static final String SCREENSHOT_DIR = "./screenshots/";
    private static final String SCREENSHOT_FORMAT = "PNG";
    
    /**
     * Captures a screenshot of the current browser state.
     *
     * @param driver the WebDriver instance
     * @param fileName the base name for the screenshot file (without extension)
     * @return the captured File object
     */
    public static File captureScreenshot(WebDriver driver, String fileName) {
        if (!(driver instanceof TakesScreenshot)) {
            throw new IllegalArgumentException("Driver does not support taking screenshots");
        }
        
        // Create screenshots directory if it doesn't exist
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
        
        // Take screenshot
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        
        // Generate file name with timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fullFileName = fileName + "_" + timestamp + ".png";
        File destinationFile = new File(screenshotDir, fullFileName);
        
        try {
            FileUtils.copyFile(screenshot, destinationFile);
            System.out.println("Screenshot saved to: " + destinationFile.getAbsolutePath());
            return destinationFile;
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Captures a screenshot without timestamp in filename.
     * Useful for predictable naming in tests.
     *
     * @param driver the WebDriver instance
     * @param fileName the exact file name (without extension)
     * @return the captured File object
     */
    public static File captureScreenshotSimple(WebDriver driver, String fileName) {
        if (!(driver instanceof TakesScreenshot)) {
            throw new IllegalArgumentException("Driver does not support taking screenshots");
        }
        
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
        }
        
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(screenshotDir, fileName + ".png");
        
        try {
            FileUtils.copyFile(screenshot, destinationFile);
            System.out.println("Screenshot saved to: " + destinationFile.getAbsolutePath());
            return destinationFile;
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Gets the screenshot directory path.
     *
     * @return the screenshot directory path
     */
    public static String getScreenshotDirectory() {
        return SCREENSHOT_DIR;
    }
}
