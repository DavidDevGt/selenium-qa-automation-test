package com.daviddev.automation.base;

import com.daviddev.automation.config.WebDriverFactory;
import com.daviddev.automation.utils.VideoRecorderUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;

/**
 * Abstract base class for all tests.
 * Provides WebDriver management, video recording, and screenshot capabilities.
 */
public abstract class BaseTest {
    
    protected WebDriver driver;
    protected VideoRecorderUtils videoRecorder;
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    
    /**
     * Sets up the test environment before each test method.
     * Initializes WebDriver and starts video recording.
     *
     * @param result the test result object
     * @throws Exception if setup fails
     */
    @BeforeMethod
    public void setUp(ITestResult result) throws Exception {
        System.out.println("Setting up test: " + result.getName());
        
        // Initialize WebDriver
        driver = WebDriverFactory.createChromeDriver();
        driverThreadLocal.set(driver);
        
        // Initialize and start video recording
        videoRecorder = new VideoRecorderUtils();
        String testClassName = this.getClass().getSimpleName();
        String testMethodName = result.getName();
        
        try {
            videoRecorder.startRecording(testClassName, testMethodName);
        } catch (Exception e) {
            System.err.println("Warning: Could not start video recording: " + e.getMessage());
            // Continue without video if recording fails
        }
        
        System.out.println("Test setup completed for: " + result.getName());
    }
    
    /**
     * Tears down the test environment after each test method.
     * Stops video recording and quits WebDriver.
     *
     * @param result the test result object
     * @throws Exception if teardown fails
     */
    @AfterMethod
    public void tearDown(ITestResult result) throws Exception {
        System.out.println("Tearing down test: " + result.getName());
        
        // Stop video recording
        if (videoRecorder != null) {
            try {
                File videoFile = videoRecorder.stopRecording();
                if (videoFile != null) {
                    System.out.println("Video saved: " + videoFile.getAbsolutePath());
                }
            } catch (Exception e) {
                System.err.println("Warning: Could not stop video recording: " + e.getMessage());
            }
        }
        
        // Quit WebDriver
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("WebDriver quit successfully");
            } catch (Exception e) {
                System.err.println("Warning: Error quitting WebDriver: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
        
        System.out.println("Test teardown completed for: " + result.getName());
    }
    
    /**
     * Gets the current WebDriver instance.
     *
     * @return the WebDriver instance
     */
    protected WebDriver getDriver() {
        return driver;
    }
    
    /**
     * Gets the WebDriver from ThreadLocal (for parallel execution support).
     *
     * @return the WebDriver instance from ThreadLocal
     */
    public static WebDriver getDriverFromThreadLocal() {
        return driverThreadLocal.get();
    }
}
