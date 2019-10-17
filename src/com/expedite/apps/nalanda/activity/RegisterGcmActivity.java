package com.expedite.apps.nalanda.activity;

import android.content.Intent;
import android.os.Bundle;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.SplashActivity;
import com.expedite.apps.nalanda.common.Datastorage;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.model.LoginDetail;
import com.google.android.gcm.GCMRegistrar;

public class RegisterGcmActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        Constants.setActionbar(getSupportActionBar(), RegisterGcmActivity.this,
                RegisterGcmActivity.this, "RegisterGcmActivity", "RegisterGcmActivity");
        try {
            /*GCMRegistrar.register(RegisterGcmActivity.this,
                    Constants.SENDER_ID);*/
            String regId = GCMRegistrar.getRegistrationId(this);
            Constants.Logwrite("RegisterGcmActivity", "Registration ID:" + regId);
            if (!regId.equals("")) {
                Datastorage.SetGetGcmRegId(RegisterGcmActivity.this, regId);
                LoginDetail.setGCM_REG_ID(regId);
            }
        } catch (Exception ex) {
            Constants.Logwrite("RegisterGcmActivity", "Exception 32:" + ex.getMessage() + "::::"
                    + ex.getStackTrace());
            Constants.writelog(
                    "RegisterGcmActivity",
                    "Exception 32:" + ex.getMessage() + "::::"
                            + ex.getStackTrace());
        }
        Intent in = new Intent(RegisterGcmActivity.this, SplashActivity.class);
        startActivity(in);
        finish();
        onClickAnimation();
    }
}
