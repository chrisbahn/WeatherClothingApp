package com.example.kevin.weatherclothingapp;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONObject;
import java.util.ArrayList;

//this is the fragment calleed by WeatherClothingActivity when a date is selected.
public class EtsyActivity extends ListActivity{

    ListView lv;

    JSONObject jso;

    private final String BUNDLE = "com.example.kevin.weatherclothingapp";
    ArrayList<String> mEtsyProductNameArray = new ArrayList<>();
    ArrayList<String> mEtsyProductCostArray = new ArrayList<>();
    ArrayList<Bitmap> mEtsyProductImageArray = new ArrayList<>();

    public final String CATEGORY = "category";
    public final String ITEM = "item";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etsy);

        Intent intent = getIntent();

        Bundle b = intent.getBundleExtra(BUNDLE);

        String searchCategory = b.getString(CATEGORY);
        String searchItem = b.getString(ITEM);

        APICreator apic = new APICreator(this);
        jso = apic.getEtsyJSONObject(searchCategory, searchItem);
        for (int i = 0; i <=4; i++){
            mEtsyProductNameArray.add(apic.getEtsyString(jso, i, "title"));
            mEtsyProductCostArray.add(apic.getEtsyString(jso, i, "price"));
            String url = apic.getEtsyURLString(jso, i, "url_75x75");
            mEtsyProductImageArray.add(apic.getEtsyImage(url));
        }

        if (mEtsyProductNameArray == null){
            Log.e("EtsyActivity.class", "imageArrayURLList in EtsyActivity class is null.");
        }

        CustomEtsyListAdapter cela = new CustomEtsyListAdapter(this, mEtsyProductNameArray, mEtsyProductCostArray, mEtsyProductImageArray);
        lv = (ListView)findViewById(android.R.id.list);
        lv.setAdapter(cela);

    }
}
