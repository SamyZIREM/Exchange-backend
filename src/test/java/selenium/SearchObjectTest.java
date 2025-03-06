package selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

public class SearchObjectTest {
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
            // 1. Se connecter avec Alice
            driver.get("http://localhost:4200/authentification");
            driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys("alice@example.com");
            driver.findElement(By.xpath("//input[@placeholder='Mot de passe']")).sendKeys("password123");
            driver.findElement(By.xpath("//button[text()='Se connecter']")).click();

            // 2. Naviguer vers la page des objets
            Thread.sleep(2000); // Attendre le chargement de la page
            driver.get("http://localhost:4200/objets");

            // 3. Effectuer une recherche
            WebElement searchBox = driver.findElement(By.xpath("//input[@placeholder='Rechercher un objet...']"));
            searchBox.sendKeys("Vélo");
            Thread.sleep(2000); // Attendre l'affichage des résultats

            // 4. Vérifier si un résultat est affiché
            boolean resultFound = driver.findElements(By.className("object-card")).size() > 0;
            if (resultFound) {
                System.out.println("✅ Recherche d'objets réussie : Des résultats ont été trouvés.");
            } else {
                System.out.println("❌ Aucune correspondance trouvée pour la recherche.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du test de recherche : " + e.getMessage());
        } finally {
            // Fermer le navigateur
            driver.quit();
        }
    }
}
