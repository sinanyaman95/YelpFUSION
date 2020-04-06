package com.humber.saynn.yelpfusion.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.humber.saynn.yelpfusion.R;

public class MainActivity extends AppCompatActivity {

    RecyclerView businessRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        businessRecycler = findViewById(R.id.businessRecycler);
    }
}
