package org.example;


import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TSPV2 {

    int[][] distanceMatrix;
    public TSPV2(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

    public List<Integer> generateRandomSequenceOfCities(){
        var randomSequence = new ArrayList<Integer>();
        for (int i = 0; i < distanceMatrix.length; i++) {
            randomSequence.add(i);
        }
        Collections.shuffle(randomSequence);
        return randomSequence;
    }

    public Integer calculateCost(List<Integer> citySequence){
        int totalCost = 0;
        for (int i = 0; i < citySequence.size() - 1; i++) {
            int fromCity = citySequence.get(i);
            int toCity = citySequence.get(i+1);
            totalCost += distanceMatrix[fromCity][toCity];
            System.out.println(i + ". = " + totalCost);
        }
        int firstCity = citySequence.get(0);
        int lastCity = citySequence.get(citySequence.size() - 1);
        totalCost += distanceMatrix[lastCity][firstCity];
        return totalCost;
    }

    public void findBestResult(int numOperations, String fileToSave, String algorithmType){
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(fileToSave));
            String[] headers = {"Iteracja", "Best Result", "Worst Result", "Average Cost", "Standard Deviation"};
            writer.writeNext(headers);

            var bestResult = Integer.MAX_VALUE;
            var worstResult = Integer.MIN_VALUE;
            var averageCost = 0;
            var std = 0;
            for (int i = 0; i < numOperations; i++) {
                List<Integer>sequence = new ArrayList<>();
                switch (algorithmType){
                    case "random":
                        sequence = generateRandomSequenceOfCities();
                        break;
                    case "greedy":
                        sequence = generateGreedySequenceOfCities();
                        break;
                    case "crossover":
                        var sequence1 = generateGreedySequenceOfCities();
                        var sequence2 = generateGreedySequenceOfCities();
                        sequence = orderedCrossover(sequence1, sequence2);
                        break;
                }
                var currentCost = calculateCost(sequence);
                averageCost += currentCost;
                if (currentCost<bestResult) {
                    bestResult = currentCost;
                } else if (currentCost>worstResult) {
                    worstResult = currentCost;
                }
                String[] row = {String.valueOf(i + 1), String.valueOf(bestResult),
                        String.valueOf(worstResult), String.valueOf(averageCost / (i + 1)),
                        String.valueOf(std)};
                writer.writeNext(row);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<List<Integer>> generateGreedySequenceOfCities(int size){
        var listOfSequences = new ArrayList<Integer>();
        var sequenceOfCities = new ArrayList<Integer>();
        var listOfFreeCities = new ArrayList<Integer>();
        var firstCity = 0;
        for (int i = 0; i < size; i++) {
            firstCity = i;
            for (int j = 0; j < size; j++) {
                listOfFreeCities.add(j);
            }
            listOfFreeCities.remove(firstCity);
            sequenceOfCities.add(firstCity);
//          sequenceOfCities.add(findNeighbourWithLowestCost(firstCity,listOfFreeCities));
            while(!listOfFreeCities.isEmpty()){
                var bestNeighbourr = findNeighbourIndexWithLowestCost(sequenceOfCities.get(sequenceOfCities.size()-1), listOfFreeCities);
                System.out.println("best neighbour: " + sequenceOfCities.get(sequenceOfCities.size()-1) + "neighbour" + bestNeighbourr);
                sequenceOfCities.add(bestNeighbourr);
                listOfFreeCities.remove(bestNeighbourr);
            }
            listOfSequences.add(sequenceOfCities);
            sequenceOfCities.clear();
            listOfFreeCities.clear();
        }
        return costOfSequences;
    }

    private Integer findNeighbourIndexWithLowestCost(int cityIndex, List<Integer> freeCities){
        var lowestCost = Integer.MAX_VALUE;
        var neighbourIndexWithLowestCost = 0;
        for (int neighbourIndex: freeCities) {
            var currentCost = distanceMatrix[cityIndex][neighbourIndex];
            if (currentCost<lowestCost){
                lowestCost = currentCost;
                neighbourIndexWithLowestCost = neighbourIndex;
            }
        }
        return neighbourIndexWithLowestCost;
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

    public List<List<Integer>> findTournamentWinners(List<List<Integer>> individuals, int numberOfIndividualsInList, int tourSize) {
        List<List<Integer>> shuffledListsOfIndividuals = new ArrayList<>();
        Set<List<Integer>> bestResults = new HashSet<>();
        while(bestResults.size() < tourSize) {
            var shuffledIndividuals = shuffleList(individuals, numberOfIndividualsInList);
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

    private List<List<Integer>> shuffleList(List<List<Integer>> individuals, int quantity){
        List<List<Integer>> randomIndividuals = new ArrayList<>(quantity);
        Collections.shuffle(individuals);
        for (int i = 0; i < quantity; i++) {
            randomIndividuals.add(individuals.get(i));
        }
        return randomIndividuals;
    }

    public void geneticAlgorithm(int popSize, int generations, int crossProbability, int mutationProbability, int tourSize){
        List<List<Integer>> individuals = generateGreedySequenceOfCities(popSize);
        for (int i = 0; i < generations; i++) {
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
