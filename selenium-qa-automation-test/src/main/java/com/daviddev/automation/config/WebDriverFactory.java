package com.daviddev.automation.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 * Factory class for creating WebDriver instances.
 * Uses WebDriverManager for automatic driver management.
 */
public class WebDriverFactory {
    
    private static final String PROPERTIES_FILE = "application.properties";
    private static Properties properties;
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = WebDriverFactory.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load " + PROPERTIES_FILE + ". Using defaults.");
        }
    }
    
    /**
     * Creates and configures a Chrome WebDriver instance with anti-detection measures.
     *
     * @return configured WebDriver instance
     */
    public static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        
        // Check if headless mode is enabled
        boolean headless = Boolean.parseBoolean(
            properties.getProperty("browser.headless", "false"));
        if (headless) {
            options.addArguments("--headless=new"); // Use new headless mode
        }
        
        // Maximize window if configured
        boolean maximize = Boolean.parseBoolean(
            properties.getProperty("browser.maximize", "true"));
        if (maximize) {
            options.addArguments("--start-maximized");
        }
        
        // Additional Chrome options for stability
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        
        // Basic configuration - running in normal mode (not headless)
        // Headless mode is easier to detect
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--start-maximized");
        
        // --- MEDIDAS ANTI-DETECCIÓN PARA GOOGLE ---
        
        // 1. Deshabilitar la automatización detectada por JS
        options.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        
        // 2. Permitir orígenes remotos (necesario para algunas configuraciones)
        options.addArguments("--remote-allow-origins=*");
        
        // 3. Deshabilitar parches de detección específicos
        options.addArguments("--disable-blink-features=AutomationControlled");
        
        // Intentar crear el driver con las opciones configuradas
        WebDriver driver;
        try {
            driver = new ChromeDriver(options);
        } catch (org.openqa.selenium.SessionNotCreatedException e) {
            System.err.println("⚠️  No se pudo crear sesión con configuración actual.");
            System.err.println("   Posible causa: Chrome ya está abierto con ese perfil.");
            System.err.println("   Intentando con configuración alternativa...");
            
            // Crear nuevas opciones sin el perfil de usuario
            ChromeOptions fallbackOptions = new ChromeOptions();
            if (headless) {
                fallbackOptions.addArguments("--headless=new");
            }
            if (maximize) {
                fallbackOptions.addArguments("--start-maximized");
            }
            fallbackOptions.addArguments("--no-sandbox");
            fallbackOptions.addArguments("--disable-dev-shm-usage");
            fallbackOptions.addArguments("--disable-gpu");
            fallbackOptions.addArguments("--disable-extensions");
            fallbackOptions.addArguments("--window-size=1920,1080");
            fallbackOptions.addArguments("--start-maximized");
            fallbackOptions.setExperimentalOption("excludeSwitches", Arrays.asList("enable-automation"));
            fallbackOptions.setExperimentalOption("useAutomationExtension", false);
            fallbackOptions.addArguments("--remote-allow-origins=*");
            fallbackOptions.addArguments("--disable-blink-features=AutomationControlled");
            
            driver = new ChromeDriver(fallbackOptions);
        }
        
        // Set page load timeout
        int pageLoadTimeout = Integer.parseInt(
            properties.getProperty("page.load.timeout", "30"));
        driver.manage().timeouts().pageLoadTimeout(
            java.time.Duration.ofSeconds(pageLoadTimeout));
        
        // Set implicit wait
        driver.manage().timeouts().implicitlyWait(
            java.time.Duration.ofSeconds(10));
        
        return driver;
    }
    
    /**
     * Gets a property value from the configuration.
     *
     * @param key the property key
     * @param defaultValue default value if key not found
     * @return the property value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Gets the Google URL from configuration.
     * Uses /ncr (No Country Redirect) to avoid regional redirects that may trigger CAPTCHA.
     *
     * @return Google URL with ncr suffix
     */
    public static String getGoogleUrl() {
        // Use /ncr to avoid country redirects which can trigger more CAPTCHAs
        return getProperty("google.url", "https://www.google.com/ncr");
    }
    
    /**
     * Gets the Selenium Docs URL from configuration.
     *
     * @return Selenium documentation URL
     */
    public static String getSeleniumDocsUrl() {
        return getProperty("selenium.docs.url", "https://www.selenium.dev/documentation/");
    }
}
