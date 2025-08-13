package tests;

import SeleniumFramework.utils.*;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.*;
import java.time.Duration;
import java.util.*;

import static SeleniumFramework.utils.AllureUtil.attachScreenshot;
import static SeleniumFramework.utils.AllureUtil.saveTextLog;

public class SignUpSuperUser {
    private WebDriver driver;
    private ElementUtil util;
    private final String csvFilePath = "InputData/NewCredential.csv";


    private String randomEmail;
    private String userFullName;
    private String emailPrefix;

    @BeforeClass
    public void setup() {
        ConfigReader.loadConfig();
        driver = DriverFactory.initDriver();
        util = new ElementUtil(driver, 60);
        driver.get(ConfigReader.get("UATbaseURL"));
        saveTextLog("Opened SIT base URL");
    }

    @Test(description = "Sign up SuperUser and capture credentials from email")
    @Step("Perform superuser signup and email credentials extraction")
    public void signUpSuperUserAndCaptureCredentials() throws InterruptedException {
//      for (int i = 1; i <= 30; i++) {
//      System.out.println("Run #" + i + " starting...");

        initializeUserData();
        fillSignUpForm();
        submitForm();
//        openMailinatorInbox();
//        extractAndSaveCredentials();

//      System.out.println("Run #" + i + " completed.");
//        }
    }


    @Step("Initialize user data")
    private void initializeUserData() {
        randomEmail = "superuser" + System.currentTimeMillis() + "@mailinator.com";
        emailPrefix = randomEmail.split("@")[0];
        userFullName = RandomStringUtils.randomAlphabetic(5) + " " + RandomStringUtils.randomAlphabetic(7);

        saveTextLog("Generated user full name: " + userFullName);
        saveTextLog("Generated email: " + randomEmail);
    }

    @Step("Fill sign-up form")
    private void fillSignUpForm() {
        util.waitAndClick(By.xpath("//p[text()='Sign up']"));
        saveTextLog("Clicked Sign Up link");

        util.waitAndSendKeys(By.id("name"), userFullName);
        saveTextLog("Entered name");

        util.waitAndSendKeys(By.id("email"), randomEmail);
        saveTextLog("Entered email");

        util.waitAndSendKeys(By.id("mobile_no"), "9876543211");
        saveTextLog("Entered mobile number");

        util.waitAndClick(By.xpath("//*[@id='__next']/div[1]/div[2]/div/form/div[4]/div/div/div"));
        util.waitAndClick(By.xpath("//li[text()='Super User']"));
        saveTextLog("Selected user type Super User");

        driver.findElement(By.xpath("//input[@value='NA']")).click();
        saveTextLog("Selected NA for notification preference");

        attachScreenshot(driver, "Filled Sign Up Form");
    }

    @Step("Submit signup form")
    private void submitForm() {
        util.waitAndClick(By.xpath("//button[text()='sign up']"));
        saveTextLog("Clicked Sign Up button");
    }

    @Step("Open Mailinator inbox")
    private void openMailinatorInbox() throws InterruptedException {
        Thread.sleep(5000);
        driver.get("https://www.mailinator.com/v4/public/inboxes.jsp?to=" + emailPrefix);
        Thread.sleep(10000);
        saveTextLog("Opened mailinator inbox for: " + emailPrefix);
    }

    @Step("Extract credentials from onboarding email")
    private void extractAndSaveCredentials() throws InterruptedException {
        util.isElementVisible(By.xpath("//td[contains(text(), 'Successfully Onboarded')]"));
        WebElement emailRow = driver.findElement(By.xpath("//td[contains(text(), 'Successfully Onboarded')]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", emailRow);
        emailRow.click();
        Thread.sleep(5000);

        driver.switchTo().frame("html_msg_body");

        List<WebElement> rows = driver.findElements(By.xpath("//table//tr"));
        String userId = "", password = "";

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() == 2) {
                String label = cells.get(0).getText().toLowerCase().trim();
                String value = cells.get(1).getText().trim();
                if (label.contains("user id")) userId = value;
                else if (label.contains("password")) password = value;
            }
        }

        driver.switchTo().defaultContent();

        if (!userId.isEmpty() && !password.isEmpty()) {
            saveLatestToCSV(userFullName, userId, password);
            saveTextLog("Credentials extracted and saved: UserID = " + userId);
        } else {
            saveTextLog("Failed to extract credentials from email.");
            attachScreenshot(driver, "Missing Credentials in Email");
        }
    }

    @Step("Save latest credentials to CSV")
    private void saveLatestToCSV(String userFullName, String userId, String password) {
        List<String[]> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) records.add(parts);
            }
        } catch (IOException e) {
            saveTextLog("Could not read CSV file: " + e.getMessage());
        }

        // Keep only last 2 records + add new
        if (records.size() > 2) records = records.subList(records.size() - 2, records.size());
        records.add(new String[]{userFullName, userId, password});

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            for (String[] record : records) {
                writer.write(String.join(",", record));
                writer.newLine();
            }
            saveTextLog("CSV updated successfully");
        } catch (IOException e) {
            saveTextLog("Could not write CSV file: " + e.getMessage());
        }
    }



    @AfterClass
    public void tearDown() {
        if (driver != null) driver.quit();
    }
}
