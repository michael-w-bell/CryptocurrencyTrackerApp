/**
 * This class creates the required CSV file when a new user signs up.
 */
package com.michaelbell.cryptocurrencytrackerapp.utils;

import com.michaelbell.cryptocurrencytrackerapp.Filepath;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateCsvFile {

    public static void createFile(String filename){
        File newFile = new File(Filepath.CSV_SOURCE.getFile() + filename + ".csv");
        try {
            FileWriter writer = new FileWriter(newFile);
            System.out.println(filename + ".csv created successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not created!");
        }
    }

}
