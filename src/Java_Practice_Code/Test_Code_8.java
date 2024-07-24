package Java_Practice_Code;

import java.util.Scanner;

public class Test_Code_8 {
    public static void main(String[] args) {

            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter first number: ");
            double num1 = scanner.nextDouble();

            System.out.println("Enter second number: ");
            double num2 = scanner.nextDouble();

            System.out.println("Enter operation (+, -, *, /): ");
            char operation = scanner.next().charAt(0);

            switch (operation) {
                case '+':
                    System.out.println("Result: " + (num1 + num2));
                    break;
                case '-':
                    System.out.println("Result: " + (num1 - num2));
                    break;
                case '*':
                    System.out.println("Result: " + (num1 * num2));
                    break;
                case '/':
                    if (num2 != 0) {
                        System.out.println("Result: " + (num1 / num2));
                    } else {
                        System.out.println("Cannot divide by zero!");
                    }
                    break;
                default:
                    System.out.println("Invalid operation!");
                    break;
            }

            scanner.close();
        }
    }



