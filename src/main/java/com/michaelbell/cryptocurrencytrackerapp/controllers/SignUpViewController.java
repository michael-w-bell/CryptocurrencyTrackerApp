package com.michaelbell.cryptocurrencytrackerapp.controllers;


import com.michaelbell.cryptocurrencytrackerapp.Filepath;
import com.michaelbell.cryptocurrencytrackerapp.utils.DBUtils;
import com.michaelbell.cryptocurrencytrackerapp.utils.NewWindows;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpViewController implements Initializable {

    @FXML private PasswordField txtConfirmPassword;
    @FXML private TextField txtEmail;
    @FXML private Label lblErrorMain;
    @FXML private TextField txtName;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtUsername;
    @FXML private Button btnSignUp;
    @FXML private Button btnLogIn;

    @FXML
    void handleAbout(ActionEvent event) { NewWindows.showAboutView(); }

    @FXML
    void handleClose(ActionEvent event) { Platform.exit(); }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnSignUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(!txtUsername.getText().trim().isEmpty() && !txtPassword.getText().isEmpty()
                        && !txtEmail.getText().isEmpty() && !txtConfirmPassword.getText().isEmpty() && !txtName.getText().isEmpty()) {
                    if(txtPassword.getText().equals(txtConfirmPassword.getText())){
                        //passwords match
                        DBUtils.signUpUser(event, txtUsername.getText(), txtPassword.getText(), txtEmail.getText(), txtName.getText());
                    } else {
                        //passwords don't match
                        lblErrorMain.setText("Passwords do not match!");
                    }
                } else {
                    System.out.println("Fill out all information!");
                    lblErrorMain.setText("Fill out all information!");
                }


            }
        });

        btnLogIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, Filepath.SIGN_IN.getFile(), null, null, null);
            }
        });
    }
}
