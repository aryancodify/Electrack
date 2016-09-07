package com.elecatrach.poc.electrack.admin.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.elecatrach.poc.electrack.R;

/**
 * Created by mahendra.chhimwal on 4/20/2016.
 */
public class AdminHomeFragment extends Fragment implements View.OnClickListener {

    private Button mBtnRequest;
    private LinearLayout mAllRequest;
    private Button mBtnComplaints;
    private LinearLayout mAllComplaints;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_home_fragment, container, false);

        mBtnComplaints = (Button) v.findViewById(R.id.btnAssign_c);
        mBtnRequest = (Button) v.findViewById(R.id.btnAssign);
        mAllRequest = (LinearLayout) v.findViewById(R.id.all_request);
        mAllComplaints = (LinearLayout) v.findViewById(R.id.all_request_c);
        mBtnRequest.setOnClickListener(this);
        mBtnComplaints.setOnClickListener(this);
        mAllComplaints.setOnClickListener(this);
        mAllRequest.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {

    }
}
