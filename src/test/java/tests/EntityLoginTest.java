package tests;


import SeleniumFramework.pages.*;
import SeleniumFramework.utils.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.awt.*;
import java.time.Duration;
import java.util.List;

import static SeleniumFramework.utils.CredentialUtil.*;

public class EntityLoginTest {
    private WebDriver driver;
    private ElementUtil util;
    private Robot robot;

    private final String csvFilePath = "InputData/EntityUserCredential.csv";
    private final String bankListCsv = "InputData/BankList.csv";
    private final String entityListCsv = "InputData/EntityList.csv";

    private String entityName;
    private String userId;
    private String newPassword;

    @BeforeMethod
    public void setup() throws AWTException {
        ConfigReader.loadConfig();
        driver = DriverFactory.initDriver();
        util = new ElementUtil(driver, 120);
        robot = new Robot();
        driver.get(ConfigReader.get("SITbaseURL"));
    }

    @Test
    public void loginAndUpdatePasswordForEntityUser() throws Exception {
//        loginEntityUser();
//        resetEntityUserPassword();
//        logout();
        loginWithNewPassword();
//        performProfileIntegration("PAYMENT");
//        performProfileIntegration("GSTIN");
        uploadReconFile("PAYMENT");
        uploadReconFile("GSTIN");
//        logout();
    }

    private void loginEntityUser() {
        String[] creds = CredentialUtil.readLatestCredentialsEntityUser(csvFilePath);
        if (creds == null || creds.length < 3) throw new RuntimeException("No Entity credentials found");
        String userId = creds[1];
        String password = creds[2];
        new LoginPage(driver).login(userId, password);
        new TwoFactorAuthPage(driver).enterOtp();
        util.waitAndClick(By.xpath("//button[text()='Submit']"));
        System.out.println("Entity logged in");
    }


    private void resetEntityUserPassword() throws Exception {
        String[] creds = CredentialUtil.readLatestCredentialsEntityUser(csvFilePath);
        String entityName = creds[0];
        String userId = creds[1];
        String oldPassword = creds[2];
        String newPassword = "Entity@123";
        new ResetPasswordPage(driver).resetPassword(oldPassword, newPassword);
        updateLatestPasswordEntity(csvFilePath, entityName, userId, newPassword);
        System.out.println("SuperUser password updated");
    }

    private void logout() {
        util.waitForSeconds(5);
        util.waitAndClick(By.xpath("(//div[@id='panel1bh-header'])[1]"));
        util.waitAndClick(By.xpath("//p[text()='Logout']"));
        System.out.println("Entity User logged out.");
    }

    private void loginWithNewPassword() throws Exception {
        String[] creds = CredentialUtil.readLatestCredentialsEntityUser(csvFilePath);
        String userId = creds[1];
        String newPassword = creds[2];
        driver.get(ConfigReader.get("SITbaseURL"));
        util.waitForSeconds(5);
        new LoginPage(driver).login(userId, newPassword);
        new TwoFactorAuthPage(driver).enterOtp();
        util.waitAndClick(By.xpath("//button[text()='Submit']"));
        System.out.println("Logged in with updated password");
    }

    private void performProfileIntegration(String reconType) throws InterruptedException {
        util.waitForSeconds(5);
        util.waitAndClick(By.xpath("//p[text()='Profile Integration']"));
        util.waitForSeconds(5);
        util.waitAndClick(By.xpath("//button[text()='Create Profile']"));
        util.waitForSeconds(5);
        selectFromDropdown("//label[text()='Select Bank']/following::input[1]", readLatestBankName(bankListCsv));
        selectFromDropdown("//label[text()='Select Entity']/following::input[1]", readLatestEntityName(entityListCsv));
        selectFromDropdown("//label[text()='Recon Type']/following::input[1]", reconType);

        driver.findElement(By.xpath("//span[text()='Normal File']")).click();
        String filePath = System.getProperty("user.dir") + "/InputData/ENTITY_Recon_File.xlsx";
        uploadFile(filePath);

        assignHeaders(reconType);
    }

    private void uploadReconFile(String reconType) throws InterruptedException {
        util.waitForSeconds(5);
        util.waitAndClick(By.xpath("//p[text()='Recon']"));
        util.waitAndClick(By.xpath("//button[text()='Upload Recon File.']"));

        selectFromDropdown("//label[text()='Select Bank']/following::input[1]", readLatestBankName(bankListCsv));
        selectFromDropdown("//label[text()='Select Entity']/following::input[1]", readLatestEntityName(entityListCsv));
        selectFromDropdown("//label[text()='Recon Type']/following::input[1]", reconType);

        String filePath = System.getProperty("user.dir") + "/InputData/ENTITY_Recon_File.xlsx";
        uploadFile(filePath);

        util.waitAndClick(By.xpath("//button[text()='Submit']"));
        System.out.println("Recon file uploaded for recon type: " + reconType);
    }

    private void selectFromDropdown(String xpath, String value) throws InterruptedException {
        WebElement dropdown = driver.findElement(By.xpath(xpath));
        dropdown.click();
        dropdown.sendKeys(value);
        robot.delay(1000);
        new Actions(driver).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).perform();
    }

    private void uploadFile(String filePath) {
        WebElement upload = driver.findElement(By.name("bulkReconUpload"));
        upload.sendKeys(filePath);
        System.out.println("File uploaded: " + filePath);
    }

    private void assignHeaders(String reconType) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[text()='Assign headers']")));

        List<WebElement> dropTargets = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//h4[text()='Assign headers']/following::div[contains(@class, 'MuiBox-root')][@style]")));

        ActionUtil actionUtil = new ActionUtil(driver);

        WebElement sourceAG = driver.findElement(By.xpath("//div[text()='AG_REF']"));
        WebElement sourcePG = driver.findElement(By.xpath("//div[text()='PG_REF']"));
        WebElement sourceAmount = driver.findElement(By.xpath("//div[text()='Amount']"));

        if ("PAYMENT".equalsIgnoreCase(reconType)) {
            actionUtil.jsDragAndDrop(sourceAG, dropTargets.get(1));
            actionUtil.jsDragAndDrop(sourcePG, dropTargets.get(3));
            actionUtil.jsDragAndDrop(sourceAmount, dropTargets.get(5));
            System.out.println("JS-based drag and drop for PAYMENT completed");
        } else {
            actionUtil.jsDragAndDrop(sourcePG, dropTargets.get(0));
            util.waitForSeconds(3);
            actionUtil.jsDragAndDrop(sourceAmount, dropTargets.get(1));
            util.waitForSeconds(3);
            actionUtil.jsDragAndDrop(sourceAmount, dropTargets.get(2));
            System.out.println("JS-based drag and drop for GSTIN completed");
        }
        util.waitAndClick(By.xpath("//button[text()='Submit']"));
    }

//    @AfterMethod
//    public void tearDown() {
//        if (driver != null) driver.quit();
//    }
}
