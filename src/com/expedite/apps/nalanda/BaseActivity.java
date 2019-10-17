package com.expedite.apps.nalanda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.expedite.apps.nalanda.activity.AccountListActivity;
import com.expedite.apps.nalanda.activity.AccountListRemoveActivity;
import com.expedite.apps.nalanda.activity.AddAccountActivity;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.database.DatabaseHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    public DatabaseHandler db = null;
    public ActionBarDrawerToggle mToggle;
    public static Typeface sFontRegular, sFontold;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sFontRegular = null;
        sFontold = null;
        sFontRegular = Typeface.createFromAsset(getAssets(), "montserrat_regular.ttf");
        sFontold = Typeface.createFromAsset(getAssets(), "montserrat_bold.ttf");
    }

    /**
     Check Internet connection coding */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    public void initSocial(Activity activity, LinearLayout mView, String imgUrl, final String imgLink) {
        try {
            if (mView != null && imgUrl != null && !imgUrl.isEmpty()) {
                LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.adview_row, null);
                mView.addView(view);
                final ImageView img = (ImageView) mView.findViewById(R.id.adview);
                String[] parts = imgUrl.split("\\.");
                if (parts != null && parts.length > 0 && parts[parts.length - 1].equalsIgnoreCase("gif")) {
                    Glide.with(BaseActivity.this)
                            .load(imgUrl)
                            .asGif()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img);
                } else {
                    Glide.with(BaseActivity.this)
                            .load(imgUrl)
                            .asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img);
                }

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (imgLink != null && !imgLink.isEmpty()) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(imgLink));
                            startActivity(i);
                        }
                    }
                });
            } else {
                mView.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    /**
     * Closes the open keyboard if exist in focus
     */
    public void hideKeyboard(Activity activity) {
        try {
            InputMethodManager keyboard = (InputMethodManager) activity.getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                IBinder iBinder = activity.getCurrentFocus().getWindowToken();
                if (iBinder != null && keyboard != null)
                    keyboard.hideSoftInputFromWindow(iBinder, 0);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Closes the open keyboard if exist in focus
     */
    public void hideKeyboardInDialog(final View caller) {
        try {
            caller.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) caller.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(caller.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }, 100);
        } catch (Exception exception) {
            Common.printStackTrace(exception);
        }
    }

    public String convertDate(Context context, String dateToBeConverted) {
        String formattedDate = "";
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", locale);
        DateFormat targetFormat = new SimpleDateFormat("dd MMM, yyyy", locale);
        try {
            Date dateConverted = originalFormat.parse(dateToBeConverted);
            formattedDate = targetFormat.format(dateConverted);
        } catch (Exception exception) {
            Constants.writelog(context.getClass().getSimpleName() + " convertDate", exception.toString());
            try {
                formattedDate = targetFormat.format(targetFormat);
            } catch (Exception ex) {
                Common.printStackTrace(ex);
            }
        }
        return formattedDate;
    }

    public String convertDate_v1(Context context, String dateToBeConverted) {
        String formattedDate = "";
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", locale);
        DateFormat targetFormat = new SimpleDateFormat("dd MMM, yyyy", locale);
        try {
            Date dateConverted = originalFormat.parse(dateToBeConverted);
            formattedDate = targetFormat.format(dateConverted);
        } catch (Exception exception) {
            Constants.writelog(context.getClass().getSimpleName() + " convertDate", exception.toString());
            try {
                formattedDate = targetFormat.format(targetFormat);
            } catch (Exception ex) {
                Common.printStackTrace(ex);
            }
        }
        return formattedDate;
    }

    public void accountListClick(Activity mContext) {
        try {
            Intent intent = new Intent(mContext, AccountListActivity.class);
            startActivity(intent);
            mContext.finish();
            onClickAnimation();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    public void addAccountClick(Activity mContext) {
        try {
            Constants.googleAnalyticEvent(mContext, Constants.button_click, "AddAccountActivity");
            Intent intent = new Intent(mContext, AddAccountActivity.class);
            startActivity(intent);
            mContext.finish();
            onClickAnimation();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    public void removeAccountClick(Activity mContext) {
        try {
            Constants.googleAnalyticEvent(mContext, Constants.button_click, "RemoveAccount");
            Intent intent = new Intent(mContext, AccountListRemoveActivity.class);
            startActivity(intent);
            mContext.finish();
            onClickAnimation();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    public void setDefaultAccount(Activity mContext) {
        try {
            Intent intent = new Intent(mContext, AccountListActivity.class);
            startActivity(intent);
            mContext.finish();
            onClickAnimation();
        } catch (Exception ex) {
            Common.printStackTrace(ex);
        }
    }

    public void onClickAnimation() {
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onBackClickAnimation() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
