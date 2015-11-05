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
    TextView weatherDay;

    //sets the size of the Custom Adapter, along with the Strings and Bitmap Arrays which are needed for it.
    public CustomListAdapter(Activity context, ArrayList<String> dayList, ArrayList<Bitmap> forecastIcons, ArrayList<String> forecastConditions) {
        super(context, R.layout.weather_list, dayList);
        // TODO Auto-generated constructor stub
        this.forecastIconBMArrayList = forecastIcons;
        this.forecastDayStringList = dayList;
        this.context=context;
        this.forecastDayInfoList = forecastConditions;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.weather_list, null, true);

        //The UI view cannot read ArrayLists, so the ALs have to be converted into Arrays.
        Bitmap[] forecastIconBMArray = forecastIconBMArrayList.toArray(new Bitmap[forecastIconBMArrayList.size()]);
        String[] forecastDayStringArray = forecastDayStringList.toArray(new String[forecastDayStringList.size()]);
        String[] forecastConditionsArray = forecastDayInfoList.toArray(new String[forecastDayInfoList.size()]);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.weatherIconImageView);
        imageView.setImageBitmap(forecastIconBMArray[position]);

        weatherDay = (TextView) rowView.findViewById(R.id.weatherDayTextView);
        weatherDay.setText(forecastDayStringArray[position]);

        TextView weatherInfo = (TextView) rowView.findViewById(R.id.weatherConditionsTextView);
        weatherInfo.setText(forecastConditionsArray[position]);

        return rowView;
    }
}
