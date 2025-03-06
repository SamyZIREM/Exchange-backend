package selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class ConsultExchangeTest {

    public static void main(String[] args) {
        // Définir le chemin vers chromedriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrateur\\Desktop\\chromedriver-win64\\chromedriver.exe");

        // Options de Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        // Créer une instance de ChromeDriver
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        try {
            // 1. Se connecter avec l'utilisateur Alice
            driver.get("http://localhost:4200/authentification");
            driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys("alice@example.com");
            driver.findElement(By.xpath("//input[@placeholder='Mot de passe']")).sendKeys("password123");
            driver.findElement(By.xpath("//button[text()='Se connecter']")).click();

            // 2. Attendre que la redirection vers la page des objets soit effectuée
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlContains("/objets"));
            System.out.println("✅ Connexion réussie, redirection vers la page des objets !");

            // 3. Cliquer sur le bouton "Voir mes échanges"
            WebElement seeExchangesButton = driver.findElement(By.xpath("//button[text()='Voir mes échanges']"));
            seeExchangesButton.click();

            // 4. Attendre que la page des échanges soit visible
            WebElement echangeContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("echange-container")));
            System.out.println("✅ Page des échanges chargée.");

            // 5. Vérifier qu'il y a des échanges affichés
            WebElement echangeCard = driver.findElement(By.className("echange-card"));
            if (echangeCard.isDisplayed()) {
                System.out.println("✅ Échange trouvé !");
            } else {
                System.out.println("❌ Aucun échange trouvé.");
            }

        } catch (Exception e) {
            System.out.println("Erreur lors du test de consultation des échanges : " + e.getMessage());
        } finally {
            // Fermer le navigateur
            // driver.quit();
        }
    }
}
