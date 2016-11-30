package com.example.bianca.caloriecounter.service;

import android.content.Context;
import android.util.Log;

import com.example.bianca.caloriecounter.content.Aliment;
import com.example.bianca.caloriecounter.content.User;
import com.example.bianca.caloriecounter.content.database.DatabaseSettings;
import com.example.bianca.caloriecounter.net.AlimentRestClient;
import com.example.bianca.caloriecounter.util.Cancellable;
import com.example.bianca.caloriecounter.util.OnErrorListener;
import com.example.bianca.caloriecounter.util.OnSuccessListener;
import com.example.bianca.caloriecounter.util.ResourceException;

import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by bianca on 19.11.2016.
 */

public class AlimentManager extends Observable{
    private static final String TAG = AlimentManager.class.getSimpleName();
    private final DatabaseSettings mDatabase;

    private ConcurrentMap<String, Aliment> mAliments = new ConcurrentHashMap<>();
    private final Context context;
    private AlimentRestClient mAlimentRestCLient;
    private String mToken;
    private User mUser;
    private AlimentRestClient alimentRestClient;
    private User currentUser;

    public AlimentManager(Context context) {
        this.context = context ;
        this.mDatabase = new DatabaseSettings(context);
    }

    public User getCurrentUser() {
        return null;

    }

    public void setAlimentClient(AlimentRestClient alimentRestClient) {
        this.alimentRestClient = alimentRestClient;
    }

    public Cancellable loginAsync( String username, String pass, final OnSuccessListener<String> successListener,
                                   final OnErrorListener errorListener){
        final User user = new User(username, pass);
        Log.d(TAG, "User: "+user.toString());
        return alimentRestClient.getToken(user,
                new OnSuccessListener<String>(){

                    @Override
                    public void onSuccess(String tok) {
                        mToken = tok;
                        if (mToken != null) {
                            user.setToken(mToken);
                            setCurrentUser(user);
                            mDatabase.saveUser(user);
                            successListener.onSuccess(mToken);
                        }else{
                            errorListener.onError(new ResourceException(new IllegalArgumentException("Invalid credentials!")));
                        }
                    }
                }
                ,errorListener);

    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        alimentRestClient.setUser(currentUser);
    }

    public void subscribeChangeListener() {
        //TODO: ADD socketCLient
    }

    public void unsubscribeChangeListener() {
        //TODO:
    }

    public Cancellable getAlimentAsync(String string, OnSuccessListener<Aliment> onSuccessListener, OnErrorListener onErrorListener) {
        
    }
}
