package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_18 {
    public static void main(String[] args) {
        // Max number in two input
        Scanner sc = new Scanner(System.in);
        System.out.println("ENter the number 1");
        int num1 = sc.nextInt();
        System.out.println("Enter the number 2");
        int num2 = sc.nextInt();
        //System.out.println(Math.max(num1,num2));
        if (num1 > num2){
            System.out.println(num1);

        }else if (num2 < num1) {
            System.out.println(num2);
        }
        System.out.println("Both number is equal");
    }
}
