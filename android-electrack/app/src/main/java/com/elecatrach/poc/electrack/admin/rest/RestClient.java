package com.elecatrach.poc.electrack.admin.rest;

import com.elecatrach.poc.electrack.common.application.AppConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * This will be Rest Client throughout the application. This should be singleTon Class
 */
public class RestClient {
    private RestService apiService;

    private static RestClient me = new RestClient();


    private RestClient() {
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .disableHtmlEscaping().create();

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
            }
        };
        RestAdapter restAdapter = new RestAdapter.Builder().setLogLevel(RestAdapter.LogLevel
                .FULL).setEndpoint(AppConfig.BASE_URL).setConverter(new GsonConverter(gson)
        ).setRequestInterceptor(requestInterceptor).build();

        apiService = restAdapter.create(RestService.class);

    }

    public RestService getApiService() {
        return apiService;
    }

    public static RestClient getRestClient() {
        return me;
    }

}
