package com.example.bianca.caloriecounter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bianca.caloriecounter.content.Aliment;
import com.example.bianca.caloriecounter.util.Cancellable;
import com.example.bianca.caloriecounter.util.DialogUtils;
import com.example.bianca.caloriecounter.util.OnErrorListener;
import com.example.bianca.caloriecounter.util.OnSuccessListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by bianca on 30.11.2016.
 */
public class AlimentDetailFragment extends Fragment {
    public static final String ALIMENT_NAME = "aliment_name";
    private static final String TAG = AlimentDetailFragment.class.getSimpleName();
    private Bundle arguments;
    private Aliment aliment;
    private PieChart mChart;

    private App myApp;

    private Cancellable fetchAlimAsync;
    private LinearLayout alimentView;
    TextView alimentTextView;
    private CollapsingToolbarLayout appBarLayout;

    private FloatingActionButton editFab;
    private FloatingActionButton deleteFab;
    private ImageView warnind;
    private boolean networkOnline;
//    private int id = getResources().getIdentifier("@:drawable/junk_food.jpg", null, null);

    public AlimentDetailFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);
        myApp = (App) context.getApplicationContext();
        mChart = new PieChart(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ALIMENT_NAME)) {
            // In a real-world scenario, use a Loader
            // to load content from a content provider.
            Activity activity = this.getActivity();
            warnind = (ImageView) activity.findViewById(R.id.warning_image);
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.warning);
            warnind.setAnimation(animation);
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            editFab = (FloatingActionButton) activity.findViewById(R.id.edit_fab);
            editFab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d(TAG, "edit aliment");
                    Context context = v.getContext();
                    Intent intent = new Intent(context, EditAlimentActivity.class);
                    intent.putExtra("Aliment", String.valueOf(aliment));
                    context.startActivity(intent);
                }
            });
            deleteFab = (FloatingActionButton) activity.findViewById(R.id.delete_fab);
            deleteFab.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {
                    Log.d(TAG, "delete aliment");
                    fetchAlimAsync = myApp.getAlimentManager().deleteAlimentAsync(
                            aliment.getName(),
                            new OnSuccessListener<Aliment>() {

                                @Override
                                public void onSuccess(final Aliment al) {
                                    Log.d(TAG, "redirect to SearchAlimentActivity");
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(v.getContext(), SearchAlimentActivity.class));
                                        }
                                    });
                                }
                            }, new OnErrorListener() {

                                @Override
                                public void onError(final Exception e) {
                                    Log.d(TAG, e.toString());
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DialogUtils.showError(getActivity(), e);
                                        }
                                    });
                                }
                            });
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.aliment_detail, container, false);
        alimentView = (LinearLayout) rootView.findViewById(R.id.aliment_text);
        alimentTextView = (TextView) rootView.findViewById(R.id.text_aliment);
        mChart = (PieChart) rootView.findViewById(R.id.pieChart1);
        mChart.setUsePercentValues(true);
        mChart.setDrawEntryLabels(true);
        mChart.setHoleRadius(20);
        mChart.setTransparentCircleRadius(20);
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
        if (isNetworkConnected()) {
            Log.d(TAG, "fetch aliment async");
            Log.d(TAG, "Online mode");
            fetchAlimAsync = myApp.getAlimentManager().getAlimentAsync(
                    getArguments().getString(ALIMENT_NAME),
                    new OnSuccessListener<Aliment>() {

                        @Override
                        public void onSuccess(final Aliment al) {
                            Log.d(TAG, "CHECK");
                            Log.d(TAG, al.toString());
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
                            Log.d(TAG, e.toString());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DialogUtils.showError(getActivity(), e);
                                }
                            });
                        }
                    });
        } else {
            Log.d(TAG, "Offline mode - fetch from DB");
            aliment = myApp.getAlimentManager().getAlimentFromDatabase(getArguments().getString(ALIMENT_NAME));
            fillAlimentDetails();
    }
    }

    protected boolean isNetworkConnected() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            return mConnectivityManager.getActiveNetworkInfo() != null;

        }catch (NullPointerException e){
            return false;

        }
    }

    private void fillAlimentDetails() {
        if (aliment != null) {
            Log.d(TAG, aliment.toString());
            if (appBarLayout != null) {
                appBarLayout.setTitle(aliment.getName());
                Log.d(TAG, "Title "+String.valueOf(appBarLayout.getTitle()));
            }
            alimentTextView.setText(aliment.toStringFancy());
            if (aliment.getFats()> 15.0){
                Log.d(TAG, "JUNG_FOOD");
                warnind.setImageResource(R.drawable.junk_food);
            }
            addDataToChart(aliment);
        }else{
            Log.d(TAG, "Aliment is null");
        }

    }

    private void addDataToChart(Aliment aliment) {
        ArrayList<LegendEntry> xValues = new ArrayList<>();
        ArrayList<PieEntry> yValues = new ArrayList<>();
        xValues.add(new LegendEntry("PROTEINS", Legend.LegendForm.DEFAULT, 1, 1, null, 1));
        xValues.add(new LegendEntry("FATS", Legend.LegendForm.DEFAULT, 1, 1, null, 1));
        xValues.add(new LegendEntry("CARBS", Legend.LegendForm.DEFAULT, 1, 1, null, 1));
        yValues.add(new PieEntry((float) aliment.getProteins()));
        yValues.add(new PieEntry((float) aliment.getFats()));
        yValues.add(new PieEntry((float) aliment.getCarbs()));

        PieDataSet pieDataSet = new PieDataSet(yValues, "Proteins Fats Carbs");
        pieDataSet.setSliceSpace(3);
        pieDataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            colors.add(c);
        }
        colors.add(ColorTemplate.getHoloBlue());
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);

        Legend legend = mChart.getLegend();
        legend.setEntries(xValues);
        mChart.setData(pieData);
        mChart.setDescription(new Description());
    }
}
