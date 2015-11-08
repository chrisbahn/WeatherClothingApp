package com.example.kevin.weatherclothingapp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

// code on retrieval of user's current location is modified from this page: http://developer.android.com/training/location/retrieve-current.html and https://github.com/googlesamples/android-play-location/blob/master/BasicLocationSample/app/src/main/java/com/google/android/gms/location/sample/basiclocationsample/MainActivity.java
public class WeatherClothingActivity extends ListActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    protected static final String TAG = "WeatherClothingActivity";

    ListView weatherList;
    private static final int CHANGE_LOCATION_REQUEST_CODE = 0;
    private Button mChangeLocationButton;
    private static final String KEY_INDEX = "index";
    String latitude;
    String longitude;


    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */
    protected static Location mLastLocation;

    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    private String newCityName;
    private String newCityZMW;
    private boolean isNewCity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_clothing);

//        mLatitudeLabel = getResources().getString(R.string.latitude_label);
//        mLongitudeLabel = getResources().getString(R.string.longitude_label);
//        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
//        mLongitudeText = (TextView) findViewById((R.id.longitude_text));

        buildGoogleApiClient();

        APICreator apic = new APICreator(this);
        ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
        ArrayList<String> dayList = new ArrayList<>();

        //This runs for the even numbers, because that's how WeatherUnderground sets their days (odd numbers are nights).
        for (int i = 0; i <= 6; i = i + 2) {
            Bitmap weatherIcon = apic.getForecastIcon(i);
            String day = apic.getForecastDay(i);
            bitmapArrayList.add(weatherIcon);
            dayList.add(day);
        }

        //sets the list Adapter.
        CustomListAdapter adapter = new CustomListAdapter(this, dayList, bitmapArrayList);
        weatherList = (ListView) findViewById(android.R.id.list);
        weatherList.setAdapter(adapter);
        //TODO set the location fragment and display it.

        //TODO set the change clothing options fragment and display it.

        weatherList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //creates fragments that show the options for the dates which have been selected by the user.
                FragmentManager fm;
                fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                EtsyFragment ef = new EtsyFragment();
                ft.replace(android.R.id.content, ef);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        // todo this button launches ChangeLocationActivity, to change the location called by weather underground.
        mChangeLocationButton = (Button)findViewById(R.id.change_location_button);
        mChangeLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launchChangeLocationActivity = new Intent(WeatherClothingActivity.this, ChangeLocationActivity.class);
                // this means we expect some result to be returned to the activity
                startActivityForResult(launchChangeLocationActivity, CHANGE_LOCATION_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if (requestCode == CHANGE_LOCATION_REQUEST_CODE && resultCode == RESULT_OK) {
            // get data from extra needed to change city
            newCityZMW = data.getStringExtra(ChangeLocationActivity.EXTRA_NEW_LOCATION_ZMW);
            newCityName = data.getStringExtra(ChangeLocationActivity.EXTRA_NEW_LOCATION_CITYNAME);
            isNewCity = true;
            if (isNewCity) { // this if statement toggles whether forecast is pulled from user's own location, or the newly chosen location
                // todo Create new forecast based on this new data
            }
        }
    }


    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putString(KEY_INDEX, latitude);
        savedInstanceState.putString(KEY_INDEX, longitude);
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = String.valueOf(mLastLocation.getLatitude());
            longitude = String.valueOf(mLastLocation.getLongitude());
        } else {
            Toast.makeText(this, R.string.no_location_detected, Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
}