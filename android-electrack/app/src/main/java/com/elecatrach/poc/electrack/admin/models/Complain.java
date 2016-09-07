package com.elecatrach.poc.electrack.admin.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by manu on 4/24/2016.
 */
public class Complain implements Parcelable {

    @SerializedName("assign")
    private String assign;

    @SerializedName("desc")
    private String desc;

    @SerializedName("status")
    private String status;

    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("type")
    private String type;

    @SerializedName("userId")
    private String userId;

    @SerializedName("requestId")
    private String requestId;

    protected Complain(Parcel in) {
        assign = in.readString();
        desc = in.readString();
        status = in.readString();
        timestamp = in.readString();
        type = in.readString();
        userId = in.readString();
        requestId = in.readString();
    }

    public static final Creator<Complain> CREATOR = new Creator<Complain>() {
        @Override
        public Complain createFromParcel(Parcel in) {
            return new Complain(in);
        }

        @Override
        public Complain[] newArray(int size) {
            return new Complain[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAssign() {
        return assign;
    }

    public void setAssign(String assign) {
        this.assign = assign;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(assign);
        dest.writeString(desc);
        dest.writeString(status);
        dest.writeString(timestamp);
        dest.writeString(type);
        dest.writeString(userId);
        dest.writeString(requestId);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
