package com.example.bianca.caloriecounter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bianca.caloriecounter.content.Aliment;
import com.example.bianca.caloriecounter.util.Cancellable;
import com.example.bianca.caloriecounter.util.DialogUtils;
import com.example.bianca.caloriecounter.util.OnErrorListener;
import com.example.bianca.caloriecounter.util.OnSuccessListener;

/**
 * Created by bianca on 30.11.2016.
 */
public class AlimentDetailFragment extends Fragment {
    public static final String ALIMENT_NAME = "aliment_name";
    private static final String TAG = AlimentDetailFragment.class.getSimpleName();
    ;
    private Bundle arguments;
    private Aliment aliment;

    private App myApp;

    private Cancellable fetchAlimAsync;
    private TextView alimentsTextView;
    private CollapsingToolbarLayout appBarLayout;


    Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);
        myApp = (App) context.getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ALIMENT_NAME)) {
            // In a real-world scenario, use a Loader
            // to load content from a content provider.
            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.aliment_detail, container, false);
        alimentsTextView = (TextView) rootView.findViewById(R.id.aliment_text);
        fillAlimentDetails();
        fetchAlimentAsync();
        return rootView;
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    private void fetchAlimentAsync() {
        fetchAlimAsync = myApp.getAlimentManager().getAlimentAsync(
                getArguments().getString(ALIMENT_NAME),
                new OnSuccessListener<Aliment>() {

                    @Override
                    public void onSuccess(final Aliment al) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                aliment = al;
                                fillAlimentDetails();
                            }
                        });
                    }
                }, new OnErrorListener() {

                    @Override
                    public void onError(final Exception e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogUtils.showError(getActivity(), e);
                            }
                        });
                    }
                });
    }

    private void fillAlimentDetails() {
        if (aliment != null) {
            if (appBarLayout != null) {
                appBarLayout.setTitle(aliment.getName());
            }
            alimentsTextView.setText(aliment.getName());
        }
    }
}
