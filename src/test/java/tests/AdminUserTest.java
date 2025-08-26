package tests;

import SeleniumFramework.pages.LoginPage;
import SeleniumFramework.pages.TwoFactorAuthPage;
import SeleniumFramework.utils.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.awt.*;
import java.time.Duration;

public class AdminUserTest {

    WebDriver driver;
    private ElementUtil util;
    private final String userIdsCsv = "InputData/User_Ids.csv";

    @BeforeMethod
    public void setup() {
        ConfigReader.loadConfig();
        driver = DriverFactory.initDriver();
        util = new ElementUtil(driver, 20);

        String environment = "SIT";
        driver.get(ConfigReader.get(environment + "baseURL"));
    }

    @Test
    public void adminUserFlow() throws Exception {
        loginAsSuperUser();
        configureOrEditPricing();
        String fetchedUserId = fetchAndStoreUserIdFromUI();
        loadCreditPoints(fetchedUserId);
        reverseLoadedCreditPoints();
        logOut();
    }

    private void loginAsSuperUser() throws Exception {
        String userId = ConfigReader.get("UserID");
        String password = ConfigReader.get("Password");
        new LoginPage(driver).login(userId, password);
        new TwoFactorAuthPage(driver).enterOtp();
        util.waitAndClick(By.xpath("//button[text()='Submit']"));
        System.out.println(" SuperUser logged in successfully.");
        util.waitForSeconds(3);
    }

    private void configureOrEditPricing() throws Exception {
        util.waitAndClick(By.xpath("//p[text()='User Management']"));
        util.waitAndClick(By.xpath("//p[text()='Pricing']"));
        util.waitForSeconds(5);

        // Select Super Username
        WebElement superUserName = driver.findElement(By.xpath("//label[text()='Search In']/following::input[1]"));
        superUserName.click();
        superUserName.sendKeys("Super Username");
        RobotUtil.pressDownAndEnter(new Robot(), 3000);

        // Read latest user details from CSV
        String userDetailsCsv = "InputData/NewCredential.csv";
        String[] latestUserDetails = CredentialUtil.readLatestFullDetails(userDetailsCsv);

        if (latestUserDetails != null) {
            String userFullName = latestUserDetails[0];
            System.out.println("Using User Full Name from CSV: " + userFullName);
            util.waitAndSendKeys(By.xpath("//input[@id='userValue']"), userFullName);
        } else {
            System.out.println("No User Full Name found in CSV, sending default text");
        }

        // Search and open pricing menu
        util.waitAndClick(By.xpath("//button[text()='Search']"));
        util.waitForSeconds(3);
        util.waitAndClick(By.xpath("//table/tbody/tr[1]/td[11]//button/span[1]/img"));

        boolean isConfigure = util.isElementPresent(By.xpath("//li[text()='Configure Pricing']"));
        boolean isEdit = util.isElementPresent(By.xpath("//li[text()='Edit Pricing']"));

        if (isConfigure) {
            util.waitAndClick(By.xpath("//li[text()='Configure Pricing']"));
        } else if (isEdit) {
            util.waitAndClick(By.xpath("//li[text()='Edit Pricing']"));
        } else {
            System.out.println("No Configure or Edit Pricing option found.");
            return;
        }

        util.waitForVisibility(By.xpath("//span[text()='Set pricing']"));

        // Field locators
        By creditPoint = By.xpath("//input[@placeholder='Enter Credit Point Value']");
        By setupFee = By.xpath("//input[@placeholder='Enter Setup Fee']");
        By SetupFeeUTR = By.xpath("//input[@placeholder='Enter Setup Fee utr']");

        // Clear old values if in edit mode
        if (isEdit) {
            System.out.println("Edit the pricing details");
//            util.clearAndType(creditPoint, "0.30");
//            util.clearAndType(setupFee, "15000");
//            util.clearAndType(SetupFeeUTR, CredentialUtil.generateRandomUTR());
        } else {
            util.waitAndSendKeys(creditPoint, "0.25");
            util.waitAndSendKeys(setupFee, "10000");
            util.waitAndSendKeys(SetupFeeUTR, CredentialUtil.generateRandomUTR());
        }

        util.waitAndClick(By.xpath("//button[text()='Save']"));
        util.waitForSeconds(3);
    }


    private String fetchAndStoreUserIdFromUI() throws InterruptedException {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//tr[@role='row'])[2]/td[1]/div")));

        WebElement userIdElement = driver.findElement(By.xpath("(//tr[@role='row'])[2]/td[1]/div"));
        String userId = userIdElement.getText();
        System.out.println("Fetched User ID: " + userId);

        CredentialUtil.writeToCSV(userIdsCsv, userId);
        return userId;

    }

    private void loadCreditPoints(String userId) throws Exception {
        util.waitForSeconds(3);
        util.waitAndClick(By.xpath("(//p[text()='Recon Management'])[1]"));
        util.waitAndClick(By.xpath("//p[text()='Load Credit Points']"));
        util.waitForSeconds(3);
        util.waitAndClick(By.xpath("//button[text()='Load Credit Point']"));
        util.waitForVisibility(By.xpath("//label[text()='Select Super User']"));
        WebElement entityUserInput = driver.findElement(By.xpath("//label[text()='Select Super User']/following::input[1]"));
        entityUserInput.click();
        util.waitForSeconds(3);

        //  Use last user ID from CSV
        String lastUserId = CredentialUtil.readLastUserIdFromCSV(userIdsCsv);
        entityUserInput.sendKeys(lastUserId);
        RobotUtil.pressDownAndEnter(new Robot(), 3000);

        util.waitAndSendKeys(By.id("bank_name"), "BOB");
        util.waitAndSendKeys(By.id("amount"), "10000");
        String randomUTR = CredentialUtil.generateRandomUTR();
        WebElement utrInput = driver.findElement(By.xpath("//input[@id='transaction_utr']"));
        utrInput.clear();
        utrInput.sendKeys(randomUTR);
        util.waitAndSendKeys(By.id("remark"), "Test Credit Load points");

        util.waitAndClick(By.xpath("//button[text()='Next']"));
        new TwoFactorAuthPage(driver).enterOtp();
        util.waitAndClick(By.xpath("//button[text()='Submit']"));
        util.waitForSeconds(3);
    }

    private void reverseLoadedCreditPoints() throws Exception {
        util.waitAndClick(By.xpath("//table/tbody/tr[1]/td[12]//button/span[1]/img"));
        util.waitAndClick(By.xpath("//li[text()='Reverse']"));
        util.waitAndSendKeys(By.name("remark"), "Load Credit Point Reverse");
        util.waitAndClick(By.xpath("//button[text()='Next']"));
        new TwoFactorAuthPage(driver).enterOtp();
        util.waitAndClick(By.xpath("//button[text()='Submit']"));
        util.waitForSeconds(3);
    }

    private void logOut() throws Exception {
        util.waitForSeconds(3); // Optional wait
        util.waitAndClick(By.xpath("(//div[@id='panel1bh-header'])[1]"));
        util.waitAndClick(By.xpath("//p[text()='Logout']"));
        System.out.println(" Entity User logged out.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
