package com.elecatrach.poc.electrack.client.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class CurrentStatsFragment extends Fragment {

    LineChart lineChart;
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();
    int thisMonth;
    int thisYear;
    private int counter = 1;
    SharedPreferences preferences;

    public CurrentStatsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle 
            savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_current_stats, container, false);


        Window window = getActivity().getWindow();

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Calendar calendar = Calendar.getInstance();
        thisMonth = calendar.get(Calendar.MONTH);
        thisYear = calendar.get(Calendar.YEAR);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        final ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());

       /* ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Insights of " +
                Months.values()[thisMonth].name() + " " + thisYear);*/
        lineChart = (LineChart) v.findViewById(R.id.chart);
        lineChart.setScaleEnabled(false);

        TextView unit = (TextView) v.findViewById(R.id.totalunits);
        TextView unitdata = (TextView) v.findViewById(R.id.totalunitdata);
        TextView cost = (TextView) v.findViewById(R.id.duedate);
        TextView costdata = (TextView) v.findViewById(R.id.duedatedata);
        TextView loadlimit = (TextView) v.findViewById(R.id.expirydate);
        final EditText loadlimitdata = (EditText) v.findViewById(R.id.expirydata);


        final MorphingButton submit = (MorphingButton) v.findViewById(R.id.btnMorph1);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onsubmitButtonClicked(submit);
                int x = Integer.valueOf(loadlimitdata.getText().toString().trim());
                newloadlimitline(x);
                if (connectionDetector.isConnectingToInternet())
                    callService1(loadlimitdata.getText().toString().trim());
                else {
                    final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), 
                            SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...").setContentText
                            ("You are not connected to internet!");
                    dialog.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismissWithAnimation();
                        }
                    }, 1000);
                }

            }
        });

        if (connectionDetector.isConnectingToInternet()) callService();
        else
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE).setTitleText
                    ("Oops...").setContentText("You are not connected to internet!")

                    .show();


        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), 
                "fonts/KaushanScript-Regular.otf");

        unit.setTypeface(custom_font);
        unitdata.setTypeface(custom_font);
        cost.setTypeface(custom_font);
        costdata.setTypeface(custom_font);
        loadlimit.setTypeface(custom_font);
        loadlimitdata.setTypeface(custom_font);

        unitdata.setText(String.valueOf(preferences.getInt("user_current_unit",200)));
        costdata.setText("₹ " + String.valueOf(preferences.getInt("user_current_cost",1000)));
        loadlimitdata.setText(String.valueOf(preferences.getInt("limitline",70)));

        return v;
    }

    private void onsubmitButtonClicked(final MorphingButton submit) {

        if (counter == 0) {
            counter++;
            morphToSquare(submit, 500);
        } else if (counter == 1) {
            counter = 0;
            morphToSuccess(submit);

        }

    }

    private void morphToSquare(final MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create().duration(duration)
                .cornerRadius((int) getResources().getDimension(R.dimen.mb_corner_radius_2))
                .width((int) getResources().getDimension(R.dimen.mb_width_200)).height((int) 
                        getResources().getDimension(R.dimen.sucessheight)).color(getResources()
                        .getColor(R.color.mb_blue)).colorPressed(getResources().getColor(R.color
                        .mb_blue_dark)).text("Submit");
        btnMorph.morph(square);
    }

    private void morphToSuccess(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create().duration(500).cornerRadius(
                (int) getResources().getDimension(R.dimen.mb_height_56)).width((int) getResources
                ().getDimension(R.dimen.sucesswidth)).height((int) getResources().getDimension(R
                .dimen.sucessheight)).color(getResources().getColor(R.color.mb_green))
                .colorPressed(getResources().getColor(R.color.mb_green_dark)).text("Success");
        btnMorph.morph(circle);

    }

    protected void callService() {

        int corrected_month = thisMonth + 1;
        String final_url = Const.URL_USAGE_DATA + preferences.getString("user_id","U1") + "/bill/" + "monthly?month=" +
                corrected_month + "&year=" + thisYear;

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog
                .PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Data..");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, final_url, new 
                Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);

                JSONObject jsonObject;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObject = jsonArray.getJSONObject(i);

                        entries.add(new Entry(Float.parseFloat(jsonObject.optString("totalUnit"))
                                , i));
                        labels.add(jsonObject.optJSONObject("timestamp").optString("dayOfMonth"));

                    }

                    LineDataSet dataset = new LineDataSet(entries, "# number of units");

                    LineData data = new LineData(labels, dataset);
                    lineChart.setData(data);

                    lineChart.setVisibleXRange(0.0f, 6.0f);

                    YAxis leftAxis = lineChart.getAxisLeft();
                    LimitLine ll = new LimitLine((float) preferences.getInt("limitline",70), "Load Limit");
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

                    pDialog.dismissWithAnimation();

                    lineChart.animateY(2500);


                } catch (JSONException e) {
                    e.printStackTrace();
                    pDialog.dismissWithAnimation();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(CurrentStatsFragment.this.getActivity(), "Some error in loading " +
                                "data", Toast.LENGTH_SHORT).show();
                Log.e("error", error.toString());
                pDialog.dismissWithAnimation();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    protected void callService1(String newLoadLimit) {

        String url = Const.URL_UPDATE_LOAD + preferences.getInt("limitline",0) + "/update?loadLimit=" + newLoadLimit;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response
                .Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public enum Months {

        January,
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
        December

    }

    private void newloadlimitline(int newvalue) {

        lineChart.invalidate();

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("limitline",newvalue);
        editor.commit();


        LimitLine ll = new LimitLine((float) newvalue, "Load Limit");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(4f);
        ll.setTextColor(Color.BLACK);
        ll.setTextSize(12f);

        leftAxis.addLimitLine(ll);

    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Insights of " +
                Months.values()[thisMonth].name() + " " + thisYear);
    }


}
