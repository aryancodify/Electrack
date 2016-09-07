package com.elecatrach.poc.electrack.client.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class PredictionFragment extends Fragment {

    private CombinedChart mChart;
    private final int itemcount = 12;
    ArrayList<Entry> line_entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    ArrayList<BarEntry> entries = new ArrayList<>();
    SharedPreferences preferences;

    String[] jsonFields = new String[7];


    public PredictionFragment() {
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
        View v = inflater.inflate(R.layout.fragment_prediction, container, false);

       /* ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Predicted Data");*/

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        mChart = (CombinedChart) v.findViewById(R.id.chart1);
        mChart.setDescription("");
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });

        ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());
        if(connectionDetector.isConnectingToInternet())
            callService();
        else
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("You are not connected to internet!")
                    .show();


        return  v;

    }


    private LineData generateLineData() {

        LineData d = new LineData();

        LineDataSet set = new LineDataSet(line_entries, "Line DataSet");
        set.setColors(new int[]{ R.color.text_color_accent});
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(240, 238, 70));
        set.setCircleRadius(5f);
        set.setFillColor(Color.rgb(240, 238, 70));
        set.setDrawCubic(true);
        set.setDrawValues(true);
        set.setValueTextSize(10f);
        set.setDrawValues(false);




        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineData lineData = new LineData(labels,set);


        return lineData;
    }

    private BarData generateBarData() {

        BarData d = new BarData();

        BarDataSet set = new BarDataSet(entries, "Bar DataSet");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setValueTextColor(Color.rgb(0, 0, 0));
        set.setValueTextSize(10f);
        BarData barData = new BarData(labels,set);

        return barData;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    protected  void callService(){
        String final_url = Const.base_url + "/predition/"+ preferences.getString("user_id","U1");

        jsonFields[0] = "abc";
        jsonFields[1] = "nextToMonth";
        jsonFields[2] = "nextMonth";
        jsonFields[3] = "currentMonth";
        jsonFields[4] = "p1";
        jsonFields[5] = "p2";
        jsonFields[6] = "p3";

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Data..");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,final_url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        int iteration = 0;
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            switch (jsonObject.optString("start")){
                                case "p3":
                                    iteration = 6; break;
                                case "p2" :
                                    iteration = 5; break;
                                case "p1" :
                                    iteration = 4; break;
                                case "currentMonth":
                                    iteration = 3; break;
                            }
                            int startMonth = jsonObject.optInt("startMonth");
                            labels.add(Months.values()[startMonth].toString());

                        int entry = 0;
                        while (iteration>0){
                            entries.add(new BarEntry(Float.parseFloat(jsonObject.optString(jsonFields[iteration])),entry));
                            line_entries.add(new Entry(Float.parseFloat(jsonObject.optString(jsonFields[iteration])),entry));
                            labels.add(Months.valueOf(labels.get(labels.size() - 1)).next().toString());
                            iteration--;
                            entry++;
                            startMonth++;
                        }

                            labels.remove(labels.size() - 1);
                            CombinedData data = new CombinedData(labels);
                            data.setData(generateBarData());
                            data.setData(generateLineData());

                            MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);

                            mChart.setMarkerView(mv);

                            mChart.setData(data);
                            mChart.animateXY(3000, 3000);
                            pDialog.dismissWithAnimation();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PredictionFragment.this.getActivity(), "Some error in " +
                                    "loading " + "data", Toast.LENGTH_SHORT).show();
                            pDialog.dismissWithAnimation();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PredictionFragment.this.getActivity(), "Some error in " +
                                "loading " + "data", Toast.LENGTH_SHORT).show();
                        Log.e("error", error.toString());
                        pDialog.dismissWithAnimation();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public enum Months {

        January(),
        Febuary,
        March,
        April,
        May,
        June,
        July,
        August,
        September,
        October,
        November,
        December;

        Months next(){
            return Months.values()[(ordinal() + 1) % values().length];

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Predicted Data");
    }



}
