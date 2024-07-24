package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter sallery");
        int sallery = sc.nextInt();
        System.out.println("Enter month");
        int month = sc.nextInt();

        int annualIncome =sallery*month;
        System.out.println(annualIncome);

        if (annualIncome < 300000) {
            System.out.println("No Tax");
        } else if (annualIncome > 300000 && annualIncome <= 700000) {
            float annualTax1 = annualIncome*0.05f;
            System.out.println("Annual tax 5% ==" +annualTax1);
        } else if (annualIncome >= 700000 && annualIncome <= 1000000) {
            float annualTax2 = annualIncome*0.10f;
            System.out.println("Annual tax 10% ==" +annualTax2);
        } else if (annualIncome >= 1000000 && annualIncome <= 1200000) {
            float annualTax3 = annualIncome*0.15f;
            System.out.println("Annual Tax 15% ==" +annualTax3);
        } else if (annualIncome >= 1200000 && annualIncome <= 1500000) {
            float annualTax4 = annualIncome*0.20f;
            System.out.println("Annual Tax 20% ==" +annualTax4);
        } else if (annualIncome > 1500000) {
            float annualTax5 = annualIncome*0.30f;
            System.out.println("Annual Tax 30% ==" +annualTax5);
        }


    }
}
