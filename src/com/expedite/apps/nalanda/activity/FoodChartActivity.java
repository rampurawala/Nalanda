package com.expedite.apps.nalanda.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.MyApplication;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.adapter.FoodChartListAdapter;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.common.Datastorage;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.model.FoodChartListModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodChartActivity extends BaseActivity {
    private ProgressBar mProgressBar;
    private FoodChartListAdapter mFoodChartAdapter;
    private RecyclerView mFoodChartRecycleView;
    private GridLayoutManager mGridLayoutManager;
    private String Year_Id = "", SchoolId = "", StudentId = "", ClassSecId = "";
    private String tag = "FoodChartActivity";
    private String mIsFromHome = "";
    private ArrayList<FoodChartListModel.TimeTableList> mFoodChartList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_chart_list_layout);
        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");

        init();
        getFoodChartList();
    }

    public void init() {
        Activity abc = this;
        Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                "Food Chart", "Food Chart");
        SchoolId = Datastorage.GetSchoolId(FoodChartActivity.this);
        ClassSecId = Datastorage.GetClassSecId(FoodChartActivity.this);
        Year_Id = Datastorage.GetCurrentYearId(FoodChartActivity.this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mFoodChartRecycleView = (RecyclerView) findViewById(R.id.foodChartRecycleView);
        mGridLayoutManager = new GridLayoutManager(FoodChartActivity.this, 1);
        mFoodChartRecycleView.setLayoutManager(mGridLayoutManager);
        mFoodChartAdapter = new FoodChartListAdapter(FoodChartActivity.this, mFoodChartList);
        mFoodChartRecycleView.setAdapter(mFoodChartAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FoodChartActivity.this.finish();
                onBackClickAnimation();
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
            super.onBackPressed();
            onBackClickAnimation();
        } else {
            Intent intent = new Intent(FoodChartActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void getFoodChartList() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                Call<FoodChartListModel> call = ((MyApplication) getApplicationContext())
                        .getmRetrofitInterfaceAppService().GetFoodChartList(SchoolId, ClassSecId, "1", Year_Id, Constants.PLATFORM);

                call.enqueue(new Callback<FoodChartListModel>() {
                    @Override
                    public void onResponse(Call<FoodChartListModel> call, Response<FoodChartListModel> response) {
                        try {
                            FoodChartListModel tmp = response.body();
                            if (tmp != null && tmp.getArrays() != null && tmp.getArrays().get(0).getResponse() != null
                                    && !tmp.getArrays().get(0).getResponse().isEmpty()
                                    && tmp.getArrays().get(0).getResponse().equalsIgnoreCase("1")) {
                                if (tmp.getArrays().get(0).getTimeTableList() != null &&
                                        tmp.getArrays().get(0).getTimeTableList().size() > 0) {
                                    mFoodChartList.clear();
                                    mFoodChartList.addAll(tmp.getArrays().get(0).getTimeTableList());
                                    mFoodChartAdapter.notifyDataSetChanged();
                                    ((View) findViewById(R.id.llMainLayout)).setVisibility(View.VISIBLE);
                                } else {
                                    ((View) findViewById(R.id.llMainLayout)).setVisibility(View.GONE);
                                }
                            } else {
                                if (tmp != null && tmp.getArrays() != null && tmp.getArrays().get(0).getMessage() != null && tmp.getArrays().get(0).getMessage() != "") {
                                    Common.showToast(FoodChartActivity.this, tmp.getArrays().get(0).getMessage());
                                } else {
                                    Common.showToast(FoodChartActivity.this, "No Data Available..");
                                }
                            }
                            mProgressBar.setVisibility(View.GONE);
                        } catch (Exception ex) {
                            Constants.writelog(tag, "Exception_113:" + ex.getMessage()
                                    + "::::::" + ex.getStackTrace());
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<FoodChartListModel> call, Throwable t) {
                        Constants.writelog(tag, "Exception_113:" + t.getMessage());
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

            } catch (Exception ex) {
                mProgressBar.setVisibility(View.GONE);
                Constants.writelog(tag, "Exception_113:" + ex.getMessage()
                        + "::::::" + ex.getStackTrace());
            }
        } else {
            Common.showToast(FoodChartActivity.this, getString(R.string.msg_connection));
        }
    }

}
