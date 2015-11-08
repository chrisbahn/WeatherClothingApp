package com.example.kevin.weatherclothingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.device.associates.AssociatesAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class SettingsActivity extends Activity{

    // Code researched and adapted from examples found on these pages:
    // http://mobilemerit.com/amazon-product-advertising-api-tutorial/
    // https://developer.amazon.com/public/apis/earn/mobile-associates/docs/setting-up-your-environment
    // https://developer.amazon.com/appsandservices/apis/earn/mobile-associates
    // http://developer.android.com/reference/android/webkit/WebView.html
    // https://developer.amazon.com/public/apis/earn/mobile-associates/javadocs/amazon-mobile-associates-javadoc
    // https://developer.amazon.com/public/apis/earn/mobile-associates/docs/direct-linking
//
    private static final String TAG = "Project1Test1";
    public final String STORE = "store";
    public final String CATEGORY = "category";
    public final String ITEM = "item";

    private TextView mStoreToSearchTextView;
    private TextView mCategoryToSearchTextView;
    private TextView mItemsToSearchTextView;

    RadioButton rbSearchAmazon;
    RadioButton rbSearchEtsy;
    RadioButton rbSearchMens;
    RadioButton rbSearchWomens;
    RadioButton rbSearchChildrens;

    RadioGroup radioGroupItemsToSearch;
    RadioButton rbSearchHats;
    RadioButton rbSearchCoats;
    RadioButton rbSearchShirts;
    RadioButton rbSearchDresses;
    RadioButton rbSearchPants;
    RadioButton rbSearchSuits;
    RadioButton rbSearchSocks;
    RadioButton rbSearchShoes;

    RadioGroup radioGroupStoreToSearch;
    RadioGroup radioGroupCategoryToSearch;
    String storeToSearch;
    String categoryType;
    String itemType;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        String APPLICATION_KEY = getKeyFromRawResource("APPLICATION_KEY");
        AssociatesAPI.initialize(new AssociatesAPI.Config(APPLICATION_KEY, this));

        //---STORE TO SEARCH RADIOBUTTONS---
        radioGroupStoreToSearch = (RadioGroup) findViewById(R.id.store_to_search_radiogroup);
        rbSearchAmazon = (RadioButton) findViewById(R.id.store_to_search_amazon);
        rbSearchEtsy = (RadioButton) findViewById(R.id.store_to_search_etsy);
        radioGroupStoreToSearch.check(R.id.store_to_search_amazon);

        radioGroupStoreToSearch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbSearchAmazon.isChecked()) {
                    DisplayToast("You have selected Amazon!");
                    storeToSearch = "Amazon";
                } else if (rbSearchEtsy.isChecked()) {
                    DisplayToast("You have selected Etsy!");
                    storeToSearch = "Etsy";
                }
            }
        });

        //---CATEGORY TO SEARCH RADIOBUTTONS---
        mCategoryToSearchTextView = (TextView) findViewById(R.id.category_to_search_textview);

        radioGroupCategoryToSearch = (RadioGroup) findViewById(R.id.category_to_search_radiogroup);
        rbSearchMens = (RadioButton) findViewById(R.id.category_to_search_mens);
        rbSearchWomens = (RadioButton) findViewById(R.id.category_to_search_womens);
        rbSearchChildrens = (RadioButton) findViewById(R.id.category_to_search_childrens);
        radioGroupCategoryToSearch.check(R.id.category_to_search_mens);

        radioGroupCategoryToSearch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbSearchMens.isChecked()) {
                    DisplayToast("You have selected Men's Clothing!");
                    categoryType = "Men's Clothing";
                } else if (rbSearchWomens.isChecked()) {
                    DisplayToast("You have selected Women's Clothing!");
                    categoryType = "Women's Clothing";
                } else if (rbSearchChildrens.isChecked()) {
                    DisplayToast("You have selected Children & Baby Clothing!");
                    categoryType = "Children & Baby Clothing";
                }
            }
        });

        //---ITEMS TO SEARCH RADIOBUTTONS---
        mItemsToSearchTextView = (TextView) findViewById(R.id.items_to_search_textview);
        radioGroupItemsToSearch = (RadioGroup) findViewById(R.id.items_to_search_radiogroup);
        rbSearchHats = (RadioButton) findViewById(R.id.item_to_search_hats);
        rbSearchCoats = (RadioButton) findViewById(R.id.item_to_search_coats);
        rbSearchShirts = (RadioButton) findViewById(R.id.item_to_search_shirts);
        rbSearchDresses = (RadioButton) findViewById(R.id.item_to_search_dresses);
        rbSearchPants = (RadioButton) findViewById(R.id.item_to_search_pants);
        rbSearchSuits = (RadioButton) findViewById(R.id.item_to_search_suits);
        rbSearchSocks = (RadioButton) findViewById(R.id.item_to_search_socks);
        rbSearchShoes = (RadioButton) findViewById(R.id.item_to_search_shoes);
        radioGroupItemsToSearch.check(R.id.item_to_search_hats);

        radioGroupItemsToSearch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (rbSearchHats.isChecked()) {
                    DisplayToast("You have selected Hats!");
                    itemType = "Hats";
                } else if (rbSearchCoats.isChecked()) {
                    DisplayToast("You have selected Jackets and Coats!");
                    itemType = "Jackets and Coats";
                } else if (rbSearchShirts.isChecked()) {
                    DisplayToast("You have selected Shirts!");
                    itemType = "Shirts";
                } else if (rbSearchDresses.isChecked()) {
                    DisplayToast("You have selected Dresses and skirts!");
                    itemType = "Dresses and skirts";
                } else if (rbSearchPants.isChecked()) {
                    DisplayToast("You have selected Pants!");
                    itemType = "Pants";
                } else if (rbSearchSuits.isChecked()) {
                    DisplayToast("You have selected Suits, sport coats and blazers!");
                    itemType = "Suits, sport coats and blazers";
                } else if (rbSearchSocks.isChecked()) {
                    DisplayToast("You have selected Socks and hosiery!");
                    itemType = "Socks and hosiery";
                } else if (rbSearchShoes.isChecked()) {
                    DisplayToast("You have selected Shoes!");
                    itemType = "Shoes";
                }
            }
        });


        Button b = (Button) findViewById(R.id.clickbutton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // code runs when button clicked
                Log.e(TAG, "itemType: " + itemType + "; storeToSearch: " + storeToSearch + "; categoryType: " + categoryType);
                Intent i = getIntent();
                i.putExtra(storeToSearch, STORE);
                i.putExtra(categoryType, CATEGORY);
                i.putExtra(itemType, ITEM);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    private void DisplayToast(String msg) {
        Toast.makeText(getBaseContext(), msg,
                Toast.LENGTH_SHORT).show();
    }

    //gets the key from the raw file.
    private String getKeyFromRawResource(String requestedKey) {
        InputStream keyStream = null;
        if (requestedKey.equals("etsy_key")){
            keyStream = this.getResources().openRawResource(R.raw.etsykey);
        }
        else if (requestedKey.equals("key")) {
            keyStream = this.getResources().openRawResource(R.raw.key);
        }
        else if (requestedKey.equals("APPLICATION_KEY")) {
            keyStream = this.getResources().openRawResource(R.raw.amazon_application_key);
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

}
