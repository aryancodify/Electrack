package com.elecatrach.poc.electrack.admin.models;

import android.content.Context;

import com.elecatrach.poc.electrack.common.managers.PrefManager;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mahendra.chhimwal on 4/26/2016.
 */
public class AgentManager {

    private static Context mAppContext;
    private List<FieldAgent> mFieldAgents;


    private AgentManager() {
    }

    private static AgentManager me;

    public static AgentManager getInstance(Context context) {
        if (me == null) {
            mAppContext = context.getApplicationContext();
            me = new AgentManager();
            me.createFieldAgents();
        }
        return me;
    }

    public List<FieldAgent> getmFieldAgents() {
        return mFieldAgents;
    }

    private void createFieldAgents() {
        PrefManager prefManager = new PrefManager(mAppContext);
        mFieldAgents = new ArrayList<>();
        FieldAgent agent = new FieldAgent();
        agent.setAddress("Connaught Place, New Delhi");
        agent.setName("Mahesh Shankar");
        agent.setAssignedComplains(null);
        agent.setAssignedTaskCount(prefManager.getAgentTask("Mahesh Shankar"), mAppContext);
        LatLng latLng = new LatLng(28.631847, 77.217332);
        agent.setLocation(latLng);
        mFieldAgents.add(agent);

        FieldAgent agent2 = new FieldAgent();
        agent2.setAddress("Hauz Khas, South Delhi");
        agent2.setName("Dany Sammy");
        agent2.setAssignedComplains(null);
        agent2.setAssignedTaskCount(prefManager.getAgentTask("Dany Sammy"), mAppContext);
        LatLng latLng2 = new LatLng(28.549158, 77.186680);
        agent2.setLocation(latLng2);
        mFieldAgents.add(agent2);


        FieldAgent agent3 = new FieldAgent();
        agent3.setAddress("Mayur Puri, Delhi");
        agent3.setName("Mike Finch");
        agent3.setAssignedComplains(null);
        agent3.setAssignedTaskCount(prefManager.getAgentTask("Mike Finch"), mAppContext);
        LatLng latLng3 = new LatLng(28.626936, 77.138958);
        agent3.setLocation(latLng3);
        mFieldAgents.add(agent3);

        FieldAgent agent4 = new FieldAgent();
        agent4.setAddress("Laxmi Nagar, Delhi");
        agent4.setName("Surya Bhai");
        agent4.setAssignedComplains(null);
        agent4.setAssignedTaskCount(prefManager.getAgentTask("Surya Bhai"), mAppContext);
        LatLng latLng4 = new LatLng(28.634169, 77.271824);
        agent4.setLocation(latLng4);
        mFieldAgents.add(agent4);

    }
}
