package com.michaelbell.cryptocurrencytrackerapp.controllers;

import com.michaelbell.cryptocurrencytrackerapp.services.getAPI;
import com.michaelbell.cryptocurrencytrackerapp.utils.EditCSV;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;

public class AddNewCoinViewController implements Initializable {

    @FXML private Button btnCancel;
    @FXML private Button btnSubmit;
    @FXML private Label lblError;
    @FXML private TextField txtCoinAmount;
    @FXML private ComboBox<String> cbCoinNames;
    @FXML private TextField txtTotalSpent;
    Map<Integer, String> coinNameMap;
    ArrayList<String> nameList = new ArrayList<String>();
    ObservableList<String> observableNameList;
    Boolean validText = false;

    public AddNewCoinViewController() throws Throwable {
        coinNameMap = getAPI.getCoinNames();
        for(int i = 0; i < coinNameMap.size(); i++){
            //System.out.println("Coin: " + coinNameMap.get(i));
            nameList.add(coinNameMap.get(i));
        }
        //System.out.println(nameList);
        observableNameList = FXCollections.observableArrayList(nameList);
        //System.out.println("observable list: " + observableNameList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbCoinNames.setItems(observableNameList);
        cbCoinNames.getSelectionModel().select(0);

        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) btnCancel.getScene().getWindow();
                stage.close();
            }
        });

        btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //get user inputs
                if(!txtCoinAmount.getText().isEmpty() && !txtTotalSpent.getText().isEmpty()){
                    if(txtTotalSpent.getText().matches("(^[0-9]*)(\\.?)[0-9]*") && txtCoinAmount.getText().matches("(^[0-9]*)(\\.?)[0-9]*")){
                        validText = true;
                    } else {
                        lblError.setText("Error. Must only use numbers and optionally one '.'!");
                    }
                } else {
                    lblError.setText("All information is required!");
                }
                //call editCSV.addNew
                if(validText){
                    String coinSymbol = cbCoinNames.getValue();
                    String spent = txtTotalSpent.getText();
                    String coinAmount = txtCoinAmount.getText();
                    String filename = MainViewController.getFileName();
                    String avgPurchasePrice = String.valueOf(Double.parseDouble(txtTotalSpent.getText())/ Double.parseDouble(txtCoinAmount.getText()));

                    //format avgPurchasePrice
                    if(Double.parseDouble(avgPurchasePrice) > 1){
                        avgPurchasePrice = String.format("%.2f", Double.parseDouble(avgPurchasePrice));
                    } else{
                        avgPurchasePrice = String.format("%.6f", Double.parseDouble(avgPurchasePrice));
                    }
                    EditCSV editCSV = new EditCSV(filename);
                    editCSV.addNew(coinSymbol, coinAmount, avgPurchasePrice, spent, filename);

                    //close window
                    Stage stage = (Stage) btnSubmit.getScene().getWindow();
                    stage.close();
                }
            }
        });

    }
}

