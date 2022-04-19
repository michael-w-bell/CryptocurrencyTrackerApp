/**
 * The CryptoCurrencyTrackerApp program implements an application that utilizes a mySQL database to store users.
 * Once a user has signed up, a CSV file is automatically generated. With which will store their cryptocurrency portfolio data.
 * After logging in, a user will see their cryptocurrency portfolio displayed in a table. They will be able to edit each coin,
 * add new coins, or delete a coin.
 * When the user is done with their session, they simply click sign out, and their data is safe from other users.
 * If a user wished to delete their account, they can do so from File>Delete Account. This will remove them from the database and
 * the associated CSV file.
 *
 * @author Michael Bell
 */

package com.michaelbell.cryptocurrencytrackerapp;

import com.michaelbell.cryptocurrencytrackerapp.utils.DBUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("UI/SigninView.fxml"));
        stage.setTitle("Cryptocurrency Portfolio Tracker");
        stage.setScene(new Scene(root, 900,600));
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        DBUtils.createDatabase();
        launch();
    }
}


