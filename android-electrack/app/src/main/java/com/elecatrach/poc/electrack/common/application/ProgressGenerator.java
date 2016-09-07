package com.elecatrach.poc.electrack.common.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dd.processbutton.iml.ActionProcessButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProgressGenerator {

    SharedPreferences sharedpreferences;

    public interface OnCompleteListener {

        void onComplete(Boolean success);
    }

    private OnCompleteListener mListener;
    private int mProgress;

    public ProgressGenerator(OnCompleteListener listener) {
        mListener = listener;
    }

    public void start(String email, final String password, Context context, final ActionProcessButton button) {

        mProgress += 10;
        button.setProgress(mProgress);
        if (mProgress < 100)
        {
            mProgress += 10;
            button.setProgress(mProgress);
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = preferences.edit();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_LOGIN + email + "/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgress += 10;
                        button.setProgress(mProgress);
                        //Log.e("response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(response.contains("error")){
                                mListener.onComplete(false);
                            }
                            else{

                                editor.putString("user_name",jsonObject.optString("name"));
                                editor.putString("user_address",jsonObject.optString("address"));
                                String type = jsonObject.optString("type");
                                editor.putBoolean("isUser",type.contains("user"));
                                editor.putInt("limitline",jsonObject.optInt("loadLimit"));
                                editor.putString("user_id",jsonObject.optString("userId"));
                                editor.putInt("user_current_unit",jsonObject.optInt("totalUnits"));
                                editor.putInt("user_current_cost",jsonObject.optInt("totalUnits")*5);

                                editor.commit();


                                mListener.onComplete(true);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            mListener.onComplete(false);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("error", error.toString());
                        mListener.onComplete(false);
                    }
                }) {

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("password", password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }




    }



