package com.humber.saynn.yelpfusion.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.humber.saynn.yelpfusion.R;
import com.humber.saynn.yelpfusion.entities.Business;
import com.humber.saynn.yelpfusion.service.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://api.yelp.com/v3";
    private static final String BUSINESS_URL = "/businesses/north-india-restaurant-san-francisco";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    private final String API_KEY = getString(R.string.api_key);


    RecyclerView businessRecycler;
    ArrayList<Business> businesses;
    RequestQueue q;
    LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        businesses = new ArrayList<>();
        currentLocation = getLatLng();
        businessRecycler = findViewById(R.id.businessRecycler);
        q = RequestQueueSingleton.getInstance(this).getRequestQueue();
        businesses = getBusinessData(q);
    }

    private LatLng getLatLng() {
        LatLng latLng;
        Context ctx = getApplicationContext();
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latLng = new LatLng(location.getLatitude(),location.getLongitude());

        return latLng;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                currentLocation = getLatLng();
            }
        }
    }

    private ArrayList<Business> getBusinessData(RequestQueue q) {
        ArrayList<Business> temp = new ArrayList<>();
        String business_url = BASE_URL + BUSINESS_URL;
        final JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("Bearer", API_KEY);
            jsonBody.put("longitude", currentLocation.longitude);
            jsonBody.put("latitude", currentLocation.latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                business_url,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("sy",response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }



        ){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
            };





        return temp;
    }
}
