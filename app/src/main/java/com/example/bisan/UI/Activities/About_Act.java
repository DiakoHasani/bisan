package com.example.bisan.UI.Activities;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bisan.R;

public class About_Act extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.act_about);

        Initials_Objects();

        UI_Initial();

    }


    private void Initials_Objects(){

    }

    private void UI_Initial(){

    }

}
