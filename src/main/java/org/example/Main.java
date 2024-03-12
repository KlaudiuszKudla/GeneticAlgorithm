package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator(10,100,"src/main/resources/test3.csv");
        randomDataGenerator.generateRandomValues();
        int[][] distanceMatrix = randomDataGenerator.fetchDistanceMatrix("src/main/resources/test3.csv");
        if (distanceMatrix != null) {
            for (int[] row : distanceMatrix) {
                for (int value : row) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }
        }
        TSP tsp = new TSP(distanceMatrix);
        List<Integer> randomSequence = tsp.generateRandomSequenceOfCities();
        System.out.println(randomSequence);
        var cost = tsp.calculateCost(randomSequence);
        System.out.println(cost);
        tsp.findBestResult(3000000,"src/main/resources/results3.csv", "greedy" );
//        tsp.greedyAlgorithm(100,"src/main/resources/results2.csv");
    }
}