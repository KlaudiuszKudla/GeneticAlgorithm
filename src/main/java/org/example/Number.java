package org.example;

public class Number {
    private static int number;
    public int getNumber(){
        return number;
    }

    public static void main(String[] args) {
        Number number1 = new Number();
        System.out.println(number1.getNumber());
        char[] chars = {'A', '2', '!'};
        System.out.println(chars[chars.length]);
        System.out.println(chars[chars.length]);
    }
}
