package com.michaelbell.cryptocurrencytrackerapp.controllers;

import com.michaelbell.cryptocurrencytrackerapp.models.Crypto;
import com.michaelbell.cryptocurrencytrackerapp.utils.EditCSV;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCoinViewController implements Initializable {

    @FXML private ComboBox<Crypto> coinSelection;
    @FXML private Label lblError;
    @FXML private TextField totalCoins;
    @FXML private TextField totalSpent;
    @FXML private Button btnCancel;
    @FXML private Button btnSubmit;
    @FXML private Button btnDelete;
    Boolean validText = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Crypto> data = MainViewController.getData();
        coinSelection.setItems(data);
        coinSelection.getSelectionModel().select(0);

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
                if(!totalCoins.getText().isEmpty() && !totalSpent.getText().isEmpty()){
                    if(totalCoins.getText().matches("(^[0-9]*)(\\.?)[0-9]*") && totalSpent.getText().matches("(^[0-9]*)(\\.?)[0-9]*")){
                        validText = true;
                    } else {
                        lblError.setText("Error. Must only use numbers and optionally one '.'!");
                    }
                } else {
                    lblError.setText("All information is required!");
                }

                if(validText){
                    //get input data
                    String coinName = coinSelection.getValue().getCoinName();
                    String spent = totalSpent.getText();
                    String coins = totalCoins.getText();
                    String filename = MainViewController.getFileName();
                    String avgPurchasePrice = String.valueOf(Double.parseDouble(totalSpent.getText())/Double.parseDouble(totalCoins.getText()));

                    //format avgPurchasePrice
                    if(Double.parseDouble(avgPurchasePrice) > 1){
                        avgPurchasePrice = String.format("%.2f", Double.parseDouble(avgPurchasePrice));
                    } else{
                        avgPurchasePrice = String.format("%.6f", Double.parseDouble(avgPurchasePrice));
                    }

                    //update csv file

                    EditCSV edit = new EditCSV(coinName, coins, avgPurchasePrice, spent, filename);

                    //close window
                    Stage stage = (Stage) btnSubmit.getScene().getWindow();
                    stage.close();
                }
            }
        });

        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(coinSelection.getValue().getCoinName().isEmpty()){
                    lblError.setText("Must select a coin to delete!");
                } else {
                    String coinName = coinSelection.getValue().getCoinName();
                    String fileName = MainViewController.getFileName();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Deletion");
                    alert.setHeaderText("Please Confirm!");
                    alert.setContentText("Are you sure you want to delete " + coinName + "?");
                    alert.showAndWait().ifPresent(type ->{
                        if(type == ButtonType.OK) {
                            //remove coin from file
                            EditCSV editCsv = new EditCSV(fileName);
                            editCsv.removeCoin(coinName);

                            //close edit window
                            Stage stage = (Stage) btnDelete.getScene().getWindow();
                            stage.close();
                        }
                    });
                }
            }
        });
    }

}


