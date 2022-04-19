/**
 *This class handles the pop-up views for the About view, Edit view, and the Add new view.
 */

package com.michaelbell.cryptocurrencytrackerapp.utils;

import com.michaelbell.cryptocurrencytrackerapp.Filepath;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class NewWindows {

    public static void showAboutView() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(new File(Filepath.ABOUT.getFile()).toURI().toURL());
            root = loader.load();
            Scene scene = new Scene(root, 400, 400);
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showEditView() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(new File(Filepath.EDIT.getFile()).toURI().toURL());
            root = loader.load();
            Scene scene = new Scene(root, 300, 300);
            Stage stage = new Stage();
            stage.setTitle("Edit Coin");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAddNewCoinView() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(new File(Filepath.ADD_NEW.getFile()).toURI().toURL());
            root = loader.load();
            Scene scene = new Scene(root, 500, 250);
            Stage stage = new Stage();
            stage.setTitle("Add New Coin");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
