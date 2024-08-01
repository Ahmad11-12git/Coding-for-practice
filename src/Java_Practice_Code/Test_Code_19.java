package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_19 {
    public static void main(String[] args) {

        // Grade calculatas
        /*
        Write a program that calcuates and displays
                the letter grade for a given numerical
                score (eg A, B, C, D or F)
                base on the following grade scale;
        A: 90-100
        B: 80-89
        C: 70-79
        D: 60-69
        F: 0:59
        */

        //1. Find the user input
        // Score - dat type
        // return -> grade - data - type - char A
        //
        // 2. Basic logic
        //if(score>=)
        //3. Write the code

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the Student Score");
        int score = sc.nextInt();

        char grade = 'F';
        if (score>= 90 && score<=100) {
            grade = 'A';
        } else if (score>=80 && score<=89) {
            grade ='B';
        } else if (score>=70 && score<=79) {
        grade ='C';
        }else if (score>=60 && score<=69) {
            grade = 'D';
        }else if (score>=50 && score<=59) {
            grade = 'B';
        }else if (score<=0 || score>=100) {
            System.out.println("He you are not a god");
        }else {
            grade = 'F';
        }
        System.out.println("Your grade is -> "+ grade);
    }

}
