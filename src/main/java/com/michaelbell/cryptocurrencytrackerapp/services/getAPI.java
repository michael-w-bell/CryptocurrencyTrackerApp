/**
 * This class calls the Coin Market Cap API to retrieve the latest prices of the top (LIMIT) cryptocurrencies.
 * The API provides a JSON response, and this class maps the prices to the coins provided in the CSV file.
 */
package com.michaelbell.cryptocurrencytrackerapp.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.michaelbell.cryptocurrencytrackerapp.API;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;

public class getAPI {
    private final static int LIMIT = 100;

    /**
     * Creates the HashMap that the coins and their prices are stored in.
     * Also creates the list of parameters to use when making the API call.
     * Calls makeAPICall()
     */
    public static Map<String, Double> coinMarketCapAPI(List<String> coinNames) throws Throwable {
        String result = "";
        Map<String, Double> resultMap = new HashMap<>();
        List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
        paratmers.add(new BasicNameValuePair("start","1"));
        paratmers.add(new BasicNameValuePair("limit",String.valueOf(LIMIT)));
        paratmers.add(new BasicNameValuePair("convert","USD"));

        try {
            result = makeAPICall(API.LATEST_URL.get(), paratmers);
            //testing*******
            //System.out.println(result);
            //System.out.println(result.length());
            //**********
        } catch (IOException e) {
            System.out.println("Error: cannot access content - " + e);
        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e);
        }

        coinNames.forEach(name -> {
            resultMap.put(name, null);
        });

        return parse(resultMap, result);
    }//END coinMarketCapAPI***********

    /**
     * Sends the HTTP request to the Coin Market Cap API.
     * returns the JSON response.
     */
    private static String makeAPICall (String uri, List<NameValuePair> parameters) throws URISyntaxException, org.apache.hc.core5.http.ParseException, IOException{
        String response_content = "";

        URIBuilder query = new URIBuilder(uri);
        query.addParameters(parameters);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());

        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", API.KEY.get());

        CloseableHttpResponse response = client.execute(request);

        try {
            //System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return response_content;
    }//END makeAPICall *********

    /**
     * Maps the JSON response to the coins found in the Map that was created from the coins in the CSV file.
     */
    private static Map<String, Double> parse(Map<String, Double> resultMap, String data) throws ParseException {
        Map<String, Double> coins = new HashMap<String, Double>();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(data);

        for(Map.Entry<String, Double> entry: resultMap.entrySet()) {
            String name = entry.getKey();
            Double price;

            for(int i =0; i < LIMIT; i++) {
                JSONArray dataDetail = (JSONArray) obj.get("data");
                JSONObject intObj = (JSONObject) dataDetail.get(i);
                if(name.compareTo((String) intObj.get("symbol")) == 0) {
                    JSONObject quoteObj = (JSONObject) intObj.get("quote");
                    JSONObject usdObj = (JSONObject) quoteObj.get("USD");
                    price = (Double) usdObj.get("price");
                    //System.out.println((Double) usdObj.get("price"));
                    coins.put(name, price);
                }//END if
            }//END inner for loop
        }//END outer for loop

        //System.out.println("coins" + coins);
        return coins;

    }//END parse

    /**
     * gets the top (LIMIT) of cryptocurrency coin names.
     * Makes call to Coin Market Cap API, receives JSON data.
     */
    public static Map<Integer, String> getCoinNames() throws Throwable {
        String URL = API.MAP_URL.get();
        String result = "";
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("start", "1"));
        parameters.add(new BasicNameValuePair("limit", String.valueOf(LIMIT)));
        parameters.add(new BasicNameValuePair("sort", "cmc_rank"));

        try {
            result = makeAPICall(URL, parameters);

            //testing*******
            //System.out.println(result);

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(result);
        } catch (IOException e) {
            System.out.println("Error: cannot access content - " + e);
        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e);
        }

        return parseAllCoinsList(result);
    }

    /**
     * Creates map of all coin names provided from getCoinNames()
     */
    private static Map<Integer, String> parseAllCoinsList(String data) throws ParseException {
        Map<Integer, String> coins = new HashMap<Integer, String>();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(data);
        int id = 0;

        for(int i =0; i < LIMIT; i++) {
            JSONArray dataDetail = (JSONArray) obj.get("data");
            JSONObject intObj = (JSONObject) dataDetail.get(i);
            String coinSymbol = (String) intObj.get("symbol");

            coins.put(id, coinSymbol);
            id++;
        }
        //System.out.println("coins" + coins);
        return coins;

    }//END parse

}
