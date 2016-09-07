package com.elecatrach.poc.electrack.admin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.elecatrach.poc.electrack.R;
import com.elecatrach.poc.electrack.admin.fragments.AdminComplaintsFragment;
import com.elecatrach.poc.electrack.admin.fragments.AnalyticsFragment;
import com.elecatrach.poc.electrack.admin.fragments.AnomalyFragment;
import com.elecatrach.poc.electrack.common.activities.LoginActivity;
import com.elecatrach.poc.electrack.common.managers.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {

 /*   @Override
    protected Fragment createFragment() {
        return new AdminHomeFragment();
    }*/

    private ViewPager mViewPager;
    private TabLayout mTabs;
    private int currentFragment = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.admin_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!= null){
            getSupportActionBar().setTitle("");
        }


        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("anomaly")) {

                currentFragment = 2;


            }
        }
        initAllViews();
        setupViewPager();
        setUpTabLayout();
    }

    private void initAllViews() {
        mTabs = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void setupViewPager() {
        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFrag(new AdminComplaintsFragment(), "COMPLAINTS");
        mPagerAdapter.addFrag(new AnalyticsFragment(), "ANALYTICS");
        mPagerAdapter.addFrag(new AnomalyFragment(), "ANOMALIES");
        mViewPager.setAdapter(mPagerAdapter);
    }


    private void setUpTabLayout() {
            /*TextView tvOneWayTrip = (TextView) LayoutInflater.from(this).inflate(R.layout
            .custom_tab_layout, null);
            tvOneWayTrip.setText("ONE WAY TRIP");
            tvOneWayTrip.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_favourite,
            0, 0);
            mTabs.getTabAt(0).setCustomView(tvOneWayTrip);

            TextView tvRoundTrip = (TextView) LayoutInflater.from(this).inflate(R.layout
            .custom_tab, null);
            tvRoundTrip.setText("ROUND TRIP ");
            tvRoundTrip.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_call, 0, 0);
            mTabs.getTabAt(1).setCustomView(tvRoundTrip);*/

        mTabs.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(currentFragment);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                PrefManager manager = new PrefManager(this);
                manager.setUserLoggedIn(false);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
