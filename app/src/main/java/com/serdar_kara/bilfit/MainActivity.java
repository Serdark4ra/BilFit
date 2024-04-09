package com.serdar_kara.bilfit;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.serdar_kara.bilfit.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.Navi.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_leaderBoard) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (item.getItemId() == R.id.navigation_program) {
                Intent intent = new Intent(MainActivity.this, ProgramActivity.class);
                startActivity(intent);
                finish();
            } else if (item.getItemId() == R.id.navigation_friends) {
                Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
                startActivity(intent);
                finish();
            } else if (item.getItemId() == R.id.navigation_report) {
                Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                startActivity(intent);
                finish();
            }
            return false;
        });
    }



}