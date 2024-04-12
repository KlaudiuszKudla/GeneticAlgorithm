package org.example.algorithms;

import com.opencsv.CSVWriter;
import org.example.Individual;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TA {

    private Individual individual;
    private double[][] distanceMatrix;
    private int size;
    private int cost;
    private double temperature;
    private double coolingRate;

    public TA(double[][] distanceMatrix, double temperature, double coolingRate) {
        this.distanceMatrix = distanceMatrix;
        this.size = distanceMatrix.length;
        Individual individual1 = new Individual(distanceMatrix);
        individual1.generateRandomSequenceOfCities();
        this.individual = individual1;
        this.cost = individual1.getCost();
        this.temperature = temperature;
        this.coolingRate = coolingRate;

    }

//    public void simulatedAnnealingAlgorithm(String fileToSave){
//        var iteration = 0;
//        // 1. find neighbours and choose bestOne then do the same with best one - inversion, swap, insert????
//        try (CSVWriter writer = new CSVWriter(new FileWriter(fileToSave))){
////            String[] headers = {"Iteracja", "CurrentCost","Best Result", "Worst Result", "Average Cost"};
//            String[] headers = {"Iteracja","Best Result", "Worst Result", "Average Cost"};
//            writer.writeNext(headers);
//            for (int j = 0; j < 10; j++) {
//                int bestResult = Integer.MAX_VALUE;
//                int worstResult = Integer.MIN_VALUE;
//                long sum = 0;
//                List<Integer> costs = new ArrayList<>();
//                long average = 0;
//                for (int i = 0; i < 10_000; i++) {
//                    Individual neighbour = this.individual.inversionMutation();
//                    var neighbourCost = neighbour.getCost();
//                    var individualCost = individual.getCost();
//                    if (neighbourCost < individualCost || acceptWorseResult(neighbourCost, individualCost)) {
//                        individual = neighbour;
//                    }
//                    temperature *= coolingRate;
//                    var currentCost = individual.getCost();
//                    if (currentCost < bestResult) bestResult = currentCost;
//                    if (currentCost > worstResult) worstResult = currentCost;
//                    sum += currentCost;
//                    costs.add(currentCost);
//                    average += currentCost;
//                }
////                String[] row = {String.valueOf(i+1), String.valueOf(currentCost), String.valueOf(bestResult), String.valueOf(worstResult), String.valueOf(average/(i+1))};
//                    String[] row = {String.valueOf(j+1), String.valueOf(bestResult), String.valueOf(worstResult), String.valueOf(average/(j+1))};
//                    writer.writeNext(row);
//                    iteration++;
//            }
//
////            }
////            double standardDeviation = calculateStandardDeviation(costs);
////            writer.writeNext(new String[]{"OdchylenieStandardowe", String.valueOf(standardDeviation)});
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public void simulatedAnnealingAlgorithm(String fileToSave) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileToSave))) {
            String[] headers = {"Iteracja", "Best Result", "Worst Result", "Average Cost"};
            writer.writeNext(headers);
            int bestResult = Integer.MAX_VALUE;
            int worstResult = Integer.MIN_VALUE;

            for (int j = 0; j < 10; j++) {
                long sum = 0;
                List<Integer> costs = new ArrayList<>();
                for (int i = 0; i < 10_000; i++) {
                    Individual neighbour = this.individual.inversionMutation();
                    var neighbourCost = neighbour.getCost();
                    var individualCost = individual.getCost();
                    if (neighbourCost < individualCost || acceptWorseResult(neighbourCost, individualCost)) {
                        individual = neighbour;
                    }
                    temperature *= coolingRate;
                    var currentCost = individual.getCost();
                    if (currentCost < bestResult) bestResult = currentCost;
                    if (currentCost > worstResult) worstResult = currentCost;
                    sum += currentCost;
                    costs.add(currentCost);
                }
                // Obliczenie średniej z kosztów bieżącej iteracji
                long average = sum / 10_000;
                String[] row = {String.valueOf(j + 1), String.valueOf(bestResult), String.valueOf(worstResult), String.valueOf(average)};
                writer.writeNext(row);

                // Resetowanie zmiennych przed kolejną iteracją
                bestResult = Integer.MAX_VALUE;
                worstResult = Integer.MIN_VALUE;
                individual.generateRandomSequenceOfCities();
                this.cost = individual.getCost();
                temperature = 0.99;
                coolingRate = 0.99;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean acceptWorseResult(int neighbourCost, int individualCost) {
        double probability = Math.exp((individualCost - neighbourCost) / temperature);
        return Math.random() < probability;
    }


    private double calculateStandardDeviation(List<Integer> data) {
        double mean = data.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
        double variance = data.stream().mapToDouble(num -> Math.pow(num - mean, 2)).average().orElse(0.0);
        return Math.sqrt(variance);
    }


    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public void setDistanceMatrix(double[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
