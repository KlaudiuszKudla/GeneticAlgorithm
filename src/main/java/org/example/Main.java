package org.example;

import java.util.*;
import java.util.stream.Collectors;
class A{
    static void method(){
        System.out.println("A");
    }
}

class B extends A {
    protected static void method(){
        System.out.println("B");
    }
}
public class Main {

    public static int i = 3;
    static void display(int num) {
        System.out.println("Displaying integer: " + num);
    }

    // Ta sama nazwa metody, ale przyjmuje String jako argument
    static void display(String text) {
        System.out.println("Displaying string: " + text);
    }



    public static void main(String[] args) {

        A a = new B();
        a.method();

//        // Wywołanie metody statycznej z argumentem int
//        display(10);
//        // Wywołanie tej samej metody, ale z argumentem String
//        display("Hello");

//        for (int i = 0; i < 5; i++) {
//            System.out.println(i);
//
//        }
//        int x = 5;
//        if (x >= 5 ){
//            System.out.println("C");
//        } else if (x > 3) {
//            System.out.println("X");
//        }else {
//            System.out.println("L");
//        }

//        int i = 4;
//        for (int i = 0; i < 5; i++) {
//            System.out.println(i);
//        }

//
//            String a = "test";
//            String b = "test";
//            String c = new String("test");
//            String d = new String("test");
//
//            System.out.println(a == b);
//            System.out.println(a == c);
//            System.out.println(a == d);
//            System.out.println(c == d);
//            System.out.println(c.equals(d));
//            System.out.println(Objects.equals(a,b));


//            int x = 5;
//            switch (x){
//                case 5:
//                    System.out.println("L");
//                case 6:
//                    System.out.println("O");
//                default:
//                    System.out.println("L");
//            }

//        try{
//            int[] arr = {1,2,3};
//            System.out.println(arr[3]);
//        }catch (ArrayIndexOutOfBoundsException e){
//            System.out.println("Array index ..");
//        }finally {
//            System.out.println("some message.");
//        }
//        List<List<Integer>> lista = Arrays.asList(
//                Arrays.asList(1,2),
//                Arrays.asList(3,4)
//        );
//        List<Integer> numbers = lista.stream()
//                .flatMap(Collection::stream)
//                .map(i -> i + 2)
//                .collect(Collectors.toList());
//
//        System.out.println(numbers);

//            System.out.print("" + 1 + 2);
//            System.out.print("" + (1 + 2));

//            for (int j = 0; j < 6; j++) {
//                if (j / 2 == 0){
//                    continue;
//                }
//                if (j % 5 == 4){
//                    break;
//                }
//                System.out.print(j);
//            }
//        List<String> list = new ArrayList<String>(){{add("A");}};
//        int number = 2;
//        String str = "Ala";
//        foo(number,list,str);
//        System.out.println(number);
//        System.out.println(list);
//        System.out.println(str);
//
//        List<Integer> lista = Arrays.asList(5,4,3,1,50);
//        System.out.println(getOnlyBigs(lista));


        }
    static void foo(int number, List<String> list, String str) {
        number += 7;
        list.add("B");
        str += "ma kota";
    }

//    static boolean isBig(Integer n){return n > 30;}
//    static List<Integer> getOnlyBigs(List<Integer> nums){
//        return nums == null ? null: nums.stream().filter(u -> isBig(u)).collect(Collectors.toList());
    }
//    }

