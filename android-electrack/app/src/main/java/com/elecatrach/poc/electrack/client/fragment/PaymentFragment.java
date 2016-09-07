package com.elecatrach.poc.electrack.client.fragment;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dd.processbutton.iml.ActionProcessButton;
import com.elecatrach.poc.electrack.R;
import com.elecatrach.poc.electrack.common.application.ConnectionDetector;
import com.elecatrach.poc.electrack.common.application.Const;
import com.elecatrach.poc.electrack.common.application.ProgressGenerator1;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class PaymentFragment extends Fragment implements ProgressGenerator1
        .OnCompleteListener{

    TextView amount;
    TextView totalunitdata;
    TextView duedatedata;
    TextView expirydata;
    SharedPreferences preferences;
    ActionProcessButton btnPay;
    String bill_id;
    Boolean isBillPaid = false;


    public PaymentFragment() {
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
        View v = inflater.inflate(R.layout.fragment_payment, container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        amount = (TextView)v.findViewById(R.id.amount);
        TextView totalunits = (TextView)v.findViewById(R.id.totalunits);
        totalunitdata = (TextView)v.findViewById(R.id.totalunitdata);
        TextView duedate = (TextView)v.findViewById(R.id.duedate);
        duedatedata = (TextView)v.findViewById(R.id.duedatedata);
        TextView expirydate = (TextView)v.findViewById(R.id.expirydate);
        expirydata = (TextView)v.findViewById(R.id.expirydata);
        TextView cardheading  = (TextView)v.findViewById(R.id.cardheading);

        final ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());


        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/KaushanScript-Regular.otf");

        amount.setTypeface(custom_font);
        totalunits.setTypeface(custom_font);
        totalunitdata.setTypeface(custom_font);
        duedate.setTypeface(custom_font);
        duedatedata.setTypeface(custom_font);
        expirydate.setTypeface(custom_font);
        expirydata.setTypeface(custom_font);
        cardheading.setTypeface(custom_font);

        btnPay = (ActionProcessButton) v.findViewById(R.id.paybutton);
        final ProgressGenerator1 progressGenerator1  = new ProgressGenerator1(this);
        btnPay.setMode(ActionProcessButton.Mode.ENDLESS);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!preferences.getBoolean("isBillPaid",false)){

                    btnPay.setEnabled(false);
                    progressGenerator1.start(btnPay);

                    if (connectionDetector.isConnectingToInternet())
                        callService1();
                    else
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("You are not connected to internet!")
                                .show();
                }
                else{

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Bill Payment")
                            .setContentText("Your bill has already been paid")
                            .show();

                }


            }
        });


        if(connectionDetector.isConnectingToInternet())
            callService();
        else
            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("You are not connected to internet!")
                    .show();



        return v;
    }



    protected  void callService(){
        String final_url = Const.URL_USAGE_DATA + preferences.getString("user_id","U1") + "/bill/last";

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Fetching Data..");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,final_url ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            final SharedPreferences.Editor editor = preferences.edit();

                            editor.putBoolean("isBillPaid",jsonObject.optBoolean("paid"));
                            editor.commit();
                            if(jsonObject.optBoolean("paid")){
                                btnPay.setText("Bill already paid");
                            }

                            amount.setText("₹ "+jsonObject.optString("totalAmount"));
                            totalunitdata.setText(jsonObject.optString("totalUnit"));

                            duedatedata.setText(jsonObject.optJSONObject("dueDate").optString("month")
                                    +" "
                                    + jsonObject.optJSONObject("dueDate").optString("dayOfMonth")
                                    + " "
                                    + jsonObject.optJSONObject("dueDate").optString("year")
                            );
                            expirydata.setText(jsonObject.optJSONObject("expiryDate").optString("month")
                                    +" "
                                    + jsonObject.optJSONObject("expiryDate").optString("dayOfMonth")
                                    + " "
                                    + jsonObject.optJSONObject("expiryDate").optString("year")
                            );

                            bill_id = jsonObject.optString("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pDialog.dismissWithAnimation();
                        }

                        pDialog.dismissWithAnimation();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());
                        pDialog.dismissWithAnimation();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void callService1(){

        String final_url = Const.URL_USAGE_DATA + preferences.getString("user_id","U1") + "/bill/" + bill_id;
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, final_url  ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    Log.e("response","Code reaches here");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PaymentFragment.this.getActivity(), "Some error in " +
                                "loading " + "data", Toast.LENGTH_SHORT).show();

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


    @Override
    public void onComplete() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Payment")
                .setContentText("Your payment has been successfully done")
                .show();
        btnPay.setEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Pay Bill");
    }

}
