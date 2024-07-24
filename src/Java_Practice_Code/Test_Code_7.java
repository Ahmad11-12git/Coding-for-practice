package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_7 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter sallery");
        int sallery = sc.nextInt();
        System.out.println("Enter month");
        int month = sc.nextInt();

        int annualIncome =sallery*month;
        System.out.println(annualIncome);

        String result = switch (annualIncome) {
            case 1 -> "Number is 1";
            case 2 -> "Number is 2";
            case 3 -> "Number is 3";
            default -> "Number is not 1, 2, or 3";
        };

        System.out.println(result);


    }
}
