package com.expedite.apps.nalanda.activity;


import android.content.Intent;
import android.os.Bundle;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.constants.Constants;

public class SuccessfullyActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successfull);

        Constants.setActionbar(getSupportActionBar(), SuccessfullyActivity.this,
                SuccessfullyActivity.this, "Successfully Send", "SuccessfullyActivity");
    }

    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(SuccessfullyActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            onBackClickAnimation();
        } catch (Exception err) {
            Common.printStackTrace(err);
        }
    }
}
