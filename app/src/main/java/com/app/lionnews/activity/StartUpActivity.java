package com.app.lionnews.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.lionnews.R;

public class StartUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0));
        setContentView(R.layout.activity_start_up);
        if(Math.random() >= 0.5)
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        else
            startActivity(new Intent(getApplicationContext(), BrowserActivity.class));

    }
}
