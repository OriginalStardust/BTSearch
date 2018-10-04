package com.sunchang.callhtmltest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LaunchActivity extends AppCompatActivity {

    private boolean logined;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("login_status", MODE_PRIVATE);
                MyApplication.LOGIN_STATUS[0] = pref.getBoolean(MainActivity.DAYANGD, false);
                MyApplication.LOGIN_STATUS[1] = pref.getBoolean(MainActivity.LONGBAIDU, false);
                MyApplication.LOGIN_STATUS[2] = pref.getBoolean(MainActivity.UOL78, false);
                MyApplication.LOGIN_STATUS[3] = pref.getBoolean(MainActivity.SY9D, false);
                MyApplication.LOGIN_STATUS[4] = pref.getBoolean(MainActivity.I080, false);
                for (boolean b : MyApplication.LOGIN_STATUS) {
                    if (b) {
                        logined = true;
                    }
                }
                if (logined) {
                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    LaunchActivity.this.startActivity(intent);
                    LaunchActivity.this.finish();
                } else {
                    Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
                    LaunchActivity.this.startActivity(intent);
                    LaunchActivity.this.finish();
                }
            }
        }).start();
    }
}
