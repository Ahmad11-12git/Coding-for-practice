package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_17 {
    public static void main(String[] args) {
        // Take a user input and check if number is even or odd
        // logic building -Ready
        //1. Figure out the inputs
        //2.  How to take input in the java?

        //3. Do we need conversion or direclty
        //4. Rough logic - num%2 ==0 even, odd
        // 5. Optimize
        // int - size 32 bit -> range (2,147,483,647)

        Scanner sc  = new Scanner(System.in);
        System.out.println("Enter the value");
        int user_input = sc.nextInt();
        System.out.println(user_input);
        if (user_input%2 == 0) {
            System.out.println("Even");
        }else {
            System.out.println("Odd");
        }
    }
}
