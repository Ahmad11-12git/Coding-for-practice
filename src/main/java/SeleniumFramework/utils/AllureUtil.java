package SeleniumFramework.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class AllureUtil {

    public static void DelPreviousReport() throws IOException {
        try {
            String filePath = System.getProperty("user.dir") + "\\allure-results";
            File file = new File(filePath);
            FileUtils.deleteDirectory(file);
            System.out.println("Files deleted........");
        } catch (Exception e) {
            System.out.println("ERR: " + e.getMessage());
            throw e;
        }
    }

    public static void changeStepName(String name) {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        lifecycle.updateStep(testStep -> testStep.setName(name));
    }

    // Screenshot using Allure manual attachment (more reliable)
    public static void attachScreenshot(WebDriver driver, String message) {
        saveTextLog(message); // This attaches the message as plain text
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment("Screenshot  for  - " + message, "image/png", "png", screenshot);
        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }

    // Allure plain text attachment
    @Attachment(value = "{0}", type = "text/plain")
    public static String saveTextLog(String message) {
        System.out.println(message);
        return message;
    }
}