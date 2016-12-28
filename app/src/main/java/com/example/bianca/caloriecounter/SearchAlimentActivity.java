package com.example.bianca.caloriecounter;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bianca.caloriecounter.content.Aliment;
import com.example.bianca.caloriecounter.util.Cancellable;
import com.example.bianca.caloriecounter.util.DialogUtils;
import com.example.bianca.caloriecounter.util.OnErrorListener;
import com.example.bianca.caloriecounter.util.OnSuccessListener;

import java.util.List;

public class SearchAlimentActivity extends AppCompatActivity implements SensorEventListener {

    public static final String TAG = SearchAlimentActivity.class.getSimpleName();
    private App myApp;
    private boolean twoPane;
    private Cancellable getAlimentsAsyncCall;
    private View contentLoadingView;
    private RecyclerView recyclerView;
    private boolean alimentLoaded;
    private AlimentRecyclerViewAdapter adapter;

    private SensorManager sensorManager;
    private TextView count;
    boolean activityRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        myApp = (App) getApplication();
        setContentView(R.layout.activity_search_aliment);

        count = (TextView) findViewById(R.id.step_count);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
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
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;
        // if you unregister the last listener, the hardware will stop detecting step events
//        sensorManager.unregisterListener(this);
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
                    public void onSuccess(final List<Aliment> alim) {
                        Log.d(TAG, "getAlimentsAsyncCall - success");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showContent(alim);
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
        adapter = new AlimentRecyclerViewAdapter(aliments);
        recyclerView.setAdapter(adapter);
        contentLoadingView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
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
                Log.d(TAG, "Create new aliment");
                Context context = view.getContext();
                Intent intent = new Intent(context, EditAlimentActivity.class);
                context.startActivity(intent);
            }
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            Log.d(TAG, "pedometer "+String.valueOf(event.values[0]));
            count.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class AlimentRecyclerViewAdapter extends RecyclerView.Adapter<AlimentRecyclerViewAdapter.ViewHolder> {

        private final List<Aliment> aliments;

        public AlimentRecyclerViewAdapter(List<Aliment> alims) {
            aliments = alims;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.aliment_list_content, parent, false);
            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder "+aliments.get(position));
            holder.item = aliments.get(position);
            holder.idView.setText(aliments.get(position).getName());
            holder.contentView.setText(" - calories: "+String.valueOf(aliments.get(position).getCalories()));

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

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return aliments.size();
        }


        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
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
