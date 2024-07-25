package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_11 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number : ");
        String dayName = sc.next();
        int day;
        switch (dayName){
            case "Monday":
                day = 1;
                break;
            case "Tuesday":
                day = 2;
                break;
            case "Wednesday":
                day = 3;
                break;
            default:
                day= 0;
        }
        if (day == 0) {
            System.out.println("Invalid day name entered.");
        } else {
            System.out.println("The weekday " + dayName + " is day number " + day);
        }
    }
}
