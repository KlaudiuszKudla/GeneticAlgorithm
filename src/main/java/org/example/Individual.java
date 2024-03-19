package org.example;

import org.example.interfaces.IIndividual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Individual implements IIndividual {
    private List<Integer> sequenceOfCities;
    private int cost;
    private int[][] distanceMatrix;
    private int size;

    public void generateRandomSequenceOfCities(){
        if (sequenceOfCities == null) {
            sequenceOfCities = new ArrayList<>(size);
        }
        for (int i = 0; i < distanceMatrix.length; i++) {
            sequenceOfCities.add(i);
        }
        Collections.shuffle(sequenceOfCities);
    }

    public void generateGreedySequenceOfCities(int firstCity){
        var listOfFreeCities = new ArrayList<Integer>();
        if (sequenceOfCities == null){
            sequenceOfCities = new ArrayList<>(size);
        }
        for (int j = 0; j < size; j++) {
            listOfFreeCities.add(j);
        }
        listOfFreeCities.remove(firstCity);
        sequenceOfCities.add(firstCity);
        while(!listOfFreeCities.isEmpty()){
            var lastCity = sequenceOfCities.get(sequenceOfCities.size()-1);
            var bestNeighbourr = findNeighbourIndexWithLowestCost(lastCity, listOfFreeCities);
                sequenceOfCities.add(bestNeighbourr);
                listOfFreeCities.remove(bestNeighbourr);
            }
            sequenceOfCities.clear();
            listOfFreeCities.clear();
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

    public void calculateCost(){
        int totalCost = 0;
        for (int i = 0; i < sequenceOfCities.size() - 1; i++) {
            int fromCity = sequenceOfCities.get(i);
            int toCity = sequenceOfCities.get(i+1);
            totalCost += distanceMatrix[fromCity][toCity];
            System.out.println(i + ". = " + totalCost);
        }
        int firstCity = sequenceOfCities.get(0);
        int lastCity = sequenceOfCities.get(sequenceOfCities.size() - 1);
        totalCost += distanceMatrix[lastCity][firstCity];
        this.cost = totalCost;
    }

    public Individual(List<Integer> sequenceOfCities, int cost, int[][] distanceMatrix) {
        this.sequenceOfCities = sequenceOfCities;
        this.cost = cost;
        this.distanceMatrix = distanceMatrix;
        this.size = distanceMatrix.length;
    }

    public Individual(int cost) {
        this.cost = cost;
    }


    public Individual(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.size = distanceMatrix.length;
    }

    public Individual(){};

    public List<Integer> getSequenceOfCities() {
        return sequenceOfCities;
    }

    public void setSequenceOfCities(List<Integer> sequenceOfCities) {
        this.sequenceOfCities = sequenceOfCities;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
