package com.example.bianca.caloriecounter;

import android.app.Application;
import android.util.Log;

import com.example.bianca.caloriecounter.service.AlimentManager;

public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    protected static final String EXTRA_MESSAGE = "com.example.caloriecounter.MESSAGE";
    private AlimentManager mAlimentManager;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        mAlimentManager = new AlimentManager(this);
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "onTerminate");
        super.onTerminate();
    }

    public AlimentManager getAlimentManager() {
        return mAlimentManager;
    }
}
