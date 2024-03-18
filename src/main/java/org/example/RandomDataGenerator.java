package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomDataGenerator {


    private int[][] distanceMatrix;
    private int numCities;
    private int maxDistance;


    public RandomDataGenerator(int numCities, int maxDistance) {
        this.numCities = numCities;
        this.maxDistance = maxDistance;
    }

    public void generateRandomValues(){
        int[][] distanceMatrix = new int[numCities][numCities];
        Random random = new Random();

        for (int i = 0; i < numCities; i++) {
            for (int j = i+1; j < numCities; j++) {
                var rand = random.nextInt(maxDistance) + 1;
                distanceMatrix[i][j] = rand;
                distanceMatrix[j][i] = rand;
            }
        }
        this.distanceMatrix = distanceMatrix;
    }

    public void saveToFile(String fileToSave){
        try(CSVWriter writer = new CSVWriter(new FileWriter(fileToSave))){
            for (int i = 0; i < numCities; i++) {
                String[] line = new String[numCities];
                for (int j = 0; j < numCities; j++) {
                    line[j] = String.valueOf(this.distanceMatrix[i][j]);
                }
                writer.writeNext(line);
            }
        }catch (IOException e ){
            e.printStackTrace();
        }
    }

    public int[][] fetchDistanceMatrix(String fileName){
        try {
            CSVReader reader = new CSVReader(new FileReader(fileName));
            String[] nextNumber;
            int numColumns = 0;

            while((nextNumber = reader.readNext()) != null){
                numColumns++;
            }
            int[][] distanceMatrix = new int[numColumns][numColumns];
            int rowIndex = 0;
            reader.close();
            reader = new CSVReader(new FileReader(fileName));
            while((nextNumber = reader.readNext()) != null){
                for(int columnIndex = 0; columnIndex< numColumns; columnIndex++){
                    distanceMatrix[rowIndex][columnIndex] = Integer.parseInt(nextNumber[columnIndex]);
                }
                rowIndex++;
            }
            return distanceMatrix;

        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
