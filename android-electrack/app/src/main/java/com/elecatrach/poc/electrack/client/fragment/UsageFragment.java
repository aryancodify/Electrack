package com.elecatrach.poc.electrack.client.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.elecatrach.poc.electrack.R;
import com.elecatrach.poc.electrack.admin.models.Usage;
import com.elecatrach.poc.electrack.common.application.ConnectionDetector;
import com.elecatrach.poc.electrack.common.application.Const;
import com.elecatrach.poc.electrack.common.application.MyMarkerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class UsageFragment extends Fragment {

    List<Usage> usageArrayList = new ArrayList<>();
    List<BarEntry> entries = new ArrayList<>();
    List<String> labels = new ArrayList<>();
    BarChart chart;
    SharedPreferences preferences;

    public UsageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        chart = new BarChart(getActivity());
        chart.setBackgroundColor(Color.parseColor("#ffffff"));

        chart.setScaleEnabled(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

      /*  ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Month-by-Month Usage");*/
        ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
        if(connectionDetector.isConnectingToInternet())
        callService();
        else
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("You are not connected to internet!")
                    .show();


        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

                DatewiseFragment datewiseFragment = new DatewiseFragment();
                Bundle args = new Bundle();
                int selected_month = usageArrayList.get(e.getXIndex()).getMonth_value();
                int selected_year = usageArrayList.get(e.getXIndex()).getYear();
                int selected_months_units = usageArrayList.get(e.getXIndex()).getTotalUnits();
                int selected_months_cost  =  usageArrayList.get(e.getXIndex()).getTotalAmount();
                args.putInt("selected_month", selected_month);
                args.putInt("selected_year", selected_year);
                args.putInt("selected_months_units",selected_months_units);
                args.putInt("selected_months_cost",selected_months_cost);
                datewiseFragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container_body, datewiseFragment).addToBackStack
                        (null);
                fragmentTransaction.commit();

            }

            @Override
            public void onNothingSelected() {

            }
        });

        chart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent me) {

            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {

            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {

            }
        });






        // Inflate the layout for this fragment
        return chart;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity){
            a=(Activity) context;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected  void callService(){
        String final_url = Const.URL_USAGE_DATA + preferences.getString("user_id","U1") + "/bill";

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Data..");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,final_url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
               //         Log.e("response",response);
                        Usage usage;
                        JSONObject jsonObject;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length();i++){

                               jsonObject = jsonArray.getJSONObject(i);
                                usage = new Usage();
                                usage.setMonth(jsonObject.optJSONObject("timestamp").optString("month"));
                                usage.setTotalAmount(jsonObject.optInt("totalAmount"));
                                usage.setTotalUnits(jsonObject.optInt("totalUnit"));
                                usage.setMonth_value(jsonObject.optJSONObject("timestamp").optInt("monthValue"));
                                usage.setYear(jsonObject.optJSONObject("timestamp").optInt("year"));

                                usageArrayList.add(usage);

                                entries.add(new BarEntry(Float.parseFloat(jsonObject.optString("totalUnit")),i));
                                labels.add(jsonObject.optJSONObject("timestamp").optString("month"));

                            }

                            BarDataSet dataset = new BarDataSet(entries, "# number of units");



                            BarData data = new BarData(labels, dataset);
                            chart.setData(data);

                            chart.setVisibleXRange(0.0f,6.0f);

                            YAxis leftAxis = chart.getAxisLeft();
                            LimitLine ll = new LimitLine((float)preferences.getInt("loadlimit",0),"Load Limit");
                            ll.setLineColor(Color.RED);
                            ll.setLineWidth(4f);
                            ll.setTextColor(Color.BLACK);
                            ll.setTextSize(12f);

                            leftAxis.addLimitLine(ll);


                           chart.setDescription("");

                            dataset.setColors(ColorTemplate.JOYFUL_COLORS);

                            pDialog.dismissWithAnimation();

                            chart.animateY(2500);

                            MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);

                            chart.setMarkerView(mv);




                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(UsageFragment.this.getActivity(), "Some error in " +
                                    "loading " + "data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UsageFragment.this.getActivity(), "Some error in " +
                                "loading " + "data", Toast.LENGTH_SHORT).show();
                        pDialog.dismissWithAnimation();
                        Log.e("error",error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Month-by-Month Usage");
    }

}