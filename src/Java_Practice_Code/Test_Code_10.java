package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_10 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter yor Name");
        String Name = sc.nextLine();
        System.out.println("Enter Yor Salary");
        int salary = sc.nextInt();
        System.out.println("Enter yor Edge");
        int edge = sc.nextInt();

        if (salary <= 300000)
        {
            System.out.println("Hi "+ Name + " Yor are not eligible for the tax payer");
        }else {
            System.out.println("Hi "+ Name + " You are eligible for the tax paying");
        }
        System.out.println("Hi " + Name + " Yor edge is " + edge );
    }
}
