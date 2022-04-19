package com.michaelbell.cryptocurrencytrackerapp.controllers;

import com.michaelbell.cryptocurrencytrackerapp.Filepath;
import com.michaelbell.cryptocurrencytrackerapp.utils.DBUtils;
import com.michaelbell.cryptocurrencytrackerapp.utils.NewWindows;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SignInController implements Initializable {

    @FXML private Button btnCreateNewAccount;
    @FXML private Button btnLogIn;
    @FXML private MenuItem menuAbout;
    @FXML private TextField txtPassword;
    @FXML private TextField txtUsername;


    @FXML
    void handleClose(ActionEvent event) { Platform.exit(); }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        btnLogIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.logInUser(event, txtUsername.getText(), txtPassword.getText());
            }
        });

        btnCreateNewAccount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                DBUtils.changeScene(event, Filepath.SIGN_UP.getFile(), null, null, null);
            }
        });

        menuAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NewWindows.showAboutView();
            }
        });
    }
}
