package com.orosys.dayrate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.orosys.dayrate.library.RateStar;

public class MainActivity extends AppCompatActivity {
    private RateStar rateStar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rateStar = findViewById(R.id.layout);
        rateStar.setStarImage(R.drawable.star);
        rateStar.setRateStar(2.5f);
        rateStar.setEnableHalf(true);
        rateStar.setSpace(100);
        rateStar.setRateCount(5);
        rateStar.setBgAlpha(0.3f);
    }
}
