package com.example.kevin.weatherclothingapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kevin on 11/4/2015.
 */
public class CustomEtsyListAdapter extends ArrayAdapter<String> {

    public Activity mContext;

    public final String TAG = "CustomEtsyListAdapter";

    TextView mTitle;
    TextView mPrice;
    TextView mDescription;
    ImageView mEtsyImage;

    ArrayList<String> mTitleArrayList = new ArrayList<>();
    ArrayList<String> mPriceArrayList = new ArrayList<>();
    ArrayList<Bitmap> mEtsyImageArrayList = new ArrayList<>();


    public CustomEtsyListAdapter(Activity context, ArrayList<String> titleList, ArrayList<String> priceList, ArrayList<Bitmap> imageList) {
        super(context, R.layout.etsy_list, titleList);

        // TODO Auto-generated constructor stub
        this.mTitleArrayList = titleList;
        this.mPriceArrayList = priceList;
        this.mEtsyImageArrayList = imageList;
        this.mContext = context;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=mContext.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.etsy_list, null, true);

        //The UI view cannot read ArrayLists, so the ALs have to be converted into Arrays.
        String[] mTitleList = mTitleArrayList.toArray(new String[mTitleArrayList.size()]);
        String[] mPriceList = mPriceArrayList.toArray(new String[mPriceArrayList.size()]);
        Bitmap[] mImageList = mEtsyImageArrayList.toArray(new Bitmap[mEtsyImageArrayList.size()]);

        System.out.println(mTitleList[position]);

        if(mTitleList == null){
            Log.e(TAG, "mTitleList.");
        }

        mTitle = (TextView) rowView.findViewById(R.id.etsyProductName);
        mTitle.setText(mTitleList[position]);

        mPrice = (TextView) rowView.findViewById(R.id.etsyProductPrice);
        mPrice.setText("$" + mPriceList[position]);

        mEtsyImage = (ImageView) rowView.findViewById(R.id.etsyImage);
        mEtsyImage.setImageBitmap(mImageList[position]);

        return rowView;
    }
}
