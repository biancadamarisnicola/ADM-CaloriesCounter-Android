package com.example.bianca.caloriecounter.service;

import com.example.bianca.caloriecounter.content.User;
import com.example.bianca.caloriecounter.content.database.DatabaseSettings;

import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by bianca on 19.11.2016.
 */

public class AlimentManager extends Observable{
    private static final String TAG = NoteManager.class.getSimpleName();
    private final DatabaseSettings mDatabase;

    private ConcurrentMap<String, Aliment> mAliments = new ConcurrentHashMap<>();
    private final Context context;
    private AlimentRestClient mAlimentRestCLient;
    private String mToken;
    private User mUser;

    public AlimentManager(Context context) {
        this.context = context ;
        this.mDatabase = new DatabaseSettings(context);
    }

    public User getCurrentUser() {
        return null;

    }
}
