package SeleniumFramework.pages;

import SeleniumFramework.utils.ElementUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ResetPasswordPage {
    WebDriver driver;
    ElementUtil util;

    public ResetPasswordPage(WebDriver driver) {
        this.driver = driver;
        this.util = new ElementUtil(driver);
    }

    public void resetPassword(String oldPass, String newPass) {
        util.waitAndSendKeys(By.id("old_password"), oldPass);
        util.waitAndSendKeys(By.id("new_password"), newPass);
        util.waitAndSendKeys(By.id("confirm_new_password"), newPass);
        util.waitAndClick(By.xpath("//button[text()='Submit']"));
    }
}
