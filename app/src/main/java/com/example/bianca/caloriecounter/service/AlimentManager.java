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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by bianca on 19.11.2016.
 */

public class AlimentManager extends Observable {
    private static final String TAG = AlimentManager.class.getSimpleName();
    private final DatabaseSettings mDatabase;

    private ConcurrentMap<String, Aliment> aliments = new ConcurrentHashMap<>();
    private final Context context;
    private AlimentRestClient mAlimentRestCLient;
    private String mToken;
    private User mUser;
    private AlimentRestClient alimentRestClient;
    private User currentUser;
    private String alimentsLastUpdate;

    public AlimentManager(Context context) {
        this.context = context;
        this.mDatabase = new DatabaseSettings(context);
    }

    public User getCurrentUser() {
        return null;

    }

    public void setAlimentClient(AlimentRestClient alimentRestClient) {
        this.alimentRestClient = alimentRestClient;
    }

    public Cancellable loginAsync(String username, String pass, final OnSuccessListener<String> successListener,
                                  final OnErrorListener errorListener) {
        final User user = new User(username, pass);
        Log.d(TAG, "User: " + user.toString());
        return alimentRestClient.getToken(user,
                new OnSuccessListener<String>() {

                    @Override
                    public void onSuccess(String tok) {
                        mToken = tok;
                        if (mToken != null) {
                            user.setToken(mToken);
                            setCurrentUser(user);
                            mDatabase.saveUser(user);
                            successListener.onSuccess(mToken);
                        } else {
                            errorListener.onError(new ResourceException(new IllegalArgumentException("Invalid credentials!")));
                        }
                    }
                }
                , errorListener);

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

    public Cancellable getAlimentAsync(final String name, final OnSuccessListener<Aliment> onSuccessListener,
                                       final OnErrorListener onErrorListener) {
        Log.d(TAG, "get aliment async");
        return alimentRestClient.readAsync(name, new OnSuccessListener<Aliment>() {

                    @Override
                    public void onSuccess(Aliment aliment) {
                        Log.d(TAG, "read aliment async succedded");
                        if (aliment == null) {
                            setChanged();
                            aliments.remove(name);
                        } else {
                            Log.d(TAG, "Aliment not null");
                            if (!aliment.equals(aliments.get(aliment.getName()))) {
                                setChanged();
                                aliments.put(name, aliment);
                                Log.d(TAG, name + " fetched");
                            }
                        }
                        onSuccessListener.onSuccess(aliment);
                        notifyObservers();
                    }
                }
                , onErrorListener);
    }

    public Cancellable deleteAlimentAsync(final String name, final OnSuccessListener<Aliment> onSuccessListener,
                                       final OnErrorListener onErrorListener) {
        Log.d(TAG, "delete aliment async");
        return alimentRestClient.deleteAsync(name, new OnSuccessListener<Aliment>() {

                    @Override
                    public void onSuccess(Aliment aliment) {
                        Log.d(TAG, "delete aliment async succedded");
                        onSuccessListener.onSuccess(aliment);
                        notifyObservers();
                    }
                }
                , onErrorListener);
    }

    public Cancellable saveAlimentAsync(final Aliment aliment, boolean update, final OnSuccessListener<Aliment> onSuccessListener,
                                        final OnErrorListener onErrorListener) {
        Log.d(TAG, "save aliment async");
        return alimentRestClient.saveAsync(aliment, update, new OnSuccessListener<Aliment>() {

                    @Override
                    public void onSuccess(Aliment aliment) {
                        Log.d(TAG, "save aliment async succedded");
                        onSuccessListener.onSuccess(aliment);
                        notifyObservers();
                    }
                }
                , onErrorListener);
    }

    public Cancellable getAlimentsAsync(final OnSuccessListener<List<Aliment>> onSuccessListener,
                                        final OnErrorListener onErrorListener) {
        Log.d(TAG, "get aliments Async...");
        return alimentRestClient.searchAsync(alimentsLastUpdate, new OnSuccessListener<List<Aliment>>() {
            @Override
            public void onSuccess(List<Aliment> result) {
                Log.d(TAG, "get aliments async succeeded");
                Log.d(TAG, String.valueOf(result.size()));
                List<Aliment> alim = result;
                if (alim != null) {
                    updateCachedNotes(alim);
                } else {
                    Log.d(TAG, "Aliment list is null");
                }
                onSuccessListener.onSuccess(cachedNotesByUpdated());
                notifyObservers();
            }
        }, onErrorListener);
    }

    private List<Aliment> cachedNotesByUpdated() {
        List<Aliment> alim = new ArrayList<>(aliments.values());
        Log.d(TAG, String.valueOf(alim.size()));
        Collections.sort(alim, new AlimentsComparator());
        return alim;
    }

    private void updateCachedNotes(List<Aliment> alim) {
        Log.d(TAG, "updateCachedNotes");
        for (Aliment a : alim) {
            aliments.put(a.getName(), a);
        }
        setChanged();
    }

    private class AlimentsComparator implements java.util.Comparator<Aliment> {
        @Override
        public int compare(Aliment n1, Aliment n2) {
            return (n1.getName().compareTo(n2.getName()));
        }
    }
}
