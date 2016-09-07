package com.elecatrach.poc.electrack.client.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.elecatrach.poc.electrack.R;
import com.elecatrach.poc.electrack.client.adapter.MyAdapter;
import com.elecatrach.poc.electrack.client.fragment.CurrentStatsFragment;
import com.elecatrach.poc.electrack.client.fragment.PaymentFragment;
import com.elecatrach.poc.electrack.client.fragment.PredictionFragment;
import com.elecatrach.poc.electrack.client.fragment.SupportFragment;
import com.elecatrach.poc.electrack.client.fragment.UsageFragment;
import com.elecatrach.poc.electrack.client.gcm.GcmMessageHandler;
import com.elecatrach.poc.electrack.client.gcm.RegistrationIntentService;
import com.elecatrach.poc.electrack.common.activities.LoginActivity;
import com.elecatrach.poc.electrack.common.managers.PrefManager;


public class MainActivity extends AppCompatActivity {

    String TITLES[] = {"Current Stats", "Usage history", "Unit Predictor", "Payment Due",
            "Support"};
    //    String NAME = "Dhiraj Sharma";
//    String EMAIL = "Dhiraj.sharma\n@gmail.com";
    int PROFILE = R.drawable.jakir;
    int ICONS[] = {R.drawable.icon_stats_new, R.drawable.icon_history_new, R.drawable
            .icon_analytics_new, R.drawable.icon_payment, R.drawable.icon_support_new};

    private Fragment mCurrentFrag;
    private Fragment mPreviousFrag;
    private boolean mIsCurrentFragIsHome;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear
    // layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    public static final String GCM_TOKEN = "gcmToken";
    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle
    Boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the
        // RecyclerView Object to the xml View

        if (mRecyclerView != null) {
            mRecyclerView.setHasFixedSize(true);                            // Letting the system
            // know that the list objects are of fixed size
        }

        Intent intent = getIntent();


        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("screen")) {
                flag = false;
                String fragment_name = extras.getString("screen", "");
                if (fragment_name.contains("bill")) {
                    displayPaymentFragment();
                }
                if (fragment_name.contains("current")) {
                    displayinitialView();
                }

            }


        }

        mAdapter = new MyAdapter(TITLES, ICONS, PROFILE, MainActivity.this);       // Creating
        // the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter
        // to RecyclerView

        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new
                GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent
                    motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY
                        ());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Drawer.closeDrawers();
                    //    Toast.makeText(MainActivity.this, "The Item Clicked is: " +
                    // recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();

                    Fragment fragment = null;
                    switch (recyclerView.getChildPosition(child)) {

                        case 1:
                            fragment = new CurrentStatsFragment();
                            break;
                        case 2:
                            fragment = new UsageFragment();
                            break;
                        case 3:
                            fragment = new PredictionFragment();
                            break;
                        case 4:
                            fragment = new PaymentFragment();
                            break;
                        case 5:
                            fragment = new SupportFragment();
                            break;
                        case 6:
                            PrefManager manager = new PrefManager(MainActivity.this);
                            manager.setUserLoggedIn(false);
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            MainActivity.this.startActivity(intent);
                            MainActivity.this.finish();
                            break;
                        default:
                            fragment = new CurrentStatsFragment();
                            break;

                    }
                    mPreviousFrag = mCurrentFrag;
                    if (fragment != null) {
                        mCurrentFrag = fragment;
                        mIsCurrentFragIsHome = true;
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();

                        if (fragment instanceof UsageFragment) {
                            fragmentTransaction.add(R.id.container_body, fragment);
                            fragmentTransaction.addToBackStack(null);
                        } else if (fragment instanceof CurrentStatsFragment) {
                            fragmentTransaction.replace(R.id.container_body, fragment);
                            fragmentTransaction.addToBackStack(null);
                        } else {
                            fragmentTransaction.add(R.id.container_body, fragment);
                            fragmentTransaction.addToBackStack(null);
                        }
                        fragmentTransaction.commit();

                    }
                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout
        // Manager


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned
        // to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.openDrawer, R
                .string.closeDrawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed/
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (toolbar != null) {
                    toolbar.setAlpha(1 - slideOffset / 2);
                }
            }


        }; // Drawer Toggle Object Made
        Drawer.addDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        Drawer.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        // display the first navigation drawer view on app launch
        if (flag) displayinitialView();

    }


    private void displayinitialView() {
        Fragment fragment = new CurrentStatsFragment();
        mCurrentFrag = fragment;
        mIsCurrentFragIsHome = true;
        mPreviousFrag = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();

        // set the toolbar title
        getSupportActionBar().setTitle(R.string.title_home);
    }

    private void displayPaymentFragment() {

        Fragment fragment = new PaymentFragment();
        mCurrentFrag = fragment;
        mIsCurrentFragIsHome = true;
        mPreviousFrag = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_body, fragment).addToBackStack(null);
        fragmentTransaction.commit();

        getSupportActionBar().setTitle("Pay Bill");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


  /*  @Override
    public void onBackPressed() {
        if(!mIsCurrentFragIsHome){
            showBackPressedFrag();
        }else {
            super.onBackPressed();
        }
    }*/

    private void showBackPressedFrag() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, mPreviousFrag);
        fragmentTransaction.commit();
        mCurrentFrag = mPreviousFrag;
        mPreviousFrag = null;
        mIsCurrentFragIsHome = true;
    }
}
