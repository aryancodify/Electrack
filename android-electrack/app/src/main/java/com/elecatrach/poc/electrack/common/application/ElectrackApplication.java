package com.elecatrach.poc.electrack.common.application;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

public class ElectrackApplication extends Application {

    private static ElectrackApplication appInst;

    public static Context getAppInst() {
        return appInst;
    }

    public static boolean isLocationEnabled(Context context) {

        LocationManager locationManager = null;
        boolean gps_enabled= false,network_enabled = false;

        if(locationManager ==null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try{
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch(Exception ex){
            //do nothing...
        }

        try{
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){
            //do nothing...
        }

        return gps_enabled || network_enabled;


    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInst = this;
    }
}
