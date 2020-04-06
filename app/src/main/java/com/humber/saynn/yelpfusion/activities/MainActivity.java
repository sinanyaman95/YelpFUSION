package com.humber.saynn.yelpfusion.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.humber.saynn.yelpfusion.R;
import com.humber.saynn.yelpfusion.adapters.BusinessAdapter;
import com.humber.saynn.yelpfusion.entities.Business;
import com.humber.saynn.yelpfusion.service.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://api.yelp.com/v3";
    private static final String BUSINESS_URL = "/businesses/north-india-restaurant-san-francisco";
    private static final String SEARCH_URL = "/businesses/search?term=delis&latitude=";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 99;
    private static final String API_KEY = "rcZTLdXbUfdghaFUQu-On0AlNdn3_zfV4EMd1_UkVizGgfVcrW49lljmumdgbhFNFE46p7DJJW6m--H0QSXq67uH5-kUDowWzgY72V1hQcV9f5JwRVGUJGSGG1CKXnYx";


    RecyclerView businessRecycler;
    ArrayList<Business> businesses;
    RequestQueue q;
    LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        businesses = new ArrayList<>();
        businessRecycler = findViewById(R.id.businessRecycler);
        currentLocation = getLatLng();
        RequestQueueSingleton requestQueueSingleton = RequestQueueSingleton.getInstance(this);
        q = requestQueueSingleton.getRequestQueue();
        businesses = getBusinessData(q);

        BusinessAdapter businessAdapter = new BusinessAdapter(this, businesses);
        businessRecycler.setAdapter(businessAdapter);
        businessRecycler.setLayoutManager(new LinearLayoutManager(this));
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
        Location location = getLastKnownLocation();
        if (location != null) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            return null;
        }


        return latLng;
    }

    private Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //Already handled
            }
            Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
            return bestLocation;
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
        Log.d("syDebug", "LatLng: " + currentLocation.latitude+"," + currentLocation.longitude);
        String search_url = BASE_URL + SEARCH_URL + currentLocation.latitude + "&longitude=" + currentLocation.longitude;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                search_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Business b = new Business();
                        try {
                            JSONArray jsonArray = new JSONArray("businesses");
                            for(int i = 0; i < jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);
                                b.setName(object.getString("name"));
                                Log.d("syDebug","Business name: " + b.getName());
                                b.setImage(getImageBitmap(object.getString("image_url")));
                                JSONObject location = object.getJSONObject("location");
                                b.setLocation(location.getString("address1"));
                                b.setViews(object.getLong("review_count"));
                                JSONObject coordinates = object.getJSONObject("categories");
                                b.setLatlng(new LatLng(
                                        coordinates.getDouble("latitude"),
                                        coordinates.getDouble("longtitude")));
                                b.setRating(object.getDouble("rating"));
                                JSONArray categories = object.getJSONArray("categories");
                                JSONObject categoryObject = categories.getJSONObject(0);
                                b.setDescription(categoryObject.getString("Title"));
                                businesses.add(b);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + API_KEY);
                return params;
            }
        };
        q.add(jsonObjectRequest);
        return temp;
    }

    private Bitmap getImageBitmap(String imageUrl) {
        Bitmap bitmap;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            // Log exception
            return null;
        }
        return bitmap;

    }
}
