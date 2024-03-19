package org.example;


import java.util.*;

public class TSPV2 {

    private int[][] distanceMatrix;
    private List<Individual> individuals;
    private int size;
    public TSPV2(int[][] distanceMatrix) {
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

    public List<Integer> orderedCrossover(List<Integer> unit1, List<Integer> unit2){
        Random rand = new Random();
        var size = unit1.size();
        var firstcut = rand.nextInt(size);
        var secondCut = rand.nextInt(firstcut, size);
        Set<Integer> freeCities = new HashSet<>();
        freeCities.addAll(unit1);
        for (int i = firstcut; i <= secondCut; i++) {
            freeCities.remove(unit1.get(i));
        }
        var lastIndexOfUnit2 = 0;
        if (secondCut<99){
            for (int i = secondCut+1; i <size ; i++) {
                lastIndexOfUnit2 = i;
                var firstFreeCityIndex = findFirstFreeCity(freeCities, unit2, size, lastIndexOfUnit2);
                freeCities.remove(unit2.get(firstFreeCityIndex));
                unit1.set(i, unit2.get(firstFreeCityIndex));
                lastIndexOfUnit2 = firstFreeCityIndex;
            }
        }
        if (firstcut>0){
            for (int i = 0; i <firstcut ; i++) {
                lastIndexOfUnit2=i;
                var firstFreeCityIndex = findFirstFreeCity(freeCities, unit2, size, lastIndexOfUnit2);
                freeCities.remove(unit2.get(firstFreeCityIndex));
                unit1.set(i, unit2.get(firstFreeCityIndex));
                lastIndexOfUnit2 = firstFreeCityIndex;
            }
        }
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

    private List<Integer> swapMutation(List<Integer> sequenceOfCitites){
        var sizeOfList = sequenceOfCitites.size();
        var random = new Random();
        var firstIndex = random.nextInt(sizeOfList);
        var secondIndex = random.nextInt(sizeOfList);
        while(firstIndex == secondIndex){
            secondIndex = random.nextInt(sizeOfList);
        }
        var firstCity = sequenceOfCitites.get(firstIndex);
        sequenceOfCitites.set(firstIndex, sequenceOfCitites.get(secondIndex));
        sequenceOfCitites.set(secondIndex, firstCity);
        return sequenceOfCitites;
    }

    private List<Integer> inversionMutation(List<Integer> sequenceOfCities){
        var sizeOfList = sequenceOfCities.size();
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

    public List<List<Integer>> findTournamentWinners(List<Individual> individuals, int numberOfIndividualsInList, int tourSize) {
        List<List<Integer>> shuffledListsOfIndividuals = new ArrayList<>();
        Set<List<Integer>> bestResults = new HashSet<>();
        while(bestResults.size() < tourSize) {
            var shuffledIndividuals = shuffleIndividuals(individuals, numberOfIndividualsInList);
            var bestResult = findBest(shuffledIndividuals);
            bestResults.add(bestResult);
        }
        return new ArrayList<>(bestResults);
    }

    private List<Integer> findBest(List<List<Integer>> individuals){
        List<Integer> bestResult = new ArrayList<>();
        int bestScore = Integer.MAX_VALUE;
        for (List<Integer> individual: individuals){
            var score = calculateCost(individual);
            if (score < bestScore){
                bestScore = score;
                bestResult = individual;
            }
        }
        return bestResult;
    }

    private List<Individual> shuffleIndividuals(List<Individual> individuals, int size){
        Random rand = new Random();
        List<Individual> newIndividuals =  new ArrayList<>();
        for (int i = size-1; i >= 0 ; i++) {
            int j = rand.nextInt(i+1);
            swapIndividuals(i, j);
        }
        for (int i = 0; i < size; i++) {
            newIndividuals.add(individuals.get(i));
        }
        return newIndividuals;
    }

    private List<Individual> swapIndividuals(int firstIndex, int secondIndex){
        Individual individual = individuals.get(firstIndex);
        individuals.set(firstIndex, individuals.get(secondIndex));
        individuals.set(secondIndex, individual);
        return individuals;
    }

    private List<Individual> createPopulation


    public void geneticAlgorithm(int popSize, int generations, int crossProbability, int mutationProbability, int tourSize){
        generateGreedySequenceOfCities();
        List<Individual> population = shuffleIndividuals(this.individuals,popSize);
        for (int i = 0; i < generations; i++) {
            List<Individual> tournamentParticipants = shuffleIndividuals(population, tourSize);
            List<List<Integer>> newGeneration = new ArrayList<>();
            List<List<Integer>> tournamentWinners = findTournamentWinners(individuals, popSize / 10, tourSize);
            for (int j = 0; j < tournamentWinners.size(); j++) {
                List<Integer> unit1 = tournamentWinners.get(j);
                for (int k = j + 1; k < tournamentWinners.size(); k++) {
                    if (shouldCross(crossProbability)){
                        List<Integer>unit2 = tournamentWinners.get(k);
                        List<Integer> crossedUnit = orderedCrossover(unit1, unit2);
                        if (shouldMutate(mutationProbability)){
                            crossedUnit = swapMutation(crossedUnit);
                        }
                        newGeneration.add(crossedUnit);
                    }

                }
                if (shouldMutate(mutationProbability)){
                    unit1 = swapMutation(unit1);
                }
                newGeneration.add(unit1);
            }
            // TODO find best results in newGeneration and remowe worse in population and add better

        }
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








}
