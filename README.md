# Selenium QA Automation Test

Proyecto de automatización de pruebas funcionales utilizando Selenium WebDriver con WebDriverManager para navegación automatizada de documentación Selenium.

## 📋 Descripción

Este proyecto implementa un framework de automatización de pruebas en Java que:
- Realiza búsquedas en Google para acceder a sitios web
- Navega por la documentación oficial de Selenium
- Captura screenshots en cada paso importante
- Graba video de la sesión completa de prueba
- Utiliza WebDriverManager para gestión automática de drivers

## 🚀 Tecnologías Utilizadas

| Tecnología | Versión | Descripción |
|------------|---------|-------------|
| Java | 21 LTS | Lenguaje de programación |
| Selenium WebDriver | 4.27.0 | Automatización de navegadores |
| WebDriverManager | 5.9.2 | Gestión automática de drivers |
| TestNG | 7.10.2 | Framework de testing |
| Monte Screen Recorder | 0.7.7.0 | Grabación de video |

## 📁 Estructura del Proyecto

```
selenium-qa-automation-test/
├── src/
│   ├── main/java/com/daviddev/automation/
│   │   ├── base/
│   │   │   └── BaseTest.java              # Clase base para tests
│   │   ├── config/
│   │   │   └── WebDriverFactory.java      # Factory de WebDriver
│   │   ├── listeners/
│   │   │   └── TestListener.java          # Listeners de TestNG
│   │   ├── pages/
│   │   │   ├── GoogleSearchPage.java      # Page Object: Búsqueda Google
│   │   │   ├── GoogleResultsPage.java     # Page Object: Resultados Google
│   │   │   └── SeleniumDocsPage.java      # Page Object: Documentación Selenium
│   │   └── utils/
│   │       ├── ScreenshotUtils.java       # Utilidad para screenshots
│   │       └── VideoRecorderUtils.java    # Utilidad para grabación
│   └── test/java/com/daviddev/automation/
│       └── tests/
│           └── SeleniumDocsNavigationTest.java  # Test principal
├── screenshots/                           # Capturas de pantalla
├── recordings/                            # Videos de sesiones
├── pom.xml                                # Configuración Maven
└── build.gradle                           # Configuración Gradle
```

## 🎯 Funcionalidades

### SeleniumDocsNavigationTest

Test automatizado que:
1. **Abre Google** y busca "selenium.dev documentation"
2. **Accede** al sitio oficial de Selenium
3. **Navega** por las siguientes secciones:
   - Overview
   - WebDriver
   - Selenium Manager
   - Grid
   - IE Driver Server
   - IDE
   - Test Practices
   - Legacy
   - About
4. **Captura screenshot** en cada sección
5. **Graba video** de toda la sesión

## 📸 Evidencias

El proyecto incluye evidencias de ejecución:

### Screenshots (11 capturas)
Ubicadas en `selenium-qa-automation-test/screenshots/`:
- Navegación por Google
- Página principal de Selenium
- Todas las secciones de documentación

### Video de Sesión
Ubicado en `selenium-qa-automation-test/recordings/`:
- Grabación completa de la ejecución del test

## 🔧 Requisitos Previos

- Java 21 o superior
- Maven 3.8+ o Gradle 8+
- Chrome Browser instalado

## ▶️ Ejecución

### Con Maven
```bash
cd selenium-qa-automation-test
mvn clean test
```

### Con Gradle
```bash
cd selenium-qa-automation-test
./gradlew clean test
```

## 📊 Reportes

- **Screenshots**: Se guardan automáticamente en `screenshots/`
- **Videos**: Se guardan automáticamente en `recordings/`
- **Reporte TestNG**: `target/surefire-reports/` (Maven) o `build/reports/` (Gradle)

## 🔐 Configuración

El archivo `application.properties` permite configurar:
- Navegador a utilizar (chrome, firefox, edge)
- Modo headless
- Resolución de pantalla
- Timeouts

## 👨‍💻 Autor

**DavidDevGt**  
[josuedavidvl18@gmail.com](mailto:josuedavidvl18@gmail.com)

## 📄 Licencia

Este proyecto es para fines educativos y de demostración.

---

<div align="center">
  <sub>Built with ❤️ using Selenium WebDriver & WebDriverManager</sub>
</div>
