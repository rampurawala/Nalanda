package com.expedite.apps.nalanda.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;


import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.MyApplication;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.common.Datastorage;
import com.expedite.apps.nalanda.constants.Constants;

import com.expedite.apps.nalanda.fragment.PracticeTestFragment;
import com.expedite.apps.nalanda.model.AppService;
import com.google.gson.Gson;




import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BtmNavigationActivity extends BaseActivity {
    FragmentManager manager;
    FragmentTransaction transaction;
    private TextView mTextMessage;
    String jsonListPending, jsonListCompleted;
    List<AppService.ListArray> listPending = new ArrayList<>();
    List<AppService.ListArray> listCompleted = new ArrayList<>();
    ProgressBar prg_test;
    BottomNavigationView navigation;

    private LocalBroadcastManager mBroadcastManager;
    BroadcastReceiver mReceiverFilter;
    Common common;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_pending:
                    fragment = new PracticeTestFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("flag", "1");
                    bundle.putString("testList", jsonListPending);
                    fragment.setArguments(bundle);
                    break;
                case R.id.navigation_completed:
                    fragment = new PracticeTestFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("flag", "0");
                    bundle1.putString("testList", jsonListCompleted);
                    fragment.setArguments(bundle1);
                    break;
            }
            transaction = manager.beginTransaction();
            transaction.replace(R.id.f1, fragment).commit();
            //  transaction.disallowAddToBackStack();
            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btm_navigation);
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                setTitle("Practice Test");
            }


            mTextMessage = (TextView) findViewById(R.id.message);
            prg_test = findViewById(R.id.prg_test);
            common = new Common(this);
            navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            manager = getSupportFragmentManager();
            // set Fragmentclass Arguments
            getPracticeTestList();

            mBroadcastManager = ((MyApplication) getApplicationContext())
                    .getLocalBroadcastInstance();
            mReceiverFilter = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, final Intent intent) {
                    getPracticeTestList();
                }
            };
        } catch (Exception e) {
            Constants.writelog("BtmNavigationActivity", "onCreate 101:" + e.getMessage());
        }
        // streamFragment.setArguments(bundle);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mBroadcastManager.registerReceiver(mReceiverFilter, new IntentFilter(
                getResources().getString(R.string.broadcast_feedback_key)));
    }


    private void getPracticeTestList() {
        if (isOnline()) {
            navigation.setVisibility(View.GONE);
            prg_test.setVisibility(View.VISIBLE);
            String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GetPracticeTestList(mStudentId, mSchoolId, mYearId, common.getSession(Constants.APPname), Constants.CODEVERSION, Constants.PLATFORM);
            call.enqueue(new Callback<AppService>() {
                @Override
                public void onResponse(Call<AppService> call, Response<AppService> response) {
                    try {
                        navigation.setVisibility(View.VISIBLE);

                        prg_test.setVisibility(View.GONE);
                        AppService tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                          /*  Time Today = new Time();
                            Today.setToNow();*/
                            navigation.setSelectedItemId(R.id.navigation_pending);
                            navigation.getMenu().getItem(0).setChecked(true);

                            PracticeTestFragment practiceTestFragment = new PracticeTestFragment();
                            int size = tmps.getStrlist().size();
                            listCompleted.clear();
                            listPending.clear();

                            for (int i = 0; i < size; i++) {
                                if (tmps.getStrlist().get(i).getThird().equals("0")) {
                                    listCompleted.add(tmps.getStrlist().get(i));
                                } else {
                                    listPending.add(tmps.getStrlist().get(i));
                                }
                            }

                            Bundle bundle = new Bundle();
                            bundle.putString("flag", "1");
                            Gson gson = new Gson();
                            jsonListPending = gson.toJson(listPending);

                            Gson gson1 = new Gson();
                            jsonListCompleted = gson1.toJson(listCompleted);

                            bundle.putString("testList", jsonListPending);
                            practiceTestFragment.setArguments(bundle);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.f1, practiceTestFragment)/*.disallowAddToBackStack()*/
                                    .commit();

                            //mcommon.setSession(Constants.LastAppControlCall, Today.monthDay + "2");
                        }
                    } catch (Exception ex) {
                        prg_test.setVisibility(View.GONE);
                        navigation.setVisibility(View.VISIBLE);
                        Constants.writelog("BtmNavigationActivity", "getPracticetestLiat 107:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<AppService> call, Throwable t) {
                    prg_test.setVisibility(View.GONE);
                    navigation.setVisibility(View.VISIBLE);
                    Constants.writelog("BtmNavigationActivity", "getPracticetestLiat 113:" + t.getMessage());
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    super.onBackPressed();

                    onBackClickAnimation();
                    break;
            }
        } catch (Exception ex) {
            Constants.writelog("onOptionsItemSelected 177:", ex.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearStack();
        onBackClickAnimation();
       /* Intent i=new Intent(BtmNavigationActivity.this,HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);*/
    }

    public void clearStack() {
        //Here we are clearing back stack fragment entries
        int backStackEntry = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getSupportFragmentManager().popBackStackImmediate();
            }
        }

        //Here we are removing all the fragment that are shown here
        if (getSupportFragmentManager().getFragments() != null && getSupportFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getSupportFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getSupportFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiverFilter != null)
            mBroadcastManager.unregisterReceiver(mReceiverFilter);
    }
}
