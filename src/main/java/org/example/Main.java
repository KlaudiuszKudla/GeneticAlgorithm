package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator(500,1000);
        randomDataGenerator.generateRandomValues();
        randomDataGenerator.saveToFile("src/main/resources/test2.csv");
        int[][] distanceMatrix = randomDataGenerator.fetchDistanceMatrix("src/main/resources/test2.csv");
        var individual = new Individual(distanceMatrix);
        individual.generateRandomSequenceOfCities();
        if (distanceMatrix != null) {
            for (int[] row : distanceMatrix) {
                for (int value : row) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }
        }
//        TSP tsp = new TSP(distanceMatrix);
//        List<Integer> randomSequence = tsp.generateRandomSequenceOfCities();
//        System.out.println(randomSequence);
//        var cost = tsp.calculateCost(randomSequence);
//        System.out.println(cost);
//        tsp.findBestResult(3000,"src/main/resources/results2.csv", "crossover" );
    }
}