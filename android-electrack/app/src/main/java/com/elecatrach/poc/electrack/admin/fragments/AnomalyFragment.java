package com.elecatrach.poc.electrack.admin.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.elecatrach.poc.electrack.R;
import com.elecatrach.poc.electrack.admin.activities.AssignTaskMapActivity;
import com.elecatrach.poc.electrack.admin.adapters.AnomalyAdapter;
import com.elecatrach.poc.electrack.admin.adapters.ComplainsAdapter;
import com.elecatrach.poc.electrack.admin.models.Anomaly;
import com.elecatrach.poc.electrack.admin.models.Complain;
import com.elecatrach.poc.electrack.admin.rest.RestClient;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by manu on 5/6/2016.
 */
public class AnomalyFragment extends Fragment implements View.OnClickListener, AnomalyAdapter
        .AnomalyListener {

    private static final int ANOMALY_REQ_CODE = 2000;
    private RecyclerView mListView;
    private AnomalyAdapter mAdapter;
    private List<Anomaly> mAnomalies;
    private Anomaly mAnomaly;
    private LinearLayout mNoDataFound;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View v = inflater.inflate(R.layout.anomaly_view, container, false);
        mListView = (RecyclerView) v.findViewById(R.id.rview);
        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        mNoDataFound = (LinearLayout) v.findViewById(R.id.no_complains_view);
        getData();
        return v;
    }

    @Override
    public void onClick(View v) {

    }

    private void hideProgressBar() {
        if (getView() != null && getView().findViewById(R.id.progressBar) != null) {
            getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }

    private void getData() {
        RestClient.getRestClient().getApiService().getAnomaly(new Callback<List<Anomaly>>() {
            @Override
            public void success(List<Anomaly> anomalies, Response response) {
                hideProgressBar();
                mAnomalies = anomalies;
                if (mAnomalies != null && mAnomalies.size() > 0) {
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
                Snackbar.make(mNoDataFound, "Some error in fetching anomalies. Please try " +
                        "letter" + ".", Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    private void setAdapterToListView() {
        mAdapter = new AnomalyAdapter(getActivity(), mAnomalies, this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onAnomalySelected(Anomaly anomaly) {
        mAnomaly =anomaly;
        Intent intent = new Intent(getActivity(), AssignTaskMapActivity.class);
        intent.putExtra("COMP_OR_ANOMALY", false);
        intent.putExtra("ANOMALY",anomaly);
        startActivityForResult(intent, ANOMALY_REQ_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ANOMALY_REQ_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        mAnomaly.setStatus("inprogress");
                        setAdapterToListView();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
                break;
        }
    }
}
