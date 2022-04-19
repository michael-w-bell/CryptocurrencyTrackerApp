/**
 * This class deletes the associated CSV file when a new user signs up.
 */
package com.michaelbell.cryptocurrencytrackerapp.utils;

import com.michaelbell.cryptocurrencytrackerapp.Filepath;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DeleteCsvFile {

    public static void deleteFile(String filename){
        String file = Filepath.CSV_SOURCE.getFile() + filename;

        try{
            Files.delete(Paths.get(file));
        } catch (IOException e) {
            System.out.println("error when deleting csv file!");
            e.printStackTrace();
        }
    }
}
