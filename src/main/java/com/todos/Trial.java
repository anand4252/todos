package com.todos;

public class Trial {
    public static void main(String[] args) {
        System.out.println("Hello world!!");
        addTwoNumbers(3, 2);
    }

    private static void addTwoNumbers(int a, int b) {
        int sum = a - b;
        System.out.println("The sum of " + a + " and " + b + " is: " + sum);
    }
}
