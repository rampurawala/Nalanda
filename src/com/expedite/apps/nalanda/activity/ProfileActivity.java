package com.expedite.apps.nalanda.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.adapter.ProfileListAdapter;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.common.Datastorage;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.database.DatabaseHandler;
import com.expedite.apps.nalanda.lazylist.ImageLoader1;
import com.expedite.apps.nalanda.model.LoginDetail;
import com.squareup.picasso.Picasso;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


public class ProfileActivity extends BaseActivity {
    private TextView tv;
    private ImageView picId;
    private ListView lstview;
    private ProfileListAdapter adapter;
    private Button btnUpdate;
    private String[] Lable = {};
    private Boolean isupdate = false, isProfileExist = false;
    private String[] StudData = {}, parts = {};
    private HashMap<Integer, String> map = new HashMap<Integer, String>();
    private int Cnt_Count = 0, YearId = 0;
    private String StudentId = "", SchoolId = "", studimagepathdb = "", Stud_Image_Path = "";
    private String mIsFromHome = "";
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");
        init();
    }

    public void init() {
        Constants.setActionbar(getSupportActionBar(), ProfileActivity.this, ProfileActivity.this,
                "Student Profile", "StudentProfile");
        db = new DatabaseHandler(ProfileActivity.this);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        try {
            Cnt_Count = Datastorage.GetMultipleAccount(ProfileActivity.this);
            if (Cnt_Count == 1) {
                setTitle("Profile" + "-" + Datastorage.GetStudentName(ProfileActivity.this));
            }
            SchoolId = Datastorage.GetSchoolId(ProfileActivity.this);
            StudentId = Datastorage.GetStudentId(ProfileActivity.this);
            if (SchoolId == null || SchoolId == "" || StudentId == null || StudentId == "") {
                try {
                    String defaultAcDetails = db.getDefaultAccount();
                    String[] parts = defaultAcDetails.split(",");
                    if (parts != null) {
                        StudentId = parts[3];
                        SchoolId = parts[2];
                        Datastorage.SetStudentID(ProfileActivity.this, StudentId);
                        Datastorage.SetSchoolId(ProfileActivity.this, SchoolId);
                    }
                } catch (Exception ex) {
                    Constants.writelog("ProfileActivity", "Ex:" + ex.getStackTrace());
                }
            }
            String defacnt = db.GetDefaultAcademicYearAccount(
                    Integer.parseInt(SchoolId), Integer.parseInt(StudentId));
            if (defacnt != null && defacnt.length() > 0) {
                String[] splterstr = defacnt.split(",");
                YearId = Integer.parseInt(splterstr[1]);
            } else {
                YearId = Integer.parseInt(Datastorage
                        .GetCurrentYearId(ProfileActivity.this));
            }
            tv = (TextView) findViewById(R.id.tvmarkqueetextschoolmatesprofile);
            tv.setSelected(true);
            if(LoginDetail.getLastUpdatedTime()==null || LoginDetail.getLastUpdatedTime()==""
                    || LoginDetail.getLastUpdatedTime().isEmpty())
            {
                tv.setHeight(0);
            }
            else
            {
                tv.setText(LoginDetail.getLastUpdatedTime());
            }
           // tv.setText(LoginDetail.getLastUpdatedTime());
            btnUpdate = (Button) findViewById(R.id.btnUpdateProfile);
            btnUpdate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    isupdate = true;
                    new MyTask().execute();
                }
            });

            lstview = (ListView) (findViewById(R.id.lstmessages));
            picId = (ImageView) findViewById(R.id.imgbullet);
            new MyTask().execute();

            // Refresh
            int lastautoupdatedate = Datastorage
                    .GetLastAutoUpdateProfile(ProfileActivity.this);
            if (lastautoupdatedate != 1) {
                MyTaskRefresh mytaskRef = new MyTaskRefresh();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    mytaskRef.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    // mytaskRef.execute();
                } else {
                    mytaskRef.execute();
                }
            }
            new MyTaskLogUservisited().execute();
        } catch (Exception err) {
            Constants.writelog("Viewprofile:", "OnCreate()157 Exception:"
                    + err.getMessage() + "StackTrace::"
                    + err.getStackTrace().toString());
            Toast.makeText(ProfileActivity.this, "Plz try again.",
                    Toast.LENGTH_LONG).show();
            Intent in = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(in);
            finish();
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String studDetails = db.GetStudentProfileDetails(
                    Integer.parseInt(StudentId), Integer.parseInt(SchoolId), YearId);
            if (isupdate) {
                showprofilekumkum(true);
            } else {
                if (studDetails != null && studDetails != "" && studDetails != "0") {
                    String[] parts = studDetails.split(",");
                    ArrayList<String> list = new ArrayList<String>();
                    for (String s : parts) {
                        if (!s.equals("")) {
                            list.add(s);
                        }
                    }
                    parts = list.toArray(new String[list.size()]);
                    Lable = new String[parts.length - 1];
                    StudData = new String[parts.length - 1];
                    for (int i = 0; i < parts.length - 1; i++) {
                        String[] data = parts[i].split(":");
                        Lable[i] = data[0].toString();
                        StudData[i] = data[1].toString();
                    }
                    studimagepathdb = parts[parts.length - 1];
                    isProfileExist = true;
                } else {
                    showprofilekumkum(true);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(ProfileActivity.this);
                } else {
                    adapter = new ProfileListAdapter(
                            ProfileActivity.this, Lable, StudData);
                    lstview.setAdapter(adapter);
                    lstview.clearFocus();
                    Bitmap studimg = null;
                    if (isProfileExist) {
                        if (studimagepathdb != "") {
                            File myDir = Constants.CreatePhotoGalleryFolder();
                            String fname = studimagepathdb;
                            if (fname != null && fname != "") {
                                File file = new File(myDir, fname);
                                studimg = Constants.decodeFile(file);
                                picId.setImageBitmap(studimg);
                            } else {
                                picId.setImageResource(R.drawable.nopics);
                            }
                        }
                    } else {
                        Picasso.with(ProfileActivity.this).load(Stud_Image_Path).into(picId);
                    }
                }
                mProgressBar.setVisibility(View.GONE);
            } catch (Exception err) {
                Constants.writelog("viewprofile",
                        "Error :314 " + err.getMessage());
                Intent in = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(in);
                finish();
            }
        }
    }

    private class MyTaskRefresh extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            showprofilekumkum(false);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                ProfileListAdapter adapter = new ProfileListAdapter(
                        ProfileActivity.this, Lable, StudData);
                lstview.setAdapter(adapter);
                lstview.clearFocus();
                adapter.notifyDataSetChanged();
                int loader = R.drawable.nopics;
                Bitmap studimg = null;
                if (isProfileExist) {
                    if (studimagepathdb != "") {
                        File myDir = Constants.CreatePhotoGalleryFolder();
                        String fname = studimagepathdb;
                        if (fname != null && fname != "") {
                            File file = new File(myDir, fname);
                            studimg = Constants.decodeFile(file);
                            picId.setImageBitmap(studimg);
                        } else {
                            picId.setImageResource(R.drawable.nopics);
                        }
                    }
                } else {
                    String image_url = Stud_Image_Path;
                    ImageLoader1 imgLoader = new ImageLoader1(
                            ProfileActivity.this);
                    imgLoader.DisplayImage(image_url, loader, picId);
                }
            } catch (Exception err) {
                Constants.writelog("viewprofile",
                        "MyTaskRef :384 " + err.getMessage());
                Intent in = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(in);
                finish();
            }
        }
    }

    private class MyTaskLogUservisited extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                LogUserVisted();
            } catch (Exception ex) {
            }
            return null;
        }
    }

    private void showprofilekumkum(Boolean isCompulsory) {
        try {
            SoapObject request = new SoapObject(Constants.NAMESPACE,
                    Constants.METHOD_NAME_STUDENT_PROFILE);
            int studId = Integer.parseInt(Datastorage
                    .GetStudentId(ProfileActivity.this));
            int schoolId = Integer.parseInt(Datastorage
                    .GetSchoolId(ProfileActivity.this));
            request.addProperty("stud_id", studId);
            request.addProperty("school_id", schoolId);
            request.addProperty("Is_All", 1);
            request.addProperty("yearid", YearId);
            SoapObject result = Constants.CallWebMethod(
                    ProfileActivity.this, request,
                    Constants.METHOD_NAME_STUDENT_PROFILE, isCompulsory);
            if (result != null && result.getPropertyCount() > 0) {
                String info = result.getPropertyAsString(0);
                parts = info.split(",");
                Constants.Logwrite("Viewprofile:", "parts:" + parts);
                Lable = new String[parts.length];
                Constants.Logwrite("Viewprofile:", "parts:" + Lable);
                StudData = new String[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    String[] data = parts[i].split(":");
                    Lable[i] = data[0].toString();
                    StudData[i] = data[1].toString();
                }
                String grno = parts[0];
                String name = parts[1];
                String Classname = parts[2];
                String Caste = parts[3];
                String Category = "";
                if (parts.length > 4) {
                    Category = parts[4];
                }
                String DOB = "";
                if (parts.length > 5) {
                    DOB = parts[5];
                }
                String ContactNumber = "";
                if (parts.length > 6) {
                    ContactNumber = parts[6];
                }
                Stud_Image_Path = getStudentImagePath();
                String[] patharr = Stud_Image_Path.split("/");
                String imagepath = patharr[patharr.length - 1];
                patharr[patharr.length - 1] = URLEncoder.encode(patharr[patharr.length - 1], "UTF-8");
                String urlnew = "";
                for (int i = 0; i < patharr.length; i++) {
                    if (i == 0) {
                        urlnew += patharr[i];
                    } else {
                        urlnew += "/" + patharr[i];
                    }
                }
                Bitmap studimg = Constants.getImage(urlnew);
                if (studimg != null) {
                    Constants.SaveImage(studimg, imagepath);
                }
                int isupdate = db.updateStudProfile(studId, schoolId, YearId,
                        grno, name, Classname, Caste, Category, DOB,
                        ContactNumber, imagepath);
                if (isupdate != 1) {
                    db.AddStudentProfileDetails(studId, schoolId, YearId, grno,
                            name, Classname, Caste, Category, DOB,
                            ContactNumber, imagepath);
                }
                Datastorage.SetLastAutoUpdateProfile(ProfileActivity.this,
                        1);
                Constants.Logwrite("Viewprofile:", "StudentImagePath:" + Stud_Image_Path);
            } else {
                Constants.writelog("ProfileActivity",
                        "Error: showprofilekumkum() No responce from server.");
            }
        } catch (Exception err) {
            Constants.writelog("Viewprofile:",
                    "showprofilekumkum()440 Exception:" + err.getMessage()
                            + "StackTrace::" + err.getStackTrace().toString());
            Intent in = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(in);
            finish();
        }
    }

    private void LogUserVisted() {
        Constants.Logwrite("ProfileActivity", "LogUserVisited Start.");
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.LOG_USER_VISITED);
        request.addProperty("moduleid", 1);
        request.addProperty("schoolid",
                Datastorage.GetSchoolId(ProfileActivity.this));
        request.addProperty("User_Id",
                Datastorage.GetUserId(ProfileActivity.this));
        request.addProperty("phoneno",
                Datastorage.GetPhoneNumber(ProfileActivity.this));
        request.addProperty("pageid", 1);

        try {
            Constants.CallWebMethod(ProfileActivity.this, request,
                    Constants.LOG_USER_VISITED, false);
        } catch (Exception err) {
        }
    }

    private String getStudentImagePath() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.GET_STUDENT_IMAGE_PATH_V1);
        request.addProperty("school_id",
                Datastorage.GetSchoolId(ProfileActivity.this));
        request.addProperty("Studid",
                Datastorage.GetStudentId(ProfileActivity.this));
        try {
            SoapObject result = Constants.CallWebMethod(
                    ProfileActivity.this, request, Constants.GET_STUDENT_IMAGE_PATH_V1, false);
            if (result != null && result.getPropertyCount() > 0) {
                String info = result.getPropertyAsString(0);
                Stud_Image_Path = info;
                Constants.Logwrite("Viewprofile:", "ImagePath:" + Stud_Image_Path);
            } else {
                Constants.Logwrite("ProfileActivity",
                        "Error: getStudentImagePath() No responce from server.");
                Constants
                        .writelog("ProfileActivity",
                                "Error: getStudentImagePath() No responce from server.");
            }
        } catch (Exception err) {
            Constants.writelog("Viewprofile:",
                    "GetStudentImagePath()502 Exception:" + err.getMessage()
                            + "StackTrace::" + err.getStackTrace().toString());
            return Stud_Image_Path;
        }
        return Stud_Image_Path;
    }

    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        try {
            getMenuInflater().inflate(R.menu.profile_menu, menu);
            MenuItem detailProfileView = menu.findItem(R.id.DetailProfileView);
            detailProfileView.setVisible(false);
            menu.findItem(R.id.parents_profile).setVisible(true);

        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        return true;
    }


//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        try {
//            menu.clear();
//            map = new HashMap<Integer, String>();
//            menu.add(0, 1, 1, "Add Account").setTitle("Add Account");
//            //  menu.add(0, 2, 2, "Remove Account").setTitle("Remove Account");
//            menu.add(0, 3, 3, "Set Default Account").setTitle("Set Default Account");
//            menu.add(0, 4, 4, "School Mates").setTitle("School Mates");
//            map = Constants.AddAccount(menu, db);
//        } catch (Exception err) {
//            Constants.writelog("Viewprofile:",
//                    "OnPrepareOptionmenu()563 Exception:" + err.getMessage()
//                            + "StackTrace::" + err.getStackTrace().toString());
//        }
//        return true;
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int iid = item.getItemId();
        String name = item.toString();
        if (iid == android.R.id.home) {
            hideKeyboard(ProfileActivity.this);
            ProfileActivity.this.finish();
            onBackClickAnimation();
        } else if (iid == R.id.parents_profile) {
            if (isOnline()) {
                if (StudentId != null && !StudentId.isEmpty() && SchoolId != null &&
                        !SchoolId.isEmpty()) {
                    intent = new Intent(ProfileActivity.this, ParentsProfileActivity.class);
                    intent.putExtra("StudentId", StudentId);
                    intent.putExtra("SchoolId", SchoolId);
                    startActivity(intent);
                    onClickAnimation();
                }
            } else {
                Common.showToast(ProfileActivity.this, getString(R.string.msg_connection));
            }
        } else if (iid == R.id.AddAccount) {
            addAccountClick(ProfileActivity.this);
        } else if (iid == R.id.DefaultAccount) {
            accountListClick(ProfileActivity.this);
        } else if (iid == R.id.SchoolMates) {
            intent = new Intent(ProfileActivity.this, StudentListActivity.class);
            intent.putExtra("IsFromProfile", "IsFromProfile");
            startActivity(intent);
            finish();
            onClickAnimation();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        try {
            if (mIsFromHome != null && !mIsFromHome.isEmpty()) {
                super.onBackPressed();
                onBackClickAnimation();
            } else {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                onBackClickAnimation();
            }
        } catch (Exception err) {
            Constants.writelog("Viewprofile:",
                    "OnBackPressed()625 Exception:" + err.getMessage()
                            + "StackTrace::" + err.getStackTrace().toString());
        }
    }
}
