package com.michaelbell.cryptocurrencytrackerapp.controllers;

import com.michaelbell.cryptocurrencytrackerapp.Filepath;
import com.michaelbell.cryptocurrencytrackerapp.Totals;
import com.michaelbell.cryptocurrencytrackerapp.models.Crypto;
import com.michaelbell.cryptocurrencytrackerapp.services.readCSV;
import com.michaelbell.cryptocurrencytrackerapp.utils.DBUtils;
import com.michaelbell.cryptocurrencytrackerapp.utils.NewWindows;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainViewController implements Initializable {

    @FXML private Button btnSignOut;
    @FXML private Label lblLastUpdated;
    @FXML private Label lblName;
    @FXML private Label lblTotalValue;
    @FXML private TableView<Crypto> mainTable = new TableView<>();
    @FXML private MenuItem menuSignout;
    @FXML private MenuItem menuEditCoin;
    @FXML private MenuItem menuDeleteAccount;
    @FXML private MenuBar myMenuBar;
    @FXML private Button btnEditCoin;

    private static String filename;
    private String username;
    static ArrayList<Crypto> cryptoData = new ArrayList<Crypto>();
    static ObservableList<Crypto> data = FXCollections.observableArrayList(cryptoData);

    @FXML
    void handleExit(ActionEvent event) { Platform.exit(); }

    @FXML
    void handleAbout(ActionEvent event) { NewWindows.showAboutView(); }

    @FXML
    void handleAddNewCoin(ActionEvent event) {
        NewWindows.showAddNewCoinView();
        refreshList();
    }

    @FXML
    void handleEditCoin(ActionEvent event) {
        NewWindows.showEditView();
        refreshList();
    }

    @FXML
    void handleRefreshPrices(ActionEvent event) { refreshList(); }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnSignOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) btnSignOut.getScene().getWindow();
                DBUtils.signOutUser(stage);
            }
        });
        menuSignout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) myMenuBar.getScene().getWindow();
                DBUtils.signOutUser(stage);
            }
        });

        menuDeleteAccount.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) myMenuBar.getScene().getWindow();
                System.out.println("Username " + username);
                DBUtils.deleteUser(stage, username, filename);
            }
        });

    }

    /**
     *Reads the associated CSV file and populates the tableview from the information in the CSV file.
     * if there is no data in the table, the edit button and edit menu button are disabled.
     * @param name The name of the user.
     * @param csvFileName The CSV file name for that specific user.
     * @param username The username of the user.
     */
    public void setUserInformation(String name, String csvFileName, String username) {
        filename = csvFileName;
        this.username = username;
        lblName.setText(name);
        //resets list data
        cryptoData = new ArrayList<Crypto>();
        data = FXCollections.observableArrayList(cryptoData);

        try {
            readCSV.readFile(Filepath.CSV_SOURCE.getFile() + csvFileName, cryptoData, data);
            lblTotalValue.setText("$" + Totals.totalValue(cryptoData));
            populateTable();
            getLastUpdatedDateTime();
        } catch (Throwable e) {
            System.out.println("read file error");
            e.printStackTrace();
        }
        //check if table is empty. if it is then disable edit buttons
        if(data.isEmpty()){
            btnEditCoin.setDisable(true);
            menuEditCoin.setDisable(true);
        }

    }

    /**
     * Populates the tablview.
     */
    public void populateTable(){
        mainTable.getItems().clear();

        TableColumn coinColumn = new TableColumn("Coin");
        coinColumn.setCellValueFactory(new PropertyValueFactory<>("coinName"));

        TableColumn costColumn = new TableColumn("Purchase Cost");
        costColumn.setCellValueFactory(new PropertyValueFactory<>("totalSpent"));

        TableColumn coinBoughtColumn = new TableColumn("Coins Bought");
        coinBoughtColumn.setCellValueFactory(new PropertyValueFactory<>("coinsBought"));

        TableColumn costPerCoinColumn = new TableColumn("AVG Cost per Coin");
        costPerCoinColumn.setCellValueFactory(new PropertyValueFactory<>("costPerCoin"));

        TableColumn currentPriceColumn = new TableColumn("Current Coin Price");
        currentPriceColumn.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));

        TableColumn currentValueColumn = new TableColumn("Curren Value");
        currentValueColumn.setCellValueFactory(new PropertyValueFactory<>("currentValue"));

        TableColumn profitColumn = new TableColumn("Profit");
        profitColumn.setCellValueFactory(new PropertyValueFactory<>("profit"));

        TableColumn roiColumn = new TableColumn("ROI");
        roiColumn.setCellValueFactory(new PropertyValueFactory<>("roi"));


        mainTable.setItems(data);
        mainTable.getColumns().addAll(coinColumn, costColumn, coinBoughtColumn, costPerCoinColumn, currentPriceColumn, currentValueColumn, profitColumn, roiColumn);
        mainTable.setMaxHeight(300);
        mainTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * refreshes the tableview
     * if there is no data in the table, the edit button and edit menu button are disabled.
     */
    private void refreshList() {
        ArrayList<Crypto> newCryptoData = new ArrayList<Crypto>();
        data = FXCollections.observableArrayList(newCryptoData);

        try {
            readCSV.readFile(Filepath.CSV_SOURCE.getFile() + filename, newCryptoData , data);
        } catch (Throwable th) {
            System.out.println("Error trying to read CSV file!");
            th.printStackTrace();
        }

        mainTable.getItems().clear();
        mainTable.getItems().setAll(data);
        lblTotalValue.setText("$" + Totals.totalValue(newCryptoData));
        getLastUpdatedDateTime();

        //check if table is empty. if it is disabled edit buttons
        if(data.isEmpty()){
            btnEditCoin.setDisable(true);
            menuEditCoin.setDisable(true);
        } else {
            btnEditCoin.setDisable(false);
            menuEditCoin.setDisable(false);
        }
    }

    /**
     * sets the Last updated label to the current time
     */
    private void getLastUpdatedDateTime(){
        //display as Wednesday, November 3, 2021 2:30 PM
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM dd, yyyy hh:mm a");
        Date date = new Date(System.currentTimeMillis());
        lblLastUpdated.setText(format.format(date));
    }

    public static ObservableList<Crypto> getData() {
        return data;
    }

    public static String getFileName() {
        return filename;
    }
}
