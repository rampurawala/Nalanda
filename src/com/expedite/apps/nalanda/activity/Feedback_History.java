package com.expedite.apps.nalanda.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.adapter.FeedbackListAdapter;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.common.Datastorage;
import com.expedite.apps.nalanda.common.GetIp;
import com.expedite.apps.nalanda.constants.Constants;

import org.ksoap2.serialization.SoapObject;

public class Feedback_History extends BaseActivity {

    private String[] mFeedBackArrayList = new String[0];
    private FeedbackListAdapter mFeedBackAdapter;
    private RecyclerView mFeedBackRecycleView;
    private ProgressBar mProgressBar;
    private GridLayoutManager mGridLayoutManager;
    private String tag = "Feedback_History";

    //private String VersionCode = "1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback__history);
        Activity abc = this;
        Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                "Query History", "Query History");
        init();
        new getFeedbackHistory().execute();
    }

    public void init() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        //Feed Back
        mFeedBackRecycleView = (RecyclerView) findViewById(R.id.feedbackRecycleView);
        mGridLayoutManager = new GridLayoutManager(Feedback_History.this, 1);
        mFeedBackRecycleView.setLayoutManager(mGridLayoutManager);
        mFeedBackAdapter = new FeedbackListAdapter(Feedback_History.this, mFeedBackArrayList);
        mFeedBackRecycleView.setAdapter(mFeedBackAdapter);
    }

    public class getFeedbackHistory extends AsyncTask<Void, Void, Void> {
        private final ProgressDialog dialog = new ProgressDialog(Feedback_History.this);

        @Override
        protected void onPreExecute() {
            try {
               /* dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setMessage("Fetching details...");
                dialog.show();*/
                mProgressBar.setVisibility(View.VISIBLE);
            } catch (Exception ex) {
                Constants.writelog(tag,
                        "getFeedbackHistory()87 Exception:" + ex.getMessage() + ":::::"
                                + ex.getStackTrace());
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            GetFeedbackList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(getApplicationContext());
                } else {
                    if (mFeedBackArrayList != null && mFeedBackArrayList.length > 0) {
                        if (!mFeedBackArrayList[0].equals("0")) {
                            mFeedBackAdapter = new FeedbackListAdapter(Feedback_History.this, mFeedBackArrayList);
                            mFeedBackRecycleView.setAdapter(mFeedBackAdapter);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    mFeedBackArrayList[1], Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please Try Again.", Toast.LENGTH_SHORT).show();
                    }
                }
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (Exception err) {
                Constants.writelog(tag,
                        "onPost :183 " + err.getMessage());
                Intent in = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(in);
                finish();
                onBackClickAnimation();
            } finally {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.clear();
//        MenuInflater inflater = getMenuInflater();
//        //inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Feedback_History.this.finish();
                Common.onBackClickAnimation(Feedback_History.this);
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(Feedback_History.this, ReportBugActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            onBackClickAnimation();
        } catch (Exception err) {
            Constants.Logwrite("StudInfo:", "BackPressed:" + err.getMessage()
                    + "StackTrace::" + err.getStackTrace().toString());
        }
    }


    private String[] GetFeedbackList() {
        try {
            SoapObject request = new SoapObject(GetIp.NAMESPACE,
                    Constants.METHOD_NAME_GET_FEEdBACK_HISTORY);
            int studId = Integer.parseInt(Datastorage
                    .GetStudentId(getApplicationContext()));
            int schoolId = Integer.parseInt(Datastorage
                    .GetSchoolId(getApplicationContext()));
            int yearid = Integer.parseInt(Datastorage
                    .GetCurrentYearId(getApplicationContext()));
            request.addProperty("studid", studId);
            request.addProperty("schoolid", schoolId);
            request.addProperty("yearid", yearid);
            request.addProperty("platform", "0");
            SoapObject result = Constants.CallWebMethod(
                    getApplicationContext(), request,
                    Constants.METHOD_NAME_GET_FEEdBACK_HISTORY, true);
            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                if (obj2 != null) {
                    int count = obj2.getPropertyCount();
                    mFeedBackArrayList = new String[count];
                    try {
                        for (int i = 0; i < count; i++) {
                            mFeedBackArrayList[i] = obj2.getProperty(i).toString();
                        }
                    } catch (Exception ex) {
                        Constants.writelog(tag,
                                "GetFeedbackList()_Error :193 " + ex.getMessage());
                    }
                }
                //  categoryname=category.split(",");
            } else {
                Constants.writelog(tag,
                        "Error: GetFeedbackList()255 No responce from server.");
            }
        } catch (Exception err) {
            Constants.writelog(tag, "GetFeedbackList()321 Exception:"
                    + err.getMessage() + "StackTrace::"
                    + err.getStackTrace().toString());
            Intent in = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(in);
            finish();
            onBackClickAnimation();
        }
        return mFeedBackArrayList;
    }
}
