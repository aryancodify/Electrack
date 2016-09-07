package com.elecatrach.poc.electrack.client.fragment;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dd.morphingbutton.MorphingButton;
import com.elecatrach.poc.electrack.R;
import com.elecatrach.poc.electrack.common.application.ConnectionDetector;
import com.elecatrach.poc.electrack.common.application.Const;
import com.elecatrach.poc.electrack.common.application.MyMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DatewiseFragment extends Fragment {

    LineChart lineChart;
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    int selected_month;
    int selected_year;
    int selected_months_units;
    int selected_months_amount;
    SharedPreferences preferences;

    public DatewiseFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_datewise, container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            selected_month = bundle.getInt("selected_month", 1);
            selected_year = bundle.getInt("selected_year", 1);
            selected_months_units = bundle.getInt("selected_months_units", 1);
            selected_months_amount = bundle.getInt("selected_months_cost", 1);

        }
/*
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Insights of "+ CurrentStatsFragment.Months.values()[--selected_month].toString()+" "+selected_year);*/

        lineChart = (LineChart) v.findViewById(R.id.chart);
        lineChart.setScaleEnabled(false);
        TextView unit = (TextView) v.findViewById(R.id.totalunits);
        TextView unitdata = (TextView) v.findViewById(R.id.totalunitdata);
        TextView cost = (TextView) v.findViewById(R.id.duedate);
        TextView costdata = (TextView) v.findViewById(R.id.duedatedata);
        TextView loadlimit = (TextView) v.findViewById(R.id.expirydate);
        final TextView loadlimitdata = (TextView) v.findViewById(R.id.expirydata);

        final MorphingButton submit = (MorphingButton) v.findViewById(R.id.btnMorph1);
        submit.setVisibility(View.GONE);

        ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
        if (connectionDetector.isConnectingToInternet())
            callService();
        else
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("You are not connected to internet!")
                    .show();

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/KaushanScript-Regular.otf");

        unit.setTypeface(custom_font);
        unitdata.setTypeface(custom_font);
        cost.setTypeface(custom_font);
        costdata.setTypeface(custom_font);
        loadlimit.setTypeface(custom_font);
        loadlimitdata.setTypeface(custom_font);

        unitdata.setText(String.valueOf(selected_months_units));
        costdata.setText("₹ " + String.valueOf(selected_months_amount));
        loadlimitdata.setText(String.valueOf(preferences.getInt("limitline", 0)));


        return v;
    }

    protected void callService() {
        String final_url = Const.URL_USAGE_DATA + preferences.getString("user_id", "U1") + "/bill/" + "monthly?month=" + selected_month + "&year=" + selected_year;

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Data..");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, final_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response", response);
                        JSONObject jsonObject;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                jsonObject = jsonArray.getJSONObject(i);

                                entries.add(new Entry(Float.parseFloat(jsonObject.optString("totalUnit")), i));
                                labels.add(jsonObject.optJSONObject("timestamp").optString("dayOfMonth"));

                            }

                            LineDataSet dataset = new LineDataSet(entries, "# number of units");

                            LineData data = new LineData(labels, dataset);
                            lineChart.setData(data);

                            lineChart.setVisibleXRange(0.0f, 6.0f);

                            YAxis leftAxis = lineChart.getAxisLeft();
                            LimitLine ll = new LimitLine((float) preferences.getInt("limitline", 70), "Load Limit");
                            ll.setLineColor(Color.RED);
                            ll.setLineWidth(4f);
                            ll.setTextColor(Color.BLACK);
                            ll.setTextSize(12f);

                            leftAxis.addLimitLine(ll);


                            lineChart.setDescription("");

                            dataset.setDrawFilled(true);

                            dataset.setDrawCubic(true);

                            dataset.setColors(ColorTemplate.JOYFUL_COLORS);

                            MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);

                            lineChart.setMarkerView(mv);

                            lineChart.animateY(2500);

                            pDialog.dismissWithAnimation();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DatewiseFragment.this.getActivity(), "Some error in " +
                                    "loading " + "data", Toast.LENGTH_SHORT).show();
                            pDialog.dismissWithAnimation();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        Toast.makeText(DatewiseFragment.this.getActivity(), "Some error in " +
                                "loading " + "data", Toast.LENGTH_SHORT).show();
                        Log.e("error", error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Insights of " +
                CurrentStatsFragment.Months.values()[--selected_month].toString() + " " + selected_year);
    }


}
