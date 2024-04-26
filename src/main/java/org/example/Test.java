package org.example;

import org.example.algorithms.GeneticAlghoritm;
import org.example.algorithms.Greedy;
import org.example.algorithms.RandomAlgorithm;
import org.example.algorithms.SimmulatedAnnealing;
import org.example.generators.DataFromFile;

public class Test {

    public static void main(String[] args) {
//        RandomDataGenerator randomDataGenerator = new RandomDataGenerator(100, 1000);
//        randomDataGenerator.generateRandomValues();
//        randomDataGenerator.saveToFile("src/main/resources/test3.csv");
        DataFromFile dataFromFile = new DataFromFile();
//        double[][] distanceMatrix = dataFromFile.fetchDistanceMatrix("src/main/resources/bays29/bays29.tsp");
        double[][] distanceMatrix = dataFromFile.getDistanceMatrixByCords("src/main/resources/att48/att48.tsp");

        GeneticAlghoritm tsp = new GeneticAlghoritm(distanceMatrix);
//        tsp.geneticAlgorithmV2(120, 100, 70, 10, 13, "src/main/resources/att48/genetic/results1.csv");

//        Greedy greedy = new Greedy(distanceMatrix);
//        greedy.greedyAlgorithm("src/main/resources/att48/greedy/results.csv");
//
//        RandomAlgorithm randomAlgorithm = new RandomAlgorithm(distanceMatrix);
//        randomAlgorithm.randomAlgorithm("src/main/resources/att48/random/results.csv", 10_000);
//
        SimmulatedAnnealing ta = new SimmulatedAnnealing(distanceMatrix, 150_000, 0.99);
        ta.simulatedAnnealingAlgorithm("src/main/resources/att48/SA/results1.csv");




    }

}
