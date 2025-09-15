package com.example.notesapp.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NoteUITest {

    @LocalServerPort
    private int port;

    private static WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    public static void setupClass() {
        // Setup Chrome driver automatically
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setup() {
        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run without opening browser window
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    @DisplayName("Test 1: Add a new note successfully")
    public void testAddNote() {
        // Navigate to the application
        driver.get("http://localhost:" + port);

        // Find elements
        WebElement titleInput = driver.findElement(By.id("noteTitle"));
        WebElement contentInput = driver.findElement(By.id("noteContent"));
        WebElement addButton = driver.findElement(By.id("addNoteBtn"));

        // Enter note details
        titleInput.sendKeys("Selenium Test Note");
        contentInput.sendKeys("This note was created by Selenium automated test");

        // Click add button
        addButton.click();

        // Wait for success message
        WebElement successMessage = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("success")
                )
        );

        // Verify success message
        assertTrue(successMessage.getText().contains("successfully"));

        // Verify note appears in the list
        WebElement noteTitle = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//h3[contains(text(), 'Selenium Test Note')]")
                )
        );
        assertNotNull(noteTitle);
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Try to add note with empty title")
    public void testAddNoteWithEmptyTitle() {
        // Navigate to the application
        driver.get("http://localhost:" + port);

        // Find elements
        WebElement titleInput = driver.findElement(By.id("noteTitle"));
        WebElement contentInput = driver.findElement(By.id("noteContent"));
        WebElement addButton = driver.findElement(By.id("addNoteBtn"));

        // Leave title empty
        titleInput.clear();
        contentInput.sendKeys("Content without title");

        // Click add button
        addButton.click();

        // Wait for error message
        WebElement errorMessage = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.className("error")
                )
        );

        // Verify error message
        assertTrue(errorMessage.getText().contains("Title cannot be empty"));
    }
}
