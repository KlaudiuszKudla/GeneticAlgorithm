package org.example;


import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TSP {

    int[][] distanceMatrix;
    public TSP(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }

//    public List<Integer> bruteForce(){
//        for (int i = 0; i < distanceMatrix.length; i++) {
//            for (int j = 0; j < distanceMatrix.length; j++) {
//
//            }
//        }
//    }

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
                        sequence = greedyAlgorithm();
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

    public List<Integer> greedyAlgorithm(){
            var sequenceOfNeighbours = new ArrayList<Integer>();
            var listOfFreeCities = new ArrayList<Integer>();
            var random = new Random();
            for (int j = 0; j < distanceMatrix.length; j++) {
                listOfFreeCities.add(j);
            }
            int firstCity = random.nextInt(listOfFreeCities.size());
            listOfFreeCities.remove(firstCity);
            sequenceOfNeighbours.add(firstCity);
//          sequenceOfNeighbours.add(findNeighbourWithLowestCost(firstCity,listOfFreeCities));
            while(!listOfFreeCities.isEmpty()){
                var bestNeighbourr = findNeighbourWithLowestCost(sequenceOfNeighbours.get(sequenceOfNeighbours.size()-1), listOfFreeCities);
                System.out.println("best neighbour: " + sequenceOfNeighbours.get(sequenceOfNeighbours.size()-1) + "neighbour" + bestNeighbourr);
                sequenceOfNeighbours.add(bestNeighbourr);
                listOfFreeCities.remove(bestNeighbourr);
            }
            return sequenceOfNeighbours;
    }

    private Integer findNeighbourWithLowestCost(int cityIndex, List<Integer> freeCities){
        var lowestCost = Integer.MAX_VALUE;
        var neighbourIndexWithLowestCost = 0;
        for (int i = 0; i < freeCities.size(); i++) {
            var neighbourIndex = freeCities.get(i);
            var currentCost = distanceMatrix[cityIndex][neighbourIndex];
            if (currentCost<lowestCost){
                lowestCost = currentCost;
                neighbourIndexWithLowestCost = neighbourIndex;
            }
        }
        return neighbourIndexWithLowestCost;
    }



}
