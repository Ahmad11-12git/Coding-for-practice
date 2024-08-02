package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_22 {
    public static void main(String[] args) {
            //Positive, Negative, or Zero
        //Write a program that determines if a number is positive, negative, or zero.
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number negative or positive ");
        int number = sc.nextInt();

        if (number > 0) {
            System.out.println(number + " Is positive number");
        } else if (number<0) {
            System.out.println(number + " Is negative number");
        }else {
            System.out.println("This number is zero");
        }

    }
}
