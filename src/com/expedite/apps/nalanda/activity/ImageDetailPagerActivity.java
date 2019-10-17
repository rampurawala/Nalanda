package com.expedite.apps.nalanda.activity;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.adapter.ImageViewPagerAdapter;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.constants.Constants;

//created by vishwa on 11/6/2019
public class ImageDetailPagerActivity extends BaseActivity {
    private ImageViewPagerAdapter mImageAdapter;
    private ViewPager mViewPager;
    private int mSelectedPosition = 0;
    private String[] url, localpath, album;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewpager_activity);
        try {
            if (getIntent() != null) {
                mSelectedPosition = getIntent().getExtras().getInt("mSelectedPosition", 0);
                Bundle b = this.getIntent().getExtras();
                url = b.getStringArray("ImgURL");
                localpath = b.getStringArray("localImgURL");
                album = b.getStringArray("album");
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        init();
    }

    private void init() {
        try {
            Constants.setActionbar(getSupportActionBar(), ImageDetailPagerActivity.this, getApplicationContext(),
                    "Photo Gallery", "Photo Gallery");
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        try {
            mImageAdapter = new ImageViewPagerAdapter(ImageDetailPagerActivity.this, localpath, url, album);
            mViewPager.setAdapter(mImageAdapter);
            mViewPager.setCurrentItem(mSelectedPosition);
        } catch (NullPointerException ex) {
            Common.printStackTrace(ex);
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
        if (url.length > 1) {
            ((View) findViewById(R.id.btnLeft)).setVisibility(View.VISIBLE);
            ((View) findViewById(R.id.btnRight)).setVisibility(View.VISIBLE);
        } else {
            ((View) findViewById(R.id.btnLeft)).setVisibility(View.GONE);
            ((View) findViewById(R.id.btnRight)).setVisibility(View.GONE);
        }
        ((View) findViewById(R.id.btnLeft)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() > 0) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
                } else {
                    Common.showToast(ImageDetailPagerActivity.this, getString(R.string.msg_first_image));
                }

            }
        });
        ((View) findViewById(R.id.btnRight)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager.getCurrentItem() + 1 == url.length) {
                    Common.showToast(ImageDetailPagerActivity.this, getString(R.string.msg_last_image));
                } else {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBackClickAnimation();
        finish();
    }
}
