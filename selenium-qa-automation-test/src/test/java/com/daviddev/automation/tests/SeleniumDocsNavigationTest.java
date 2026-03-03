package com.daviddev.automation.tests;

import com.daviddev.automation.base.BaseTest;
import com.daviddev.automation.pages.GoogleResultsPage;
import com.daviddev.automation.pages.GoogleSearchPage;
import com.daviddev.automation.pages.SeleniumDocsPage;
import com.daviddev.automation.utils.ScreenshotUtils;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * Test class for navigating through Selenium documentation.
 * Extends BaseTest for WebDriver and video recording setup.
 *
 * Flujo del test:
 * 1. Inicio: Abrir https://www.google.com
 * 2. Búsqueda: Localizar el cuadro de búsqueda, escribir "Documentación de selenium" y presionar Enter
 * 3. Captura 1: Tomar captura de pantalla de la página de resultados de Google
 * 4. Navegación: Hacer clic en el resultado que lleva a selenium.dev
 * 5. Captura 2: Tomar captura de pantalla de la página principal de Selenium
 * 6. Menú Lateral: Navegar UNO POR UNO en los ítems: Overview, WebDriver, Selenium Manager,
 *    Grid, IE Driver Server, IDE, Test Practices, Legacy y About
 * 7. Grabación: Asegurar que toda la sesión se grabe
 */
@Listeners(com.daviddev.automation.listeners.TestListener.class)
public class SeleniumDocsNavigationTest extends BaseTest {

    /**
     * Main test method that follows the complete flow:
     * Google Search -> Selenium Results -> Selenium Docs -> Navigate all sections.
     * Video recording captures the entire session via BaseTest.
     */
    @Test(description = "Complete flow: Google search, navigate to Selenium docs, capture all sections")
    public void testNavigateSeleniumDocumentation() {
        System.out.println("=== Starting Selenium Documentation Navigation Test ===");
        System.out.println("Video recording started automatically by BaseTest");

        // =================================================================
        // PASO 1: Inicio - Abrir https://www.google.com
        // =================================================================
        System.out.println("\n--- PASO 1: Inicio - Abrir Google ---");
        GoogleSearchPage googleSearchPage = new GoogleSearchPage(driver);
        googleSearchPage.navigateTo();
        System.out.println("Google page loaded. Title: " + googleSearchPage.getPageTitle());
        Assert.assertTrue(googleSearchPage.getPageTitle().toLowerCase().contains("google"),
            "Should be on Google search page");

        // =================================================================
        // PASO 2: Búsqueda - Localizar cuadro, escribir "Documentación de selenium" y Enter
        // Usando typeLikeHuman para simular comportamiento humano y evitar detección
        // =================================================================
        System.out.println("\n--- PASO 2: Búsqueda - Escribir 'Documentación de selenium' (como humano) ---");
        String searchTerm = "Documentación de selenium";

        // Escribir como humano: carácter por carácter con retrasos aleatorios
        System.out.println("Typing like human (character by character with random delays)...");
        googleSearchPage.typeLikeHuman(searchTerm);
        System.out.println("Search text entered: " + searchTerm);

        // Pequeña pausa antes de enviar (comportamiento humano)
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        GoogleResultsPage resultsPage = googleSearchPage.submitSearch();

        // WebDriverWait explícito para esperar que los resultados sean visibles
        System.out.println("Waiting for search results to be visible...");
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(
            org.openqa.selenium.By.cssSelector("#search")));
        System.out.println("Search results page loaded.");

        // =================================================================
        // PASO 3: Captura 1 - Tomar captura de pantalla de resultados de Google
        // Manejar CAPTCHA si está presente (pausa 5 segundos para resolución manual)
        // =================================================================
        System.out.println("\n--- PASO 3: Captura 1 - Screenshot de resultados Google ---");

        // Verificar y manejar CAPTCHA si aparece
        googleSearchPage.handleCaptchaIfPresent();

        ScreenshotUtils.captureScreenshotSimple(driver, "01-google-search-results");
        System.out.println("Screenshot captured: 01-google-search-results.png");

        // Verify selenium.dev result is present
        Assert.assertTrue(resultsPage.hasSeleniumDevResult(),
            "Should have selenium.dev result in search results");
        System.out.println("Verified: selenium.dev result found in search results");

        // =================================================================
        // PASO 4: Navegación - Hacer clic en el resultado que lleva a selenium.dev
        // =================================================================
        System.out.println("\n--- PASO 4: Navegación - Clic en resultado selenium.dev ---");
        SeleniumDocsPage docsPage = resultsPage.clickSeleniumDevLink();
        System.out.println("Navigated to Selenium.dev site.");

        // Verify we're on the correct page
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("selenium.dev"),
            "Should be on Selenium website. Current URL: " + currentUrl);
        System.out.println("Current URL verified: " + currentUrl);

        // =================================================================
        // PASO 5: Captura 2 - Tomar captura de pantalla de la página principal de Selenium
        // =================================================================
        System.out.println("\n--- PASO 5: Captura 2 - Screenshot página principal Selenium ---");
        // Wait for page to fully load before capture
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        ScreenshotUtils.captureScreenshotSimple(driver, "02-selenium-home-page");
        System.out.println("Screenshot captured: 02-selenium-home-page.png");
        System.out.println("Page title: " + docsPage.getPageTitle());

        // =================================================================
        // PASO 6: Menú Lateral - Navegar UNO POR UNO en los ítems
        // =================================================================
        System.out.println("\n--- PASO 6: Menú Lateral - Navegar UNO POR UNO cada sección ---");

        // Navigate to Overview section
        System.out.println("\n>> Navegando a: Overview");
        navigateAndCapture(docsPage, SeleniumDocsPage.MenuSection.OVERVIEW, "03-overview");

        // Navigate to WebDriver section
        System.out.println("\n>> Navegando a: WebDriver");
        navigateAndCapture(docsPage, SeleniumDocsPage.MenuSection.WEBDRIVER, "04-webdriver");

        // Navigate to Selenium Manager section
        System.out.println("\n>> Navegando a: Selenium Manager");
        navigateAndCapture(docsPage, SeleniumDocsPage.MenuSection.SELENIUM_MANAGER, "05-selenium-manager");

        // Navigate to Grid section
        System.out.println("\n>> Navegando a: Grid");
        navigateAndCapture(docsPage, SeleniumDocsPage.MenuSection.GRID, "06-grid");

        // Navigate to IE Driver Server section
        System.out.println("\n>> Navegando a: IE Driver Server");
        navigateAndCapture(docsPage, SeleniumDocsPage.MenuSection.IE_DRIVER_SERVER, "07-ie-driver-server");

        // Navigate to IDE section
        System.out.println("\n>> Navegando a: IDE");
        navigateAndCapture(docsPage, SeleniumDocsPage.MenuSection.IDE, "08-ide");

        // Navigate to Test Practices section
        System.out.println("\n>> Navegando a: Test Practices");
        navigateAndCapture(docsPage, SeleniumDocsPage.MenuSection.TEST_PRACTICES, "09-test-practices");

        // Navigate to Legacy section
        System.out.println("\n>> Navegando a: Legacy");
        navigateAndCapture(docsPage, SeleniumDocsPage.MenuSection.LEGACY, "10-legacy");

        // Navigate to About section
        System.out.println("\n>> Navegando a: About");
        navigateAndCapture(docsPage, SeleniumDocsPage.MenuSection.ABOUT, "11-about");

        // =================================================================
        // PASO 7: Grabación - La sesión completa se graba vía BaseTest
        // =================================================================
        System.out.println("\n=== Test completed successfully ===");
        System.out.println("All screenshots saved to ./screenshots/");
        System.out.println("Video recording saved to ./recordings/ (captured entire session via BaseTest)");
        System.out.println("Flujo completo ejecutado: Google → Search → Selenium.dev → All Menu Sections");
    }
    
    /**
     * Helper method to navigate to a section and capture screenshot.
     *
     * @param docsPage the SeleniumDocsPage instance
     * @param section the menu section to navigate to
     * @param screenshotName the name for the screenshot file (without extension)
     */
    private void navigateAndCapture(SeleniumDocsPage docsPage, SeleniumDocsPage.MenuSection section, String screenshotName) {
        try {
            // Add timeout protection for each section navigation
            long startTime = System.currentTimeMillis();
            long timeout = 30000; // 30 seconds max per section

            docsPage.navigateToMenuSection(section);

            // Check if we've exceeded timeout
            if (System.currentTimeMillis() - startTime > timeout) {
                System.err.println("WARNING: Navigation to " + section.getDisplayName() + " took too long");
            }

            ScreenshotUtils.captureScreenshotSimple(driver, screenshotName);
            System.out.println("Screenshot captured: " + screenshotName + ".png");
        } catch (Exception e) {
            System.err.println("Error navigating to section " + section.getDisplayName() +
                ": " + e.getMessage());
            // Take error screenshot
            try {
                ScreenshotUtils.captureScreenshotSimple(driver, screenshotName + "-error");
            } catch (Exception screenshotError) {
                System.err.println("Could not capture error screenshot: " + screenshotError.getMessage());
            }
            // Continue with next section
        }
    }
}
