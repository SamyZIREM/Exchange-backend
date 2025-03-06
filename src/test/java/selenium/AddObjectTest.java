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
import java.util.concurrent.TimeUnit;

public class AddObjectTest {
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

            // 2. Attendre que la redirection vers la page des objets soit effectuée
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Attendre que l'URL contienne "/objets" pour confirmer la redirection
            wait.until(ExpectedConditions.urlContains("/objets"));
            System.out.println("✅ Connexion réussie, redirection vers la page des objets !");

            // 3. Faire défiler la page jusqu'à la section d'ajout d'objet
            WebElement addObjectForm = driver.findElement(By.xpath("//div[@class='add-object-form']"));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", addObjectForm);

            // 4. Attendre que l'élément soit visible et interactif
            WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder=\"Nom de l'objet\"]")));
            nameInput.sendKeys("Matelas");

            // 5. Utiliser XPath pour le champ Description de l'objet
            WebElement descriptionInput = driver.findElement(By.xpath("//input[@placeholder=\"Description de l'objet\"]"));
            descriptionInput.sendKeys("Matelas IKEA neuf.");

            // 6. Bouton "Ajouter l'objet" avec la nouvelle méthode XPath
            WebElement addButton = driver.findElement(By.xpath("//button[contains(text(),\"Ajouter l'objet\")]"));
            addButton.click();

            // 7. Attendre quelques secondes pour que l'objet soit ajouté
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Matelas')]")));

            // 8. Vérifier si l'objet a été ajouté avec succès
            boolean isObjectAdded = driver.findElements(By.xpath("//div[contains(text(),'Matelas')]")).size() > 0;
            if (isObjectAdded) {
                System.out.println("✅ Objet ajouté avec succès !");
            } else {
                System.out.println("❌ L'objet n'a pas été ajouté.");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du test d'ajout de l'objet : " + e.getMessage());
        } finally {
            // Fermer le navigateur
            //driver.quit();
        }
    }
}
