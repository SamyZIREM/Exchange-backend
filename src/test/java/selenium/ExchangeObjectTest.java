package selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ExchangeObjectTest {

    public static void main(String[] args) {
        // Définir le chemin vers chromedriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrateur\\Desktop\\chromedriver-win64\\chromedriver.exe");

        // Options de Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        // Créer une instance de ChromeDriver
        WebDriver driver = new ChromeDriver(options);

        try {
            // 1. Se connecter avec l'utilisateur Alice
            driver.get("http://localhost:4200/authentification");
            driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys("alice@example.com");
            driver.findElement(By.xpath("//input[@placeholder='Mot de passe']")).sendKeys("password123");
            driver.findElement(By.xpath("//button[text()='Se connecter']")).click();

            // 2. Attendre que la redirection vers la page des objets soit effectuée
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); // Augmenter le délai
            wait.until(ExpectedConditions.urlContains("/objets"));
            System.out.println("✅ Connexion réussie, redirection vers la page des objets !");

            // 3. Ouvrir la pop-up d'échange
            WebElement echangeButton = driver.findElement(By.xpath("//button[text()='Échanger']"));
            echangeButton.click();

            // 4. Attendre que la pop-up d'échange soit ouverte
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("popup-overlay")));
            System.out.println("✅ Pop-up d'échange ouverte.");

            // 5. Rechercher l'objet "Ordinateur" dans la barre de recherche
            WebElement searchBar = driver.findElement(By.cssSelector("input[placeholder='Rechercher un objet...']"));
            searchBar.sendKeys("Ordinateur");

            // 6. Attendre que le bouton "Échanger avec cet objet" soit visible et cliquable
            WebElement proposeExchangeButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(text(), 'Échanger avec cet objet')]")));

            // Utiliser JavaScriptExecutor pour cliquer sur le bouton si nécessaire
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", proposeExchangeButton);
            System.out.println("✅ Clic sur le bouton 'Échanger avec cet objet' effectué !");

        } catch (Exception e) {
            System.out.println("Erreur lors du test de l'échange de l'ordinateur : " + e.getMessage());
        } finally {
            // Fermer le navigateur
            driver.quit();
        }
    }
}
