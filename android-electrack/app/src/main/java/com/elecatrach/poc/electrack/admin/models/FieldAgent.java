package com.elecatrach.poc.electrack.admin.models;

import android.content.Context;

import com.elecatrach.poc.electrack.common.managers.PrefManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by mahendra.chhimwal on 4/26/2016.
 */
public class FieldAgent {

    public int getAssignedTaskCount() {
        return assignedTaskCount;
    }

    public void setAssignedTaskCount(int assignedTaskCount, Context context) {
        this.assignedTaskCount = assignedTaskCount;
        PrefManager manager = new PrefManager(context);
        manager.setNewFieldAgent(this.name, assignedTaskCount);
    }

    private int assignedTaskCount = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Complain> getAssignedComplains() {
        return assignedComplains;
    }

    public void setAssignedComplains(List<Complain> assignedComplains) {
        this.assignedComplains = assignedComplains;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    private String name;
    private List<Complain> assignedComplains;
    private String address;
    private LatLng location;
}
