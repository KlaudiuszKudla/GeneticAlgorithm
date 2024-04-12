package org.example.algorithms;

import com.opencsv.CSVWriter;
import org.example.Individual;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RandomAlgorithm extends TSP{

    public RandomAlgorithm(double[][] distanceMatrix) {
        super(distanceMatrix);
    }

    public void randomAlgorithm(String fileToSave, int numOperations){
        try(CSVWriter csvWriter = new CSVWriter(new FileWriter(fileToSave))){
            String[] headers = {"Iteracja", "Current", "Best", "Worst", "Average"};
            csvWriter.writeNext(headers);
            int bestResult = Integer.MAX_VALUE;
            int worstResult = Integer.MIN_VALUE;
            long sum = 0;
            List<Integer> costs = new ArrayList<>();
            long average = 0;
            for (int i = 0; i < numOperations; i++) {
                Individual individual = new Individual(distanceMatrix);
                individual.generateRandomSequenceOfCities();
                var currentCost = individual.getCost();
                if (currentCost < bestResult) bestResult = currentCost;
                if (currentCost > worstResult) worstResult = currentCost;
                sum += currentCost;
                costs.add(currentCost);
                average+= currentCost;
                String[] row = {String.valueOf(i+1), String.valueOf(currentCost), String.valueOf(bestResult), String.valueOf(worstResult), String.valueOf(average/(i+1))};
                csvWriter.writeNext(row);
            }
            double standardDeviation = calculateStandardDeviation(costs);
            csvWriter.writeNext(new String[]{"OdchylenieStandardowe", String.valueOf(standardDeviation)});

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private double calculateStandardDeviation(List<Integer> data) {
        double mean = data.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
        double variance = data.stream().mapToDouble(num -> Math.pow(num - mean, 2)).average().orElse(0.0);
        return Math.sqrt(variance);
    }
}
