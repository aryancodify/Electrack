package com.elecatrach.poc.electrack.admin.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by manu on 5/6/2016.
 */

public class Anomaly implements Parcelable{

    protected Anomaly(Parcel in) {
        userId = in.readString();
        address = in.readString();
        status = in.readString();
    }

    public static final Creator<Anomaly> CREATOR = new Creator<Anomaly>() {
        @Override
        public Anomaly createFromParcel(Parcel in) {
            return new Anomaly(in);
        }

        @Override
        public Anomaly[] newArray(int size) {
            return new Anomaly[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String userId;

    private String address;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String  status;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(address);
        if(status==null)
            status="open";
        dest.writeString(status);
    }
}
