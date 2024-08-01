package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_21 {
    public static void main(String[] args) {
            // if else - codition (if , elseif, else)-multiple condition
        // Switch - Better way to 2+ condition
        //which day it is -day to format 1 to 7
        // 3 -> Wed
        Scanner sc = new Scanner(System.in);
        System.out.println(" Enter the day from 1 to 7, Tell waht day it it's");
        int day = sc.nextInt();

        switch (day){
            case 1 :
                System.out.println("Mon");
                break;
            case 2 :
                System.out.println("Tue");
                break;
            case 3 :
                System.out.println("Wed");
                break;
            case 4 :
                System.out.println("Thu");
                break;
            case 5 :
                System.out.println("Fri");
                break;
            case 6 :
                System.out.println("Set");
                break;
            case 7 :
                System.out.println("Sun");
                break;
            default:
                System.out.println("No idea what is a day");
        }
    }
}
