package org.example;

public class Test {

    public static void main(String[] args) {
        RandomDataGenerator randomDataGenerator = new RandomDataGenerator(100, 100);
        randomDataGenerator.generateRandomValues();
        randomDataGenerator.saveToFile("src/main/resources/test3.csv");
        int[][] distanceMatrix = randomDataGenerator.fetchDistanceMatrix("src/main/resources/test3.csv");
        TSP tsp = new TSP(distanceMatrix);
        tsp.geneticAlgorithm(100, 1000, 70, 10, 5, "src/main/resources/results3.csv");
    }
}
