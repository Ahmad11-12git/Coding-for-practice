package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_13 {
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter the month name: ");
            String month = sc.next();
            sc.close();

            int monthNumber;
            switch (month.toLowerCase()) {
                case "january":
                    monthNumber = 1;
                    break;
                case "february":
                    monthNumber = 2;
                    break;
                case "march":
                    monthNumber = 3;
                    break;
                case "april":
                    monthNumber = 4;
                    break;
                case "may":
                    monthNumber = 5;
                    break;
                case "june":
                    monthNumber = 6;
                    break;
                case "july":
                    monthNumber = 7;
                    break;
                case "august":
                    monthNumber = 8;
                    break;
                case "september":
                    monthNumber = 9;
                    break;
                case "october":
                    monthNumber = 10;
                    break;
                case "november":
                    monthNumber = 11;
                    break;
                case "december":
                    monthNumber = 12;
                    break;
                default:
                    monthNumber = 0; // Invalid month
            }

            if (monthNumber == 0) {
                System.out.println("Invalid month name entered.");
            } else {
                System.out.println("The month " + month + " is month number " + monthNumber);
            }
        }
    }

