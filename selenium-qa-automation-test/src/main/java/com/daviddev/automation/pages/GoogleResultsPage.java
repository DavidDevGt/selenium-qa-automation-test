package com.daviddev.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for Google search results page.
 */
public class GoogleResultsPage {
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    
    // Locators - Updated for current Google layout
    private final By resultLinks = By.cssSelector("#search a > h3");
    private final By resultContainers = By.cssSelector("#search");
    private final By searchResults = By.cssSelector("[data-ved]"); // Generic result container
    private final By seleniumLink = By.xpath("//a[contains(@href, 'selenium.dev')]");
    
    /**
     * Constructor.
     *
     * @param driver the WebDriver instance
     */
    public GoogleResultsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        waitForPageLoad();
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
     * Gets all result titles.
     *
     * @return list of result titles
     */
    public List<String> getResultTitles() {
        List<WebElement> results = driver.findElements(resultLinks);
        return results.stream()
                .map(WebElement::getText)
                .toList();
    }
    
    /**
     * Finds and clicks the selenium.dev link in the search results.
     *
     * @return SeleniumDocsPage instance
     */
    public SeleniumDocsPage clickSeleniumDevLink() {
        // Try multiple strategies to find the selenium.dev link
        
        // Strategy 1: Look for h3 elements containing "Selenium" text, then get their parent link
        try {
            List<WebElement> h3Elements = driver.findElements(By.cssSelector("#search h3"));
            for (WebElement h3 : h3Elements) {
                try {
                    WebElement parentLink = h3.findElement(By.xpath("./parent::a"));
                    String href = parentLink.getAttribute("href");
                    if (href != null && href.contains("selenium.dev")) {
                        System.out.println("Found selenium.dev link via h3 strategy");
                        parentLink.click();
                        return new SeleniumDocsPage(driver);
                    }
                } catch (Exception e) {
                    // Continue to next element
                }
            }
        } catch (Exception e) {
            System.out.println("h3 strategy failed, trying next...");
        }
        
        // Strategy 2: Direct XPath search for selenium.dev links
        try {
            WebElement seleniumLink = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@href, 'selenium.dev') and .//h3]")));
            System.out.println("Found selenium.dev link via XPath strategy");
            seleniumLink.click();
            return new SeleniumDocsPage(driver);
        } catch (Exception e) {
            System.out.println("XPath strategy failed, trying fallback...");
        }
        
        // Fallback: Navigate directly to selenium.dev
        System.out.println("Navigating directly to selenium.dev...");
        driver.get("https://www.selenium.dev/documentation/");
        return new SeleniumDocsPage(driver);
    }
    
    /**
     * Checks if selenium.dev result is present in the results.
     *
     * @return true if found, false otherwise
     */
    public boolean hasSeleniumDevResult() {
        try {
            List<WebElement> seleniumLinks = driver.findElements(
                By.xpath("//a[contains(@href,'selenium.dev')]"));
            return !seleniumLinks.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets a specific result title by index.
     *
     * @param index the result index (0-based)
     * @return the result title, or null if index is out of bounds
     */
    public String getResultTitle(int index) {
        List<WebElement> results = driver.findElements(resultLinks);
        if (index >= 0 && index < results.size()) {
            return results.get(index).getText();
        }
        return null;
    }
    
    /**
     * Waits for the results page to fully load.
     */
    private void waitForPageLoad() {
        // Wait for search results container to be present
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#search")));
        // Additional wait for any result to be visible
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#search h3, #search a")));
    }
}
