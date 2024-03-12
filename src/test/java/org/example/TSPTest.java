package org.example;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TSPTest {

    private static Stream<Arguments> provideSequencesAndExpectedCosts() {
        return generateAllCombinations(5)
                .map(combination -> Arguments.of(combination, calculateExpectedCost(combination)));
    }

    @ParameterizedTest
    @MethodSource("provideSequencesAndExpectedCosts")
    public void calculateCost_ShouldReturnCorrectCost(List<Integer> sequence, int expectedCost) {
        int[][] distanceMatrix = {
                {0, 3, 5, 7, 9},
                {3, 0, 8, 2, 4},
                {5, 8, 0, 1, 6},
                {7, 2, 1, 0, 10},
                {9, 4, 6, 10, 0}
        };
        TSP tsp = new TSP(distanceMatrix);
        int result = tsp.calculateCost(sequence);
        assertEquals(expectedCost, result);
    }

    @ParameterizedTest
    @MethodSource("provideSequencesAndExpectedCosts")
    public void generateRandomSequenceOfCities_ShouldGenerateValidSequence(List<Integer> sequence, int expectedCost) {
        int[][] distanceMatrix = {
                {0, 3, 5, 7, 9},
                {3, 0, 8, 2, 4},
                {5, 8, 0, 1, 6},
                {7, 2, 1, 0, 10},
                {9, 4, 6, 10, 0}
        };
        TSP tsp = new TSP(distanceMatrix);
        List<Integer> randomSequence = tsp.generateRandomSequenceOfCities();
        assertEquals(distanceMatrix.length, randomSequence.size());
        for (int cityIndex : randomSequence) {
            assertTrue(cityIndex >= 0 && cityIndex < distanceMatrix.length);
        }
    }

    private static Stream<List<Integer>> generateAllCombinations(int n) {
        List<Integer> elements = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            elements.add(i);
        }
        return permute(elements);
    }

    private static Stream<List<Integer>> permute(List<Integer> elements) {
        if (elements.size() == 1) {
            return Stream.of(elements);
        } else {
            return elements.stream().flatMap(e ->
                    permute(elements.stream().filter(el -> !el.equals(e)).toList())
                            .map(p -> {
                                List<Integer> permutation = new ArrayList<>();
                                permutation.add(e);
                                permutation.addAll(p);
                                return permutation;
                            })
            );
        }
    }

    private static int calculateExpectedCost(List<Integer> sequence) {
        int[][] distanceMatrix = {
                {0, 3, 5, 7, 9},
                {3, 0, 8, 2, 4},
                {5, 8, 0, 1, 6},
                {7, 2, 1, 0, 10},
                {9, 4, 6, 10, 0}
        };
        TSP tsp = new TSP(distanceMatrix);
        return tsp.calculateCost(sequence);
    }
}