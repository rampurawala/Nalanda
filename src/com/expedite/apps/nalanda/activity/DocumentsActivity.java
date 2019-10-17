package com.expedite.apps.nalanda.activity;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.BuildConfig;
import com.expedite.apps.nalanda.MyApplication;
import com.expedite.apps.nalanda.R;

import com.expedite.apps.nalanda.adapter.DocumentAdapter;
import com.expedite.apps.nalanda.common.Datastorage;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.model.AppService;
import com.expedite.apps.nalanda.model.Document;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentsActivity extends BaseActivity {
    RecyclerView mDocRecyclerview;
    ArrayList<Document.DocumentItemList> StudentsArray = new ArrayList<>();
    DocumentAdapter mdocumentAdapter;
    String Studid = "", Schoolid = "", YearId = "", versionCode = "", platform = "";
    String S = "", A = "";
    String json;
    LinearLayout layout;
    ProgressBar prgsDocument;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle("Documents");
        }

        layout = findViewById(R.id.empty_folder_lyt_file_detail);
        prgsDocument = findViewById(R.id.prgsDocument);
        mDocRecyclerview = (RecyclerView) findViewById(R.id.rc_Doc);
        GridLayoutManager mGridlayout = new GridLayoutManager(DocumentsActivity.this, 1);
        mDocRecyclerview.setLayoutManager(mGridlayout);
        mdocumentAdapter = new DocumentAdapter(DocumentsActivity.this, StudentsArray);
        mDocRecyclerview.setAdapter(mdocumentAdapter);

        /*json ="{\"DocumentItemList\":[{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/scanned/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"TC\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/scanned/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"Fees_Pending_Letter\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/scanned/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"LC\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/scanned/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"I_Card_Secondary_Higher\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"Sci_Board_Registration_Form\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"Escortcard_Morning\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"LC_VERIFICATION\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/scanned/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"LC\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/scanned/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\",\"DocumentId\":\"15\",\"DocumentName\":\"H.S.C._CHARACTER_CERTIFICATE\",\"DocumentType\":\"1\",\"ShareUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display_Pdfs/2017-2018_15_ALL.pdf\"},{\"DisplayUrl\":\"https://s3.ap-south-1.amazonaws.com/espschools/Sure_Upload_Data/635342300033861294_espl_demo_2013_2014/DMS/document/2017-2018_15/Display\"}";*/
        // final Document.DocumentItemList tmp = mFilteredList.get(position);
        //List<Document.DocumentItemList> data = new Gson().fromJson(json, Document.class);
        //List<Document.DocumentItemList> data = new Document.DocumentItemList(json, Document.class);
      /*  List<Document.DocumentItemList> data = (List<Document.DocumentItemList>) new Gson().fromJson(json, Document.class);

        //data.get(1).getDocumentName();
        mdocumentAdapter = new DocumentAdapter(DocumentsActivity.this, StudentsArray);
        StudentsArray.addAll(data.);
        mdocumentAdapter.notifyDataSetChanged();

        Document data=*/


       /* JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(Data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonObject.toString();
        */


       /* if(true)
        {
            List<Document.DocumentItemList> jsonData =null;
            ArrayList<Document.DocumentItemList> StudentsArray = new ArrayList<>();
            StudentsArray.addAll(jsonData);
            mdocumentAdapter.notifyDataSetChanged();
        }else {
           // getDocuments();
        }*/

        getDocuments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mdocumentAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                DocumentsActivity.this.finish();
                onBackClickAnimation();
                return true;
            default:
                break;
        }
        return false;
    }

    public void onBackClickAnimation() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    
    private void getDocuments() {
        if (isOnline()) {
            prgsDocument.setVisibility(View.VISIBLE);
            String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            Call<Document> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .GetStudentDocuments(mStudentId, mYearId, mSchoolId, Constants.PLATFORM, Constants.CODEVERSION);
            call.enqueue(new Callback<Document>() {
                @Override
                public void onResponse(Call<Document> call, Response<Document> response) {
                    try {
                        prgsDocument.setVisibility(View.GONE);
                        Document tmps = response.body();
                        if (tmps != null && tmps.getResultFlag() != null && !tmps.getResultFlag().isEmpty()
                                && tmps.getResultFlag().equalsIgnoreCase("1")) {
                            StudentsArray.addAll(tmps.getDocumentItemLists());
                            mdocumentAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(DocumentsActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        prgsDocument.setVisibility(View.GONE);
                        Constants.writelog("DocumentsActivity", "getDocuments 107:" + ex.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<Document> call, Throwable t) {
                    prgsDocument.setVisibility(View.GONE);
                    Constants.writelog("DocumentsActivity", "getDocuments 113:" + t.getMessage());
                }
            });
        }
    }
}
