package com.expedite.apps.nalanda.activity;

import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.MyApplication;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.common.Datastorage;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.model.AppService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PracticeTestResultActivity extends BaseActivity {
    ProgressBar prg_summmary;
    String testId,testName,flag = "",refereshflag="0" ;
    TextView totalQuestion, value1, value2, value3, value4,value5, totalCorrect, totalWrong, totalTime, totalScore;
    Button btnReview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_test_result);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("Summary Test");
        }
        prg_summmary = findViewById(R.id.prg_summmary);
        totalQuestion = findViewById(R.id.totalQue);
        totalCorrect = findViewById(R.id.totalCorrect);
        totalWrong = findViewById(R.id.totalWrong);
        totalScore = findViewById(R.id.totalScore);
        btnReview=findViewById(R.id.btnReview);
        totalTime = findViewById(R.id.totalTaken);
        value1 = findViewById(R.id.value1);
        value2 = findViewById(R.id.value2);
        value3 = findViewById(R.id.value3);
        value4 = findViewById(R.id.value4);
        value5 = findViewById(R.id.value5);
        testId = getIntent().getStringExtra("testId");
        testName=getIntent().getStringExtra("testName");
        flag = getIntent().getStringExtra("flag");
        if(getIntent().getStringExtra("refereshflag")==null || getIntent().getStringExtra("refereshflag").equals("")){

        }else {
            refereshflag = getIntent().getStringExtra("refereshflag");
        }

        getSummaryResult();
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(PracticeTestResultActivity.this,PracticeTestQAActivity.class);
                i.putExtra("flag","0");
                i.putExtra("testId",testId);
                i.putExtra("testName",testName);
                i.putExtra("fromCompleted", flag);
                startActivity(i);
            }
        });
    }

    private void getSummaryResult() {
        if (isOnline()) {
            prg_summmary.setVisibility(View.VISIBLE);
            String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GetCalculateSummary(mStudentId, mSchoolId, mYearId, testId, Constants.APPname, Constants.CODEVERSION, Constants.PLATFORM);
            call.enqueue(new Callback<AppService>() {
                @Override
                public void onResponse(Call<AppService> call, Response<AppService> response) {
                    try {
                        prg_summmary.setVisibility(View.GONE);
                        AppService tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            String totalParts[] = tmps.getStrlist().get(0).getFirst().split("#@#@");
                            String correctParts[] = tmps.getStrlist().get(0).getSecond().split("#@#@");
                            String wrongParts[] = tmps.getStrlist().get(0).getThird().split("#@#@");
                            String timeParts[] = tmps.getStrlist().get(0).getFourth().split("#@#@");
                            String ScoreParts[] = tmps.getStrlist().get(0).getFifth().split("#@#@");
                            totalQuestion.setText(totalParts[0]);
                            value1.setText(totalParts[1]);
                            totalCorrect.setText(correctParts[0]);
                            value2.setText(correctParts[1]);
                            totalWrong.setText(wrongParts[0]);
                            value3.setText(wrongParts[1]);
                            totalTime.setText(timeParts[0]);
                            value4.setText(timeParts[1]);
                            totalScore.setText(ScoreParts[0]);
                            value5.setText(ScoreParts[1]);
                        }
                    } catch (Exception ex) {
                        prg_summmary.setVisibility(View.GONE);
                        Constants.writelog("BtmNavigationActivity", "getPracticetestLiat 107:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<AppService> call, Throwable t) {
                    prg_summmary.setVisibility(View.GONE);
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
                    if (refereshflag != "" && refereshflag.equals("1")) {
                        ((MyApplication) PracticeTestResultActivity.this
                                .getApplicationContext()).getLocalBroadcastInstance().sendBroadcast(
                                new Intent(new Intent(PracticeTestResultActivity.this
                                        .getResources().getString(R.string.broadcast_feedback_key))));
                    }
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
        // Intent i=new Intent(PracticeTestResultActivity.this,BtmNavigationActivity.class);
        //  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //  i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //  startActivity(i);

        if (refereshflag != "" && refereshflag.equals("1")) {
            ((MyApplication) PracticeTestResultActivity.this
                    .getApplicationContext()).getLocalBroadcastInstance().sendBroadcast(
                    new Intent(new Intent(PracticeTestResultActivity.this
                            .getResources().getString(R.string.broadcast_feedback_key))));
        }




        PracticeTestResultActivity.this.finish();
        onBackClickAnimation();
    }
}
