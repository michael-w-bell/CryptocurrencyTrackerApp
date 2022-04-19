package com.michaelbell.cryptocurrencytrackerapp;

public enum API {
    KEY, LATEST_URL, MAP_URL;

    public String get(){
        switch (this) {
            case KEY:
                return "c636cdc7-4bab-4622-8fc4-10c3ad0ca4df";
            case LATEST_URL:
                return "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
            case MAP_URL:
                return "https://pro-api.coinmarketcap.com/v1/cryptocurrency/map";
            default:
                return null;
        }
    }
}
