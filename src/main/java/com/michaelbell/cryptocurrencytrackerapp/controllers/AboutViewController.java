package com.michaelbell.cryptocurrencytrackerapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutViewController implements Initializable {

    @FXML
    private TextArea aboutTextArea;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aboutTextArea.setText("This is a cryptocurrency portfolio tracker application. " +
                "This application lets you create an account where you can save your current cryptocurrency holdings information " +
                "(which coins you own, how many coins, and how much you've invested). " +
                "With this information, this application uses CoinMarketCap to get the current price information for the top 100 coins. " +
                "It then calculates your total portfolio value, and profit/loss for each coin you own." +
                "\n \n \n " +
                "Created by: Michael Bell");
        aboutTextArea.setWrapText(true);
    }
}

