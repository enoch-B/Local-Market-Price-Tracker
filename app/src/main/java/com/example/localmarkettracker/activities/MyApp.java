// package com.example.localmarkettracker;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

public class MyApp extends Application {

    // This method is called before any other component of your application (like Activities) 
    // are created. It's the correct place to initialize MultiDex.
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // Call MultiDex.install() to load secondary dex files
        MultiDex.install(this); 
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // You can add other application-wide initialization logic here
    }
}