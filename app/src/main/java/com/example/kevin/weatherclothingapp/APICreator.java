package com.example.kevin.weatherclothingapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class APICreator {

    //TODO create methods that can be used to cover repetitive code.

    ArrayList<Bitmap> weatherIconArrayList;

    ArrayList<String> etsyImageURLStringArray;

    private final String TAG = "APICreator Class";

    Activity activity;

    APICreator(Activity a){
        this.activity = a;
    }

    //this gets the image from Etsy that has been selected.
    public JSONObject getEtsyJSONObject(String category, String item){

        String etsyKey = getKeyFromRawResource("etsykey");
        String baseURL = "https://openapi.etsy.com/v2/listings/active?api_key=%s&limit=5&category=clothing&fields=title,price,url&includes=MainImage&keywords=";
        String conditions = category + "," + item;
        String urlWithConditions = baseURL + conditions;
        String etsyUrl = String.format(urlWithConditions, etsyKey);
        Log.e(TAG, etsyUrl);

        etsyImageURLStringArray = null;

        JSONObject etsyJSO = null;

        try {
            etsyJSO = new RequestEtsyImagesString().execute(etsyUrl).get();
        }
        catch (Exception e){
            Log.e(TAG, "Unable to get String. Check getEtsyJSONObject method.", e);
        }

        return etsyJSO;
    }


    public Bitmap getEtsyImage(String url){
        Bitmap bitmap = null;
        try{
            bitmap = new RequestImages().execute(url).get();
        }
        catch (Exception e){
            Log.e(TAG, "Unable to get Bitmap. Check getEtsyImage method.", e);
        }
        return bitmap;
    }

    //returns a Bitmap of the forecast icon for the selected day.
    public JSONObject getWunderGroundJSONData() {
        weatherIconArrayList = null;
        String key = getKeyFromRawResource("key");
        String wuTempUrl = String.format("http://api.wunderground.com/api/%s/forecast/q/MN/Minneapolis.json", key);
        JSONObject jso = null;
        try{
            jso = new RequestWunderGroundData().execute(wuTempUrl).get();
        }
        catch (Exception e){
            Log.e(TAG, "Unable to get Bitmap. Check getWunderGroundJSONData method.", e);
        }
        if (jso == null){
            Log.e(TAG, "weatherIconArrayList variable in getWunderGroundJSONData method is null.");
        }
        return jso;
    }

    protected ArrayList<String> getTemperatureDescription(JSONObject jso) {
        // the variable temperatureDescription has four possibilities based on the temperature forecast from wunderground. <32 freezing, 32-50 cold, 50-75 warm, and >75 hot. The variables highTemp and lowTemp are pulled from the field fcttext in the wunderground forecast.

        String temperatureDescription = null;
        ArrayList<String> tempDescriptionList = new ArrayList<>();
        int lowTemp;
        int highTemp;

        try {
            for (int i = 0; i <= 4; i++) {
                JSONObject jso1 = jso.getJSONObject("forecast");
                JSONObject jso2 = jso1.getJSONObject("simpleforecast");
                JSONArray jsa3 = jso2.getJSONArray("forecastday");
                JSONObject jso4 = jsa3.getJSONObject(i);
                JSONObject jso5 = jso4.getJSONObject("high");
                highTemp = jso5.getInt("fahrenheit");

                jso5 = jso4.getJSONObject("low");
                lowTemp = jso5.getInt("fahrenheit");

                if (lowTemp <= 32) {
                    temperatureDescription = "freezing";
                } else if (32 < lowTemp&&lowTemp <= 50) {
                    temperatureDescription = "cold";
                } else if (50 < lowTemp&& highTemp <= 75) {
                    temperatureDescription = "warm";
                } else if (highTemp > 75) {
                    temperatureDescription = "hot";
                }
                tempDescriptionList.add(temperatureDescription);
            }
        }
        catch (Exception e){
            Log.e(TAG, "JSON error in getTemperatureDescription");
        }

        return tempDescriptionList;
    }


    public ArrayList<Bitmap> getWeatherIconArrayList(JSONObject jso){

        ArrayList<Bitmap> weatherIconsArrayList = new ArrayList<>();
        String forecastIconURL;
        Bitmap weatherIcon;

        try {
            for (int i = 0; i <= 6; i = i + 2) {
                JSONObject jso1 = jso.getJSONObject("forecast");
                JSONObject jso2 = jso1.getJSONObject("txt_forecast");
                JSONArray jsa3 = jso2.getJSONArray("forecastday");
                JSONObject jso4 = jsa3.getJSONObject(i);
                forecastIconURL = jso4.getString("icon_url");
                weatherIcon = new RequestImages().execute(forecastIconURL).get();
                weatherIconsArrayList.add(weatherIcon);
            }
        }
        catch (Exception e){
            Log.e(TAG, "JSON error in getForecastDayArrayList");
        }
        return weatherIconsArrayList;
    }

    //returns the selected day for the Forecast (Monday, Tuesday, etc.).
    public ArrayList<String> getWunderGroundStringInfo(JSONObject jso, String s) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        String day;
        try {
            for (int i = 0; i <= 6; i = i+2) {
                JSONObject jso1 = jso.getJSONObject("forecast");
                JSONObject jso2 = jso1.getJSONObject("txt_forecast");
                JSONArray jsa3 = jso2.getJSONArray("forecastday");
                JSONObject jso4 = jsa3.getJSONObject(i);
                day = jso4.getString(s);
                stringArrayList.add(day);
            }
        }
        catch (Exception e){
            Log.e(TAG, "Error in getForecastStringInfo method.");
        }
        return stringArrayList;
    }

    class RequestEtsyImagesString extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... urlInfo) {
            String responseString;
            JSONObject response = null;
            try {
                URL url = new URL(urlInfo[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream responseStream = new BufferedInputStream(connection.getInputStream());
                InputStreamReader streamReader = new InputStreamReader(responseStream);
                StringBuffer buffer = new StringBuffer();
                int c;
                while ((c = streamReader.read()) != -1) {
                    buffer.append((char) c);
                }
                    responseString = buffer.toString();
                    response = new JSONObject(responseString);

            } catch (Exception e) {
                Log.e(null, "Error fetching EtsyInfo", e);
            }
            if (response == null){
                Log.e(TAG, "response variable in RequestEtsyImagesString class is null.");
            }
            return response;
        }
    }

    class RequestImages extends AsyncTask<String, String, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... url){

            Bitmap bitmap = getImage(url[0]);

            return bitmap;
        }
    }

    protected String getWeatherDescription(String weatherUndergroundIcon) {

        String weatherDescription;
        // todo this variable has three possibilities based on the weatherIcon from the wunderground forecast: mild, rainy, and snowy. IN the main program, weatherIcon is a BitMap, but the icon text can be pulled from the wunderground json data easily
        if (weatherUndergroundIcon.contains("rain")||weatherUndergroundIcon.contains("tstorms")) {
            weatherDescription = "rainy";
        }
        else if (weatherUndergroundIcon.contains("flurries")||weatherUndergroundIcon.contains("sleet")||weatherUndergroundIcon.contains("snow")){
            weatherDescription = "snowy";
        }
        else {
            weatherDescription = "mild";
        }
        return weatherDescription;
    }

    protected String getEtsyString(JSONObject jso, int i, String s){
        String returnString = null;
        try {
            JSONArray jsa1 = jso.getJSONArray("results");
            JSONObject jso1 = jsa1.getJSONObject(i);
            returnString = jso1.getString(s);
        }
        catch (Exception e){
            Log.e(TAG, "Error fetching Etsy String", e);
        }
        return returnString;
    }


    protected String getEtsyURLString(JSONObject jso, int i, String s){
        String returnString = null;
        try {
            JSONArray jsa1 = jso.getJSONArray("results");
            JSONObject jso1 = jsa1.getJSONObject(i);
            JSONObject jso2 = jso1.getJSONObject("MainImage");
            returnString = jso2.getString(s);
        }
        catch (Exception e){
            Log.e(TAG, "Error fetching Etsy URL String", e);
        }
        return returnString;
    }

    protected Bitmap getImage(String imageURL){
        Bitmap image = null;
        try {
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream responseStream = connection.getInputStream();
            image = BitmapFactory.decodeStream(responseStream);
        }
        catch (Exception e){
            Log.e(TAG, "Error fetching image.");
        }
        return image;
    }

    protected String createAmazonSearchTerm(String weatherDescription, String temperatureDescription, String amazonItemsToSearch, String amazonSearchMenWomenChildren) {
        // todo The search string defined by itemType will change to depending on a) the choice made in categoryToSearch, b) the choice made in itemToSearch, and c) the two weather-forecast variables, which will affect the phrasing of itemType, i.e. "parkas" vs. "windbreakers." Currently all the variations of itemType are the ones for mild, clear weather.
        if (weatherDescription.equals("snowy") ||temperatureDescription.equals("freezing")) {
            // You would probably dress essentially the same way in snowy or very cold weather, so the clothing options pulled from the store can be the same
            if (amazonItemsToSearch.equals("Hats")) {
                amazonItemsToSearch = "Hats";
            } else if (amazonItemsToSearch.equals("Jackets and Coats")) {
                amazonItemsToSearch = "Jackets and Coats";
            } else if (amazonItemsToSearch.equals("Shirts")) {
                amazonItemsToSearch = "Shirts";
            } else if (amazonItemsToSearch.equals("Dresses and skirts")) {
                amazonItemsToSearch = "Dresses and skirts";
            } else if (amazonItemsToSearch.equals("Pants")) {
                amazonItemsToSearch.equals("Pants");
            } else if (amazonItemsToSearch.equals("Suits, sport coats and blazers")) {
                amazonItemsToSearch = "Suits, sport coats and blazers";
            } else if (amazonItemsToSearch.equals("Socks and hosiery")) {
                amazonItemsToSearch = "Socks and hosiery";
            } else if (amazonItemsToSearch.equals("Shoes")) {
                amazonItemsToSearch = "Shoes";
            }
            return amazonSearchMenWomenChildren + " " + amazonItemsToSearch;
        } else if (temperatureDescription.equals("cold")) {
            // Cold, warm or hot temperatures will return different clothing choices. Rainy weather will affect some clothing choices (shirts, for instance) but not others (like socks), .
            if (amazonItemsToSearch.equals("Hats")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Hats";
                } else amazonItemsToSearch = "Hats";
            } else if (amazonItemsToSearch.equals("Jackets and Coats")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Jackets and Coats";
                } else amazonItemsToSearch = "Jackets and Coats";
            } else if (amazonItemsToSearch.equals("Shirts")) {
                amazonItemsToSearch = "Shirts";
            } else if (amazonItemsToSearch.equals("Dresses and skirts")) {
                amazonItemsToSearch = "Dresses and skirts";
            } else if (amazonItemsToSearch.equals("Pants")) {
                amazonItemsToSearch = "Pants";
            } else if (amazonItemsToSearch.equals("Suits, sport coats and blazers")) {
                amazonItemsToSearch = "Suits, sport coats and blazers";
            } else if (amazonItemsToSearch.equals("Socks and hosiery")) {
                amazonItemsToSearch = "Socks and hosiery";
            } else if (amazonItemsToSearch.equals("Shoes")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Shoes";
                } else amazonItemsToSearch = "Shoes";
            }
            return amazonSearchMenWomenChildren + " " + amazonItemsToSearch;
        } else if (temperatureDescription.equals("warm")) {
            if (amazonItemsToSearch.equals("Hats")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Hats";
                } else amazonItemsToSearch = "Hats";
            } else if (amazonItemsToSearch.equals("Jackets and Coats")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Jackets and Coats";
                } else amazonItemsToSearch = "Jackets and Coats";
            } else if (amazonItemsToSearch.equals("Shirts")) {
                amazonItemsToSearch = "Shirts";
            } else if (amazonItemsToSearch.equals("Dresses and skirts")) {
                amazonItemsToSearch = "Dresses and skirts";
            } else if (amazonItemsToSearch.equals("Pants")) {
                amazonItemsToSearch = "Pants";
            } else if (amazonItemsToSearch.equals("Suits, sport coats and blazers")) {
                amazonItemsToSearch = "Suits, sport coats and blazers";
            } else if (amazonItemsToSearch.equals("Socks and hosiery")) {
                amazonItemsToSearch = "Socks and hosiery";
            } else if (amazonItemsToSearch.equals("Shoes")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Shoes";
                } else amazonItemsToSearch = "Shoes";
            }
            return amazonSearchMenWomenChildren + " " + amazonItemsToSearch;
        } else if (temperatureDescription.equals("hot")) {
            if (amazonItemsToSearch.equals("Hats")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Hats";
                }
                else amazonItemsToSearch = "Hats";
            }
            else if (amazonItemsToSearch.equals("Jackets and Coats")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Jackets and Coats";
                } else amazonItemsToSearch = "Jackets and Coats";
            } else if (amazonItemsToSearch.equals("Shirts")) {
                amazonItemsToSearch = "Shirts";
            } else if (amazonItemsToSearch.equals("Dresses and skirts")) {
                amazonItemsToSearch = "Dresses and skirts";
            } else if (amazonItemsToSearch.equals("Pants")) {
                amazonItemsToSearch = "Pants";
            } else if (amazonItemsToSearch.equals("Suits, sport coats and blazers")) {
                amazonItemsToSearch = "Suits, sport coats and blazers";
            } else if (amazonItemsToSearch.equals("Socks and hosiery")) {
                amazonItemsToSearch = "Socks and hosiery";
            } else if (amazonItemsToSearch.equals("Shoes")) {
                if (weatherDescription.equals("rainy")) {
                    amazonItemsToSearch = "Shoes";
                } else amazonItemsToSearch = "Shoes";
            }
            return amazonSearchMenWomenChildren + " " + amazonItemsToSearch;
        }
        return amazonSearchMenWomenChildren + " " + amazonItemsToSearch;

    }

    //gets the key from the raw file.
    protected String getKeyFromRawResource(String requestedKey) {
        InputStream keyStream = null;
        if (requestedKey.equals("etsykey")){
            keyStream = activity.getResources().openRawResource(R.raw.etsykey);
        }
        else if (requestedKey.equals("key")) {
            keyStream = activity.getResources().openRawResource(R.raw.key);
        }
        else if(requestedKey.equals("APPLICATION_KEY")){
            keyStream = activity.getResources().openRawResource(R.raw.amazon_application_key);
        }

        BufferedReader keyStreamReader = new BufferedReader(new InputStreamReader(keyStream));

        try{
            String key = keyStreamReader.readLine();
            return key;
        }
        catch (IOException e){
            Log.e(TAG, "Error reading from raw resource file", e);
            return null;
        }
    }


    //This class goes to WeatherUnderground gets the data.
    class RequestWunderGroundData extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String...urlInfo) {
            String responseString;
            JSONObject response = null;
            try {
                URL url = new URL(urlInfo[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream responseStream = new BufferedInputStream(connection.getInputStream());
                InputStreamReader streamReader = new InputStreamReader(responseStream);
                StringBuffer buffer = new StringBuffer();

                int c;
                while ((c = streamReader.read()) != -1) {
                    buffer.append((char) c);
                }
                responseString = buffer.toString();
                response = new JSONObject(responseString);

            } catch (Exception e) {
                Log.e(null, "Error fetching weather map", e);
            }
            return response;
        }

    }
}