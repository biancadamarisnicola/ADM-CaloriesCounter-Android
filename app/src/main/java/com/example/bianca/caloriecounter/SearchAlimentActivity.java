package com.example.bianca.caloriecounter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import java.util.List;

public class SearchAlimentActivity extends AppCompatActivity {

    public static final String TAG = SearchAlimentActivity.class.getSimpleName();
    private App myApp;
    private boolean twoPane;
    private Cancellable getAlimentsAsyncCall;
    private View contentLoadingView;
    private RecyclerView recyclerView;
    private boolean alimentLoaded;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        myApp = (App) getApplication();
        setContentView(R.layout.activity_search_aliment);
//        Intent intent = getIntent();
//        String message = intent.getStringExtra(App.EXTRA_MESSAGE);
//        TextView textView = new TextView(this);
//        textView.setTextSize(40);
//        textView.setText(message);
        //setupToolbar();
        setupFloatingActionBar();
        setupRecyclerView();
        checkTwoPaneMode();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onSTart");
        super.onStart();
        startGetAlimentsAsync();
        myApp.getAlimentManager().subscribeChangeListener();
    }

    private void startGetAlimentsAsync() {
        if (alimentLoaded) {
            Log.d(TAG, "start startGetAlimentsAsync - content already loaded, return");
            return;
        }
        showLoadingIndicator();
        getAlimentsAsyncCall = myApp.getAlimentManager().getAlimentsAsync(
                new OnSuccessListener<List<Aliment>>() {
                    @Override
                    public void onSuccess(final List<Aliment> aliments) {
                        Log.d(TAG, "getAlimentsAsyncCall - success");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, aliments.toString());
                                showContent(aliments);
                            }
                        });
                    }
                }, new OnErrorListener() {
                    @Override
                    public void onError(final Exception e) {
                        Log.d(TAG, "getAlimentsAsyncCall - error");
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

    private void showError(Exception e) {
        Log.e(TAG, "showError", e);
        if (contentLoadingView.getVisibility() == View.VISIBLE) {
            contentLoadingView.setVisibility(View.GONE);
        }
        DialogUtils.showError(this, e);
    }

    private void showContent(List<Aliment> aliments) {
        Log.d(TAG, "showContent");
        recyclerView.setAdapter(new AlimentRecyclerViewAdapter(aliments));
        contentLoadingView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoadingIndicator() {
        Log.d(TAG, "showLoadingIndicator");
        recyclerView.setVisibility(View.GONE);
        contentLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        //ensureGetNotesAsyncTaskCancelled();
        ensureGetAlimentsAsyncCallCancelled();
        myApp.getAlimentManager().unsubscribeChangeListener();
    }

    private void ensureGetAlimentsAsyncCallCancelled() {
        if (getAlimentsAsyncCall != null) {
            Log.d(TAG, "ensureGetAlimentsAsyncCallCancelled - cancelling the task");
            getAlimentsAsyncCall.cancel();
        }
    }

    private void checkTwoPaneMode() {
        if (findViewById(R.id.aliment_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true;
        }
    }

    private void setupRecyclerView() {
        contentLoadingView = findViewById(R.id.content_loading);
        recyclerView = (RecyclerView) findViewById(R.id.aliment_list);
    }

    private void setupFloatingActionBar() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
    }

    private class AlimentRecyclerViewAdapter extends RecyclerView.Adapter<AlimentRecyclerViewAdapter.ViewHolder> {

        private final List<Aliment> aliments;

        public AlimentRecyclerViewAdapter(List<Aliment> alims) {
            aliments = alims;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.aliment_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.item = aliments.get(position);
            holder.idView.setText(aliments.get(position).getName());
           // holder.contentView.setText((int) aliments.get(position).getCalories());
            holder.contentView.setText(String.valueOf(aliments.get(position).getCalories()));

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (twoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(AlimentDetailFragment.ALIMENT_NAME, holder.item.getName());
                        AlimentDetailFragment fragment = new AlimentDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.aliment_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, AlimentDetailActivity.class);
                        intent.putExtra(AlimentDetailFragment.ALIMENT_NAME, holder.item.getName());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return aliments.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View view;
            public final TextView idView;
            public final TextView contentView;
            public Aliment item;

            public ViewHolder(View view) {
                super(view);
                this.view = view;
                idView = (TextView) view.findViewById(R.id.name);
                contentView = (TextView) view.findViewById(R.id.calories);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + contentView.getText() + "'";
            }
        }
    }
}
