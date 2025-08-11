package SeleniumFramework.utils;

import java.awt.Robot;
import java.awt.event.KeyEvent;

public class RobotUtil {

    // Common reusable method for pressing DOWN and ENTER
    public static void pressDownAndEnter(Robot robot, int delayInMillis) throws InterruptedException {
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        Thread.sleep(delayInMillis);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    // Optional default delay version
    public static void pressDownAndEnter(Robot robot) throws InterruptedException {
        pressDownAndEnter(robot, 5000);  // Default 2 seconds
    }


}
