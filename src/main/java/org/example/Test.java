package org.example;

import java.util.List;

public class Test {

    public static void main(String[] args) {
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator(50,100,"src/main/resources/test3.csv");
        randomDataGenerator.generateRandomValues();
        int[][] distanceMatrix = randomDataGenerator.fetchDistanceMatrix("src/main/resources/test3.csv");
        TSP tsp = new TSP(distanceMatrix);
        List<Integer> randomSequence = tsp.generateRandomSequenceOfCities();
        System.out.println(randomSequence);
        List<Integer> randomSequence2 = tsp.generateRandomSequenceOfCities();
        System.out.println(randomSequence2);
        List<Integer> crossed = tsp.orderedCrossover(randomSequence, randomSequence2);
        System.out.println(crossed);

    }
}
