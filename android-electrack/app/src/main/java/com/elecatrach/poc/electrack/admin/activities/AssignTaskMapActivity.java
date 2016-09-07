package com.elecatrach.poc.electrack.admin.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elecatrach.poc.electrack.R;
import com.elecatrach.poc.electrack.admin.models.AgentManager;
import com.elecatrach.poc.electrack.admin.models.Anomaly;
import com.elecatrach.poc.electrack.admin.models.AnomalyUpdate;
import com.elecatrach.poc.electrack.admin.models.CompUpdate;
import com.elecatrach.poc.electrack.admin.models.Complain;
import com.elecatrach.poc.electrack.admin.models.FieldAgent;
import com.elecatrach.poc.electrack.admin.rest.RestClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AssignTaskMapActivity extends AppCompatActivity implements GoogleApiClient
        .ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = AssignTaskMapActivity.class.getSimpleName();
    private boolean isFromComplaints = false;
    private FrameLayout mBottomSheet;
    private Toolbar mToolbar;
    private GoogleMap mMap;
    private TextView tvName;
    private TextView tvDetail;
    private Button btnAssign;
    private String action;
    private String actionRequest;
    private String actionCompleted;

    private Complain mComplain;
    private Anomaly mAnomaly;

    private String taskType;
    private HashMap<Marker, FieldAgent> mMarkerAgentMap;

    private List<Complain> mComplains;
    private List<FieldAgent> mAgents;
    private Location mCurrentLocation;

    private GoogleApiClient googleApiClient;
    private TextView mTweetText;
    private LocationRequest mLocationRequest;
    private static final long ANIMATION_DURATION = 300;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private FieldAgent mSelectedAgent;
    private ProgressBar mProgressBar;
    private boolean isMapShown = false;

    private boolean isAssigned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task_map);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isFromComplaints = getIntent().getBooleanExtra("COMP_OR_ANOMALY", true);
        if (isFromComplaints) {
            action = "Assign";
            actionCompleted = "Assigned";
            actionRequest = "Assigning..";
            taskType = "task";
            mComplain = getIntent().getParcelableExtra("COMPLAIN");
        } else {
            action = "Ask to Visit";
            actionCompleted = "Asked";
            actionRequest = "Asking";
            taskType = "visit";
            mAnomaly = getIntent().getParcelableExtra("ANOMALY");
        }
        tvDetail = (TextView) findViewById(R.id.tv_detail);
        tvName = (TextView) findViewById(R.id.tv_name);
        btnAssign = (Button) findViewById(R.id.btnAssign);
        btnAssign.setText(action);
        mAgents = AgentManager.getInstance(this).getmFieldAgents();
        mBottomSheet = (FrameLayout) findViewById(R.id.bottom_sheet);
        mMarkerAgentMap = new HashMap<>();
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnAssignClick();
            }
        });
        loadMap();
        // getCurrentLocation();
    }

    private void getCurrentLocation() {
        if (checkForDeviceLocationEnabled()) {
            //Check if play services are there
            if (checkPlayServices()) {
                //building/initializing google api client used to extract address from lat/long
                buildGoogleApiClient();
            }
        } else {
            //Show setting alert dialog when location is not enabled.
            showSettingsAlert();
        }
    }


    protected synchronized void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();

            mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    //Checking if location is enabled on device
    private boolean checkForDeviceLocationEnabled() {
        boolean isGPSEnabled;
        boolean isNetworkEnabled;

        //Get location service
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        //Check if gps is enabled
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //Check if mobile network is enabled
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return !(!isGPSEnabled && !isNetworkEnabled);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }


    private void showAgentsOnMap() {
        for (FieldAgent agent : mAgents) {
            if (agent != null) {
                Log.e(TAG, "agent details: name" + agent.getName() + "Address: " + agent
                        .getAddress() + "assigned task " + agent.getAssignedTaskCount());
                LatLng currentLoc = new LatLng(agent.getLocation().latitude, agent.getLocation()
                        .longitude);

                String subTitle = null;
                if (agent.getAssignedTaskCount() > 0) {
                    subTitle = " has " + agent.getAssignedTaskCount() + " assigned " + taskType;
                } else {
                    subTitle = " has no " + taskType;
                }
                String title = agent.getName() + subTitle;

                Marker marker = mMap.addMarker(new MarkerOptions().position(currentLoc).title
                        (title).snippet("Tap to assign " + taskType));
                mMarkerAgentMap.put(marker, agent);
                if (mSelectedAgent != null) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new
                            LatLng(mSelectedAgent.getLocation().latitude, mSelectedAgent
                            .getLocation().longitude)).zoom(11.0f).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                } else if (agent.getName().contains("Mahesh Shankar")) {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target
                            (currentLoc).zoom(11.0f).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        }
    }


    private void hideTInfoFrameWithAnim() {
        ViewGroup parent = (ViewGroup) mBottomSheet.getParent();
        int distance = parent.getHeight() - mBottomSheet.getTop();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.setInterpolator(new AccelerateInterpolator(1));
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mBottomSheet.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.playTogether(
                //ObjectAnimator.ofFloat(mBannerInfoFrame, "alpha", 1, 0),
                ObjectAnimator.ofFloat(mBottomSheet, "translationY", 0, distance));
        animatorSet.start();
    }

    /**
     * This method will slide up the frameLayout for Banner Info with smooth animation.
     */

    private void showInfoFrameWithAnim() {
        ViewGroup parent = (ViewGroup) mBottomSheet.getParent();
        int distance = parent.getHeight() - mBottomSheet.getTop();
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.setInterpolator(new DecelerateInterpolator(1));
        mBottomSheet.setVisibility(View.VISIBLE);
        animatorSet.playTogether(
                // ObjectAnimator.ofFloat(mBannerInfoFrame, "alpha", 0, 1),
                ObjectAnimator.ofFloat(mBottomSheet, "translationY", distance, 0));
        animatorSet.start();
    }


    //AlertDialog to be shown when location is not enabled.
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Location is settings");

        // Setting Dialog Message
        alertDialog.setMessage("Location is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 1);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mCurrentLocation = location;
            Log.e("Location", location.toString());
            if (!isMapShown) {
                isMapShown = true;
                loadMap();
            }
        }
    }

    private void loadMap() {

        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                if (mMap != null) {
                    /*LatLng current = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation
                            .getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(current)
                            .zoom(11.0f).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition)
                    );*/

                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                            FieldAgent agent = mMarkerAgentMap.get(marker);
                            if (agent != null) {
                                updateInfoFrameDetails(agent);
                                mSelectedAgent = agent;
                                showInfoFrameWithAnim();
                            }
                        }
                    });

                    mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
                        @Override
                        public void onInfoWindowClose(Marker marker) {
                            hideTInfoFrameWithAnim();
                        }
                    });

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker mark) {
                            return false;
                        }

                    });

                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    showAgentsOnMap();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResultBackAndFinish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateInfoFrameDetails(FieldAgent agent) {
        tvName.setText(agent.getName() + "\n" + agent.getAddress());

        String detailText;
        if (agent.getAssignedTaskCount() == 0) {
            detailText = "has no assigned " + taskType + ". You can assign it to him.";
            tvDetail.setTextColor(ContextCompat.getColor(this, R.color.task0));
        } else if (agent.getAssignedTaskCount() == 1) {
            detailText = "have no 1 " + taskType + ". You can still assign it to him.";
            tvDetail.setTextColor(ContextCompat.getColor(this, R.color.task1));
        } else if (agent.getAssignedTaskCount() == 2) {
            detailText = "have no 2 " + taskType + ". You can consider some other agent";
            tvDetail.setTextColor(ContextCompat.getColor(this, R.color.task2));
        } else if (agent.getAssignedTaskCount() == 3) {
            detailText = "have no 3 " +
                    "" + taskType + ". You should consider some other agent \nas he have already " +
                    "enough " + taskType;
            tvDetail.setTextColor(ContextCompat.getColor(this, R.color.task3));
        } else {
            detailText = "have " + agent.getAssignedTaskCount() + " " + taskType + ". Still " +
                    "him????";
            tvDetail.setTextColor(ContextCompat.getColor(this, R.color.task4));
        }
        tvDetail.setText(detailText);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Failed google api connection");
    }

    private void onBtnAssignClick() {
        if (mSelectedAgent != null) {
            btnAssign.setText(actionRequest);
            btnAssign.setEnabled(false);
            mProgressBar.setVisibility(View.VISIBLE);

            if (isFromComplaints) {
                RestClient.getRestClient().getApiService().updateComplain(mComplain.getUserId(),
                        mComplain.getRequestId(), "inprogress", new Callback<Object>() {

                            @Override
                            public void success(Object o, Response response) {
                                if (response.getStatus() == 200) {
                                    onSucccess();
                                    successSnakeBar();
                                } else onFailiure();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                onFailiure();
                            }
                        });
            } else {
                RestClient.getRestClient().getApiService().updateAnomaly(mAnomaly.getUserId(),
                        "inprogress", new Callback<Object>() {
                            @Override
                            public void success(Object o, Response response) {
                                if (response.getStatus() == 200) {
                                    onSucccess();
                                    successSnakeBar();
                                } else onFailiure();

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                onFailiure();
                            }
                        });
            }
        }
    }


    private void setResultBackAndFinish() {
        Intent intent = new Intent(this, AdminMainActivity.class);
        if (isAssigned) {
            intent.putExtra("assigned", isAssigned);
            setResult(RESULT_OK, intent);
        } else setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    protected void onPause() {
        //googleApiClient.disconnect();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //googleApiClient.connect();
    }

    private void onSucccess() {
        mSelectedAgent.setAssignedTaskCount(mSelectedAgent.getAssignedTaskCount() + 1, this);
        updateInfoFrameDetails(mSelectedAgent);
        btnAssign.setText(actionCompleted);
        btnAssign.setEnabled(false);
        mProgressBar.setVisibility(View.GONE);
        mMap.clear();
        showAgentsOnMap();
        isAssigned = true;
    }

    private void onFailiure() {
        Snackbar.make(tvDetail, "Some problem in Assiging the task", Snackbar.LENGTH_LONG).show();
    }

    private void successSnakeBar(){
        Snackbar.make(tvDetail, taskType +" has been assigned", Snackbar.LENGTH_LONG).show();
    }
}
