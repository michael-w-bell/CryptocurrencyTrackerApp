package com.michaelbell.cryptocurrencytrackerapp;

public enum Filepath {
    CSV_SOURCE, ABOUT, ADD_NEW, EDIT, MAIN_VIEW, SIGN_IN, SIGN_UP;

    public String getFile() {
      switch (this){

          case CSV_SOURCE:
              return "src/main/resources/com/michaelbell/cryptocurrencytrackerapp/CSVFiles/";
          case ABOUT:
              return "src/main/resources/com/michaelbell/cryptocurrencytrackerapp/UI/AboutView.fxml";
          case ADD_NEW:
              return "src/main/resources/com/michaelbell/cryptocurrencytrackerapp/UI/AddNewCoinView.fxml";
          case EDIT:
              return "src/main/resources/com/michaelbell/cryptocurrencytrackerapp/UI/EditCoinView.fxml";
          case MAIN_VIEW:
              return "src/main/resources/com/michaelbell/cryptocurrencytrackerapp/UI/MainView.fxml";
          case SIGN_IN:
              return "src/main/resources/com/michaelbell/cryptocurrencytrackerapp/UI/SignInView.fxml";
          case SIGN_UP:
              return "src/main/resources/com/michaelbell/cryptocurrencytrackerapp/UI/SignUpView.fxml";
          default:
              return null;
      }
    }
}
