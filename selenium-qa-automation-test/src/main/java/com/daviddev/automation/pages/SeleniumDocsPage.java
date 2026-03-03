package com.daviddev.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for Selenium documentation site.
 */
public class SeleniumDocsPage {
    
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;
    
    // Menu section enum
    public enum MenuSection {
        OVERVIEW("Overview", "overview"),
        WEBDRIVER("WebDriver", "webdriver"),
        SELENIUM_MANAGER("Selenium Manager", "selenium_manager"),
        GRID("Grid", "grid"),
        IE_DRIVER_SERVER("IE Driver Server", "ie_driver_server"),
        IDE("IDE", "ide"),
        TEST_PRACTICES("Test Practices", "test_practices"),
        LEGACY("Legacy", "legacy"),
        ABOUT("About", "about");
        
        private final String displayName;
        private final String fileName;
        
        MenuSection(String displayName, String fileName) {
            this.displayName = displayName;
            this.fileName = fileName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        public String getFileName() {
            return fileName;
        }
    }
    
    // Locators
    private final By menuContainer = By.cssSelector("nav.td-sidebar-nav");
    private final By pageContent = By.cssSelector(".td-content");
    private final By pageTitle = By.cssSelector("h1");
    
    /**
     * Constructor.
     *
     * @param driver the WebDriver instance
     */
    public SeleniumDocsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitForPageLoad();
    }
    
    /**
     * Navigates to a specific menu section with timeout protection.
     *
     * @param section the menu section to navigate to
     */
    public void navigateToMenuSection(MenuSection section) {
        System.out.println("Navigating to section: " + section.getDisplayName());
        
        WebElement link = null;
        
        try {
            // Strategy 1: Find by href (most reliable for Selenium docs)
            By sectionLink = By.xpath("//a[contains(@href,'/documentation/" + section.getFileName().toLowerCase() + "/')]");
            link = shortWait.until(ExpectedConditions.elementToBeClickable(sectionLink));
        } catch (TimeoutException e) {
            System.out.println("Could not find link by href for: " + section.getDisplayName() + ", trying text...");
            try {
                // Strategy 2: Find by text content (handles text inside <span> elements)
                // Use .//* to search in all descendants, not just direct text
                By sectionLink = By.xpath("//a[.//*[contains(text(),'" + section.getDisplayName() + "')] or contains(text(),'" + section.getDisplayName() + "')]");
                link = shortWait.until(ExpectedConditions.elementToBeClickable(sectionLink));
            } catch (TimeoutException e2) {
                // Strategy 3: Try finding by partial href match
                System.out.println("Trying partial href match for: " + section.getDisplayName());
                try {
                    By sectionLink = By.cssSelector("a[href*='" + section.getFileName().toLowerCase() + "']");
                    link = shortWait.until(ExpectedConditions.elementToBeClickable(sectionLink));
                } catch (TimeoutException e3) {
                    throw new RuntimeException("Could not find menu section: " + section.getDisplayName());
                }
            }
        }
        
        if (link != null) {
            // Scroll to element and click
            try {
                org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", link);
                Thread.sleep(500); // Small delay after scroll
                link.click();
            } catch (Exception e) {
                // Fallback to JS click
                org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", link);
            }
        }
        
        // Wait for page content to load with timeout
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(pageContent));
            // Small delay to ensure page is fully rendered
            Thread.sleep(1000);
            System.out.println("Loaded section: " + section.getDisplayName() + " - Title: " + getPageTitle());
        } catch (TimeoutException e) {
            System.err.println("Timeout waiting for page content after navigating to: " + section.getDisplayName());
            throw e;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Gets the current page title.
     *
     * @return the page title
     */
    public String getPageTitle() {
        try {
            WebElement title = driver.findElement(pageTitle);
            return title.getText();
        } catch (Exception e) {
            return driver.getTitle();
        }
    }
    
    /**
     * Checks if a menu section is visible.
     *
     * @param section the menu section to check
     * @return true if visible, false otherwise
     */
    public boolean isMenuSectionVisible(MenuSection section) {
        try {
            // Try href first (most reliable)
            By sectionLink = By.xpath("//a[contains(@href,'/documentation/" + section.getFileName().toLowerCase() + "/')]");
            WebElement link = driver.findElement(sectionLink);
            return link.isDisplayed();
        } catch (Exception e) {
            // Fallback to text search in descendants
            try {
                By sectionLink = By.xpath("//a[.//*[contains(text(),'" + section.getDisplayName() + "')] or contains(text(),'" + section.getDisplayName() + "')]");
                WebElement link = driver.findElement(sectionLink);
                return link.isDisplayed();
            } catch (Exception e2) {
                return false;
            }
        }
    }
    
    /**
     * Gets all available menu items.
     *
     * @return list of menu item texts
     */
    public List<String> getAvailableMenuItems() {
        List<WebElement> menuItems = driver.findElements(
            By.cssSelector("nav.td-sidebar-nav a"));
        return menuItems.stream()
                .map(WebElement::getText)
                .filter(text -> !text.isEmpty())
                .toList();
    }
    
    /**
     * Navigates through all menu sections sequentially.
     */
    public void navigateAllSections() {
        System.out.println("Starting navigation through all sections...");
        
        for (MenuSection section : MenuSection.values()) {
            try {
                if (isMenuSectionVisible(section)) {
                    navigateToMenuSection(section);
                } else {
                    System.out.println("Section not visible, skipping: " + section.getDisplayName());
                }
            } catch (Exception e) {
                System.err.println("Error navigating to section " + section.getDisplayName() + 
                    ": " + e.getMessage());
            }
        }
        
        System.out.println("Completed navigation through all sections.");
    }
    
    /**
     * Waits for the page to fully load.
     */
    private void waitForPageLoad() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(menuContainer));
            wait.until(ExpectedConditions.presenceOfElementLocated(pageContent));
        } catch (TimeoutException e) {
            System.err.println("Timeout waiting for initial page load. Continuing anyway...");
        }
    }
    
    /**
     * Gets the current URL.
     *
     * @return the current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
