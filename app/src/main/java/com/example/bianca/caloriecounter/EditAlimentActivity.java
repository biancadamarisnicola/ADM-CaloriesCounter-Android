package com.example.bianca.caloriecounter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.bianca.caloriecounter.content.Aliment;
import com.example.bianca.caloriecounter.util.DialogUtils;
import com.example.bianca.caloriecounter.util.OnErrorListener;
import com.example.bianca.caloriecounter.util.OnSuccessListener;

import static android.view.View.VISIBLE;

/**
 * A login screen that offers login via email/password.
 */
public class EditAlimentActivity extends AppCompatActivity {

    private static final String TAG = EditAlimentActivity.class.getSimpleName();
    private EditText alimentName;
    private EditText calories;
    private EditText proteins;
    private EditText carbs;
    private EditText fats;
    private View mProgressView;
    private boolean update;

    private App myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = (App) getApplication();
        setContentView(R.layout.activity_edit_aliment);
        update = false;
        alimentName = (EditText) findViewById(R.id.aliment_name);
        calories = (EditText) findViewById(R.id.calories);
        proteins = (EditText) findViewById(R.id.proteins);
        carbs = (EditText) findViewById(R.id.carbs);
        fats = (EditText) findViewById(R.id.fats);
        String aliment = getIntent().getStringExtra("Aliment");
        if (aliment != null) {
            Log.d(TAG, aliment);
            parseAliment(aliment);
            update = true;
        }
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "save aliment");
                try {
                    saveAliment(view);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "cancel edit aliment");
                startActivity(new Intent(view.getContext(), SearchAlimentActivity.class));
            }
        });

        mProgressView = findViewById(R.id.save_progress);
    }

    private void parseAliment(String aliment) {
        String[] elems = aliment.split(",");
        String name = elems[0].split("=")[1];
        alimentName.setText(name.substring(1,name.length()-1));
        String cal = elems[1].split("=")[1];
        calories.setText(cal);
        String prot = elems[2].split("=")[1];
        proteins.setText(prot);
        String carb = elems[3].split("=")[1];
        carbs.setText(carb);
        String fat = elems[4].split("=")[1];
        fats.setText(fat.substring(0, fat.length()-1));
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void saveAliment(final View view) throws InterruptedException {
        alimentName.setError(null);
        calories.setError(null);
        proteins.setError(null);
        carbs.setError(null);
        fats.setError(null);

        // Store values at the time of the login attempt.
        String alimentNameString = alimentName.getText().toString();
        String caloriesString = calories.getText().toString();
        String protString = proteins.getText().toString();
        String carbsString = carbs.getText().toString();
        String fatString = fats.getText().toString();


        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(caloriesString)) {
            calories.setError(getString(R.string.error_field_required));
            focusView = calories;
            cancel = true;
        }
        if (TextUtils.isEmpty(alimentNameString)) {
            alimentName.setError(getString(R.string.error_field_required));
            focusView = alimentName;
            cancel = true;
        }
        if (TextUtils.isEmpty(protString)) {
            proteins.setError(getString(R.string.error_field_required));
            focusView = proteins;
            cancel = true;
        }
        if (TextUtils.isEmpty(carbsString)) {
            carbs.setError(getString(R.string.error_field_required));
            focusView = carbs;
            cancel = true;
        }
        if (TextUtils.isEmpty(fatString)) {
            fats.setError(getString(R.string.error_field_required));
            focusView = fats;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Thread.sleep(1000);
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mProgressView.setVisibility(VISIBLE);
            Aliment aliment = new Aliment(alimentName.getText().toString(), Double.valueOf(calories.getText().toString()), Double.valueOf(proteins.getText().toString()),
                    Double.valueOf(carbs.getText().toString()), Double.valueOf(fats.getText().toString()));
            myApp.getAlimentManager().saveAlimentAsync(aliment, update,
                    new OnSuccessListener<Aliment>() {
                        @Override
                        public void onSuccess(final Aliment aliment1) {
                            Log.d(TAG, "saveElement - success");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (aliment1 != null)
                                        Log.d(TAG, aliment1.toString());
                                    mProgressView.setVisibility(View.INVISIBLE);
                                    startActivity(new Intent(view.getContext(), SearchAlimentActivity.class));
                                }
                            });
                        }
                    }, new OnErrorListener() {
                        @Override
                        public void onError(final Exception e) {
                            Log.d(TAG, "saveElement - error");
                            runOnUiThread(new Runnable() {
                                @Override
                                    public void run() {
                                       showError(e);
                                }
                            });
                        }
                    }
            );
        }
    }

    private void showError(Exception e) {
        Log.e(TAG, "showError", e);
        if (mProgressView.getVisibility() == View.VISIBLE) {
            mProgressView.setVisibility(View.INVISIBLE);
        }
        if (isNetworkConnected()) {
            DialogUtils.showError(this, e);
        }else{
            DialogUtils.showError(this, new Exception("You are offline. Please check your internet connection"));
        }
    }

    protected boolean isNetworkConnected() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return mConnectivityManager.getActiveNetworkInfo() != null;

        }catch (NullPointerException e){
            return false;

        }
    }

}

