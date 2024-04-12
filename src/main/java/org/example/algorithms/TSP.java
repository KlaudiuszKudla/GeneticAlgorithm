package org.example.algorithms;


import com.opencsv.CSVWriter;
import org.example.Individual;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Random;

public abstract class TSP {

    protected double[][] distanceMatrix;
    protected List<Individual> individuals;
    protected int size;
    public TSP(double[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.size = distanceMatrix.length;
    }

    protected void generateGreedySequenceOfCities(int popSize){
        if (individuals == null){
            individuals = new ArrayList<>();
        }
        for (int i = 0; i < popSize; i++) {
            Individual individual = new Individual(distanceMatrix);
            individual.generateGreedySequenceOfCities(i);
            individuals.add(individual);
        }
    }
    protected void generateRandomSequenceOfCities(int popSize){
        if (individuals == null){
            individuals = new ArrayList<>();
        }
        for (int i = 0; i < popSize; i++) {
            Individual individual = new Individual(distanceMatrix);
            individual.generateRandomSequenceOfCities();
            individuals.add(individual);
        }
    }

    protected Individual findBestIndividual(List<Individual> individuals){
        Individual bestResult = new Individual();
        int bestScore = Integer.MAX_VALUE;
        for (Individual individual: individuals){
            var score = individual.getCost();
            if (score < bestScore){
                bestScore = score;
                bestResult = individual;
            }
        }
        return bestResult;
    }
    protected Individual findWorstIndividual(List<Individual> individuals){
        Individual worstResult = new Individual();
        var worstScore = 0;
        for (Individual individual: individuals){
            var currentCost = individual.getCost();
            if (currentCost > worstScore){
                worstScore = currentCost;
                worstResult = individual;
            }
        }
        return worstResult;
    }

    protected long getAverageCost(List<Individual> individuals){
        long averageCost = 0;
        for (Individual individual: individuals){
            averageCost += individual.getCost();
        }
        return averageCost/individuals.size();
    }

    protected List<Individual> shuffleIndividuals(List<Individual> individuals, int sizeOfResults){
        Collections.shuffle(individuals);
        List<Individual> shuffledIndividuals = new ArrayList<>(sizeOfResults);
        for (int i = 0; i < sizeOfResults; i++) {
            shuffledIndividuals.add(individuals.get(i));
        }
        return shuffledIndividuals;
    }

    private List<Individual> swapIndividuals(int firstIndex, int secondIndex){
        Individual individual = individuals.get(firstIndex);
        individuals.set(firstIndex, individuals.get(secondIndex));
        individuals.set(secondIndex, individual);
        return individuals;
    }

    public void saveResults(List<Individual> individuals, String filetoSave){
        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter(filetoSave));
            String[] headers = {"Iteracja", "Best Result", "Worst Result", "Average Cost", "Standard Deviation"};
            csvWriter.writeNext(headers);
            var bestResult = Integer.MAX_VALUE;
            var worstResult = Integer.MIN_VALUE;
            var averageCost = 0;
            var std = 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public void setDistanceMatrix(double[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Individual> individuals) {
        this.individuals = individuals;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
