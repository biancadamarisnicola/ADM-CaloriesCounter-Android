package com.example.bianca.caloriecounter;

import android.app.Application;
import android.util.Log;

import com.example.bianca.caloriecounter.net.AlimentRestClient;
import com.example.bianca.caloriecounter.service.AlimentManager;

public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    protected static final String EXTRA_MESSAGE = "com.example.caloriecounter.MESSAGE";
    private AlimentManager alimentManager;
    private AlimentRestClient alimentRestClient;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        alimentManager = new AlimentManager(this);
        alimentRestClient = new AlimentRestClient(this);
        alimentManager.setAlimentClient(alimentRestClient);
    }


    public AlimentManager getAlimentManager() {
        return alimentManager;
    }

    @Override
    public void onTerminate() {
        Log.d(TAG, "onTerminate");
        super.onTerminate();
    }
}
