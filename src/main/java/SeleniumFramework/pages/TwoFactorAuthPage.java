package SeleniumFramework.pages;

import SeleniumFramework.utils.ConfigReader;
import SeleniumFramework.utils.ElementUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TwoFactorAuthPage {
    WebDriver driver;
    ElementUtil util;

    public TwoFactorAuthPage(WebDriver driver) {
        this.driver = driver;
        this.util = new ElementUtil(driver);
    }

    public void enterOtp() {
        String otp = ConfigReader.get("otp");
        for (int i = 0; i < otp.length(); i++) {
            String digit = String.valueOf(otp.charAt(i));
            util.waitAndSendKeys(By.id("otp_" + i), digit);
        }
    }
}
