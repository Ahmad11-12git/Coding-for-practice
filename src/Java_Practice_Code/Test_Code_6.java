package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_6 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1st Value");
        int sallery = sc.nextInt();
        System.out.println("Enter 2nd value");
        int month = sc.nextInt();

        int annualIncome =sallery*month;
        System.out.println(annualIncome);

        switch (annualIncome) {
            case 1:
                System.out.println("Number is 1");
                break;
            case 2:
                System.out.println("Number is 2");
                break;
            case 3:
                System.out.println("Number is 3");
                break;
            default:
                System.out.println("Number is not 1, 2, or 3");
                break;
        }

    }
}
