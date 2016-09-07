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
import com.elecatrach.poc.electrack.admin.models.Complain;
import com.elecatrach.poc.electrack.common.application.ElectrackApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mahendra.chhimwal on 4/20/2016.
 */
public class ComplainsAdapter extends RecyclerView.Adapter<ComplainsAdapter.ViewHolder> {
    private Context mContext;
    private List<Complain> mComplains;
    private ComplainListener mListener;

    public ComplainsAdapter(Context context, List<Complain> complainList, ComplainListener
            listener) {
        this.mComplains = complainList;
        mContext = context;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.complain_item_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Complain complain = mComplains.get(position);
        holder.bindComplainWithHolder(complain);

    }

    @Override
    public int getItemCount() {
        return mComplains == null ? 0 : mComplains.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView dateTime;
        TextView location;
        TextView body;
        TextView status;
        Button action;
        Complain complain;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            dateTime = (TextView) itemView.findViewById(R.id.date_time);
            location = (TextView) itemView.findViewById(R.id.location);
            body = (TextView) itemView.findViewById(R.id.body);
            status = (TextView) itemView.findViewById(R.id.status);
            action = (Button) itemView.findViewById(R.id.btnAssign);
            action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBtnAssignClick();
                }
            });
        }

        private void onBtnAssignClick() {
            if (ElectrackApplication.isLocationEnabled(mContext)) {
                if (!complain.getStatus().contains("open")) {
                    Toast.makeText(mContext, "Already assigned", Toast.LENGTH_LONG).show();
                } else {
                    if (mListener != null) {
                        mListener.onComplainSelect(complain);
                    }
                }
            } else {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(myIntent);
            }

        }

        public void bindComplainWithHolder(Complain complain) {
            this.complain = complain;
            Date date = new Date(Long.parseLong(complain.getTimestamp()));
            SimpleDateFormat format = new SimpleDateFormat("E, y-M-d 'at' h:m:s a z");
            String timeDetails = format.format(date);
            dateTime.setText(timeDetails);
            title.setText(complain.getType());
            body.setText(complain.getDesc());
            status.setText(complain.getStatus());
            String text ="Assign";
            if(!complain.getStatus().contains("open")){
                text = "Assigned";
            }
            action.setText(text);
        }
    }

    public interface ComplainListener {
        void onComplainSelect(Complain complain);
    }


}
