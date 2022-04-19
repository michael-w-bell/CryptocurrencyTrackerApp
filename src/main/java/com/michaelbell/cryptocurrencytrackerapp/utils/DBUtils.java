/**
 * This class handles creating the database, table, and users, logging in users, and deleted users.
 * It also changes the scene after signing up, logging in, logging out, or deleting the account.
 */

package com.michaelbell.cryptocurrencytrackerapp.utils;

import com.michaelbell.cryptocurrencytrackerapp.Filepath;
import com.michaelbell.cryptocurrencytrackerapp.controllers.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.File;
import java.sql.*;
import java.io.IOException;

public class DBUtils {
    private static final String DB_NAME = "cryptoapp";
    private static final String DB_SERVER_ADDRESS = "jdbc:mysql://localhost:3306/";
    private static final String DB_ADDRESS =  DB_SERVER_ADDRESS + DB_NAME;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "toor";

    /**
     *changes the scene to the given FXML file
     * checks if the username and password are not null,
     * if they are it will load the signInView or signUpView,
     * if they are not it will load the mainView
     *
     * @param fxmlFile this is the fxml file that is to be loaded - never NULL
     * @param name this is the name of the user. - may be NULL if loading file other than mainView
     * @param csvFilename this is the CSV file name for the user. - may be NULL if loading file other than mainView
     * @param username this is the username of the user. - may be NULL if loading file other than mainView
     */
    public static void changeScene(ActionEvent event, String fxmlFile, String name, String csvFilename, String username) {
        Parent root = null;

        if (name != null && csvFilename != null) {
            try {
                FXMLLoader loader = new FXMLLoader(new File(fxmlFile).toURI().toURL());
                root = loader.load();
                MainViewController mainViewController = loader.getController();
                mainViewController.setUserInformation(name, csvFilename, username);

            } catch (IOException e){
                e.printStackTrace();
            }
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(new File(fxmlFile).toURI().toURL());
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    /**
     *signs the user out by changing scene to the signInView
     */
    public static void signOutUser(Stage stage){
        Parent root = null;
        String fxmlFile = Filepath.SIGN_IN.getFile();
        try {
            FXMLLoader loader = new FXMLLoader(new File(fxmlFile).toURI().toURL());
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setScene(new Scene(root, 900, 600));
        stage.show();
    }

    /**
     *Takes in the input from the user, then checks if the provided username already exists in the database.
     * if not, it creates a user in the database with the supplied input.
     * Then, makes call to create the csv file.
     * Then changes scene to the mainView
     */
    public static void signUpUser(ActionEvent event, String username, String password, String email, String name){

        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;
        String csvFilename = "";

        try {
            connection = DriverManager.getConnection(DB_ADDRESS, DB_USER, DB_PASSWORD);
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

            if(resultSet.isBeforeFirst()) {
                System.out.println("User already exists...");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("User already exists!");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users (username, name, email, password) VALUES (?, ?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, name);
                psInsert.setString(3, email);
                psInsert.setString(4, password);
                psInsert.executeUpdate();

                //create csv file
                CreateCsvFile.createFile(username);

                PreparedStatement user = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
                user.setString(1, username);
                ResultSet result = user.executeQuery();
                while(result.next()) {
                    csvFilename = result.getString("csv");
                }

                changeScene(event, Filepath.MAIN_VIEW.getFile(), name, csvFilename, username);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            closeConnectionAndPS(connection, psInsert);
        }
    }

    /**
     * checks the inputed username and password. If correct/ the user exists,
     * it retrieves the name of the user, and csv filename from the database.
     * Then, changes scene to the mainview
     */
    public static void logInUser(ActionEvent event, String username, String password){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DB_ADDRESS, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("SELECT name, password, csv FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()) {
                System.out.println("User not found!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid username!");
                alert.show();
            } else {
                while (resultSet.next()){
                    //System.out.println(resultSet);
                    String retrievedPassword = resultSet.getString("password");
                    String retrievedName = resultSet.getString("name");
                    String retrievedCsv = resultSet.getString("csv");

                    //capitalize first letter in name
                    retrievedName = retrievedName.substring(0,1).toUpperCase() + retrievedName.substring(1);

                    if(retrievedPassword.equals(password)){
                        changeScene(event, Filepath.MAIN_VIEW.getFile(), retrievedName, retrievedCsv, username);
                    } else {
                        System.out.println("Password doesn't match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Invalid password!");
                        alert.show();
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnections(connection, preparedStatement, resultSet);
        }

    }

    /**
     *Deletes the specified user from the database and also deletes the corresponding csv file.
     */
    public static void deleteUser(Stage stage, String username, String csvFilename){

        //delete user from database
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(DB_ADDRESS, DB_USER, DB_PASSWORD);
            preparedStatement = connection.prepareStatement("DELETE FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            preparedStatement.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnectionAndPS(connection, preparedStatement);
        }
        //go to login screen
        signOutUser(stage);

        //delete csv file
        DeleteCsvFile.deleteFile(csvFilename);

    }

    /**
     *closes the PreparedStatement and the Connection
     */
    private static void closeConnectionAndPS(Connection connection, PreparedStatement preparedStatement) {
        if(preparedStatement != null){
            try {
                preparedStatement.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    /**
     *closes the ResultSet, and then calls closeConnectionsAndPS()
     */
    private static void closeConnections(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        if(resultSet != null){
            try {
                resultSet.close();
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        closeConnectionAndPS(connection, preparedStatement);
    }

    /**
    *checks to see if the database has been created, if not it creates one
    * then checks to see if the table has been created in that database, if not it creates one
    * then checks to see if the sample user has been created, if not it creates the sample user.
     */
    public static void createDatabase() {
        Connection connection = null;
        Statement statement = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            //Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_SERVER_ADDRESS, DB_USER, DB_PASSWORD);
            statement = connection.createStatement();
            String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            statement.executeUpdate(sql);
            System.out.println("Database created");

            //set up table in database
            String tableSQL = "CREATE TABLE IF NOT EXISTS " + DB_NAME + ".`users` " +
                    "(`username` VARCHAR(255) NOT NULL," +
                    "`name` VARCHAR(255) NOT NULL," +
                    "`password` VARCHAR(255) NOT NULL, " +
                    "`email` VARCHAR(255) NOT NULL," +
                    "`csv` VARCHAR(255) GENERATED ALWAYS AS (CONCAT(username,'.csv')) STORED," +
                    "UNIQUE KEY `username_UNIQUE` (`username`))";
            statement.executeUpdate(tableSQL);
            System.out.println("Created table in database!");

            //check if sample user has been created, if not then create one
            connection = DriverManager.getConnection(DB_ADDRESS, DB_USER, DB_PASSWORD);
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExists.setString(1, "michaelwb");
            resultSet = psCheckUserExists.executeQuery();

            if(!resultSet.isBeforeFirst()) {
                //create sample user michaelwb
                String sampleUser = "INSERT INTO " + DB_NAME + ".`users`" +
                        "(`username`," +
                        "`name`," +
                        "`password`, " +
                        "`email`)" +
                        "VALUES" +
                        "('michaelwb'," +
                        "'michael'," +
                        "'123456'," +
                        "'michael@aol.com')";
                statement.executeUpdate(sampleUser);
                System.out.println("Created sample user in table!");
            }
        } catch ( SQLException e ){
            e.printStackTrace();
       } finally {
            closeConnections(connection, psCheckUserExists, resultSet);
        }
    }

}