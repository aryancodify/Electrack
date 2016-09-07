package com.elecatrach.poc.electrack.admin.adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elecatrach.poc.electrack.R;
import com.elecatrach.poc.electrack.admin.activities.AssignTaskMapActivity;
import com.elecatrach.poc.electrack.admin.models.Anomaly;
import com.elecatrach.poc.electrack.common.application.ElectrackApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manu on 5/6/2016.
 */
public class AnomalyAdapter extends RecyclerView.Adapter<AnomalyAdapter.ViewHolder> {
    private Context mContext;
    private List<Anomaly> mAnomalies;
    private ArrayList<String> mDescriptions;

    private AnomalyListener mListener;

    public AnomalyAdapter(Context context, List<Anomaly> anomalies, AnomalyListener listener) {
        this.mAnomalies = anomalies;
        mContext = context;
        this.mListener = listener;
        buildDescriptions();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.anomaly_item_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Anomaly anomaly = mAnomalies.get(position);
        holder.bindAnomalyWithHolder(anomaly);

    }

    @Override
    public int getItemCount() {
        return mAnomalies == null ? 10 : mAnomalies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView user;
        private TextView dateTime;
        TextView location;
        TextView body;
        Button action;
        Anomaly anomaly;
        TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            user = (TextView) itemView.findViewById(R.id.user);
            dateTime = (TextView) itemView.findViewById(R.id.date_time);
            location = (TextView) itemView.findViewById(R.id.adresss);
            body = (TextView) itemView.findViewById(R.id.body);
            action = (Button) itemView.findViewById(R.id.btnAssign);
            status = (TextView) itemView.findViewById(R.id.status);
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBtnInspectClick();
                }
            });
        }

        private void onBtnInspectClick() {
            if (ElectrackApplication.isLocationEnabled(mContext)) {
                if (!anomaly.getStatus().contains("open")) {
                    Toast.makeText(mContext, "Already asked to inspect", Toast.LENGTH_LONG).show();
                } else {
                    if (mListener != null) {
                        mListener.onAnomalySelected(anomaly);
                    }
                }
            } else {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(myIntent);
            }
        }

        public void bindAnomalyWithHolder(Anomaly anomaly) {
            this.anomaly = anomaly;
            user.setText(anomaly.getUserId());
            String stat= anomaly.getStatus();
            if(stat == null){
                stat = "open";
            }
            status.setText(stat);
            location.setText(anomaly.getAddress());
            int pos = getAdapterPosition();
            pos = pos % 4;
            body.setText(mDescriptions.get(pos));
            String text = "Ask to Inspect";
            if (anomaly.getStatus() != null) {
                if (!anomaly.getStatus().contains("open")) {
                    text = "Asked";
                }
            }
            action.setText(text);
        }
    }


    private void buildDescriptions() {
        mDescriptions = new ArrayList<>();

        mDescriptions.add("Meeter readings not moving with usual pace. Seems meter there is some " +
                "" + "problem with meter");
        mDescriptions.add("Meter readings are very low in nights from last two weeks. Seems " +
                "electricity stealing is happening at above place ");
        mDescriptions.add("No readings are recorded for last two days. May be meter has  stopped");
        mDescriptions.add("No consumption in this area in last 3 hours. Must be some  fault with " +
                "" + "transformer. Please check");
    }


    public interface AnomalyListener {
        void onAnomalySelected(Anomaly anomaly);
    }
}
