package com.elecatrach.poc.electrack.client.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.elecatrach.poc.electrack.R;
import com.elecatrach.poc.electrack.common.application.ConnectionDetector;
import com.elecatrach.poc.electrack.common.application.Const;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistrationIntentService extends IntentService {

    // abbreviated tag name
    private static final String TAG = "RegIntentService";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String GCM_TOKEN = "gcmToken";
    SharedPreferences preferences;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        preferences = PreferenceManager.getDefaultSharedPreferences(RegistrationIntentService.this);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        // Make a call to Instance API
        InstanceID instanceID = InstanceID.getInstance(this);
        String senderId = getResources().getString(R.string.gcm_defaultSenderId1);
        try {
            // request token that will be used by the server to send push notifications
            String token = instanceID.getToken(senderId, GoogleCloudMessaging.INSTANCE_ID_SCOPE);
            Log.e(TAG, "GCM Registration Token: " + token);
            pref.edit().putString(GCM_TOKEN, token).apply();

            // pass along this data
            sendRegistrationToServer(token);

        } catch (IOException e) {
            e.printStackTrace();
            pref.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
        }
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        final ConnectionDetector connectionDetector = new ConnectionDetector(RegistrationIntentService.this);
        if (connectionDetector.isConnectingToInternet())
            callService(token);
        else
            new SweetAlertDialog(RegistrationIntentService.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("You are not connected to internet!")
                    .show();

        // if registration sent was successful, store a boolean that indicates whether the generated token has been sent to server
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();

    }

    private void callService(final String token){

        String final_url = Const.URL_USAGE_DATA + preferences.getString("user_id","U1") + "/updateDeviceId/";
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, final_url  ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response",response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistrationIntentService.this, "Some error in " +
                                "loading " + "data", Toast.LENGTH_SHORT).show();

                    }
                }){



            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("deviceId",token);

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(RegistrationIntentService.this);
        requestQueue.add(stringRequest);
    }


}