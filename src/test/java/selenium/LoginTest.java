package selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class LoginTest {

    public static void main(String[] args) {
        // Définir le chemin vers chromedriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrateur\\Desktop\\chromedriver-win64\\chromedriver.exe");

        // Créer et configurer les options de Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        // Créer une instance de ChromeDriver avec les options configurées
        WebDriver driver = new ChromeDriver(options);

        try {
            // Naviguer vers la page d'authentification de ton application Angular
            driver.get("http://localhost:4200/authentification");

            // Attendre un peu pour s'assurer que la page est bien chargée
            Thread.sleep(2000);

            // Trouver le champ email et entrer l'email
            WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Email']"));
            emailField.sendKeys("alice@example.com");

            // Trouver le champ mot de passe et entrer le mot de passe
            WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Mot de passe']"));
            passwordField.sendKeys("password123");

            // Trouver et cliquer sur le bouton "Se connecter"
            WebElement loginButton = driver.findElement(By.xpath("//button[text()='Se connecter']"));
            loginButton.click();

            // Attendre que la redirection se fasse (si nécessaire)
            Thread.sleep(3000);

            // Vérifier si on est bien redirigé vers la page des objets
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("/objets")) {
                System.out.println("✅ Connexion réussie, redirection correcte vers la page des objets !");
            } else {
                System.out.println("❌ Erreur : la redirection n'a pas fonctionné.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fermer le navigateur
            driver.quit();
        }
    }
}
