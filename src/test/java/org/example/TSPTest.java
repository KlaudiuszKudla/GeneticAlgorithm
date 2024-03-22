package org.example;

import java.util.List;

public class TSPTest {

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

}
