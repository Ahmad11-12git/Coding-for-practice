package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_23 {
    public static void main(String[] args) {
        //Leap Year Checker
        //Write a program that checks if a given year is a leap year.
        /*int days = 350;
        if (days == 365) {
            System.out.println(days + " This is not a leap year");
        }else if (days == 366) {
            System.out.println(days + " This is a leap year");
        }else {
            System.out.println("It's not a total days of year ");
        }*/
        Scanner sc = new Scanner(System.in);
        System.out.print(" Enter the Year :- ");
        int year = sc.nextInt();

        if (year%4 == 0 && year%100!=0 || year%400 ==0) {
            System.out.println(year + " is a Leap year");
        }else {
            System.out.println(year + " is not a leap year");
        }

    }
}
