package com.daviddev.automation.pages;

import com.daviddev.automation.config.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for Google search homepage.
 */
public class GoogleSearchPage {
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    // Locators
    private final By searchInput = By.name("q");
    private final By searchButton = By.name("btnK");
    private final By googleLogo = By.id("logo");
    
    /**
     * Constructor.
     *
     * @param driver the WebDriver instance
     */
    public GoogleSearchPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }
    
    /**
     * Navigates to Google search page.
     */
    public void navigateTo() {
        driver.get(WebDriverFactory.getGoogleUrl());
        waitForPageLoad();
    }
    
    /**
     * Enters search text in the search box.
     *
     * @param searchTerm the text to search for
     */
    public void enterSearchText(String searchTerm) {
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        searchBox.clear();
        searchBox.sendKeys(searchTerm);
    }

    /**
     * Types text like a human with random delays between keystrokes.
     * This helps avoid bot detection by simulating realistic typing behavior.
     *
     * @param searchTerm the text to type
     */
    public void typeLikeHuman(String searchTerm) {
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        searchBox.clear();

        java.util.Random random = new java.util.Random();
        for (char c : searchTerm.toCharArray()) {
            searchBox.sendKeys(String.valueOf(c));
            // Random delay between 100ms and 300ms
            int delay = 100 + random.nextInt(200);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    /**
     * Submits the search by pressing Enter.
     *
     * @return GoogleResultsPage instance
     */
    public GoogleResultsPage submitSearch() {
        WebElement searchBox = wait.until(ExpectedConditions.elementToBeClickable(searchInput));
        searchBox.submit();
        return new GoogleResultsPage(driver);
    }
    
    /**
     * Performs a search and returns the results page.
     *
     * @param searchTerm the text to search for
     * @return GoogleResultsPage instance
     */
    public GoogleResultsPage searchFor(String searchTerm) {
        enterSearchText(searchTerm);
        return submitSearch();
    }
    
    /**
     * Waits for the page to fully load.
     */
    private void waitForPageLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(googleLogo));
        wait.until(ExpectedConditions.elementToBeClickable(searchInput));
    }
    
    /**
     * Gets the page title.
     *
     * @return the page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Checks if a CAPTCHA is present on the page.
     *
     * @return true if CAPTCHA detected, false otherwise
     */
    public boolean isCaptchaPresent() {
        try {
            // Common CAPTCHA indicators
            return driver.getPageSource().toLowerCase().contains("captcha") ||
                   driver.getPageSource().toLowerCase().contains("recaptcha") ||
                   driver.getPageSource().toLowerCase().contains("i'm not a robot") ||
                   driver.findElements(By.id("captcha")).size() > 0 ||
                   driver.findElements(By.className("g-recaptcha")).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Handles CAPTCHA detection by pausing for manual resolution.
     * Waits 5 seconds to allow manual CAPTCHA resolution if detected.
     */
    public void handleCaptchaIfPresent() {
        if (isCaptchaPresent()) {
            System.out.println("⚠️  CAPTCHA DETECTADO! Pausando 5 segundos para resolución manual...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Continuando después de pausa de CAPTCHA...");
        }
    }
}
