package com.sunchang.callhtmltest;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2018/6/20.
 */

public class MyApplication extends Application {

    public static boolean[] LOGIN_STATUS = new boolean[5];

    public static SharedPreferences.Editor status_editor;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        status_editor = context.getSharedPreferences("login_status", MODE_PRIVATE).edit();
    }

    public static Context getContext() {
        return context;
    }
}
