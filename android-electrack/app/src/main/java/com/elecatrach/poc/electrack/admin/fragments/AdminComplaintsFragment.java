package com.elecatrach.poc.electrack.admin.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.elecatrach.poc.electrack.R;
import com.elecatrach.poc.electrack.admin.activities.AssignTaskMapActivity;
import com.elecatrach.poc.electrack.admin.adapters.ComplainsAdapter;
import com.elecatrach.poc.electrack.admin.models.Complain;
import com.elecatrach.poc.electrack.admin.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by mahendra.chhimwal on 4/25/2016.
 */
public class AdminComplaintsFragment extends Fragment implements View.OnClickListener,ComplainsAdapter.ComplainListener {



    private static final int COMPLAIN_REQ_CODE = 100;
    private RecyclerView mListView;
    private ComplainsAdapter mAdapter;
    private List<Complain> mComplaints;
    private LinearLayout mNoDataFound;
    private Complain mComplain;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_complaints_list, container, false);
        mListView = (RecyclerView) v.findViewById(R.id.rview);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mNoDataFound = (LinearLayout) v.findViewById(R.id.no_complains_view);
        getData();
        return v;
    }

    @Override
    public void onClick(View v) {

    }

    private void getData() {
        RestClient.getRestClient().getApiService().getComplaints(new Callback<List<Complain>>() {
            @Override
            public void success(List<Complain> complains, Response response) {
                hideProgressBar();
                mComplaints = complains;
                if (mComplaints != null && mComplaints.size() > 0) {
                    mNoDataFound.setVisibility(View.GONE);
                    setAdapterToListView();
                } else {
                    mNoDataFound.setVisibility(View.VISIBLE);
                    setAdapterToListView();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                hideProgressBar();
                error.printStackTrace();
                Snackbar.make(mNoDataFound, "Some error in fetching complaints. Please try letter" + ".", Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    private void hideProgressBar() {
        if (getView() != null && getView().findViewById(R.id.progressBar) != null) {
            getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }

    private void setAdapterToListView() {
        mAdapter = new ComplainsAdapter(getActivity(), mComplaints,this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onComplainSelect(Complain complain) {
        this.mComplain =complain;
        Intent intent = new Intent(getActivity(), AssignTaskMapActivity.class);
        intent.putExtra("COMP_OR_ANOMALY", true);
        intent.putExtra("COMPLAIN",complain);
        startActivityForResult(intent, COMPLAIN_REQ_CODE);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case COMPLAIN_REQ_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mComplain.setStatus("inprogress");
                        setAdapterToListView();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }
}
