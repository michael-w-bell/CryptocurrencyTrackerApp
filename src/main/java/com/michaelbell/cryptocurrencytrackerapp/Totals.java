package com.michaelbell.cryptocurrencytrackerapp;

import com.michaelbell.cryptocurrencytrackerapp.models.Crypto;
import java.util.ArrayList;

public class Totals {
    //gets the current value from each coin and adds them for a total current value. Returns total current value.
    public static String totalValue(ArrayList<Crypto> cryptoData) {
        float totals = 0;
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(Crypto currentValue : cryptoData) {
            totals += Float.parseFloat(currentValue.getCurrentValue());
        }

        return String.format("%,.2f",totals);
    }//END totalValue*********
}
