package com.elecatrach.poc.electrack.admin.rest;


import com.elecatrach.poc.electrack.admin.models.Anomaly;
import com.elecatrach.poc.electrack.admin.models.AnomalyUpdate;
import com.elecatrach.poc.electrack.admin.models.CompUpdate;
import com.elecatrach.poc.electrack.admin.models.Complain;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

public interface RestService {
    @GET("/api/electra/complaint/")
    public void getComplaints(Callback<List<Complain>> callback);

    @GET("/api/electra/anomaly/")
    public void getAnomaly(Callback<List<Anomaly>> callback);

    @PUT("/api/electra/anomaly/{id}")
    public void updateAnomaly(@Path("id") String id, @Query("status") String status,
                              Callback<Object>
            callback);

    @PUT("/api/electra/complaint/{id}/{complaintId}")
    public void updateComplain(@Path("id") String id, @Path("complaintId") String compId, @Query
            ("status")String status, Callback<Object> callback);
}
