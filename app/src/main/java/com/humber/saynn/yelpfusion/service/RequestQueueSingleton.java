package com.humber.saynn.yelpfusion.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.humber.saynn.yelpfusion.activities.MainActivity;

public class RequestQueueSingleton {
    private static RequestQueueSingleton instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    private RequestQueueSingleton(Context context){
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized RequestQueueSingleton getInstance(Context context){
        if(instance == null){
            instance = new RequestQueueSingleton(context);
        }
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void make(Context ctx, String query, Response.Listener<String>
            listener, Response.ErrorListener errorListener) {
        String location = "latitude=37.786942&longitude=-122.399643";
        String url = MainActivity.BASE_URL + MainActivity.AUTOCOMPLETE_URL + location;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                listener, errorListener);
        RequestQueueSingleton.getInstance(ctx).addToRequestQueue(stringRequest);
    }

}
