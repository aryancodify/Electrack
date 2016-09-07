package com.elecatrach.poc.electrack.admin.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.elecatrach.poc.electrack.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class AnalyticsFragment extends Fragment implements OnChartValueSelectedListener{

    private PieChart mChart;
    private Typeface tf;
    Boolean flag= true;


    public AnalyticsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_analytics, container, false);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mChart = (PieChart) v.findViewById(R.id.chart1);

       mChart.setUsePercentValues(false);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Regular.ttf");

        mChart.setCenterTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans-Light.ttf"));


        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        //mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setData();

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart.spin(2000, 0, 360, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);


        return v;
    }

    private void setData() {

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(3968f, 0));
        entries.add(new Entry(7781f, 1));
        entries.add(new Entry(6260f, 2));
        entries.add(new Entry(9599f, 3));


        PieDataSet dataset = new PieDataSet(entries, "# number of units");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        dataset.setDrawValues(true);

        // creating labels
        ArrayList<String> labels = new ArrayList<>();
        labels.add("North Delhi");
        labels.add("East Delhi");
        labels.add("West Delhi");
        labels.add("South Delhi");


        PieData data = new PieData(labels, dataset); // initialize Piedata
        mChart.setData(data);


    }



    private void setDataNorth() {

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1241f, 0));
        entries.add(new Entry(1862f, 1));
        entries.add(new Entry(865f, 2));


        PieDataSet dataset = new PieDataSet(entries, "# number of units");
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);

        // creating labels
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Civil Lines");
        labels.add("Kotwali");
        labels.add(" Sadar Bazar");



        PieData data = new PieData(labels, dataset); // initialize Piedata
        mChart.setData(data);
        mChart.notifyDataSetChanged();
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart.spin(2000, 0, 360, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);


    }

    private void setDataEast() {

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(2103f, 0));
        entries.add(new Entry(2561f, 1));
        entries.add(new Entry(3117f, 2));



        PieDataSet dataset = new PieDataSet(entries, "# number of units");
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);

        // creating labels
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Shahdara");
        labels.add("Preet Vihar");
        labels.add("Gandhi Nagar");



        PieData data = new PieData(labels, dataset); // initialize Piedata
        mChart.setData(data);
        mChart.notifyDataSetChanged();
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart.spin(2000, 0, 360, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);


    }

    private void setDataWest() {

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1954f, 0));
        entries.add(new Entry(2900f, 1));
        entries.add(new Entry(1406f, 2));



        PieDataSet dataset = new PieDataSet(entries, "# number of units");
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);

        // creating labels
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Punjabi Bagh");
        labels.add("Patel Nagar");
        labels.add("Rajouri Garden");



        PieData data = new PieData(labels, dataset); // initialize Piedata
        mChart.setData(data);
        mChart.notifyDataSetChanged();
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart.spin(2000, 0, 360, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);


    }

    private void setDataSouth() {

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(3466f, 0));
        entries.add(new Entry(3100f, 1));
        entries.add(new Entry(3033f, 2));



        PieDataSet dataset = new PieDataSet(entries, "# number of units");
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);

        // creating labels
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Saket");
        labels.add(" Hauz Khas");

        labels.add(" Mehrauli");


        PieData data = new PieData(labels, dataset); // initialize Piedata
        mChart.setData(data);
        mChart.notifyDataSetChanged();
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart.spin(2000, 0, 360, Easing.EasingOption.EaseInOutQuad);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if(flag){
            switch (e.getXIndex()){

                case 0:  setDataNorth();
                    break;
                case 1:  setDataEast();
                    break;
                case 2:  setDataWest();
                    break;
                case 3:  setDataSouth();
                    break;

            }
            flag = false;
        }
        else{

            setData();
            flag = true;

        }



    }

    @Override
    public void onNothingSelected() {



    }





}
