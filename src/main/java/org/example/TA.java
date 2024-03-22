package org.example;

public class TA {

    private Individual individual;
    private int[][] distanceMatrix;
    private int size;
    private int cost;

    public TA(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.size = distanceMatrix.length;
        Individual individual1 = new Individual(distanceMatrix);
        individual1.generateRandomSequenceOfCities();
        this.individual = individual1;
        this.cost = individual1.getCost();

    }

    public void localSearch(){
        // 1. find neighbours and choose bestOne then do the same with best one - inversion, swap, insert????
        Individual neighbour = this.individual.swapMutation();
        var neighbourCost = neighbour.getCost();
        var individualCost = individual.getCost();
        if (neighbourCost < individualCost){
            individual = neighbour;
        }else if (neighbourCost > individualCost){

        }

        // 2. when finds localOptimum cant escape from this
        // 3. multiply start local serach????

    }

    public boolean acceptWorseResult(int neighbourCost, int individualCost){
        var difference = individualCost - neighbourCost;

    }

    public void simulatedAnnealingAlgorithm(Individual individual){

    }

    public Individual getIndividual() {
        return individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    public int[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public void setDistanceMatrix(int[][] distanceMatrix) {
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
