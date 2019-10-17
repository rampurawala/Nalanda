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
import com.expedite.apps.nalanda.adapter.LibraryListAdapter;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.common.Datastorage;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.model.LibraryListModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryBookHistoryListActivity extends BaseActivity {
    private ProgressBar mProgressBar;
    private LibraryListAdapter mLibraryAdapter;
    private RecyclerView mLibraryRecycleView;
    private GridLayoutManager mGridLayoutManager;
    private String Year_Id = "", SchoolId = "", StudentId = "", ClassSecId = "";
    private String tag = "LibraryBookHistoryListActivity";
    private ArrayList<LibraryListModel.BookIssueList> mBookArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_list_layout);

        init();
        getLibraryList();
    }

    public void init() {
        Activity abc = this;
        Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                "Book Issue History", "Book Issue History");
        SchoolId = Datastorage.GetSchoolId(LibraryBookHistoryListActivity.this);
        ClassSecId = Datastorage.GetClassSecId(LibraryBookHistoryListActivity.this);
        StudentId = Datastorage.GetStudentId(LibraryBookHistoryListActivity.this);
        Year_Id = Datastorage.GetCurrentYearId(LibraryBookHistoryListActivity.this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mLibraryRecycleView = (RecyclerView) findViewById(R.id.libraryRecycleView);
        mGridLayoutManager = new GridLayoutManager(LibraryBookHistoryListActivity.this, 1);
        mLibraryRecycleView.setLayoutManager(mGridLayoutManager);
        mLibraryAdapter = new LibraryListAdapter(LibraryBookHistoryListActivity.this, mBookArrayList);
        mLibraryRecycleView.setAdapter(mLibraryAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackClickAnimation();

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        try {
            super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.activity_library, menu);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {

            switch (item.getItemId()) {
                case android.R.id.home:
                    LibraryBookHistoryListActivity.this.finish();
                    onBackClickAnimation();
                    return true;
                case R.id.book_History:
                    Intent intent = new Intent(LibraryBookHistoryListActivity.this, ChangePinActivity.class);
                    startActivity(intent);
                    onClickAnimation();
                    break;
                default:
                    break;

            }
            if (mToggle.onOptionsItemSelected(item)) {
                return true;
            }

        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getLibraryList() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                // schoolId=8414&class_section_Id=32&studId=47&year_id=5&type=1
                Call<LibraryListModel> call = ((MyApplication) getApplicationContext())
                        .getmRetrofitInterfaceAppService().GetLibraryList(SchoolId, ClassSecId,
                                StudentId, Year_Id, "0", Constants.PLATFORM);

                call.enqueue(new Callback<LibraryListModel>() {
                    @Override
                    public void onResponse(Call<LibraryListModel> call, Response<LibraryListModel> response) {
                        try {
                            LibraryListModel tmp = response.body();
                            if (tmp != null && tmp.getBookarrays() != null && tmp.getBookarrays().get(0).getResponse() != null
                                    && !tmp.getBookarrays().get(0).getResponse().isEmpty()
                                    && tmp.getBookarrays().get(0).getResponse().equalsIgnoreCase("1")) {
                                if (tmp.getBookarrays().get(0).getBookIssueList() != null &&
                                        tmp.getBookarrays().get(0).getBookIssueList().size() > 0) {
                                    mBookArrayList.clear();
                                    mBookArrayList.addAll(tmp.getBookarrays().get(0).getBookIssueList());
                                    mLibraryAdapter.notifyDataSetChanged();
                                    ((View) findViewById(R.id.llMainLayout)).setVisibility(View.VISIBLE);
                                } else {
                                    ((View) findViewById(R.id.llMainLayout)).setVisibility(View.GONE);
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
                    public void onFailure(Call<LibraryListModel> call, Throwable t) {
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
            Common.showToast(LibraryBookHistoryListActivity.this, getString(R.string.msg_connection));
        }
    }

}
