package tests;

import SeleniumFramework.pages.*;
import SeleniumFramework.utils.*;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import io.qameta.allure.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;

import java.awt.*;
import java.time.Duration;
import java.util.*;
import java.util.List;

import static SeleniumFramework.utils.CredentialUtil.*;

public class SuperUserLoginTest {

  private WebDriver driver;
  private ElementUtil util;
  private Robot robot;

  private final String superUserCsv = "InputData/NewCredential.csv";
  private final String entityUserCsv = "InputData/EntityUserCredential.csv";
  private final String bankListCsv = "InputData/BankList.csv";
  private final String entityListCsv = "InputData/EntityList.csv";
  private final String rconIdsCsv = "InputData/Recon_Ids.csv";




  private String entityName;
  private String entityUserEmail;
  private String filePath;

  @BeforeClass
  public void setup() throws AWTException {
    ConfigReader.loadConfig();
    driver = DriverFactory.initDriver();
    util = new ElementUtil(driver, 120);
    robot = new Robot();
    filePath = System.getProperty("user.dir") + "/InputData/Recon_Source.xlsx";
    Allure.step("Test setup complete, driver initialized and config loaded");
  }

  @Test
  public void superUserFlow() throws Exception {
    driver.get(ConfigReader.get("SITbaseURL"));
    Allure.step("Navigated to SIT base URL");

    loginSuperUser();
    resetSuperUserPassword();
    logoutUser();
      loginSuperUserWithNewPassword();

      createBank();
     createEntity();
    createEntityUser();
    extractEntityUserCredentials();

    profileIntegration("PAYMENT");
//    profileIntegrationTypePAYMENTEdit("PAYMENT");
    profileIntegration("GSTIN");
//    profileIntegrationTypeGSTINEdit("GSTIN");
//    durationFiltersAndPagination(true);
//
    uploadSourceFile("PAYMENT");
    uploadSourceFile("GSTIN");
//    durationFiltersAndPagination(true);
//    reconModule();

    logoutUser();
  }

  @Step("Logging in as SuperUser")
  private void loginSuperUser() throws Exception {
    String[] creds = readLatestCredentialsSuperUser(superUserCsv);
    if (creds == null || creds.length < 3) throw new RuntimeException("No Superuser credentials found");
    String userId = creds[1];
    String password = creds[2];
    new LoginPage(driver).login(userId, password);
    new TwoFactorAuthPage(driver).enterOtp();
    util.waitAndClick(By.xpath("//button[text()='Submit']"));
    Allure.step("SuperUser logged in with userId: " + userId);
  }

  @Step("Resetting SuperUser Password")
  private void resetSuperUserPassword() throws Exception {
    String[] creds = readLatestCredentialsSuperUser(superUserCsv);
    String userFullName = creds[0];
    String userId = creds[1];
    String oldPassword = creds[2];
    String newPassword = "Auth@123";
    new ResetPasswordPage(driver).resetPassword(oldPassword, newPassword);
    updateLatestPasswordSuperUser(superUserCsv, userFullName, userId, newPassword);
    Allure.step("SuperUser password updated for userId: " + userId);
  }

  @Step("Logging out User")
  private void logoutUser() {
    try {
      util.waitAndClick(By.xpath("(//div[@class='profile-info-logo-title-wrapper MuiBox-root css-1ircn5c'])[1]"));
      util.waitAndClick(By.xpath("//p[text()='Logout']"));
      Allure.step("User logged out");
    } catch (Exception e) {
      Allure.step("Logout failed: " + e.getMessage());
    }
  }

  @Step("Logging in SuperUser with updated password")
  private void loginSuperUserWithNewPassword() throws Exception {
    String[] creds = readLatestCredentialsSuperUser(superUserCsv);
    String userId = creds[1];
    String newPassword = creds[2];
    driver.get(ConfigReader.get("SITbaseURL"));
    util.waitForSeconds(2);
    new LoginPage(driver).login(userId, newPassword);
    new TwoFactorAuthPage(driver).enterOtp();
    util.waitAndClick(By.xpath("//button[text()='Submit']"));
    Allure.step("Logged in with updated password for userId: " + userId);
  }



  @Step("Creating a Bank")
  private void createBank() throws Exception {
    util.waitForSeconds(3);
    WebElement settingElement = util.waitForVisibility(By.xpath("//p[text()='Setting']"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", settingElement);
    util.isElementVisible(By.xpath("//p[text()='Setting']"));
    util.waitAndClick(By.xpath("//p[text()='Setting']"));
    util.waitAndClick(By.xpath("(//p[text()='Bank Configuration'])[1]"));
    util.waitAndClick(By.xpath("//button[text()='Add Bank']"));
    String bankName = RandomStringUtils.randomAlphabetic(4).toUpperCase() + " BANK";
    util.waitAndSendKeys(By.id("bank_name"), bankName);
    util.waitAndClick(By.xpath("//button[text()='Submit']"));
    appendBankNameToCSV(bankListCsv, bankName);
    Allure.step("Bank created: " + bankName);
    util.waitForSeconds(5);
     durationFiltersAndPagination(false);
    util.scrollToElement(By.xpath("//input[@placeholder='Enter a Bank Name']"));
    util.waitAndSendKeys(By.xpath("//input[@placeholder='Enter a Bank Name']"),bankName);
    util.waitAndClick(By.xpath("//button[text()='Search']"));
    util.waitForSeconds(5);
    util.waitAndClick(By.xpath("//img[@alt='vertical-dot-button']\n"));
    util.waitForSeconds(3);
    util.waitAndClick(By.xpath("//li[text()='View Details']"));
    util.waitForSeconds(3);
    util.waitAndClick(By.xpath("//button[text()='Cancel']"));

  }

  @Step("Creating an Entity")
  private void createEntity() {
    util.waitAndClick(By.xpath("//p[text()='Manage User']"));
    util.waitAndClick(By.xpath("//button[text()='Entity']"));
    util.waitAndClick(By.xpath("//button[text()='Create Entity']"));
    entityName = RandomStringUtils.randomAlphabetic(5);
    String directorEmail = "ahmad" + RandomStringUtils.randomNumeric(4) + "@mailinator.com";
    util.waitAndSendKeys(By.id("entity_name"), entityName);
    util.waitAndSendKeys(By.id("email_id"), entityName + " User");
    util.waitAndSendKeys(By.id("director_mail"), directorEmail);
    util.waitAndClick(By.xpath("//button[text()='Submit']"));
    appendEntityNameToCSV(entityListCsv, entityName);
    Allure.step("Entity created: " + entityName);
    util.waitForSeconds(3);
     //durationFiltersAndPagination(true);

  }

  @Step("Creating an Entity User")
  private void createEntityUser() throws Exception {
    util.waitForSeconds(2);
    util.scrollToElement(By.xpath("//button[text()='User']"));
    util.isElementVisible(By.xpath("//button[text()='User']"));
    util.waitAndClick(By.xpath("//button[text()='User']"));
    util.waitAndClick(By.xpath("//button[text()='Create User']"));
    entityUserEmail = entityName.toLowerCase() + "user" + RandomStringUtils.randomNumeric(4) + "@mailinator.com";
    util.waitAndSendKeys(By.id("name"), "Entity User");
    util.waitAndSendKeys(By.id("email_id"), entityUserEmail);
    util.waitAndSendKeys(By.id("mobile_no"), "9833505676");
    WebElement entityInput = driver.findElement(By.xpath("//label[text()='Select Entity']/following::input[1]"));
    entityInput.click();
    entityInput.sendKeys(entityName);
    robot.delay(3000);
    RobotUtil.pressDownAndEnter(robot, 1000);
    driver.findElement(By.xpath("//input[@value='NA']")).click();
    util.waitAndClick(By.xpath("//button[text()='Submit']"));
    Allure.step("Entity User created: " + entityUserEmail);
 
    util.waitForSeconds(3);
     durationFiltersAndPagination(true);
  }

  @Step("Extracting Credentials for Entity User")
  private void extractEntityUserCredentials() throws InterruptedException {
    String inbox = entityUserEmail.split("@")[0];
    ((JavascriptExecutor) driver).executeScript("window.open('','_blank');");
    List<String> tabs = new ArrayList<>(driver.getWindowHandles());
    driver.switchTo().window(tabs.get(tabs.size() - 1));
    driver.get("https://www.mailinator.com/v4/public/inboxes.jsp?to=" + inbox);
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[contains(text(),'Successfully Onboarded')]"))).click();
    Thread.sleep(3000);
    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("html_msg_body")));
    List<WebElement> rows = driver.findElements(By.xpath("//table//tr"));
    String userId = "", password = "";
    for (WebElement row : rows) {
      List<WebElement> cells = row.findElements(By.tagName("td"));
      if (cells.size() == 2) {
        String label = cells.get(0).getText().toLowerCase();
        String value = cells.get(1).getText();
        if (label.contains("user id")) userId = value;
        if (label.contains("password")) password = value;
      }
    }
    driver.switchTo().defaultContent();
    driver.close();
    driver.switchTo().window(tabs.get(0));
    if (!userId.isEmpty() && !password.isEmpty()) {
      appendToCSV(entityUserCsv, entityName, userId, password);
      Allure.step("Entity User credentials saved: " + userId);
    } else {
      Allure.step("Failed to extract credentials from Mailinator.");
    }
  }

  @Step("Profile Integration for {reconType}")
  private void profileIntegration(String reconType) throws InterruptedException {
    util.waitForSeconds(5);
    util.waitAndClick(By.xpath("//p[text()='Profile Integration']"));
    util.waitAndClick(By.xpath("//button[text()='Create Profile']"));
    util.waitForSeconds(3);
    util.waitAndClick(By.xpath("//button[text()='Cancel']"));
    util.waitForSeconds(3);
    util.waitAndClick(By.xpath("//button[text()='Create Profile']"));
    util.waitForSeconds(2);
    selectFromDropdownEntity("//label[text()='Select Entity']/following::input[1]", entityListCsv);
    selectReconType(reconType);
    driver.findElement(By.xpath("(//input[@name='fileType'])[1]")).click();
    String filePath = System.getProperty("user.dir") + "/InputData/Recon_Source_File.xlsx";
    uploadFile(filePath);
    util.waitForSeconds(3);
    util.waitAndClick(By.xpath("//button[text()='Remove']"));
    util.waitForSeconds(3);
    uploadFile(filePath);
    if ("PAYMENT".equalsIgnoreCase(reconType)) {
      assignHeadersForPayment();
    } else {
      assignHeadersGSTIN();
    }
    util.waitAndClick(By.xpath("//button[text()='Submit']"));
    Allure.step("Profile Integration done for " + reconType);
    util.waitForSeconds(3);
  }
  private void profileIntegrationTypePAYMENTEdit(String reconType) {

    //------- View the profile Integration Details Payment
    String filePath = System.getProperty("user.dir") + "/InputData/Recon_Source_File.xlsx";
    util.waitForSeconds(5);
    util.waitAndClick(By.xpath("//p[text()='Profile Integration']"));
    util.waitAndClick(By.xpath("(//img[@alt='vertical-dot-button'])[1]"));
    util.waitAndClick(By.xpath("//li[text()='View Details']"));
    util.waitForSeconds(3);
    util.waitAndClick(By.xpath("//button[text()='Cancel']"));
    util.waitForSeconds(3);

    //------- Edit the profile Integration Details Payment
    util.waitAndClick(By.xpath("(//img[@alt='vertical-dot-button'])[1]"));
    util.waitAndClick(By.xpath("//li[text()='Edit']"));
    util.waitAndClick(By.xpath("(//*[@class=\"MuiSvgIcon-root MuiSvgIcon-fontSizeInherit css-1amtie4\"])[4]"));
    util.waitAndClick(By.xpath("//button[text()='Remove']"));
    util.waitForSeconds(5);
    uploadFile(filePath);
    assignHeadersForPaymentEdit();
    util.waitAndClick(By.xpath("//button[text()='Submit']"));
    util.waitForSeconds(3);
  }
  private void profileIntegrationTypeGSTINEdit(String reconType){
    //------- View the profile Integration Details GSTIN
    String filePath = System.getProperty("user.dir") + "/InputData/Recon_Source_File.xlsx";
    util.waitForSeconds(5);
    util.waitAndClick(By.xpath("//p[text()='Profile Integration']"));

    util.waitAndClick(By.xpath("(//img[@alt='vertical-dot-button'])[1]"));
    util.waitAndClick(By.xpath("//li[text()='View Details']"));
    util.waitForSeconds(3);
    util.waitAndClick(By.xpath("//button[text()='Cancel']"));
    util.waitForSeconds(3);

    //------- Edit the profile Integration Details GSTIN

    util.waitAndClick(By.xpath("(//img[@alt='vertical-dot-button'])[1]"));
    util.waitAndClick(By.xpath("//li[text()='Edit']"));
    util.waitAndClick(By.xpath("(//*[@class=\"MuiSvgIcon-root MuiSvgIcon-fontSizeInherit css-1amtie4\"])[4]"));
    util.waitAndClick(By.xpath("(//*[@class='MuiSvgIcon-root MuiSvgIcon-fontSizeInherit css-1amtie4'])[4]"));
    util.waitAndClick(By.xpath("//button[text()='Remove']"));
    util.waitForSeconds(3);
    uploadFile(filePath);
    assignHeadersGSTINEdit();
    util.waitAndClick(By.xpath("//button[text()='Submit']"));
    util.waitForSeconds(3);

  }

  @Step("Upload Source File for {reconType}")
  private void uploadSourceFile(String reconType) throws InterruptedException {
    util.waitForSeconds(5);
    util.waitAndClick(By.xpath("//p[text()='Source Data']"));
    util.waitAndClick(By.xpath("//button[text()='Upload Souce File.']"));
    util.waitForSeconds(5);
    selectFromDropdownEntity("//label[text()='Select Enity']/following::input[1]", entityListCsv);
    selectReconType(reconType);
    String filePath = System.getProperty("user.dir") + "/InputData/Recon_Source_File.xlsx";
    uploadFile(filePath);
    util.waitAndClick(By.xpath("//button[text()='Submit']"));
    Allure.step("Source File uploaded for " + reconType);
    util.waitForSeconds(3);
    util.waitAndClick(By.xpath("//button[text()='Download']"));
//     durationFiltersAndPagination(true);
  }

  private void reconModule() throws InterruptedException {
    String reconID = readLatestEntityName(rconIdsCsv);
    util.waitForSeconds(3);
    util.waitAndClick(By.xpath("//p[text()='Recon']"));
    util.waitForSeconds(3);
    fetchAndStoreReconIdFromUI();
    util.waitAndClick(By.xpath("//button[text()='Download']"));
    util.waitForSeconds(3);
    driver.navigate().back();
    util.waitForSeconds(3);
    durationFiltersAndPagination(true);
    util.waitForSeconds(3);
    util.scrollToElement(By.xpath("//button[text()='Recon Data']"));
    util.waitAndClick(By.xpath("//button[text()='Recon Data']"));
    util.waitForSeconds(3);
    util.waitAndSendKeys(By.xpath("//input[@placeholder='Enter a recon id']"),reconID);
    util.waitAndClick(By.xpath("//button[text()='Search']"));
    util.waitForSeconds(3);
    util.waitAndClick(By.xpath("//button[text()='Request Report']"));
    util.waitAndClick(By.xpath("//li[text()='XLSX']"));
    util.waitAndClick(By.xpath("//button[text()='Recon Exception']"));
    util.waitForSeconds(3);
    util.waitAndSendKeys(By.xpath("//input[@placeholder='Enter a recon id']"),reconID);
    util.waitAndClick(By.xpath("//button[text()='Search']"));
    util.waitForSeconds(3);
    util.waitAndClick(By.xpath("//button[text()='Request Report']"));
    util.waitAndClick(By.xpath("//li[text()='XLSX']"));
  }

  @Step("Select Entity from dropdown")
  private void selectFromDropdownEntity(String xpath, String csv) throws InterruptedException {
    String value = readLatestEntityName(csv);
    WebElement dropdown = driver.findElement(By.xpath(xpath));
    dropdown.click();
    dropdown.sendKeys(value);
    robot.delay(2000);
    Actions actions = new Actions(driver);
    actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).perform();
    Allure.step("Selected entity: " + value);
  }

  @Step("Select Recon Type {reconType}")
  private void selectReconType(String reconType) throws InterruptedException {
    WebElement dropdown = driver.findElement(By.xpath("//label[text()='Recon Type']/following::input[1]"));
    dropdown.click();
    dropdown.sendKeys(reconType);
    robot.delay(2000);
    Actions actions = new Actions(driver);
    actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).perform();
    Allure.step("Selected Recon Type: " + reconType);
  }

  @Step("Uploading file at path {filepath}")
  private void uploadFile(String filepath) {
    WebElement upload = driver.findElement(By.name("bulkReconUpload"));
    upload.sendKeys(filepath);
    Allure.step("File uploaded: " + filepath);
  }

  @Step("Assign Headers For Payment")
  private void assignHeadersForPayment() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement assignHeadersPanel = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//h4[text()='Assign headers']/parent::div")));
    WebElement sourceAG = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[text()='AG_Transaction ID']")));
    WebElement sourcePG = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[text()='PG_Transaction ID']")));
    WebElement sourceTx = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[text()='Tx_Amount']")));
    WebElement sourceSt = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[text()='Status']")));

    ActionUtil actionUtil = new ActionUtil(driver);
    actionUtil.dragAndDrop(sourceAG, assignHeadersPanel);
    Allure.step("Dragged 'AG_Transaction ID' to Assign headers.");
    actionUtil.dragAndDrop(sourcePG, assignHeadersPanel);
    Allure.step("Dragged 'PG_Transaction ID' to Assign headers.");
    actionUtil.dragAndDrop(sourceTx, assignHeadersPanel);
    Allure.step("Dragged 'Tx_Amount' to Assign headers.");
    actionUtil.dragAndDrop(sourceSt, assignHeadersPanel);
    Allure.step("Dragged 'Status ID' to Assign headers.");
    util.waitAndClick(By.xpath("(//*[@class=\"MuiSvgIcon-root MuiSvgIcon-fontSizeInherit css-1amtie4\"])[4]"));
  }

  private void assignHeadersForPaymentEdit(){
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement assignHeadersPanel = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//h4[text()='Assign headers']/parent::div")));
    WebElement sourceAG = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[text()='AG_Transaction ID']")));
    WebElement sourcePG = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[text()='PG_Transaction ID']")));
    WebElement sourceTx = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[text()='Tx_Amount']")));
    WebElement sourceSt = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[text()='Status']")));
    ActionUtil actionUtil = new ActionUtil(driver);
    actionUtil.dragAndDrop(sourcePG, assignHeadersPanel);
    Allure.step("Dragged 'PG_Transaction ID' to Assign headers.");
    actionUtil.dragAndDrop(sourceTx, assignHeadersPanel);
    Allure.step("Dragged 'Tx_Amount' to Assign headers.");
    actionUtil.dragAndDrop(sourceSt, assignHeadersPanel);
    Allure.step("Dragged 'Status ID' to Assign headers.");
    util.waitAndClick(By.xpath("(//*[@class=\"MuiSvgIcon-root MuiSvgIcon-fontSizeInherit css-1amtie4\"])[4]"));

  }

  @Step("Assign Headers For GSTIN")
  private void assignHeadersGSTIN() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[text()='Assign headers']")));
    List<WebElement> dropTargets = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
            By.xpath("//h4[text()='Assign headers']/following::div[contains(@class, 'MuiBox-root')][@style]")));
    if (dropTargets.size() < 3)
      throw new RuntimeException("Less than 3 drop target cells found for header assignment.");
    WebElement sourceAG_ = driver.findElement(By.xpath("//div[text()='AG_Transaction ID']"));
    WebElement sourcePG_ = driver.findElement(By.xpath("//div[text()='PG_Transaction ID']"));
    WebElement sourceTx_ = driver.findElement(By.xpath("//div[text()='Tx_Amount']"));
    ActionUtil actionUtil = new ActionUtil(driver);
    actionUtil.jsDragAndDrop(sourceAG_, dropTargets.get(1));
    actionUtil.jsDragAndDrop(sourcePG_, dropTargets.get(3));
    actionUtil.jsDragAndDrop(sourceTx_, dropTargets.get(5));
    Allure.step("JS-based drag and drop completed");
    util.waitAndClick(By.xpath("(//*[@class=\"MuiSvgIcon-root MuiSvgIcon-fontSizeInherit css-1amtie4\"])[4]"));
    util.waitAndClick(By.xpath("(//*[@class='MuiSvgIcon-root MuiSvgIcon-fontSizeInherit css-1amtie4'])[4]"));
    WebElement submitButton = driver.findElement(By.xpath("//button[text()='Submit']"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
  }

  private void assignHeadersGSTINEdit() {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    util.waitAndClick(By.xpath("//*[@class='MuiSvgIcon-root MuiSvgIcon-fontSizeInherit css-14pwbn9']"));

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h4[text()='Assign headers']")));
    List<WebElement> dropTargets = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
            By.xpath("//h4[text()='Assign headers']/following::div[contains(@class, 'MuiBox-root')][@style]")));
    if (dropTargets.size() < 3)
      throw new RuntimeException("Less than 3 drop target cells found for header assignment.");
    WebElement sourceAG_ = driver.findElement(By.xpath("//div[text()='AG_Transaction ID']"));
    WebElement sourcePG_ = driver.findElement(By.xpath("//div[text()='PG_Transaction ID']"));
    WebElement sourceTx_ = driver.findElement(By.xpath("//div[text()='Tx_Amount']"));
    WebElement sourceSt = driver.findElement(By.xpath("//div[text()='Status']"));


    ActionUtil actionUtil = new ActionUtil(driver);
    actionUtil.jsDragAndDrop(sourceAG_, dropTargets.get(1));
    actionUtil.jsDragAndDrop(sourcePG_, dropTargets.get(3));
    actionUtil.jsDragAndDrop(sourceTx_, dropTargets.get(5));
    actionUtil.jsDragAndDrop(sourceSt, dropTargets.get(5));
    util.waitAndClick(By.xpath("(//*[@class='MuiSvgIcon-root MuiSvgIcon-fontSizeInherit css-1amtie4'])[11]"));
    driver.findElement(By.xpath("(//input[@type='checkbox'])[3]")).click();
    Allure.step("JS-based drag and drop completed");
    WebElement submitButton = driver.findElement(By.xpath("//button[text()='Submit']"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
  }

  public void durationFiltersAndPagination(boolean applyDurationFilter) {

    // Step 1: Handle pagination buttons safely
    By nextButton = By.xpath("//button[@aria-label='Go to next page']");
    By prevButton = By.xpath("//button[@aria-label='Go to previous page']");

    if (util.isElementPresentAndEnabled(nextButton)) {
      util.scrollToElement(nextButton);
      util.waitAndClick(nextButton);
      util.waitForSeconds(1);
    }

    if (util.isElementPresentAndEnabled(prevButton)) {
      util.waitAndClick(prevButton);
      util.waitForSeconds(1);
    }

    // Step 2: Select records per page
    By dropdown = By.xpath("//*[@id='demo-simple-select-autowidth']");
    String[] recordsPerPage = {"25", "50", "100"};

    for (String value : recordsPerPage) {
      util.scrollToElement(dropdown);
      util.selectDropdownValue(dropdown, value);
      util.waitForSeconds(1);
    }

    util.waitForSeconds(3); // general wait after pagination

    // Step 3: Apply duration filters if applicable
    if (applyDurationFilter) {
      util.waitForSeconds(3);
      util.scrollToElement(By.xpath("//button[text()='Search']"));


      String[] durations = {"Today", "Last 1 week", "Last 1 month", "Last 3 month"};
      for (String duration : durations) {
        util.waitAndClick(By.xpath("//input[starts-with(@value, '') or @value]"));
        util.waitAndClick(By.xpath("//li[@value='" + duration + "']"));
        util.waitAndClick(By.xpath("//button[text()='Search']"));
        util.waitForSeconds(3);
      }

      // Custom duration selection
      util.waitAndClick(By.xpath("//input[@value='Last 3 month']"));
      util.waitAndClick(By.xpath("//li[@value='Custom']"));

      util.waitAndClick(By.xpath("//button[contains(@class, 'rdrNextPrevButton') and contains(@class, 'rdrNextButton')]"));
      util.waitAndClick(By.xpath("//button[contains(@class, 'rdrNextPrevButton') and contains(@class, 'rdrPprevButton')]"));

      util.waitAndClick(By.xpath("//span[@class='rdrMonthPicker']/select"));
      util.waitAndClick(By.xpath("//option[text()='July']"));

      util.waitAndClick(By.xpath("//span[@class='rdrYearPicker']/select"));
      util.waitAndClick(By.xpath("//option[text()='2024']"));

      util.waitAndClick(By.xpath("//span[text()='1']"));
      util.waitAndClick(By.xpath("(//span[text()='30'])[2]"));

      util.waitAndClick(By.xpath("//button[text()='Search']"));
      util.waitForSeconds(5);

      // Reset filter to 'All'
      util.waitAndClick(By.xpath("//button[@class='MuiButtonBase-root MuiIconButton-root MuiIconButton-sizeSmall css-kwu76i']"));
      util.waitAndClick(By.xpath("//li[@value='All']"));
      util.waitAndClick(By.xpath("//button[text()='Search']"));
      util.waitAndClick(By.xpath("//button[text()='Search']"));
    }
  }


  private String fetchAndStoreReconIdFromUI() throws InterruptedException {
    new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//tr[@role='row'])[2]/td[2]/div")));

    WebElement reconIdElement = driver.findElement(By.xpath("(//tr[@role='row'])[2]/td[2]/div"));
    String reconId = reconIdElement.getText();
    System.out.println("Fetched Recon ID: " + reconId);

    CredentialUtil.writeToCSV(rconIdsCsv, reconId);
    return reconId;

  }






//  @AfterMethod
//  public void tearDown() {
//    if (driver != null) {
//      driver.quit();
//    }
//  }
}

