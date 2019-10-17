package com.expedite.apps.nalanda.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.MyApplication;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.common.Datastorage;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.model.CurriculumListModel;
import com.expedite.apps.nalanda.model.SendEmailDownlodModel;
import com.google.android.youtube.player.YouTubePlayer;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;
import com.thefinestartist.ytpa.utils.YouTubeUrlParser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurriculumListActivity extends BaseActivity {
    private ProgressBar mProgressBar;
    RecyclerView mFolderRecyclerview, mFileRecyclerview;
    private String SchoolId = "", ClassId = "";
    private String tag = "CurriculumListActivity";
    private String mIsFromHome = "", mFolderId = "0", mFoldername = "";
    CurriculumFolderAdapter mFolderadapter = null;
    CurriculumFileAdapter mFileadapter = null;
    private ArrayList<CurriculumListModel.FolderList> mFolderArray = new ArrayList<>();
    private ArrayList<CurriculumListModel.FileList> mFileArray = new ArrayList<>();
    ArrayList<String> mFolderID = new ArrayList<>();
    ArrayList<String> mFolderName = new ArrayList<>();
    String headerText = "";
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressBar progressBar;
    private EditText mEmailid;
    private AlertDialog alert;

    RelativeLayout MainLayout;
    LinearLayout empty_folder_lyt_file_detail;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.curriculum_list_layout);
        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");
        init();
        getCurriculumList();
    }

    public void init() {
        try {
            MainLayout=findViewById(R.id.Mainlayout);
            empty_folder_lyt_file_detail = findViewById(R.id.empty_folder_lyt_file_detail);

            Activity abc = this;
            Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                    "Curriculum", "Curriculum");
            SchoolId = Datastorage.GetSchoolId(CurriculumListActivity.this);
            ClassId = Datastorage.GetClassId(CurriculumListActivity.this);
            mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
            mFolderRecyclerview = (RecyclerView) findViewById(R.id.folderrecyclerview);
            mFolderRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mFolderRecyclerview.setNestedScrollingEnabled(false);
            mFolderadapter = new CurriculumFolderAdapter(CurriculumListActivity.this, mFolderArray);
            mFolderRecyclerview.setAdapter(mFolderadapter);

            mFileRecyclerview = (RecyclerView) findViewById(R.id.filerecyclerview);
            mFileRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mFileadapter = new CurriculumFileAdapter(CurriculumListActivity.this, mFileArray);
            mFileRecyclerview.setAdapter(mFileadapter);
            mFileRecyclerview.setNestedScrollingEnabled(false);
        } catch (Exception ex) {
            Constants.writelog(tag, "Exception_72:" + ex.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        try {
            super.onCreateOptionsMenu(menu);
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
                    if (mFolderID.size() > 0) {
                     /*   mFolderId = mFolderID.get(mFolderID.size() - 1);
                        mFolderID.remove(mFolderID.size() - 1);
                        getCurriculumList();
                        onBackClickAnimation();*/
                        onBackPressed();

                    } else {
                        super.onBackPressed();
                        CurriculumListActivity.this.finish();
                        onBackClickAnimation();
                    }
                    return true;
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

    @Override
    public void onBackPressed() {
        if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
            if (mFolderID.size() > 0) {
                try {
                    mFolderId = mFolderID.get(mFolderID.size() - 1);
                    mFolderID.remove(mFolderID.size() - 1);
                    mFolderName.remove(mFolderName.size() - 1);
                    if (mFolderName.size() > 1) {
                        mFoldername = mFolderName.get(mFolderName.size() - 1);
                    } else {
                        mFoldername = mFolderName.get(0);
                    }
                } catch (Exception ex) {
                }
                getCurriculumList();
                onBackClickAnimation();
            } else {
                super.onBackPressed();
                CurriculumListActivity.this.finish();
                onBackClickAnimation();
            }
        } else {
            Intent intent = new Intent(CurriculumListActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    private void getCurriculumList() {
        if (isOnline()) {
            mProgressBar.setVisibility(View.VISIBLE);
            try {
                //http://app.vayuna.com/service.asmx/folderFileList?schoolId=8414&class_id=23&folderId=0
                Call<CurriculumListModel> call = ((MyApplication) getApplicationContext())
                        .getmRetrofitInterfaceAppService().GetCurriculumList(SchoolId, ClassId, mFolderId, Constants.PLATFORM);
                call.enqueue(new Callback<CurriculumListModel>() {
                    @Override
                    public void onResponse(Call<CurriculumListModel> call, Response<CurriculumListModel> response) {
                        try {
                            mFolderArray.clear();
                            mFileArray.clear();
                            CurriculumListModel tmp = response.body();
                            if (tmp != null && tmp.getArray() != null && tmp.getArray().get(0).getResponse() != null
                                    && !tmp.getArray().get(0).getResponse().isEmpty()
                                    && tmp.getArray().get(0).getResponse().equalsIgnoreCase("1")) {
                                mFolderArray.addAll(tmp.getArray().get(0).getFolderList());
                                mFileArray.addAll(tmp.getArray().get(0).getFileList());
                                if (!mFolderName.isEmpty()) {
                                    if (mFolderName.size() > 1) {
                                        headerText = "";
                                        for (int i = 0; i < mFolderName.size() - 1; i++) {
                                            if (i == mFolderName.size() - 1) {
                                                headerText += mFolderName.get(i);
                                            } else {
                                                headerText += mFolderName.get(i) + "->";
                                            }
                                        }
                                    } else {
                                        headerText = "";
                                    }
                                    View customview = CurriculumListActivity.this.getLayoutInflater().inflate(R.layout.mainactionbar, null);
                                    TextView label = (TextView) customview.findViewById(R.id.label);
                                    label.setText(mFoldername);
                                    if (!headerText.isEmpty()) {
                                        TextView label1 = (TextView) customview.findViewById(R.id.label1);
                                        label1.setText(headerText);
                                        label1.setVisibility(View.VISIBLE);
                                    } else {
                                        TextView label1 = (TextView) customview.findViewById(R.id.label1);
                                        label1.setVisibility(View.INVISIBLE);
                                    }
                                    getSupportActionBar().setCustomView(customview);
                                } else {
                                    View customview = CurriculumListActivity.this.getLayoutInflater().inflate(R.layout.mainactionbar, null);
                                    TextView label = (TextView) customview.findViewById(R.id.label);
                                    label.setText("Curriculum");
                                    TextView label1 = (TextView) customview.findViewById(R.id.label1);
                                    label1.setVisibility(View.INVISIBLE);
                                    getSupportActionBar().setCustomView(customview);
                                    //Constants.setActionbar(getSupportActionBar(), CurriculumListActivity.this, getApplicationContext(), "Curriculum", "Curriculum");
                                }
                                MainLayout.setVisibility(View.VISIBLE);
                                empty_folder_lyt_file_detail.setVisibility(View.GONE);
                                mFolderadapter.notifyDataSetChanged();
                                mFileadapter.notifyDataSetChanged();
                            } else {
                                if (tmp != null && tmp.getArray() != null && tmp.getArray().get(0).getMessage() != null && tmp.getArray().get(0).getMessage() != "") {
                                    Common.showToast(CurriculumListActivity.this, tmp.getArray().get(0).getMessage());
                                 //   onBackPressed();
                                    MainLayout.setVisibility(View.GONE);
                                    empty_folder_lyt_file_detail.setVisibility(View.VISIBLE);



                                } else {
                                    Common.showToast(CurriculumListActivity.this, "No Curriculum Data Available..");
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
                    public void onFailure(Call<CurriculumListModel> call, Throwable t) {
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
            Common.showToast(CurriculumListActivity.this, getString(R.string.msg_connection));
        }
    }

    private class CurriculumFolderAdapter extends RecyclerView.Adapter<CurriculumFolderAdapter.ViewHolder> {

        ArrayList<CurriculumListModel.FolderList> mFolderArray;
        Activity activity;

        public CurriculumFolderAdapter(Activity activity, ArrayList<CurriculumListModel.FolderList> mFolderArray) {
            this.activity = activity;
            this.mFolderArray = mFolderArray;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.curriculum_folder_raw_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            try {
                final CurriculumListModel.FolderList tmp = mFolderArray.get(position);
                if (tmp.getName() != null && !tmp.getName().isEmpty())
                    holder.mFolderTxt.setText(Html.fromHtml(tmp.getName()));

                holder.mLLMainFolder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mFolderID.add(mFolderId);
                        if (mFolderId.equals("0")) {
                            mFolderName.clear();
                        }
                        if (!mFolderName.contains(tmp.getName()))
                            mFolderName.add(tmp.getName());
                            mFolderId = tmp.getId();
                            mFoldername = tmp.getName();
                            getCurriculumList();

                    }
                });
            } catch (Exception ex) {
                Constants.writelog(tag, "onBindViewHolder():67 " + ex.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return mFolderArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView mFolderTxt;
            LinearLayout mLLMainFolder;

            public ViewHolder(View itemView) {
                super(itemView);
                mFolderTxt = (TextView) itemView.findViewById(R.id.txtTitle);
                mLLMainFolder = (LinearLayout) itemView.findViewById(R.id.mainfolderll);
            }
        }
    }

    private class CurriculumFileAdapter extends RecyclerView.Adapter<CurriculumFileAdapter.ViewHolder> {

        Activity activity;
        ArrayList<CurriculumListModel.FileList> mFileArray;

        public CurriculumFileAdapter(Activity activity, ArrayList<CurriculumListModel.FileList> mFileArray) {
            this.activity = activity;
            this.mFileArray = mFileArray;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.curriculum_file_raw_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            try {
                final CurriculumListModel.FileList tmp = mFileArray.get(position);
                if (tmp.getName() != null && !tmp.getName().isEmpty())
                    holder.mFileTxt.setText(Html.fromHtml(tmp.getName()));
                if (tmp.getType() != null && !tmp.getType().isEmpty()) {
                    if (tmp.getType().equals("1")) {
                        holder.mImgFile.setImageDrawable(getResources().getDrawable(R.drawable.pdficon));
                        holder.mImgDownload.setVisibility(View.VISIBLE);
                        holder.mImgDownload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ShowDialog(tmp.getId(), tmp.getName(), tmp.getUrl());
                            }
                        });
                    } else if (tmp.getType().equals("2")) {
                        holder.mImgFile.setImageDrawable(getResources().getDrawable(R.drawable.imageicon));
                        holder.mImgDownload.setVisibility(View.GONE);
                    } else if (tmp.getType().equals("3")) {
                        holder.mImgFile.setImageDrawable(getResources().getDrawable(R.drawable.audioicon));
                    } else if (tmp.getType().equals("4")) {
                        holder.mImgFile.setImageDrawable(getResources().getDrawable(R.drawable.videoicon));
                        holder.mImgDownload.setVisibility(View.GONE);
                    } else {
                        holder.mImgFile.setImageDrawable(getResources().getDrawable(R.drawable.file1));
                        holder.mImgDownload.setVisibility(View.GONE);
                    }
                }

                holder.mLLFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (tmp.getType() != null && !tmp.getType().isEmpty()) {
                            if (mFolderName.size() > 1) {
                                headerText = "";
                                for (int i = 0; i < mFolderName.size(); i++) {
                                    if (i == mFolderName.size() - 1) {
                                        headerText += mFolderName.get(i);
                                    } else {
                                        headerText += mFolderName.get(i) + "->";
                                    }
                                }
                            }
                            if (tmp.getType().equalsIgnoreCase("1")) {
                                if (tmp.getUrl() != null &&
                                        !tmp.getUrl().isEmpty()) {
                                    Intent intent = new Intent(CurriculumListActivity.this, CurriculumPdfViewActivity.class);
                                    intent.putExtra("Id", tmp.getId());
                                    intent.putExtra("Name", tmp.getName());
                                    intent.putExtra("Url", tmp.getUrl());
                                    intent.putExtra("HeaderText", headerText);
                                    startActivity(intent);
                                    onClickAnimation();
                                }
                            } else if (tmp.getType().equalsIgnoreCase("2")) {
                                if (tmp.getUrl() != null &&
                                        !tmp.getUrl().isEmpty()) {
                                    Intent intent = new Intent(CurriculumListActivity.this, CurriculumImageViewActivity.class);
                                    intent.putExtra("Id", tmp.getId());
                                    intent.putExtra("Name", tmp.getName());
                                    intent.putExtra("Url", tmp.getUrl());
                                    intent.putExtra("HeaderText", headerText);
                                    startActivity(intent);
                                    onClickAnimation();
                                }
                            } else if (tmp.getType().equalsIgnoreCase("3")) {
                                if (tmp.getUrl() != null &&
                                        !tmp.getUrl().isEmpty()) {
                                    Intent intent = new Intent(CurriculumListActivity.this, CurriculumMediaPlayerActivity.class);
                                    intent.putExtra("Id", tmp.getId());
                                    intent.putExtra("Name", tmp.getName());
                                    intent.putExtra("Url", tmp.getUrl());
                                    intent.putExtra("HeaderText", headerText);
                                    startActivity(intent);
                                    onClickAnimation();
                                }
                            } else if (tmp.getType().equalsIgnoreCase("4")) {
                                if (tmp.getUrl() != null &&
                                        !tmp.getUrl().isEmpty()) {
                                    String vidoeId = YouTubeUrlParser.getVideoId(tmp.getUrl());
                                    if (vidoeId != null && !vidoeId.isEmpty()) {
                                        Intent intent = new Intent(CurriculumListActivity.this, YouTubePlayerActivity.class);
                                        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, vidoeId);
                                        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE,
                                                YouTubePlayer.PlayerStyle.DEFAULT);
                                        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION,
                                                Orientation.AUTO);
                                        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, true);
                                        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);
                                        intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_ENTER, R.anim.fade_in);
                                        intent.putExtra(YouTubePlayerActivity.EXTRA_ANIM_EXIT, R.anim.fade_out);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }

                    }
                });
            } catch (Exception ex) {
                Constants.writelog(tag, "onBindViewHolder():67 " + ex.getMessage());
            }
        }

        @Override
        public int getItemCount() {
            return mFileArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mFileTxt;
            ImageView mImgFile;
            RelativeLayout mLLFile;
            ImageView mImgDownload;

            public ViewHolder(View itemView) {
                super(itemView);
                mFileTxt = (TextView) itemView.findViewById(R.id.txtTitle);
                mImgFile = (ImageView) itemView.findViewById(R.id.imgfile);
                mLLFile = (RelativeLayout) itemView.findViewById(R.id.mainfilell);
                mImgDownload = (ImageView) itemView.findViewById(R.id.imgDownload);

            }
        }
    }

    private void ShowDialog(final String id, final String name, final String url) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.download_file_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        mEmailid = (EditText) promptView.findViewById(R.id.editEmailid);
        final TextView mSendbtn = (TextView) promptView.findViewById(R.id.Sendbtn);
        final ImageView mClosebtn = (ImageView) promptView.findViewById(R.id.closebtn);
        progressBar = promptView.findViewById(R.id.progressBar);
        getEmail();
        mClosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alert.isShowing())
                    alert.dismiss();
            }
        });

        mSendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailid.getText().toString().trim();
                if (email.isEmpty()) {
                    Common.showToast(CurriculumListActivity.this, "Enter Emailid");
                } else if (!email.matches(emailPattern)) {
                    Common.showToast(CurriculumListActivity.this, "Enter valid Emailid");
                } else if (email != null && !email.isEmpty() && email.matches(emailPattern)) {
                    SendEmail(url, id, name, mEmailid.getText().toString().trim());
                }
            }
        });
        alert = alertDialogBuilder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    private void getEmail() {
        try {
            if (isOnline()) {
                progressBar.setVisibility(View.VISIBLE);
                Call<SendEmailDownlodModel> call = ((MyApplication) getApplicationContext().getApplicationContext())
                        .getmRetrofitInterfaceAppService().GetStudentEmailid(Datastorage.GetSchoolId(this), Datastorage.GetStudentId(this), Integer.parseInt(Datastorage.GetCurrentYearId(this)));
                call.enqueue(new Callback<SendEmailDownlodModel>() {
                    @Override
                    public void onResponse(Call<SendEmailDownlodModel> call, Response<SendEmailDownlodModel> response) {
                        SendEmailDownlodModel tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            if (tmps.getStrResult() != null && !tmps.getStrResult().isEmpty()) {
                                mEmailid.setText(tmps.getStrResult());
                            }
                        } else {
                            if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty())
                                Common.showToast(CurriculumListActivity.this, tmps.getResponse());
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<SendEmailDownlodModel> call, Throwable t) {
                        Constants.writelog("CurriculumListActivity",
                                "GetEmail:" + t.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });

            } else {
                Common.showToast(CurriculumListActivity.this, getString(R.string.msg_connection));
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void SendEmail(String doc, String CirId, String name, String email) {
        try {
            if (isOnline()) {
                progressBar.setVisibility(View.VISIBLE);
                Call<SendEmailDownlodModel> call = ((MyApplication) getApplicationContext().getApplicationContext())
                        .getmRetrofitInterfaceAppService()
                        .SendEmailid(Datastorage.GetStudentId(this), Datastorage.GetSchoolId(this), Integer.parseInt(Datastorage.GetCurrentYearId(this)), 2, email, doc, CirId, name, Constants.PLATFORM);
                call.enqueue(new Callback<SendEmailDownlodModel>() {
                    @Override
                    public void onResponse(Call<SendEmailDownlodModel> call, Response<SendEmailDownlodModel> response) {
                        SendEmailDownlodModel tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1")) {
                            if (tmps.getStrResult() != null && !tmps.getStrResult().isEmpty()) {
                                Common.showToast(CurriculumListActivity.this, tmps.getStrResult());
                                if (alert.isShowing())
                                    alert.dismiss();
                            }
                        } else {
                            if (tmps.getStrResult() != null && !tmps.getStrResult().isEmpty()) {
                                Common.showToast(CurriculumListActivity.this, tmps.getStrResult());
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<SendEmailDownlodModel> call, Throwable t) {
                        Constants.writelog("CurriculumListActivity",
                                "SendEmail:" + t.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                Common.showToast(CurriculumListActivity.this, getString(R.string.msg_connection));
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
            progressBar.setVisibility(View.GONE);
        }
    }
}
