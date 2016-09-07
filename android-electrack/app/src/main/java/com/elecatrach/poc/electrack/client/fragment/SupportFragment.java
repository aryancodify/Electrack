package com.elecatrach.poc.electrack.client.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dd.morphingbutton.MorphingButton;
import com.dd.processbutton.iml.ActionProcessButton;
import com.elecatrach.poc.electrack.R;
import com.elecatrach.poc.electrack.admin.activities.AdminMainActivity;
import com.elecatrach.poc.electrack.client.activity.MainActivity;
import com.elecatrach.poc.electrack.common.application.ConnectionDetector;
import com.elecatrach.poc.electrack.common.application.Const;
import com.elecatrach.poc.electrack.common.application.ProgressGenerator;
import com.elecatrach.poc.electrack.common.application.ProgressGenerator1;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class SupportFragment extends Fragment implements ProgressGenerator1
        .OnCompleteListener{

    private int counter = 1;
    EditText type;
    EditText description;
    ActionProcessButton btnSubmit;
    SharedPreferences preferences;
    String request_id;

    public SupportFragment() {
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
        View v = inflater.inflate(R.layout.fragment_support, container, false);

        /*((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Support/Complaint Request");*/

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        type = (EditText)v.findViewById(R.id.type);
        description = (EditText)v.findViewById(R.id.desc);



        final ConnectionDetector connectionDetector = new ConnectionDetector(getActivity());

        TextView subtitle = (TextView) v.findViewById(R.id.subheading);
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/KaushanScript-Regular.otf");
        subtitle.setTypeface(custom_font);

        btnSubmit = (ActionProcessButton) v.findViewById(R.id.submitbutton);
        final ProgressGenerator1 progressGenerator1  = new ProgressGenerator1(this);
        btnSubmit.setMode(ActionProcessButton.Mode.ENDLESS);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressGenerator1.start(btnSubmit);
                btnSubmit.setEnabled(false);
                if (connectionDetector.isConnectingToInternet())
                    callService();
                else
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("You are not connected to internet!")
                            .show();


            }
        });

        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    private void callService(){
        request_id = "";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_COMPLAINT + preferences.getString("user_id","U1") ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            request_id = jsonObject.optString("requestId");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SupportFragment.this.getActivity(), "Some error in " +
                                "loading " + "data", Toast.LENGTH_SHORT).show();

                    }
                }) {



            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("desc", description.getText().toString().trim());
                map.put("type",type.getText().toString().trim());
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }



    @Override
    public void onComplete() {

        btnSubmit.setEnabled(true);
        if(!(request_id.isEmpty())){
            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Support/Complaint")
                    .setContentText("Your complaint has been registered. Your complaint id is "+request_id)

                    .show();
        }
        else{
            try {
                Thread.sleep(100);
                onComplete();


            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Support/Complaint Request");
    }

}
