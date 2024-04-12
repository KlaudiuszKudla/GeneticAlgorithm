package org.example.algorithms;

import com.opencsv.CSVWriter;
import org.example.Individual;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Random;

public class EA extends TSP {

    public EA(double[][] distanceMatrix) {
        super(distanceMatrix);
    }

    public void geneticAlgorithm(int popSize, int generations, int crossProbability, int mutationProbability, int tourSize, String fileToSave){
        //
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileToSave))){
            String[] headers = {"Iteracja", "Best Result", "Worst Result", "Average Cost"};
            writer.writeNext(headers);
//            for (int j = 0; j < 10; j++) {
                generateRandomSequenceOfCities(popSize);
                List<Individual> population = shuffleIndividuals(this.individuals,popSize);
                for (int i = 0; i < generations; i++) {
                    List<Individual> tournamentWinners = findTournamentWinners(population,14, tourSize);
                    crossTournamentWinners(tournamentWinners, crossProbability, population, mutationProbability);
//                mutatePopulation(population, mutationProbability);
//                }
                var currentBestIndividual = findBestIndividual(population);
                var worstResult = findWorstIndividual(population).getCost();
                var averageCost = getAverageCost(population);
                String[] row = {String.valueOf(i+1), String.valueOf(currentBestIndividual.getCost()), String.valueOf(worstResult), String.valueOf(averageCost)};
                writer.writeNext(row);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Individual> findTournamentWinners(List<Individual> population, int times, int tourSize) {
        Set<Individual> bestResults = new HashSet<>();
        while(bestResults.size() < times) {
            List<Individual> tournamentParticipants = shuffleIndividuals(population, tourSize);
            var bestResult = findBestIndividual(tournamentParticipants);
            bestResults.add(bestResult);
        }
        return new ArrayList<>(bestResults);
    }

    public void crossTournamentWinners(List<Individual> tournamentWinners, int crossProbability, List<Individual> population, int mutationProbability){
        for (int j = 0; j < tournamentWinners.size() - 1; j += 2) {
            Individual parent1 = tournamentWinners.get(j);
            Individual parent2 = tournamentWinners.get(j + 1);
            if (shouldCross(crossProbability)) {
                Individual child1 = orderedCrossover(parent1, parent2);
                Individual child2 = orderedCrossover(parent2,parent1);
                createNextGeneration(child1, parent1, parent2,population,mutationProbability);
                createNextGeneration(child2, parent1, parent2,population,mutationProbability);
            }
        }
    }

    private void createNextGeneration(Individual child, Individual parent1, Individual parent2, List<Individual> population, int mutationProbability){
        if (child.getCost() < parent1.getCost() || child.getCost() < parent2.getCost()) {
            if (parent1.getCost() < parent2.getCost()) {
                Individual worstResult = findWorstIndividual(population);
                population.remove(worstResult);
            } else {
                Individual worstResult = findWorstIndividual(population);
                population.remove(worstResult);
                population.remove(worstResult);
            }
            mutateIndividual(child, mutationProbability);
            population.add(child);
        }
    }

    public Individual orderedCrossover(Individual unit1, Individual unit2){
        java.util.Random rand = new Random();
        var sequenceOfCities = unit1.getSequenceOfCities();
        var sequenceOfCities2 = unit2.getSequenceOfCities();

        var newSequenceOfCities = new ArrayList<Integer>(sequenceOfCities);
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
                newSequenceOfCities.set(i, city);
                lastIndexOfUnit2 = firstFreeCityIndex;
            }
        }
        if (firstcut>0){
            for (int i = 0; i <firstcut ; i++) {
                lastIndexOfUnit2=i;
                var firstFreeCityIndex = findFirstFreeCity(freeCities, sequenceOfCities2, size, lastIndexOfUnit2);
                var city = sequenceOfCities2.get(firstFreeCityIndex);
                freeCities.remove(city);
                newSequenceOfCities.set(i, city);
                lastIndexOfUnit2 = firstFreeCityIndex;
            }
        }
        var newIdividual = new Individual(newSequenceOfCities, unit1.getDistanceMatrix());
        newIdividual.calculateCost();
        return newIdividual;
    }

    private Integer findFirstFreeCity(Set<Integer> freeCities, List<Integer> unit2, int size, int indexUnit2){
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



    public void mutatePopulation(List<Individual> population, int mutationProbability) {
        population.stream()
                .filter(individual -> shouldMutate(mutationProbability))
                .forEach(individual -> individual.swapMutationV2());
    }

    public void mutateIndividual(Individual individual, int mutationProbability){
        if (shouldMutate(mutationProbability)){
            individual.swapMutationV2();
        }
    }

    private boolean shouldCross(int crossProbability){
        java.util.Random rand = new java.util.Random();
        int randomValue = rand.nextInt(100);
        return randomValue < crossProbability;
    }

    private boolean shouldMutate(int mutationProbability) {
        java.util.Random rand = new Random();
        int randomValue = rand.nextInt(100);
        return randomValue < mutationProbability;
    }
}
