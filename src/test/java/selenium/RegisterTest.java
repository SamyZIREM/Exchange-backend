package selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class RegisterTest {

    public static void main(String[] args) {
        // Définir le chemin vers chromedriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrateur\\Desktop\\chromedriver-win64\\chromedriver.exe");

        // Créer et configurer les options de Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        // Créer une instance de ChromeDriver avec les options configurées
        WebDriver driver = new ChromeDriver(options);

        // Naviguer vers la page d'authentification
        driver.get("http://localhost:4200/authentification");

        // Cliquer sur le lien "Pas de compte ? Inscrivez-vous ici"
        WebElement inscriptionLink = driver.findElement(By.xpath("//a[contains(text(), 'Pas de compte ? Inscrivez-vous ici')]"));
        inscriptionLink.click();

        // Remplir le champ "Nom"
        WebElement nomField = driver.findElement(By.xpath("//input[@placeholder='Nom']"));
        nomField.sendKeys("TestUser");

        // Remplir le champ "Email"
        WebElement emailField = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        emailField.sendKeys("testuser@example.com");

        // Remplir le champ "Mot de passe"
        WebElement passwordField = driver.findElement(By.xpath("//input[@placeholder='Mot de passe']"));
        passwordField.sendKeys("password123");

        // Cliquer sur le bouton "S'inscrire"
        WebElement inscrireButton = driver.findElement(By.xpath("//button[contains(text(), \"S'inscrire\")]"));
        inscrireButton.click();

        // Attendre quelques secondes pour voir le résultat (tu peux améliorer ça avec WebDriverWait)
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Fermer le navigateur
        driver.quit();
    }
}
