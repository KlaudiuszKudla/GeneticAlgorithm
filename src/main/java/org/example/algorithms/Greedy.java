package org.example.algorithms;

import com.opencsv.CSVWriter;
import org.example.Individual;

import java.io.FileWriter;
import java.io.IOException;

public class Greedy extends TSP {
    public Greedy(double[][] distanceMatrix) {
        super(distanceMatrix);
    }

    public void greedyAlgorithm(String fileToSave){
        try(CSVWriter csvWriter = new CSVWriter(new FileWriter(fileToSave))){
            String[] headers = {"Iteracja", "Current", "Best", "Worst", "Average"};
            csvWriter.writeNext(headers);
            int bestResult = Integer.MAX_VALUE;
            int worstResult = Integer.MIN_VALUE;
            long average = 0;
            for (int i = 0; i < distanceMatrix.length; i++) {
                Individual individual = new Individual(distanceMatrix);
                individual.generateGreedySequenceOfCities(i);
                var currentCost = individual.getCost();
                if (currentCost < bestResult) bestResult = currentCost;
                if (currentCost > worstResult) worstResult = currentCost;
                average+= currentCost;
                String[] row = {String.valueOf(i+1), String.valueOf(currentCost), String.valueOf(bestResult), String.valueOf(worstResult), String.valueOf(average/(i+1))};
                csvWriter.writeNext(row);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
