package org.example.generators;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.example.City;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataFromFile {

    private double distanceMatrix [][];
    private int numCities;

    public DataFromFile() {
    }

    public double[][] getDistanceMatrixByCords(String filePath){
        List<City> cities = readCitiesFromFile(filePath);
        double[][] costMatrix = new double[cities.size()][cities.size()];
        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.size(); j++) {
                costMatrix[i][j] = calculateDistance(cities.get(i), cities.get(j));
            }
        }
        return costMatrix;

    }

    private ArrayList<City> readCitiesFromFile(String filePath) {
        ArrayList<City> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean nodeCoordSectionStarted = false;
            while ((line = br.readLine()) != null) {
                if (line.contains("NODE_COORD_SECTION")) {
                    nodeCoordSectionStarted = true;
                    continue;
                }
                if (nodeCoordSectionStarted) {
                    String[] tokens = line.trim().split("\\s+");
                    if (tokens.length == 3) {
                        int id = Integer.parseInt(tokens[0]);
                        double x = Double.parseDouble(tokens[1]);
                        double y = Double.parseDouble(tokens[2]);
                        cities.add(new City(id, x, y));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return cities;
    }

    // Metoda obliczająca odległość euklidesową między dwoma miastami
    private double calculateDistance(City city1, City city2) {
        return Math.sqrt(Math.pow(city2.getX() - city1.getX(), 2) + Math.pow(city2.getY() - city1.getY(), 2));
    }

    public double[][] fetchDistanceMatrix(String fileName){
        try {
            Scanner scanner = new Scanner(new File(fileName));
            int dimension = findMatrixDimension(scanner);
            double[][] distanceMatrix = new double[dimension][dimension];

            // Przeskoczenie do sekcji z wagami krawędzi
            while (!scanner.nextLine().equals("EDGE_WEIGHT_SECTION"));

            // Wczytanie danych macierzy
            for (int i = 0; i < dimension; i++) {
                String line = scanner.nextLine();
                String[] values = line.trim().split("\\s+");

                for (int j = 0; j < dimension; j++) {
                    distanceMatrix[i][j] = Integer.parseInt(values[j]);
                }
            }
            scanner.close();
            return distanceMatrix;
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
    }

    private static int findMatrixDimension(Scanner scanner) {
        int dimension = 0;

        // Szukanie linii z informacją o wymiarze
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("DIMENSION:")) {
                dimension = Integer.parseInt(line.split(":")[1].trim());
                break;
            }
        }

        return dimension;
    }
}
