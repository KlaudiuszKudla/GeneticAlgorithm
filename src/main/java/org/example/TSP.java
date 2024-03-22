package org.example;


import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TSP {

    private int[][] distanceMatrix;
    private List<Individual> individuals;
    private int size;
    public TSP(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.size = distanceMatrix.length;
    }

    private void generateGreedySequenceOfCities(){
        if (individuals == null){
            individuals = new ArrayList<>();
        }
        for (int i = 0; i < size; i++) {
            Individual individual = new Individual(distanceMatrix);
            individual.generateGreedySequenceOfCities(i);
            individuals.add(individual);
        }
    }
    private void generateRandomSequenceOfCities(){
        if (individuals == null){
            individuals = new ArrayList<>();
        }
        for (int i = 0; i < size; i++) {
            Individual individual = new Individual(distanceMatrix);
            individual.generateRandomSequenceOfCities();
            individuals.add(individual);
        }
    }

    public Individual orderedCrossover(Individual unit1, Individual unit2){
        Random rand = new Random();
        var sequenceOfCities = unit1.getSequenceOfCities();
        var sequenceOfCities2 = unit2.getSequenceOfCities();
        var size = sequenceOfCities.size();
        var firstcut = rand.nextInt(size);
        var secondCut = rand.nextInt(firstcut, size);
        Set<Integer> freeCities = new HashSet<>(sequenceOfCities);
        for (int i = firstcut; i <= secondCut; i++) {
            freeCities.remove(sequenceOfCities.get(i));
        }
        var lastIndexOfUnit2 = 0;
        if (secondCut<size-1){
            for (int i = secondCut+1; i <size ; i++) {
                lastIndexOfUnit2 = i;
                var firstFreeCityIndex = findFirstFreeCity(freeCities, sequenceOfCities2, size, lastIndexOfUnit2);
                var city = sequenceOfCities2.get(firstFreeCityIndex);
                freeCities.remove(city);
                sequenceOfCities.set(i, city);
                lastIndexOfUnit2 = firstFreeCityIndex;
            }
        }
        if (firstcut>0){
            for (int i = 0; i <firstcut ; i++) {
                lastIndexOfUnit2=i;
                var firstFreeCityIndex = findFirstFreeCity(freeCities, sequenceOfCities2, size, lastIndexOfUnit2);
                var city = sequenceOfCities2.get(firstFreeCityIndex);
                freeCities.remove(city);
                sequenceOfCities.set(i, city);
                lastIndexOfUnit2 = firstFreeCityIndex;
            }
        }
        unit1.setSequenceOfCities(sequenceOfCities);
        unit1.calculateCost();
        return unit1;
    }



    private Integer findFirstFreeCity(Set<Integer> freeCities, List<Integer>unit2, int size, int indexUnit2){
        Integer nextCityIndex = null;
        for (int i = indexUnit2; i <size ; i++) {
            var city = unit2.get(i);
            if (freeCities.contains(city)){
                nextCityIndex = i;
                break;
            }
        }
        if (nextCityIndex == null){
            for (int i = 0; i <indexUnit2 ; i++) {
                var city = unit2.get(i);
                if (freeCities.contains(city)){
                    nextCityIndex = i;
                    break;
                }
            }
        }
        return nextCityIndex;
    }



    private List<Integer> inversionMutation(Individual individual){
        var sequenceOfCities = individual.getSequenceOfCities();
        var sizeOfList = individual.getSize();
        var random = new Random();
        var firstIndex = random.nextInt(sizeOfList);
        var secondIndex = random.nextInt(sizeOfList);
        while(firstIndex == secondIndex){
            secondIndex = random.nextInt(sizeOfList);
        }
        List<Integer> subsequence = new ArrayList<>();
        for (int i = secondIndex; i >=firstIndex ; i--) {
            subsequence.add(sequenceOfCities.get(i));
        }
        for (int i = firstIndex; i <= secondIndex ; i++) {
            sequenceOfCities.set(i, subsequence.get(i - firstIndex));
        }
        return sequenceOfCities;
    }

    public List<Individual> findTournamentWinners(List<Individual> population, int times, int tourSize) {
        Set<Individual> bestResults = new HashSet<>();
        while(bestResults.size() < tourSize) {
            List<Individual> tournamentParticipants = shuffleIndividuals(population, times);
            var bestResult = findBestIndividual(tournamentParticipants);
            bestResults.add(bestResult);
        }
        return new ArrayList<>(bestResults);
    }

    private Individual findBestIndividual(List<Individual> individuals){
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
    private Individual findWorstIndividual(List<Individual> individuals){
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

    private Integer getAverageCost(List<Individual> individuals){
        var averageCost = 0;
        for (Individual individual: individuals){
            averageCost += individual.getCost();
        }
        return averageCost;
    }

    private List<Individual> shuffleIndividuals(List<Individual> individuals, int sizeOfResults){
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


    public void geneticAlgorithm(int popSize, int generations, int crossProbability, int mutationProbability, int tourSize, String fileToSave){
        //
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileToSave))){
            String[] headers = {"Iteracja", "Best Result", "Worst Result", "Average Cost"};
            writer.writeNext(headers);
            generateGreedySequenceOfCities();
            List<Individual> population = shuffleIndividuals(this.individuals,popSize);
            for (int i = 0; i < generations; i++) {
                List<Individual> tournamentWinners = findTournamentWinners(population,popSize/10, tourSize);
                crossTournamentWinners(tournamentWinners, crossProbability, population);
                mutatePopulation(population, mutationProbability);
                var currentBestIndividual = findBestIndividual(population);
                var worstResult = findWorstIndividual(population).getCost();
                var averageCost = getAverageCost(population);
                String[] row = {String.valueOf(i+1), String.valueOf(currentBestIndividual.getCost()), String.valueOf(worstResult), String.valueOf(averageCost/popSize)};
                writer.writeNext(row);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void crossTournamentWinners(List<Individual> tournamentWinners, int crossProbability, List<Individual> population){
        for (int j = 0; j < tournamentWinners.size() - 1; j += 2) {
            Individual parent1 = tournamentWinners.get(j);
            Individual parent2 = tournamentWinners.get(j + 1);
            if (shouldCross(crossProbability)) {
                Individual child1 = orderedCrossover(parent1, parent2);
                if (parent1.getCost() < parent2.getCost()){
                population.remove(parent2);
                }else{
                population.remove(parent1);
                }
                population.add(child1);
            }
        }
    }

    public void mutatePopulation(List<Individual> population, int mutationProbability){
        for (Individual individual: population){
            if (shouldMutate(mutationProbability)){
                inversionMutation(individual);
            }
        }
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

    public void localSearch(Individual individual){
        // 1. find neighbours and choose bestOne then do the same with best one - inversion, swap, insert????
        // 2. when finds localOptimum cant escape from this
        // 3. multiply start local serach????
    }

    public void simulatedAnnealingAlgorithm(Individual individual){

    }





    private boolean shouldCross(int crossProbability){
        Random rand = new Random();
        int randomValue = rand.nextInt(100);
        return randomValue < crossProbability;
    }

    private boolean shouldMutate(int mutationProbability) {
        Random rand = new Random();
        int randomValue = rand.nextInt(100);
        return randomValue < mutationProbability;
    }


    public int[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public void setDistanceMatrix(int[][] distanceMatrix) {
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
