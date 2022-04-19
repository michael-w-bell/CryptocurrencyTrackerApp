/**
 * This class makes edits to the associated CSV file.
 */
package com.michaelbell.cryptocurrencytrackerapp.utils;

import com.michaelbell.cryptocurrencytrackerapp.Filepath;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EditCSV {

    private String coinName, coinAmount, avgPurchasePrice, totalSpent;
    private final static String DELIM = ",";
    private String fileName;
    private boolean addNew = false;

    public EditCSV(String coinName, String coinAmount, String avgPurPrice, String totalSpent, String filename) {
        this.coinName = coinName;
        this.coinAmount = coinAmount;
        this.avgPurchasePrice = avgPurPrice;
        this.totalSpent = totalSpent;
        this.fileName = Filepath.CSV_SOURCE.getFile() + filename;

        try {
            readAndUpdateFile();
        } catch (IOException e) {
            System.out.println("Error with readAndUpdateFile()");
            e.printStackTrace();
        }
    }


    public EditCSV(String fileName) {
        this.fileName = Filepath.CSV_SOURCE.getFile() + fileName;
    }

    public void addNew(String coinName, String coinAmount, String avgPurPrice, String totalSpent, String filename) {
        this.coinName = coinName;
        this.coinAmount = coinAmount;
        this.avgPurchasePrice = avgPurPrice;
        this.totalSpent = totalSpent;
        this.fileName = Filepath.CSV_SOURCE.getFile() + filename;

        addNew = true;
        try {
            addAndUpdateFile();
        } catch (IOException e) {
            System.out.println("Error with addAndUpdateFile()");
            e.printStackTrace();
        }
    }

    public void removeCoin(String coinName) {
        this.coinName = coinName;
        try {
            removeAndUpdateFile();
        } catch (IOException e) {
            System.out.println("Error with removeAndUpdateFile()");
            e.printStackTrace();
        }
    }

    private void readAndUpdateFile() throws IOException {

        List<List<String>> coins = new ArrayList<List<String>>();
        coins = createList(coins);

        for(int i =0; i< coins.size(); i++) {

            if(coins.get(i).get(0).equals(this.coinName)) {

                coins.get(i).set(1, this.coinAmount);
                coins.get(i).set(2, this.avgPurchasePrice);
                coins.get(i).set(3, this.totalSpent);
            }
        }

        rewriteCSV(coins);
    }

    private void addAndUpdateFile() throws IOException {

        List<List<String>> coins = new ArrayList<List<String>>();
        coins = createList(coins);
        rewriteCSV(coins);
    }

    private void removeAndUpdateFile() throws IOException {

        List<List<String>> coins = new ArrayList<List<String>>();
        coins = createList(coins);
        //index is equal to -1 because there will never be an item selected that is not in the list.
        int index = -1;

        for(int i=0; i< coins.size(); i++) {
            if (this.coinName.equals(coins.get(i).get(0))) {

                index = i;
            }
        }

        coins.remove(index);
        rewriteCSV(coins);

    }

    private List<List<String>> createList (List<List<String>> coins) throws IOException {

        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(fileName));
            String line;
            while((line = br.readLine()) !=null){
                List<String> coin = new ArrayList<String>();
                String[] fields = line.split(DELIM, -1);
                //add coin info to an array
                coin.add(fields[0]);
                coin.add(fields[1]);
                coin.add(fields[2]);
                coin.add(fields[3]);
                coin.add(fields[4]);
                coin.add(fields[5]);
                coin.add(fields[6]);
                coin.add(fields[7]);

                //add coin array to coins array
                coins.add(coin);
            }
            br.close();
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        }

        return coins;

    }

    private void rewriteCSV(List<List<String>> coins) throws IOException {
        FileWriter fw = new FileWriter(fileName);

        for(List<String> list : coins) {
            StringBuilder sb = new StringBuilder();
            for(String line : list) {
                if(sb.length() != 0) {
                    sb.append(",");
                }
                sb.append(line);
            }
            fw.write(sb + "\n");
        }

        //check to see if there is a new coin being added
        if(addNew) {
            fw.write(this.coinName + "," + this.coinAmount + "," + this.avgPurchasePrice + "," + this.totalSpent  + "," + "0"  + "," + "0"  + "," + "0"  + "," + "0");
        }

        fw.close();
    }

}
