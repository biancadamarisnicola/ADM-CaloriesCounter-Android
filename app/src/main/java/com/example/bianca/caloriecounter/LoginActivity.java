package com.example.bianca.caloriecounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.bianca.caloriecounter.content.User;
import com.example.bianca.caloriecounter.service.AlimentManager;
import com.example.bianca.caloriecounter.util.Cancellable;
import android.support.design.widget.FloatingActionButton;

public class LoginActivity extends AppCompatActivity {
    private Cancellable mCancellable;
    private AlimentManager mAlimentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAlimentManager = ((App) getApplication()).getAlimentManager();
        User user = mAlimentManager.getCurrentUser();
        if (user != null) {
            startSearchAlimentActivity();
            finish();
        }
        setupToolbar();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCancellable != null) {
            mCancellable.cancel();
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                Snackbar.make(view, "Authenticating, please wait", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Action", null).show();
            }
        });
    }

    private void login() {
        EditText usernameEditText = (EditText) findViewById(R.id.username);
        EditText passwordEditText = (EditText) findViewById(R.id.password);
        mCancellable = mNoteManager
                .loginAsync(
                        usernameEditText.getText().toString(), passwordEditText.getText().toString(),
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String s) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startNoteListActivity();
                                    }
                                });
                            }
                        }, new OnErrorListener() {
                            @Override
                            public void onError(final Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        DialogUtils.showError(LoginActivity.this, e);
                                    }
                                });
                            }
                        });
    }

    private void startSearchAlimentActivity() {
        startActivity(new Intent(this, SearchAlimentActivity.class));
    }
}