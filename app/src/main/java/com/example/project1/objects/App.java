package com.example.project1.objects;

import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MSPV.initHelper(this);

    }
}
