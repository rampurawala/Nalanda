package com.expedite.apps.nalanda.activity;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.expedite.apps.nalanda.adapter.AlbumPhotoListAdapter;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.common.Datastorage;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.database.DatabaseHandler;
import com.expedite.apps.nalanda.model.CircularModel;
import com.expedite.apps.nalanda.model.Contact;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoGalleryActivity extends BaseActivity {
    private Menu menu;
    private String[] TotalItemList = null, albumlist = {""};
    private Integer[] albumid = {};
    private String[] albumtime = {""}, FilePathStrings = {""};
    private File file;
    private int Is_Ref = 0;
    private String SchoolId, StudentId, Year_Id;
    private ProgressBar mProgressbar;
    private String mIsFromHome = "";
    private GridLayoutManager mGridLayoutManager;
    private RecyclerView mPhotographsRecycle;
    private AlbumPhotoListAdapter mPhotoAdapter;
    private ArrayList<CircularModel.Strlist> mAlbumlist = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        if (getIntent() != null && getIntent().getExtras() != null)
            mIsFromHome = getIntent().getExtras().getString("IsFromHome", "");
        init();
    }
    public void init() {
        try {
            Constants.setActionbar(getSupportActionBar(), PhotoGalleryActivity.this, PhotoGalleryActivity.this,
                    "PhotoGallery", "PhotoGalleryActivity");
            SchoolId = Datastorage.GetSchoolId(PhotoGalleryActivity.this);
            StudentId = Datastorage.GetStudentId(PhotoGalleryActivity.this);
            Year_Id = Datastorage.GetCurrentYearId(PhotoGalleryActivity.this);
            db = new DatabaseHandler(PhotoGalleryActivity.this);
            file = Constants.CreatePhotoGalleryFolder();
            mProgressbar = (ProgressBar) findViewById(R.id.ProgressBar);
            mGridLayoutManager = new GridLayoutManager(PhotoGalleryActivity.this, 2);
            mPhotographsRecycle = (RecyclerView) findViewById(R.id.PhotographsRecycle);
            mPhotographsRecycle.setLayoutManager(mGridLayoutManager);
            mPhotoAdapter = new AlbumPhotoListAdapter(PhotoGalleryActivity.this, FilePathStrings, albumlist,
                    albumtime, albumid);
            mPhotographsRecycle.setAdapter(mPhotoAdapter);
            new GetAlbumList().execute();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
     }

    private void DeleteAllPhotoAlbum() {
        try {
            int DeleteAlbumStatus = db.DeleteStudentPhotoGallery(Integer.parseInt(StudentId), Integer.parseInt(SchoolId));
            if (DeleteAlbumStatus > 0) {
                File[] listFile = file.listFiles();
                for (int i = 0; i < listFile.length; i++) {
                    if (listFile[0].exists()) {
                        listFile[0].delete();
                    }
                }
            }
        } catch (Exception ex) {
            Constants.writelog("PhotoGalleryActivity", "122 Ex:" + ex.getMessage() + "::::::" + ex.getStackTrace());
        }
    }

    private class GetAlbumList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                if (Is_Ref == 1) {
                    AlbumDetails();
                }
                List<Contact> contacts = db.getAlbumDetailsGrid(Integer.parseInt(StudentId), Integer.parseInt(SchoolId), 0);
                if (contacts.size() > 0) {
                    TotalItemList = new String[contacts.size()];
                    albumlist = new String[contacts.size()];
                    albumtime = new String[contacts.size()];
                    albumid = new Integer[contacts.size()];
                    FilePathStrings = new String[contacts.size()];
                    int i = 0;
                    String PhotoGallertFolderPath = Constants.GetPhotoGalleryFolderPath();
                    for (Contact cn : contacts) {
                        String PhotoFileName = cn.getAlbumPhotofile();
                        String URL = cn.getAlbumurl();
                        File fileexist = new File(file + "/" + PhotoFileName + "");
                        String File_Path_Str = PhotoGallertFolderPath + "/"
                                + PhotoFileName + "@@@###" + PhotoFileName;
                        FilePathStrings[i] = File_Path_Str;
                        albumlist[i] = cn.getAlbumName();
                        albumtime[i] = cn.getAlbumDatetime();
                        albumid[i] = cn.getAlbumId();
                        if (!fileexist.exists()) {
                            Bitmap bmp = Constants.getBitmap(URL.toString(), fileexist);
                            Constants.SaveImage(bmp, PhotoFileName);
                        }
                        i++;
                    }
                } else {
                    AlbumDetails();
                    List<Contact> contacts1 = db.getAlbumDetailsGrid(Integer.parseInt(StudentId), Integer.parseInt(SchoolId), 0);
                    if (contacts1.size() > 0) {
                        TotalItemList = new String[contacts1.size()];
                        albumlist = new String[contacts1.size()];
                        albumtime = new String[contacts1.size()];
                        albumid = new Integer[contacts1.size()];
                        FilePathStrings = new String[contacts1.size()];
                        int i = 0;
                        String PhotoGallertFolderPath = Constants.GetPhotoGalleryFolderPath();
                        for (Contact cn : contacts1) {
                            String PhotoFileName = cn.getAlbumPhotofile();
                            String URL = cn.getAlbumurl();
                            String File_Path_Str = PhotoGallertFolderPath
                                    + "/" + PhotoFileName + "@@@###"
                                    + PhotoFileName;
                            File fileexist = new File(file + "/" + PhotoFileName + "");
                            FilePathStrings[i] = File_Path_Str;
                            albumlist[i] = cn.getAlbumName();
                            albumtime[i] = cn.getAlbumDatetime();
                            albumid[i] = cn.getAlbumId();
                            if (!fileexist.exists()) {
                                Bitmap bmp = Constants.getBitmap(URL.toString(), fileexist);
                                Constants.SaveImage(bmp, PhotoFileName);
                            }
                            i++;
                        }
                    }
                }
            } catch (Exception ex) {
                Constants.writelog("PhotoGalleryActivity", "203 Ex:" + ex.getMessage() + "::::::" + ex.getStackTrace());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (Constants.isShowInternetMsg) {
                    Constants.NotifyNoInternet(getApplicationContext());
                } else {
                    if (TotalItemList != null && TotalItemList.length > 0) {
                        mPhotoAdapter = new AlbumPhotoListAdapter(PhotoGalleryActivity.this, FilePathStrings,
                                albumlist, albumtime, albumid);
                        mPhotographsRecycle.setAdapter(mPhotoAdapter);
                        mPhotoAdapter.notifyDataSetChanged();
                        mPhotographsRecycle.setVisibility(View.VISIBLE);
                    } else {
                        mPhotographsRecycle.setVisibility(View.GONE);
                        Toast.makeText(PhotoGalleryActivity.this, "No Photos Available", Toast.LENGTH_LONG).show();
                    }
                }
                mProgressbar.setVisibility(View.GONE);
            } catch (Exception ex) {
                Constants.writelog("PhotoGalleryActivity", "233 Ex:" + ex.getMessage() + "::::::" + ex.getStackTrace());
                mProgressbar.setVisibility(View.GONE);
            }
        }
    }

    public String[] AlbumDetails() {
        SoapObject request = new SoapObject(Constants.NAMESPACE, Constants.ALBUM_DETAILS);
        request.addProperty("SchoolId", Integer.parseInt(SchoolId));
        request.addProperty("ClassSecId", Integer.parseInt(Datastorage.GetClassSecId(getApplicationContext())));
        request.addProperty("PhotType", 1);
        request.addProperty("StudId", Integer.parseInt(StudentId));
        request.addProperty("yearid", Integer.parseInt(Year_Id));
        try {
            SoapObject result = Constants.CallWebMethod(PhotoGalleryActivity.this, request, Constants.ALBUM_DETAILS, true);
            Constants.Logwrite("MessageList", "Result length is " + result.toString().length());
            if (result != null && result.getPropertyCount() > 0) {
                SoapObject obj2 = (SoapObject) result.getProperty(0);
                Constants.Logwrite("MsgLogCount:", "----------------");
                if (obj2 != null) {
                    int count = obj2.getPropertyCount();
                    String[] myarray = new String[count];
                    for (int i = 0; i < count; i++) {
                        myarray[i] = obj2.getProperty(i).toString();
                        String[] msgitem = myarray[i].split("##@@");
                        String filename = msgitem[2];
                        File fileexist = new File(file + "/" + filename + "");
                        Constants.Logwrite("PhotoGalleryActivity", "Photo path 456 photoDetails:" + myarray[i]);
                        int albumid = Integer.parseInt(msgitem[0]);
                        boolean isinserted = db.CheckAlbumDetailsInserted(
                                Integer.parseInt(StudentId),
                                Integer.parseInt(SchoolId),
                                Integer.parseInt(msgitem[4]),
                                albumid, filename);
                        if (!isinserted) {
                            db.AddAlbumDetails(new Contact(Integer
                                    .parseInt(StudentId), albumid, msgitem[1], msgitem[2], msgitem[5],
                                    Integer.parseInt(SchoolId), Integer.parseInt(msgitem[4]),
                                    Long.parseLong(msgitem[8]), msgitem[6]));
                        }
                    }
                } else {
                    albumlist = null;
                }
            }
        } catch (Exception e) {
            Constants.writelog("PhotoGalleryActivity", "AlbumDetails()505 Error::" + e.getMessage());
            return albumlist;
        }
        return albumlist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.activity_photo_options, menu);
            menu.findItem(R.id.photo_view_type).setVisible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                hideKeyboard(PhotoGalleryActivity.this);
                PhotoGalleryActivity.this.finish();
                onBackClickAnimation();
                break;
            case R.id.photo_view_type:
//                if (Datastorage.GetPhotoGalleryType(PhotoGalleryActivity.this) == 0) {
//                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_view_list_white_36dp));
//                    Datastorage.SetPhotoGalleryType(PhotoGalleryActivity.this, 1);
//                    startActivity(getIntent());
//                    finish();
//                } else {
//                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_photo_album_white_36dp));
//                    Datastorage.SetPhotoGalleryType(PhotoGalleryActivity.this, 0);
//                    startActivity(getIntent());
//                    finish();
//                }
                break;

            case R.id.action_refresh:
                Constants.googleAnalyticEvent(PhotoGalleryActivity.this, Constants.button_click, "Refresh");
                Is_Ref = 1;
                new GetAlbumList().execute();
                break;

            case R.id.action_add_account:
                addAccountClick(PhotoGalleryActivity.this);
                break;

            case R.id.action_remove_account:
                removeAccountClick(PhotoGalleryActivity.this);
                break;

            case R.id.action_set_default_account:
                setDefaultAccount(PhotoGalleryActivity.this);
                break;

            case R.id.action_clear_photo:
                Constants.googleAnalyticEvent(PhotoGalleryActivity.this, Constants.button_click, "SetDefaultYear");
                DeleteAllPhotoAlbum();
                break;

            case R.id.action_about:
                Constants.googleAnalyticEvent(PhotoGalleryActivity.this, Constants.button_click, "AboutActivity");
                intent = new Intent(PhotoGalleryActivity.this, AboutActivity.class);
                startActivity(intent);
                finish();
                onClickAnimation();
                break;
            default:
                break;
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
                Intent intent = new Intent(PhotoGalleryActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        } catch (Exception err) {
            Constants.writelog("PhotoGalleryActivity", "onBackPressed()699 Error::" + err.getMessage());
        }
    }
}

