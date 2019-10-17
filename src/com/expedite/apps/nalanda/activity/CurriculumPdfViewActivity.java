package com.expedite.apps.nalanda.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.constants.Constants;


/**
 * Created by Jaydeep patel on 11/21/2017.
 */
public class CurriculumPdfViewActivity extends BaseActivity {
    private WebView mWebView;
    private ProgressBar mProgressbar;
    private String mUrl = "", mName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        if (getIntent() != null && getIntent().getExtras() != null) {
            mUrl = getIntent().getExtras().getString("Url", "");
            mName = getIntent().getExtras().getString("Name", "");
            Common.showLog("PDFUrl", mUrl);
        }
        init();
    }

    public void init() {
        try {
            Activity abc = this;
            Constants.setActionbar(getSupportActionBar(), abc, getApplicationContext(),
                    mName, mName);
            mProgressbar = (ProgressBar) findViewById(R.id.progressBar);
            mWebView = (WebView) findViewById(R.id.webview_web);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, final String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mProgressbar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (view.getProgress() == 100) {
                        mProgressbar.setVisibility(View.GONE);
                    }
                }

               /* @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
*/
                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    //Constants.writelog("CurriculumPdfView", "webviewclient():75 " + error);
                    Log.d("Webviewclient",error.toString());
                    view.loadUrl("http://docs.google.com/gview?embedded=true&url=" + mUrl);
                }
            });
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + mUrl);
            //mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            mWebView.getSettings().setLoadsImagesAutomatically(true);
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);

        } catch (Exception ex) {
            Common.printStackTrace(ex);
            Constants.writelog("CurriculumPdfView", "init():75 " + ex.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CurriculumPdfViewActivity.this.finish();
                onBackClickAnimation();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CurriculumPdfViewActivity.this.finish();
        onBackClickAnimation();
    }

}
