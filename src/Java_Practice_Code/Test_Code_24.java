package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_24 {
    public static void main(String[] args) {
        //Largest of Three Numbers
        //Write a program that finds the largest of three numbers using nested if-else statements.
        Scanner sc =new Scanner(System.in);

        System.out.println("Enter the 1st value :- ");
        int number1 = sc.nextInt();

        System.out.println("Enter the 2nd value :- ");
        int number2 = sc.nextInt();

        System.out.println(" Enter the 3rd value");
        int number3 = sc.nextInt();

        int largest;

        if (number1 >= number2 && number1 >= number3 ) {
            largest = number1;
        } else if (number2 >= number3 && number2 >= number1) {
            largest = number2;
        }else {
            largest = number3;
        }
        System.out.println("The largest number is "+ largest);
    }
}
