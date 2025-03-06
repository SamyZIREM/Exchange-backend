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
import java.util.List;

public class AcceptOrRefuseExchangeTest {

    public static void main(String[] args) {
        // Définir le chemin vers chromedriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Administrateur\\Desktop\\chromedriver-win64\\chromedriver.exe");

        // Options de Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        // Créer une instance de ChromeDriver
        WebDriver driver = new ChromeDriver(options);

        try {
            // 1. Se connecter avec l'utilisateur Bob
            driver.get("http://localhost:4200/authentification");
            driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys("bob@example.com");
            driver.findElement(By.xpath("//input[@placeholder='Mot de passe']")).sendKeys("password123");
            driver.findElement(By.xpath("//button[text()='Se connecter']")).click();

            // 2. Attendre que la redirection vers la page principale soit effectuée
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.urlContains("/objets"));
            System.out.println("✅ Connexion réussie, redirection vers la page d'accueil !");

            // 3. Cliquer sur "Voir mes échanges"
            WebElement seeExchangesButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[text()='Voir mes échanges']")));
            seeExchangesButton.click();
            System.out.println("✅ Accès à la page des échanges !");

            // 4. Attendre que la page des échanges se charge
            wait.until(ExpectedConditions.urlContains("/echanges"));
            System.out.println("✅ Redirection vers la page des échanges réussie.");

            // 5. Vérifier la présence d'un échange en attente
            WebElement exchangeRequest = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[@class='echange-card']//p[contains(text(),'EN_ATTENTE')]")));
            System.out.println("✅ Demande d'échange en attente trouvée.");

           // 6. Accepter l'échange (seulement si le bouton existe)

            // Chercher le bouton "Accepter" dans l'échange
            List<WebElement> acceptButtons = exchangeRequest.findElements(By.xpath(".//button[text()='Accepter']"));

            if (!acceptButtons.isEmpty()) {
                WebElement acceptButton = acceptButtons.get(0);

                // Scroller pour rendre visible
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", acceptButton);
                Thread.sleep(500); // Petite pause

                // Cliquer sur "Accepter"
                acceptButton.click();
                System.out.println("✅ Échange accepté !");
            } else {
                System.out.println("⚠️ Aucun bouton 'Accepter' trouvé. Peut-être que cet échange n'est pas géré par l'utilisateur actuel.");
            }


            // 7. Vérifier que l'échange est bien accepté
            WebElement confirmationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[contains(text(),'Échange accepté')]")));
            if (confirmationMessage.isDisplayed()) {
                System.out.println("✅ L'échange a été accepté avec succès !");
            } else {
                System.out.println("❌ L'échange n'a pas été accepté.");
            }

            // --- Attendre un peu avant de refuser un autre échange ---
            Thread.sleep(3000);

            // 10. Vérifier que l'échange a bien été refusé
            WebElement refusalMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[contains(text(),'Échange refusé')]")));
            if (refusalMessage.isDisplayed()) {
                System.out.println("✅ L'échange a été refusé avec succès !");
            } else {
                System.out.println("❌ L'échange n'a pas été refusé.");
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur lors du test d'acceptation/refus d'échange : " + e.getMessage());
        } finally {
            // Fermer le navigateur
            //driver.quit();
        }
    }
}
