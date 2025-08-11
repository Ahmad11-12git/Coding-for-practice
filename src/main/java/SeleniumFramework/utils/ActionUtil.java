package SeleniumFramework.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ActionUtil {

    private WebDriver driver;
    private Actions actions;

    public ActionUtil(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
    }

    // Generic drag and drop
    public void dragAndDrop(WebElement source, WebElement target) {
        try {
            actions.clickAndHold(source)
                    .pause(1000)
                    .moveToElement(target)
                    .pause(1000)
                    .release()
                    .build()
                    .perform();
            System.out.println("   Drag-and-drop successful.");
        } catch (Exception e) {
            System.out.println("    Drag-and-drop failed: " + e.getMessage());
        }
    }

    // Hover over element
    public void hoverOverElement(WebElement element) {
        actions.moveToElement(element).perform();
    }

    // Click using Actions
    public void click(WebElement element) {
        actions.moveToElement(element).click().perform();
    }

    // Double-click
    public void doubleClick(WebElement element) {
        actions.doubleClick(element).perform();
    }

    // Right-click (context click)
    public void rightClick(WebElement element) {
        actions.contextClick(element).perform();
    }

    // Click and hold (for custom uses)
    public void clickAndHold(WebElement element) {
        actions.clickAndHold(element).perform();
    }

    // Release
    public void release(WebElement element) {
        actions.release(element).perform();
    }

    public void jsDragAndDrop(WebElement source, WebElement target) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String script = "function createEvent(typeOfEvent) {\n" +
                "    var event = document.createEvent(\"CustomEvent\");\n" +
                "    event.initCustomEvent(typeOfEvent, true, true, null);\n" +
                "    event.dataTransfer = {\n" +
                "        data: {},\n" +
                "        setData: function(key, value) {\n" +
                "            this.data[key] = value;\n" +
                "        },\n" +
                "        getData: function(key) {\n" +
                "            return this.data[key];\n" +
                "        }\n" +
                "    };\n" +
                "    return event;\n" +
                "}\n" +
                "function dispatchEvent(element, event, transferData) {\n" +
                "    if (transferData !== undefined) {\n" +
                "        event.dataTransfer = transferData;\n" +
                "    }\n" +
                "    if (element.dispatchEvent) {\n" +
                "        element.dispatchEvent(event);\n" +
                "    } else if (element.fireEvent) {\n" +
                "        element.fireEvent(\"on\" + event.type, event);\n" +
                "    }\n" +
                "}\n" +
                "function simulateHTML5DragAndDrop(source, destination) {\n" +
                "    var dragStartEvent = createEvent('dragstart');\n" +
                "    dispatchEvent(source, dragStartEvent);\n" +
                "    var dropEvent = createEvent('drop');\n" +
                "    dispatchEvent(destination, dropEvent, dragStartEvent.dataTransfer);\n" +
                "    var dragEndEvent = createEvent('dragend');\n" +
                "    dispatchEvent(source, dragEndEvent, dropEvent.dataTransfer);\n" +
                "}\n" +
                "simulateHTML5DragAndDrop(arguments[0], arguments[1]);";

        js.executeScript(script, source, target);
    }


}
