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

/**
 * Created by Kevin on 10/26/2015.
 */
public class APICreator {

    //TODO etsyAPIRequest

    //TODO amazonAPIRequest

    //TODO create methods that can be used to cover repetitive code.

    Bitmap weatherIcon;
    String forecastDay;
    Bitmap etsyImage;

    private final String TAG = "APICreator Class";
    Activity activity;

    APICreator(Activity a){
        this.activity = a;
    }

    //this gets the image from Etsy that has been selected.
    public Bitmap getEtsyImage(){

        String etsyKey = getKeyFromRawResource("etsy_key");
        String etsyUrl = String.format("https://openapi.etsy.com/v2/listings/active?api_key=%s&limit=1&category=clothing&fields=title,url&includes=MainImage", etsyKey);
        etsyImage = null;

        try {
            etsyImage = new RequestEtsyImages().execute(etsyUrl).get();
        }
        catch (Exception e){
            Log.e(TAG, "Unable to get Bitmap. Check getEtsyImage method.", e);
        }
        if (etsyImage == null){
            Log.e(TAG, "etsyImage variable in getEtsyImage method is null.");
        }
        return etsyImage;
    }

    //returns a Bitmap of the forecast icon for the selected day.
    public Bitmap getForecastIcon(Integer i) {
        weatherIcon = null;
        String key = getKeyFromRawResource("key");
        String wuTempUrl = String.format("http://api.wunderground.com/api/%s/forecast/q/" + WeatherClothingActivity.mLastLocation.getLatitude() + "," + WeatherClothingActivity.mLastLocation.getLongitude() + ".json", key);
        try{
            weatherIcon = new RequestCurrentMplsWeatherIcon().execute(wuTempUrl, Integer.toString(i)).get();
        }
        catch (Exception e){
            Log.e(TAG, "Unable to get Bitmap. Check getForecastIcon method.", e);
        }
        if (weatherIcon == null){
            Log.e(TAG, "weatherIcon variable in getForecastIcon method is null.");
        }
        return weatherIcon;
    }

    //returns the selected day for the Forecast (Monday, Tuesday, etc.).
    public String getForecastDay(Integer i) {
        forecastDay = null;
        String key = getKeyFromRawResource("key");
        String wuTempUrl = String.format("http://api.wunderground.com/api/%s/forecast/q/" + WeatherClothingActivity.mLastLocation.getLatitude() + "," + WeatherClothingActivity.mLastLocation.getLongitude() + ".json", key);
        try{
            forecastDay = new RequestMinneapolisForecastDay().execute(wuTempUrl, Integer.toString(i)).get();
        }
        catch (Exception e){
            Log.e(TAG, "Unable to get String. Check getForecastDay method.", e);
        }
        if (forecastDay == null){
            Log.e(TAG, "forecastDay variable in getForecastDay method is null.");
        }
        return forecastDay;
    }

    class RequestEtsyImages extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urlInfo) {
            String responseString;
            String imageURL;
            Bitmap clothesImage = null;

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
                JSONObject response = new JSONObject(responseString);
                JSONArray jsa1 = response.getJSONArray("results");
                JSONObject jso1 = jsa1.getJSONObject(0);
                JSONObject jso2  = jso1.getJSONObject("MainImage");
                imageURL = jso2.getString("url_75x75");

                URL url2 = new URL(imageURL);
                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                InputStream responseStream2 = connection2.getInputStream();
                clothesImage = BitmapFactory.decodeStream(responseStream2);



            } catch (Exception e) {
                Log.e(null, "Error fetching EtsyInfo", e);
            }
            if (clothesImage == null){
                Log.e(TAG, "clothesImage variable in RequestEtsyImages class is null.");
            }
            return clothesImage;
        }
    }

    //gets the key from the raw file.
    private String getKeyFromRawResource(String requestedKey) {
        InputStream keyStream = null;
        if (requestedKey.equals("etsy_key")){
            keyStream = activity.getResources().openRawResource(R.raw.etsy_key);
        }
        else if (requestedKey.equals("key")) {
            keyStream = activity.getResources().openRawResource(R.raw.key);
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


    //This class goes to WeatherUnderground and gets the icon for the specific day's forecast.
    class RequestCurrentMplsWeatherIcon extends AsyncTask<String, String, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urlInfo) {
            String responseString;
            Bitmap weatherIcon = null;
            String forecastIcon;

            try {
                URL url = new URL(urlInfo[0]);
                Integer dayNum = Integer.parseInt(urlInfo[1]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream responseStream = new BufferedInputStream(connection.getInputStream());
                InputStreamReader streamReader = new InputStreamReader(responseStream);
                StringBuffer buffer = new StringBuffer();
                int c;
                while ((c = streamReader.read()) != -1) {
                    buffer.append((char) c);
                }
                responseString = buffer.toString();
                JSONObject response = new JSONObject(responseString);
                JSONObject jso1 = response.getJSONObject("forecast");
                JSONObject jso2 = jso1.getJSONObject("txt_forecast");
                JSONArray jsa3 = jso2.getJSONArray("forecastday");
                JSONObject jso4 = jsa3.getJSONObject(dayNum);
                forecastIcon = jso4.getString("icon_url");

                URL url2 = new URL(forecastIcon);
                HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
                InputStream responseStream2 = connection2.getInputStream();
                weatherIcon = BitmapFactory.decodeStream(responseStream2);

            } catch (Exception e) {
                Log.e(null, "Error fetching weather map", e);
            }
            return weatherIcon;
        }

    }

    //This class goes to WeatherUnderground and gets the day of the forecast.
    class RequestMinneapolisForecastDay extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urlInfo) {
            String responseString;
            String day = null;

            try {
                URL url = new URL(urlInfo[0]);
                Integer dayNum = Integer.parseInt(urlInfo[1]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream responseStream = new BufferedInputStream(connection.getInputStream());
                InputStreamReader streamReader = new InputStreamReader(responseStream);
                StringBuffer buffer = new StringBuffer();
                int c;
                while ((c = streamReader.read()) != -1) {
                    buffer.append((char) c);
                }
                responseString = buffer.toString();
                JSONObject response = new JSONObject(responseString);
                JSONObject jso1 = response.getJSONObject("forecast");
                JSONObject jso2 = jso1.getJSONObject("txt_forecast");
                JSONArray jsa3 = jso2.getJSONArray("forecastday");
                JSONObject jso4 = jsa3.getJSONObject(dayNum);
                day = jso4.getString("title");

            } catch (Exception e) {
                Log.e(null, "Error fetching weather map", e);
            }
            return day;
        }
    }
}