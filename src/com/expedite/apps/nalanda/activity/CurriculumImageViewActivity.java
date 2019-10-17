package com.expedite.apps.nalanda.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.common.TouchImageViewHelp;
import com.expedite.apps.nalanda.constants.Constants;


/**
 * Created by Jaydeep patel on 11/21/2017.
 */
public class CurriculumImageViewActivity extends BaseActivity {
    private TouchImageViewHelp mImageView;
    private String mUrl = "", mName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_layout);
        if (getIntent() != null && getIntent().getExtras() != null) {
            mUrl = getIntent().getExtras().getString("Url", "");
            mName = getIntent().getExtras().getString("Name", "");
            Common.showLog("ImageUrl:", mUrl);
        }
        init();
    }

    public void init() {

        try {
            Activity abc = this;
            Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                    mName, mName);
            mImageView = (TouchImageViewHelp) findViewById(R.id.img_pager_item);
            if (mUrl != null && !mUrl.isEmpty()) {
                Glide.with(CurriculumImageViewActivity.this)
                        .load(mUrl).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(mImageView);
            }


        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CurriculumImageViewActivity.this.finish();
        onBackClickAnimation();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CurriculumImageViewActivity.this.finish();
                onBackClickAnimation();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
