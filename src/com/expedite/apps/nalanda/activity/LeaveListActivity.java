package com.expedite.apps.nalanda.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.BuildConfig;
import com.expedite.apps.nalanda.MyApplication;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.adapter.LeaveTypeListAdapter;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.common.Datastorage;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.fragment.LeaveApprovedFragment;
import com.expedite.apps.nalanda.fragment.LeavePendingFragment;
import com.expedite.apps.nalanda.fragment.LeaveRejectFragment;
import com.expedite.apps.nalanda.model.LeaveListModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveListActivity extends BaseActivity {

    private ArrayList<LeaveListModel.LeaveTypeList> mLeaveTypeArrayList = new ArrayList<>();
    private ArrayList<LeaveListModel.LeaveHistory> mLeaveArrayList = new ArrayList<>();
    public static ArrayList<LeaveListModel.LeaveHistory> mPendingLeaveArrayList = new ArrayList<>();
    public static ArrayList<LeaveListModel.LeaveHistory> mApprovedLeaveArrayList = new ArrayList<>();
    public static ArrayList<LeaveListModel.LeaveHistory> mRejectedLeaveArrayList = new ArrayList<>();
    private LeaveTypeListAdapter mLeaveTypeAdapter;
    private RecyclerView mLeaveTypeRecycleView;
    private ProgressBar mProgressBar;
    private String Tag = "LeaveListActivity";
    private TextView mTxtAcedemicYear, mBtnApplyLeave, mTxtTitle;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private String SchoolId = "", StudentId = "", Status = "0";
    AppBarLayout appBarLeave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_list_layout);
        init();
        getLeaveHistoryList();
    }

    public void init() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Leave");
        }

        SchoolId = Datastorage.GetSchoolId(LeaveListActivity.this);
        StudentId = Datastorage.GetStudentId(LeaveListActivity.this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mTxtAcedemicYear = (TextView) findViewById(R.id.txtAcedemicYear);
        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        mBtnApplyLeave = (TextView) findViewById(R.id.btnApplyLeave);
        appBarLeave = findViewById(R.id.appBarLeave);
        appBarLeave.setBackgroundColor(getResources().getColor(R.color.white));
        mLeaveTypeRecycleView = (RecyclerView) findViewById(R.id.leaveTypeRecycleView);
        mLeaveTypeRecycleView.setLayoutManager(new GridLayoutManager(LeaveListActivity.this, 4));
        mLeaveTypeAdapter = new LeaveTypeListAdapter(LeaveListActivity.this, mLeaveTypeArrayList);
        mLeaveTypeRecycleView.setAdapter(mLeaveTypeAdapter);
        mLeaveTypeRecycleView.setNestedScrollingEnabled(false);
        if (getIntent() != null && getIntent().getExtras() != null) {
            Status = getIntent().getExtras().getString("value");
        }
        mBtnApplyLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaveListActivity.this, LeaveApplyActivity.class);
                startActivityForResult(intent, Constants.RESULT_LEAVE_APPLY_SUCCESS);
                onClickAnimation();
            }
        });
    }

    private void getLeaveHistoryList() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            Call<LeaveListModel> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GetLeaveHistoryList(StudentId, SchoolId, BuildConfig.VERSION_CODE + "", Constants.PLATFORM);
            call.enqueue(new Callback<LeaveListModel>() {
                @Override
                public void onResponse(Call<LeaveListModel> call, Response<LeaveListModel> response) {
                    try {
                        LeaveListModel tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            if (tmps.getTotalleave() != null && tmps.getTotalleave().size() > 0 &&
                                    tmps.getTotalleave().get(0) != null && !tmps.getTotalleave().get(0).isEmpty()) {
                                mTxtTitle.setText(Html.fromHtml(tmps.getTotalleave().get(0)));
                            } else {
                                mTxtTitle.setText("");
                            }
                            if (tmps.getAcedemicYear() != null && tmps.getAcedemicYear().size() > 0 &&
                                    tmps.getAcedemicYear().get(0) != null && !tmps.getAcedemicYear().get(0).trim().isEmpty()) {
                                ((CardView) findViewById(R.id.MainAcedemicYear)).setVisibility(View.VISIBLE);
                                mTxtAcedemicYear.setText(Html.fromHtml(tmps.getAcedemicYear().get(0)));
                            } else {
                                ((CardView) findViewById(R.id.MainAcedemicYear)).setVisibility(View.GONE);
                            }
                            if (tmps.getLeaveTypeList() != null && tmps.getLeaveTypeList().size() > 0) {
                                mLeaveTypeArrayList.clear();
                                mLeaveTypeArrayList.addAll(tmps.getLeaveTypeList());
                                mLeaveTypeAdapter.notifyDataSetChanged();
                            }
                            if (tmps.getLeaveHistory() != null && tmps.getLeaveHistory().size() > 0) {
                                mLeaveArrayList.clear();
                                mLeaveArrayList.addAll(tmps.getLeaveHistory());
                            }

                            if (mLeaveArrayList != null && mLeaveArrayList.size() > 0) {
                                mPendingLeaveArrayList.clear();
                                mApprovedLeaveArrayList.clear();
                                mRejectedLeaveArrayList.clear();

                                for (int i = 0; i < mLeaveArrayList.size(); i++) {
                                    // 0 Pending,1 Approved, 2 reject,
                                    if (mLeaveArrayList.get(i).getLeaveStatus().equalsIgnoreCase("0")) {
                                        mPendingLeaveArrayList.add(mLeaveArrayList.get(i));
                                    }
                                    if (mLeaveArrayList.get(i).getLeaveStatus().equalsIgnoreCase("1")) {
                                        mApprovedLeaveArrayList.add(mLeaveArrayList.get(i));
                                    }
                                    if (mLeaveArrayList.get(i).getLeaveStatus().equalsIgnoreCase("2")) {
                                        mRejectedLeaveArrayList.add(mLeaveArrayList.get(i));
                                    }
                                }
                                mViewPager = (ViewPager) findViewById(R.id.viewpager);
                                LeaveListPagerAdapter adapter = new LeaveListPagerAdapter(getSupportFragmentManager());
                                adapter.addFragment(new LeavePendingFragment(), "Pending (" + mPendingLeaveArrayList.size() + ")");
                                adapter.addFragment(new LeaveApprovedFragment(), "Approved (" + mApprovedLeaveArrayList.size() + ")");
                                adapter.addFragment(new LeaveRejectFragment(), "Rejected (" + mRejectedLeaveArrayList.size() + ")");
                                mViewPager.setAdapter(adapter);
                                tabLayout = (TabLayout) findViewById(R.id.id_tabs);
                                tabLayout.setupWithViewPager(mViewPager);
                                changeTabsFont();
                                if (Status != null && Status.equals("1")) {
                                    mViewPager.setCurrentItem(1, true);
                                } else if (Status != null && Status.equals("2")) {
                                    mViewPager.setCurrentItem(2, true);
                                } else {
                                    mViewPager.setCurrentItem(0, true);
                                }
                                ((View) findViewById(R.id.rlMainHistory)).setVisibility(View.VISIBLE);
                            } else {
                                ((View) findViewById(R.id.rlMainHistory)).setVisibility(View.GONE);
                            }
                        } else {
                            if (tmps != null && tmps.getMessage() != null && !tmps.getMessage().isEmpty())
                                Common.showToast(LeaveListActivity.this, tmps.getMessage());
                            ((View) findViewById(R.id.rlMainHistory)).setVisibility(View.GONE);
                        }
                        mProgressBar.setVisibility(View.GONE);
                    } catch (Exception ex) {
                        mProgressBar.setVisibility(View.GONE);
                        Constants.writelog("LeaveListActivity",
                                "getLeaveHistoryList 1877:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<LeaveListModel> call, Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                    Constants.writelog("LeaveListActivity", "getLeaveHistoryList 1877:" + t.getMessage());
                }
            });
        } else {
            Common.showToast(LeaveListActivity.this, getString(R.string.msg_connection));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                LeaveListActivity.this.finish();
                onBackClickAnimation();
                return true;
            default:
                break;
        }
        return false;
    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        vg.setPadding(0,0,0,10);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            if (j == 0) {
                vgTab.setBackgroundColor(getResources().getColor(R.color.calendar_homwwork_bg));
            }
            if (j == 1) {
                vgTab.setBackgroundColor(getResources().getColor(R.color.txt_green_bg));
            }
            if (j == 2) {
                vgTab.setBackgroundColor(getResources().getColor(R.color.txt_red_bg));
            }
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setAllCaps(false);
                    ((TextView) tabViewChild).setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                }
            }
        }
    }

    private class LeaveListPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleNames = new ArrayList();

        private LeaveListPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleNames.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleNames.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LeaveListActivity.this.finish();
        onBackClickAnimation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.RESULT_LEAVE_APPLY_SUCCESS &&
                requestCode == Constants.RESULT_LEAVE_APPLY_SUCCESS) {
            if (isOnline()) {
                getLeaveHistoryList();
            }
        }
    }
}
