package com.elecatrach.poc.electrack.common.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by manu on 4/23/2016.
 */
public class PrefManager {
    //Shared Preferences
    private SharedPreferences pref;

    //Editor for Shared preferences
    private SharedPreferences.Editor mEditor;

    //Shared pref file name
    private static final String PREF_NAME = "electrack";

    //all pref keys here
    private static final String IS_USER_LOGGED_IN = "isUserLoggedIn";
    private static final String IS_ADMIN = "is_admin";

    //Constructor
    public PrefManager(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    public void setUserLoggedIn(boolean isLoggedIn) {
        mEditor = pref.edit();
        mEditor.putBoolean(IS_USER_LOGGED_IN, isLoggedIn);
        mEditor.apply();
    }

    public void setAsAdmin(boolean isAdmin) {
        mEditor = pref.edit();
        mEditor.putBoolean(IS_ADMIN, isAdmin);
        mEditor.apply();
    }

    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGGED_IN, false);
    }

    public boolean isAdmin() {
        return pref.getBoolean(IS_ADMIN, false);
    }

    public void setNewFieldAgent(String name, int assignedTask) {
        mEditor = pref.edit();
        mEditor.putInt(name, assignedTask);
        mEditor.apply();
    }

    public int getAgentTask(String agentName){
        return pref.getInt(agentName, 0);
    }


}
