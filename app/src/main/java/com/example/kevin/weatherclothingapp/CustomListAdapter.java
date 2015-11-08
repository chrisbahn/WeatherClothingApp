package com.example.kevin.weatherclothingapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kevin on 11/1/2015.
 */
public class CustomListAdapter extends ArrayAdapter<String> {

    private Activity context;
    private ArrayList<Bitmap> forecastIconBMArrayList = new ArrayList<>();
    private ArrayList<String> forecastDayStringList = new ArrayList<>();
    private ArrayList<String> forecastDayInfoList = new ArrayList<>();
    private ArrayList<String> tempDescriptionList = new ArrayList<>();
    TextView weatherDay;
    ImageView imageView;
    TextView weatherInfo;


    //sets the size of the Custom Adapter, along with the Strings and Bitmap Arrays which are needed for it.
    public CustomListAdapter(Activity context, ArrayList<String> dayList, ArrayList<Bitmap> forecastIcons, ArrayList<String> forecastConditions, ArrayList<String> tempDescArrayList) {
        super(context, R.layout.weather_list, dayList);
        // TODO Auto-generated constructor stub
        this.forecastIconBMArrayList = forecastIcons;
        this.forecastDayStringList = dayList;
        this.context=context;
        this.forecastDayInfoList = forecastConditions;
        this.tempDescriptionList = tempDescArrayList;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.weather_list, null, true);

        imageView = (ImageView) rowView.findViewById(R.id.weatherIconImageView);
        imageView.setImageBitmap(forecastIconBMArrayList.get(position));

        weatherDay = (TextView) rowView.findViewById(R.id.weatherDayTextView);
        weatherDay.setText(forecastDayStringList.get(position));

        weatherInfo = (TextView) rowView.findViewById(R.id.weatherConditionsTextView);
        weatherInfo.setText(forecastDayInfoList.get(position));

        return rowView;
    }
}
