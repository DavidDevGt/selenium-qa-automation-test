package com.daviddev.automation.listeners;

import com.daviddev.automation.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestNG listener for logging test execution and capturing screenshots on failure.
 */
public class TestListener implements ITestListener {
    
    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("[TEST START] " + getTestName(result));
    }
    
    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("[TEST SUCCESS] " + getTestName(result));
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("[TEST FAILURE] " + getTestName(result));
        System.out.println("Error: " + result.getThrowable().getMessage());
        
        // Capture screenshot on failure if WebDriver is available
        Object testInstance = result.getInstance();
        if (testInstance != null) {
            try {
                java.lang.reflect.Field driverField = testInstance.getClass().getSuperclass().getDeclaredField("driver");
                driverField.setAccessible(true);
                WebDriver driver = (WebDriver) driverField.get(testInstance);
                
                if (driver != null) {
                    String fileName = result.getTestClass().getRealClass().getSimpleName() + "_" 
                            + result.getName() + "_FAILED";
                    ScreenshotUtils.captureScreenshotSimple(driver, fileName);
                    System.out.println("Screenshot captured for failed test: " + fileName);
                }
            } catch (Exception e) {
                System.err.println("Could not capture screenshot on failure: " + e.getMessage());
            }
        }
    }
    
    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("[TEST SKIPPED] " + getTestName(result));
    }
    
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("[TEST PARTIAL SUCCESS] " + getTestName(result));
    }
    
    @Override
    public void onStart(ITestContext context) {
        System.out.println("[SUITE START] " + context.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
        System.out.println("[SUITE FINISH] " + context.getName());
        System.out.println("Passed: " + context.getPassedTests().size());
        System.out.println("Failed: " + context.getFailedTests().size());
        System.out.println("Skipped: " + context.getSkippedTests().size());
    }
    
    private String getTestName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName() + "." + result.getName();
    }
}
