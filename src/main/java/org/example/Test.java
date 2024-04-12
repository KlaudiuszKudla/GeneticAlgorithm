package org.example;

import org.example.algorithms.EA;
import org.example.algorithms.Greedy;
import org.example.algorithms.RandomAlgorithm;
import org.example.algorithms.TA;
import org.example.generators.DataFromFile;

public class Test {

    public static void main(String[] args) {
//        RandomDataGenerator randomDataGenerator = new RandomDataGenerator(100, 1000);
//        randomDataGenerator.generateRandomValues();
//        randomDataGenerator.saveToFile("src/main/resources/test3.csv");
        DataFromFile dataFromFile = new DataFromFile();
//        double[][] distanceMatrix = dataFromFile.fetchDistanceMatrix("src/main/resources/bays29/bays29.tsp");
        double[][] distanceMatrix = dataFromFile.getDistanceMatrixByCords("src/main/resources/att48/att48.tsp");

//        EA tsp = new EA(distanceMatrix);
//        tsp.geneticAlgorithm(600, 50000, 70, 10, 16, "src/main/resources/att48/genetic/results2.csv");

//        Greedy greedy = new Greedy(distanceMatrix);
//        greedy.greedyAlgorithm("src/main/resources/u2319/greedy/results1.csv");

//        RandomAlgorithm randomAlgorithm = new RandomAlgorithm(distanceMatrix);
//        randomAlgorithm.randomAlgorithm("src/main/resources/p654/random/results1.csv", 10_000);

        TA ta = new TA(distanceMatrix, 0.99, 0.99);
        ta.simulatedAnnealingAlgorithm("src/main/resources/att48/TA/results1.csv");




    }

}
