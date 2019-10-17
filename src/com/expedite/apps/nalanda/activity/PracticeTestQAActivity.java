package com.expedite.apps.nalanda.activity;


import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;


import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.MyApplication;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.common.Datastorage;
import com.expedite.apps.nalanda.constants.Constants;
import com.expedite.apps.nalanda.model.AppService;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PracticeTestQAActivity extends BaseActivity implements View.OnClickListener {
    TextView ques, correctAnsDisplay, ans_img_opt/*, quesNo*/;
    /*RadioGroup rg_opt;*/
    String testId, quesId, correctAns, givenAns, ispeding, testName, isLast = "0", fromCompleted = "0";
    Button btn_next, previous, next;
    ProgressBar progressBar;
    Button opt1, opt2, opt3, opt4;
    FrameLayout frame_progress;
    int selected = 0;
    RelativeLayout reviewLayout;
    RelativeLayout rl_test;
    TextView ques_no_img, option_no_4, option_no_3, option_no_2, option_no_1;
    /*RadioButton opt1,opt2,opt3,opt4;*/
    long tStart;
    LinearLayout text_ques, back_opt1, back_opt2, back_opt3, back_opt4;
    ImageView img_ques, img_opt1, img_opt2, img_opt3, img_opt4, img_CorrectAns;
    CardView card_img_ques/*, card_img_opt1, card_img_opt2, card_img_opt3, card_img_opt4*/;
    /* FrameLayout frame;*/
    int count = 0, countAll = 0;
    List<AppService.ListArray> listArrays = new ArrayList<>();
    List<AppService.ListArray> allArrays = new ArrayList<>();
    long elapsedSeconds = 0, tdelta;
    LinearLayout.LayoutParams lp;
    int after_cal;
    String correctImg = "";
    String question = "";
    ImageView photoView;
    View mView;
    AlertDialog.Builder mBuilder;
    String opt_1 = "", opt_2 = "", opt_3 = "", opt_4 = "";
    AlertDialog mDialog;
    String n="";
    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_test_qa);
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }

            Intent i = getIntent();
            testId = i.getStringExtra("testId");
            ispeding = i.getStringExtra("flag");
            btn_next = findViewById(R.id.btn_next);
            testName = i.getStringExtra("testName");
            if (i.getStringExtra("fromCompleted") == null || i.getStringExtra("fromCompleted").equals("")) {

            } else {
                fromCompleted = i.getStringExtra("fromCompleted");
            }

            setTitle(testName);
            ques = findViewById(R.id.ques);

            //  quesNo = findViewById(R.id.quesNo);
            progressBar = findViewById(R.id.pg_QA);
            reviewLayout = findViewById(R.id.reviewLayout);
            text_ques = findViewById(R.id.text_ques);
            card_img_ques = findViewById(R.id.card_img_ques);
            img_ques = findViewById(R.id.img_ques);
            img_ques.setOnClickListener(this);


            next = findViewById(R.id.next);
            frame_progress = findViewById(R.id.frame_progress);
            back_opt1 = findViewById(R.id.back_opt1);
            back_opt2 = findViewById(R.id.back_opt2);
            back_opt3 = findViewById(R.id.back_opt3);
            back_opt4 = findViewById(R.id.back_opt4);
            img_opt1 = findViewById(R.id.img_opt1);
            img_opt2 = findViewById(R.id.img_opt2);
            img_opt3 = findViewById(R.id.img_opt3);
            img_opt4 = findViewById(R.id.img_opt4);
            option_no_4 = findViewById(R.id.option_no_4);
            option_no_1 = findViewById(R.id.option_no_1);
            option_no_3 = findViewById(R.id.option_no_3);
            option_no_2 = findViewById(R.id.option_no_2);
            ques_no_img = findViewById(R.id.ques_no);
            ans_img_opt = findViewById(R.id.ans_img_opt);
            //   card_img_opt1 = findViewById(R.id.card_img_opt1);
            //   card_img_opt2 = findViewById(R.id.card_img_opt2);
            //   card_img_opt3 = findViewById(R.id.card_img_opt3);
            //  card_img_opt4 = findViewById(R.id.card_img_opt4);

            previous = findViewById(R.id.previous);
            img_CorrectAns = findViewById(R.id.img_CorrectAns);
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT); // or set height to any fixed value you want
            img_opt1.setOnClickListener(this);
            img_opt2.setOnClickListener(this);
            img_opt3.setOnClickListener(this);
            img_opt4.setOnClickListener(this);
            //your_layout.setLayoutParams(lp);
            correctAnsDisplay = findViewById(R.id.correctAns);
            // frame = findViewById(R.id.frame);
            rl_test = findViewById(R.id.rl_test);
            tStart = System.currentTimeMillis();
            opt1 = findViewById(R.id.opt1);
            opt2 = findViewById(R.id.opt2);
            opt3 = findViewById(R.id.opt3);
            opt4 = findViewById(R.id.opt4);
            img_CorrectAns.setOnClickListener(this);
            //  frame.bringToFront();
         /*   long tEnd = System.currentTimeMillis();
            long tDelta = tEnd - tStart;
            double elapsedSeconds = tDelta / 1000.0;*/
            if (ispeding.equalsIgnoreCase("0")) {
                opt1.setEnabled(false);
                opt2.setEnabled(false);
                opt3.setEnabled(false);
                opt4.setEnabled(false);
                back_opt1.setEnabled(false);
                back_opt2.setEnabled(false);
                back_opt3.setEnabled(false);
                back_opt4.setEnabled(false);
                reviewLayout.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.GONE);
            } else {
                reviewLayout.setVisibility(View.GONE);
                btn_next.setVisibility(View.VISIBLE);
            }
            opt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (opt1.getVisibility() == View.VISIBLE) {
                        opt1.setBackgroundResource(R.drawable.quiz_box);
                        opt1.setTextColor(getResources().getColor(R.color.colorPrimaryDarkbg));
                        opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt2.setBackgroundResource(R.drawable.quiz_opt);
                        opt3.setBackgroundResource(R.drawable.quiz_opt);
                        opt4.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                        opt1.setPadding(15, 0, 0, 0);
                        opt2.setPadding(15, 0, 0, 0);
                        opt3.setPadding(15, 0, 0, 0);
                        opt4.setPadding(15, 0, 0, 0);
                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 1;
                    } else {
                        back_opt1.setBackgroundResource(R.drawable.quiz_box);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 1;
                    }
                }
            });
            back_opt1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //for zoom


                    back_opt1.setBackgroundResource(R.drawable.quiz_box);
                    back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt4.setBackgroundResource(R.drawable.quiz_opt);

                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));

                    back_opt1.setPadding(15, 0, 15, 0);
                    back_opt2.setPadding(15, 0, 15, 0);
                    back_opt3.setPadding(15, 0, 15, 0);
                    back_opt4.setPadding(15, 0, 15, 0);
                    opt1.setPadding(15, 0, 0, 0);
                    opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);

                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);
                    selected = 1;
                }
            });

            opt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (opt2.getVisibility() == View.VISIBLE) {
                        opt2.setBackgroundResource(R.drawable.quiz_box);
                        opt2.setTextColor(getResources().getColor(R.color.colorPrimaryDarkbg));
                        opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt1.setBackgroundResource(R.drawable.quiz_opt);
                        opt3.setBackgroundResource(R.drawable.quiz_opt);
                        opt4.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                        opt1.setPadding(15, 0, 0, 0);
                        opt2.setPadding(15, 0, 0, 0);
                        opt3.setPadding(15, 0, 0, 0);
                        opt4.setPadding(15, 0, 0, 0);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 2;
                    } else {
                        back_opt2.setBackgroundResource(R.drawable.quiz_box);
                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 2;
                    }


                    /*opt2.setBackgroundResource(R.drawable.quiz_box);
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimaryDarkbg));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setPadding(15, 0, 0, 0);opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);
                    selected = 2;*/
                }
            });

            back_opt2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//for zoom

                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);

                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));

                    back_opt2.setBackgroundResource(R.drawable.quiz_box);
                    back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setPadding(15, 0, 0, 0);
                    opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);
                    back_opt1.setPadding(15, 0, 15, 0);
                    back_opt2.setPadding(15, 0, 15, 0);
                    back_opt3.setPadding(15, 0, 15, 0);
                    back_opt4.setPadding(15, 0, 15, 0);
                    selected = 2;
                }
            });

            opt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (opt3.getVisibility() == View.VISIBLE) {
                        opt3.setBackgroundResource(R.drawable.quiz_box);
                        opt1.setBackgroundResource(R.drawable.quiz_opt);
                        opt2.setBackgroundResource(R.drawable.quiz_opt);
                        opt4.setBackgroundResource(R.drawable.quiz_opt);

                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);

                        opt3.setTextColor(getResources().getColor(R.color.colorPrimaryDarkbg));
                        opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt4.setTextColor(getResources().getColor(R.color.colorPrimary));

                        opt1.setPadding(15, 0, 0, 0);
                        opt2.setPadding(15, 0, 0, 0);
                        opt3.setPadding(15, 0, 0, 0);
                        opt4.setPadding(15, 0, 0, 0);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 3;
                    } else {
                        back_opt3.setBackgroundResource(R.drawable.quiz_box);
                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 3;
                    }

                   /* opt3.setBackgroundResource(R.drawable.quiz_box);
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimaryDarkbg));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setPadding(15, 0, 0, 0);opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);
                    selected = 3;*/
                }
            });

            back_opt3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//for zoom


                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);

                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));

                    back_opt3.setBackgroundResource(R.drawable.quiz_box);
                    back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setPadding(15, 0, 0, 0);
                    opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);
                    back_opt1.setPadding(15, 0, 15, 0);
                    back_opt2.setPadding(15, 0, 15, 0);
                    back_opt3.setPadding(15, 0, 15, 0);
                    back_opt4.setPadding(15, 0, 15, 0);
                    selected = 3;
                }
            });
            opt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (opt4.getVisibility() == View.VISIBLE) {
                        opt4.setBackgroundResource(R.drawable.quiz_box);
                        opt4.setTextColor(getResources().getColor(R.color.colorPrimaryDarkbg));
                        opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt1.setBackgroundResource(R.drawable.quiz_opt);
                        opt3.setBackgroundResource(R.drawable.quiz_opt);
                        opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                        opt1.setPadding(15, 0, 0, 0);
                        opt2.setPadding(15, 0, 0, 0);
                        opt3.setPadding(15, 0, 0, 0);
                        opt4.setPadding(15, 0, 0, 0);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 4;
                    } else {
                        back_opt4.setBackgroundResource(R.drawable.quiz_box);
                        back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                        back_opt2.setBackgroundResource(R.drawable.quiz_opt);

                        back_opt1.setPadding(15, 0, 15, 0);
                        back_opt2.setPadding(15, 0, 15, 0);
                        back_opt3.setPadding(15, 0, 15, 0);
                        back_opt4.setPadding(15, 0, 15, 0);
                        selected = 4;
                    }
                }
            });

            back_opt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //for zoom


                    opt3.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setBackgroundResource(R.drawable.quiz_opt);
                    opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt4.setBackgroundResource(R.drawable.quiz_opt);

                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));

                    back_opt4.setBackgroundResource(R.drawable.quiz_box);
                    back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                    back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                    opt1.setPadding(15, 0, 0, 0);
                    opt2.setPadding(15, 0, 0, 0);
                    opt3.setPadding(15, 0, 0, 0);
                    opt4.setPadding(15, 0, 0, 0);
                    back_opt1.setPadding(15, 0, 15, 0);
                    back_opt2.setPadding(15, 0, 15, 0);
                    back_opt3.setPadding(15, 0, 15, 0);
                    back_opt4.setPadding(15, 0, 15, 0);
                    selected = 4;
                }
            });

            opt1.setPadding(15, 0, 0, 0);
            opt2.setPadding(15, 0, 0, 0);
            opt3.setPadding(15, 0, 0, 0);
            opt4.setPadding(15, 0, 0, 0);

            getQuestion();

            btn_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ispeding.equalsIgnoreCase("1")) {
                        if (selected == 0) {
                            // frame.bringToFront();
                            Toast.makeText(PracticeTestQAActivity.this, "Please Select Any Option !", Toast.LENGTH_SHORT).show();
                        } else {
                            frame_progress.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.VISIBLE);
                            setTestAns();
                        }
                    } else {
                        countAll++;
                        if (allArrays.size() >= countAll) {
                            quesId = allArrays.get(countAll).getFirst();
                            correctAns = allArrays.get(countAll).getFourth();
                            givenAns = allArrays.get(countAll).getThird();
                            //  quesNo.setText(allArrays.get(countAll).getSixth());
                            question = allArrays.get(countAll).getSecond();
                            if (isURL(question)) {
                                img_ques.setVisibility(View.VISIBLE);
                                text_ques.setVisibility(View.GONE);
                                card_img_ques.setVisibility(View.VISIBLE);
                                getImageSize(question, "que");
                                //test
                               /* Glide.with(PracticeTestQAActivity.this)
                                        .load(question).asBitmap().dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(img_ques);*/


                                ques_no_img.setText(allArrays.get(countAll).getSixth());
                            } else {
                                img_ques.setVisibility(View.GONE);
                                text_ques.setVisibility(View.VISIBLE);
                                card_img_ques.setVisibility(View.GONE);
                                ques.setText(allArrays.get(countAll).getSixth() + " " + allArrays.get(countAll).getSecond());
                            }
                            //     ques.setText(allArrays.get(countAll).getSixth() + " " + allArrays.get(countAll).getSecond());

                            opt_1 = allArrays.get(countAll).getSeventh();

                            opt_2 = allArrays.get(countAll).getEighth();
                            opt_3 = allArrays.get(countAll).getNineth();
                            opt_4 = allArrays.get(countAll).getTenth();

                            if (isURL(opt_1)) {
                                back_opt1.setVisibility(View.VISIBLE);
                                opt1.setVisibility(View.GONE);
                                back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                                getImageSize(opt_1, "opt_1");
                                //test
//                                Glide.with(PracticeTestQAActivity.this)
//                                        .load(opt_1).asBitmap().dontAnimate()
//                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                        .placeholder(R.drawable.placeholder)
//                                        .error(R.drawable.placeholder)
//                                        .into(img_opt1);
                            } else {
                                //  opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                back_opt1.setVisibility(View.GONE);
                                opt1.setVisibility(View.VISIBLE);
                                opt1.setBackgroundResource(R.drawable.quiz_opt);
                                opt1.setText("[A] " + allArrays.get(countAll).getSeventh());
                            }

                            if (isURL(opt_2)) {
                                back_opt2.setVisibility(View.VISIBLE);
                                opt2.setVisibility(View.GONE);
                                back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                                getImageSize(opt_2, "opt_2");
                                //test
                               /* Glide.with(PracticeTestQAActivity.this)
                                        .load(opt_2).asBitmap().dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(img_opt2);*/
                            } else {
                                // opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                opt2.setVisibility(View.VISIBLE);
                                back_opt2.setVisibility(View.GONE);
                                opt2.setBackgroundResource(R.drawable.quiz_opt);
                                opt2.setText("[B] " + allArrays.get(countAll).getEighth());
                            }

                            if (isURL(opt_3)) {
                                back_opt3.setVisibility(View.VISIBLE);
                                opt3.setVisibility(View.GONE);
                                back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                                getImageSize(opt_3, "opt_3");
                                //test
                               /* Glide.with(PracticeTestQAActivity.this)
                                        .load(opt_3).asBitmap().dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(img_opt3);*/
                            } else {
                                //   opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                                back_opt3.setVisibility(View.GONE);
                                opt3.setVisibility(View.VISIBLE);
                                opt3.setBackgroundResource(R.drawable.quiz_opt);
                                opt3.setText("[C] " + allArrays.get(countAll).getNineth());
                            }

                            if (isURL(opt_4)) {
                                back_opt4.setVisibility(View.VISIBLE);
                                opt4.setVisibility(View.GONE);
                                back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                                getImageSize(opt_4, "opt_4");
                                //test
                               /* Glide.with(PracticeTestQAActivity.this)
                                    .load(opt_4).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_opt4);*/
                            } else {
                                opt4.setVisibility(View.VISIBLE);
                                back_opt4.setVisibility(View.GONE);
                                opt4.setBackgroundResource(R.drawable.quiz_opt);
                                opt4.setText("[D] " + allArrays.get(countAll).getTenth());
                            }

                            if (correctAns == null) {
                                correctAns = "0";
                            }
                            if (givenAns == null) {
                                givenAns = "0";
                            }
                            for (int i = 1; i <= 4; i++) {
                                int color = 0;
                                int colorText = 0;

                                back_opt1.setPadding(15, 0, 15, 0);
                                back_opt2.setPadding(15, 0, 15, 0);
                                back_opt3.setPadding(15, 0, 15, 0);
                                back_opt4.setPadding(15, 0, 15, 0);

                                opt1.setPadding(15, 0, 0, 0);
                                opt2.setPadding(15, 0, 0, 0);
                                opt3.setPadding(15, 0, 0, 0);
                                opt4.setPadding(15, 0, 0, 0);

                                if (correctAns.equals(i + "") && givenAns.equals(i + "")) {
                                    color = R.drawable.quiz_right;
                                    colorText = R.color.White;
                                } else if (correctAns.equals(i + "")) {
                                    color = R.drawable.quiz_right_diff;
                                    colorText = R.color.Black;
                                } else if (givenAns.equals(i + "")) {
                                    color = R.drawable.quiz_wrong;
                                    colorText = R.color.White;
                                }
                                if (color != 0) {
                                    if (i == 1) {
                                        opt1.setBackgroundResource(color);
                                        opt1.setTextColor(colorText);
                                        opt1.setPadding(15, 0, 0, 0);
                                    } else if (i == 2) {
                                        opt2.setBackgroundResource(color);
                                        opt2.setTextColor(colorText);
                                        opt2.setPadding(15, 0, 0, 0);
                                    } else if (i == 3) {
                                        opt3.setBackgroundResource(color);
                                        opt3.setTextColor(colorText);
                                        opt3.setPadding(15, 0, 0, 0);
                                    } else {
                                        opt4.setBackgroundResource(color);
                                        opt4.setTextColor(colorText);
                                        opt4.setPadding(15, 0, 0, 0);
                                    }
                                }
                            }
                        }
                    }

                }
            });

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countAll++;
                    next.setVisibility(View.VISIBLE);
                    previous.setVisibility(View.VISIBLE);
                    if (allArrays.size() - 1 == countAll) {
                        next.setVisibility(View.GONE);
                        previous.setVisibility(View.VISIBLE);
                    }
                    if (allArrays.size() >= countAll) {
                        quesId = allArrays.get(countAll).getFirst();
                        correctAns = allArrays.get(countAll).getFourth();
                        givenAns = allArrays.get(countAll).getThird();
                        //  quesNo.setText(allArrays.get(countAll).getSixth());
                        question = allArrays.get(countAll).getSecond();
                        if (isURL(question)) {
                            img_ques.setVisibility(View.VISIBLE);
                            text_ques.setVisibility(View.GONE);
                            card_img_ques.setVisibility(View.VISIBLE);
                            getImageSize(question, "que");
                            //test
                           /* Glide.with(PracticeTestQAActivity.this)
                                    .load(question).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_ques);*/
                            ques_no_img.setText(allArrays.get(countAll).getSixth());
                        } else {
                            img_ques.setVisibility(View.GONE);
                            text_ques.setVisibility(View.VISIBLE);
                            card_img_ques.setVisibility(View.GONE);
                            ques.setText(allArrays.get(countAll).getSixth() + " " + allArrays.get(countAll).getSecond());
                        }
                        //   ques.setText(allArrays.get(countAll).getSixth() + " " + allArrays.get(countAll).getSecond());


                      /*  String opt_1 = allArrays.get(countAll).getSeventh();
                        String opt_2 = allArrays.get(countAll).getEighth();
                        String opt_3 = allArrays.get(countAll).getNineth();
                        String opt_4 = allArrays.get(countAll).getTenth();

                        if (isURL(opt_1)) {
                            back_opt1.setVisibility(View.VISIBLE);
                            opt1.setVisibility(View.GONE);
                            back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                            Glide.with(PracticeTestQAActivity.this)
                                    .load(opt_1).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_opt1);
                        } else {
                            opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                            back_opt1.setVisibility(View.GONE);
                            opt1.setVisibility(View.VISIBLE);
                            opt1.setBackgroundResource(R.drawable.quiz_opt);
                            opt1.setText("[A] " + allArrays.get(countAll).getSeventh());
                        }

                        if (isURL(opt_2)) {
                            back_opt2.setVisibility(View.VISIBLE);
                            opt2.setVisibility(View.GONE);

                            back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                            Glide.with(PracticeTestQAActivity.this)
                                    .load(opt_2).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_opt2);
                        } else {
                            opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                            opt2.setVisibility(View.VISIBLE);
                            back_opt2.setVisibility(View.GONE);
                            opt2.setBackgroundResource(R.drawable.quiz_opt);
                            opt2.setText("[B] " + allArrays.get(countAll).getEighth());
                        }

                        if (isURL(opt_3)) {
                            back_opt3.setVisibility(View.VISIBLE);
                            opt3.setVisibility(View.GONE);

                            back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                            Glide.with(PracticeTestQAActivity.this)
                                    .load(opt_3).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_opt3);
                        } else {
                            opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                            back_opt3.setVisibility(View.GONE);
                            opt3.setVisibility(View.VISIBLE);
                            opt3.setBackgroundResource(R.drawable.quiz_opt);
                            opt3.setText("[C] " + allArrays.get(countAll).getNineth());
                        }

                        if (isURL(opt_4)) {
                            back_opt4.setVisibility(View.VISIBLE);
                            opt4.setVisibility(View.GONE);
                            back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                            Glide.with(PracticeTestQAActivity.this)
                                    .load(opt_4).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_opt4);
                        } else {
                            opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                            back_opt4.setVisibility(View.GONE);
                            opt4.setVisibility(View.VISIBLE);
                            opt4.setBackgroundResource(R.drawable.quiz_opt);
                            opt4.setText("[D] " + allArrays.get(countAll).getTenth());
                        }*/

                      /*  opt1.setText("[A] " + allArrays.get(countAll).getSeventh());
                        opt2.setText("[B] " + allArrays.get(countAll).getEighth());
                        opt3.setText("[C] " + allArrays.get(countAll).getNineth());
                        opt4.setText("[D] " + allArrays.get(countAll).getTenth());
                        opt1.setBackgroundResource(R.drawable.quiz_opt);
                        opt2.setBackgroundResource(R.drawable.quiz_opt);
                        opt3.setBackgroundResource(R.drawable.quiz_opt);
                        opt4.setBackgroundResource(R.drawable.quiz_opt);
                        opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt4.setTextColor(getResources().getColor(R.color.colorPrimary));*/
                        setCorrectAns(countAll);
                        /*String correct = "";
                        String correctImg = "";
                        if (allArrays.get(countAll).getFourth().equals("1")) {
                            correctImg = allArrays.get(countAll).getSeventh();
                            correct = "[A] " + allArrays.get(countAll).getSeventh();
                        } else if (allArrays.get(countAll).getFourth().equals("2")) {
                            correctImg = allArrays.get(countAll).getEighth();
                            correct = "[B] " + allArrays.get(countAll).getEighth();
                        } else if (allArrays.get(countAll).getFourth().equals("3")) {
                            correctImg = allArrays.get(countAll).getNineth();
                            correct = "[C] " + allArrays.get(countAll).getNineth();
                        } else if (allArrays.get(countAll).getFourth().equals("4")) {
                            correctImg = allArrays.get(countAll).getTenth();
                            correct = "[D] " + allArrays.get(countAll).getTenth();
                        }

                        if (correctImg != "") {
                            if (isURL(correctImg)) {
                                img_CorrectAns.setVisibility(View.VISIBLE);
                                correctAnsDisplay.setVisibility(View.GONE);
                                Glide.with(PracticeTestQAActivity.this)
                                        .load(correctImg).asBitmap().dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(img_CorrectAns);

                            } else {
                                img_CorrectAns.setVisibility(View.GONE);
                                correctAnsDisplay.setVisibility(View.VISIBLE);
                                correctAnsDisplay.setText("Correct Ans : " + correct);
                            }
                        }
                        if (correctAns == null) {
                            correctAns = "0";
                        }
                        if (givenAns == null) {
                            givenAns = "0";
                        }
                        for (int i = 1; i <= 4; i++) {
                            int color = 0;
                            int colorText = 0;

                            back_opt1.setPadding(15, 0, 15, 0);
                            back_opt2.setPadding(15, 0, 15, 0);
                            back_opt3.setPadding(15, 0, 15, 0);
                            back_opt4.setPadding(15, 0, 15, 0);

                            opt1.setPadding(15, 0, 0, 0);opt2.setPadding(15, 0, 0, 0);
                            opt3.setPadding(15, 0, 0, 0);
                            opt4.setPadding(15, 0, 0, 0);

                            if (givenAns.equals(i + "")) {
                                color = R.drawable.quiz_box;
                                colorText = R.color.colorPrimaryDarkbg;
                            }

                            if (color != 0) {
                                if (i == 1) {
                                    if (back_opt1.getVisibility() == View.VISIBLE) {
                                        back_opt1.setBackgroundResource(color);
                                        back_opt1.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt1.setBackgroundResource(color);
                                        opt1.setTextColor(getResources().getColor(colorText));
                                        opt1.setPadding(15, 0, 0, 0);}
                                } else if (i == 2) {
                                    if (back_opt2.getVisibility() == View.VISIBLE) {
                                        back_opt2.setBackgroundResource(color);
                                        back_opt2.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt2.setBackgroundResource(color);
                                        opt2.setTextColor(getResources().getColor(colorText));
                                        opt2.setPadding(15, 0, 0, 0);
                                    }
                                } else if (i == 3) {
                                    if (back_opt3.getVisibility() == View.VISIBLE) {
                                        back_opt3.setBackgroundResource(color);
                                        back_opt3.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt3.setBackgroundResource(color);
                                        opt3.setTextColor(getResources().getColor(colorText));
                                        opt3.setPadding(15, 0, 0, 0);
                                    }
                                } else {
                                    if (back_opt4.getVisibility() == View.VISIBLE) {
                                        back_opt4.setBackgroundResource(color);
                                        back_opt4.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt4.setBackgroundResource(color);
                                        opt4.setTextColor(getResources().getColor(colorText));
                                        opt4.setPadding(15, 0, 0, 0);
                                    }
                                }
                            }
                        }*/
                    }
                }
            });

            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countAll--;
                    next.setVisibility(View.VISIBLE);
                    previous.setVisibility(View.VISIBLE);
                    if (countAll == 0) {
                        next.setVisibility(View.VISIBLE);
                        previous.setVisibility(View.GONE);
                    }
                    if (allArrays.size() >= countAll) {
                        quesId = allArrays.get(countAll).getFirst();
                        correctAns = allArrays.get(countAll).getFourth();
                        givenAns = allArrays.get(countAll).getThird();
                        //  quesNo.setText(allArrays.get(countAll).getSixth());
                        question = allArrays.get(countAll).getSecond();
                        if (isURL(question)) {
                            img_ques.setVisibility(View.VISIBLE);
                            text_ques.setVisibility(View.GONE);
                            card_img_ques.setVisibility(View.VISIBLE);
                            getImageSize(question, "que");
                            //test
                           /* Glide.with(PracticeTestQAActivity.this)
                                    .load(question).asBitmap().dontAnimate()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(img_ques);*/
                            ques_no_img.setText(allArrays.get(countAll).getSixth());
                        } else {
                            img_ques.setVisibility(View.GONE);
                            text_ques.setVisibility(View.VISIBLE);
                            card_img_ques.setVisibility(View.GONE);
                            ques.setText(allArrays.get(countAll).getSixth() + " " + allArrays.get(countAll).getSecond());
                        }
                        //    ques.setText(allArrays.get(countAll).getSixth() + " " + allArrays.get(countAll).getSecond());



                        /*
                        opt1.setText("[A] " + allArrays.get(countAll).getSeventh());
                        opt2.setText("[B] " + allArrays.get(countAll).getEighth());
                        opt3.setText("[C] " + allArrays.get(countAll).getNineth());
                        opt4.setText("[D] " + allArrays.get(countAll).getTenth());
                        opt1.setBackgroundResource(R.drawable.quiz_opt);
                        opt2.setBackgroundResource(R.drawable.quiz_opt);
                        opt3.setBackgroundResource(R.drawable.quiz_opt);
                        opt4.setBackgroundResource(R.drawable.quiz_opt);
                        opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                        opt4.setTextColor(getResources().getColor(R.color.colorPrimary));*/

                        //  frame.bringToFront();
                        setCorrectAns(countAll);
                       /* String correct = "";
                        String correctImg = "";
                        if (allArrays.get(countAll).getFourth().equals("1")) {
                            correctImg = allArrays.get(countAll).getSeventh();
                            correct = "[A] " + allArrays.get(countAll).getSeventh();
                        } else if (allArrays.get(countAll).getFourth().equals("2")) {
                            correctImg = allArrays.get(countAll).getEighth();
                            correct = "[B] " + allArrays.get(countAll).getEighth();
                        } else if (allArrays.get(countAll).getFourth().equals("3")) {
                            correctImg = allArrays.get(countAll).getNineth();
                            correct = "[C] " + allArrays.get(countAll).getNineth();
                        } else if (allArrays.get(countAll).getFourth().equals("4")) {
                            correctImg = allArrays.get(countAll).getTenth();
                            correct = "[D] " + allArrays.get(countAll).getTenth();
                        }

                        if (correctImg != "") {
                            if (isURL(correctImg)) {
                                img_CorrectAns.setVisibility(View.VISIBLE);
                                correctAnsDisplay.setVisibility(View.GONE);
                                Glide.with(PracticeTestQAActivity.this)
                                        .load(correctImg).asBitmap().dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                        .into(img_CorrectAns);

                            } else {
                                img_CorrectAns.setVisibility(View.GONE);
                                correctAnsDisplay.setVisibility(View.VISIBLE);
                                correctAnsDisplay.setText("Correct Ans : " + correct);
                            }
                        }

//                        String correct = "";
//
//                        if (allArrays.get(countAll).getFourth().equals("1")) {
//                            correct = "[A] " + allArrays.get(countAll).getSeventh();
//                        } else if (allArrays.get(countAll).getFourth().equals("2")) {
//                            correct = "[B] " + allArrays.get(countAll).getEighth();
//                        } else if (allArrays.get(countAll).getFourth().equals("3")) {
//                            correct = "[C] " + allArrays.get(countAll).getNineth();
//                        } else if (allArrays.get(countAll).getFourth().equals("4")) {
//                            correct = "[D] " + allArrays.get(countAll).getTenth();
//                        }
//                        correctAnsDisplay.setText("Correct Ans : " + correct);

                        if (correctAns == null) {
                            correctAns = "0";
                        }
                        if (givenAns == null) {
                            givenAns = "0";
                        }
                        for (int i = 1; i <= 4; i++) {
                            int color = 0;
                            int colorText = 0;

                            back_opt1.setPadding(15, 0, 15, 0);
                            back_opt2.setPadding(15, 0, 15, 0);
                            back_opt3.setPadding(15, 0, 15, 0);
                            back_opt4.setPadding(15, 0, 15, 0);

                            opt1.setPadding(15, 0, 0, 0);opt2.setPadding(15, 0, 0, 0);
                            opt3.setPadding(15, 0, 0, 0);
                            opt4.setPadding(15, 0, 0, 0);
                            *//*if (correctAns.equals(i + "") && givenAns.equals(i + "")) {
                                color = R.drawable.quiz_right;
                                colorText = R.color.White;
                            } else if (correctAns.equals(i + "")) {
                                color = R.drawable.quiz_right_diff;
                                colorText = R.color.Black;
                            } else *//*
                            if (givenAns.equals(i + "")) {
                                color = R.drawable.quiz_box;
                                colorText = R.color.colorPrimaryDarkbg;
                            }

                            if (color != 0) {
                                if (i == 1) {
                                    if (back_opt1.getVisibility() == View.VISIBLE) {
                                        back_opt1.setBackgroundResource(color);
                                        back_opt1.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt1.setBackgroundResource(color);
                                        opt1.setTextColor(getResources().getColor(colorText));
                                        opt1.setPadding(15, 0, 0, 0);}
                                } else if (i == 2) {
                                    if (back_opt2.getVisibility() == View.VISIBLE) {
                                        back_opt2.setBackgroundResource(color);
                                        back_opt2.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt2.setBackgroundResource(color);
                                        opt2.setTextColor(getResources().getColor(colorText));
                                        opt2.setPadding(15, 0, 0, 0);
                                    }
                                } else if (i == 3) {
                                    if (back_opt3.getVisibility() == View.VISIBLE) {
                                        back_opt3.setBackgroundResource(color);
                                        back_opt3.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt3.setBackgroundResource(color);
                                        opt3.setTextColor(getResources().getColor(colorText));
                                        opt3.setPadding(15, 0, 0, 0);
                                    }
                                } else {
                                    if (back_opt4.getVisibility() == View.VISIBLE) {
                                        back_opt4.setBackgroundResource(color);
                                        back_opt4.setPadding(15, 0, 15, 0);
                                    } else {
                                        opt4.setBackgroundResource(color);
                                        opt4.setTextColor(getResources().getColor(colorText));
                                        opt4.setPadding(15, 0, 0, 0);
                                    }
                                }
                            }

                        }*/
                    }
                }
            });
        } catch (Exception ex) {
            Constants.writelog("PracticeTestQAActivity", "onCreate 1043:" + ex.getMessage());
        }
    }


    private void getQuestion() {
        try {
            if (isOnline()) {
                frame_progress.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                String mStudentId = Datastorage.GetStudentId(getApplicationContext());
                String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
                String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
                Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                        .GetPracticeTestQAList(mStudentId, mSchoolId, mYearId, testId, Constants.APPname, Constants.CODEVERSION, Constants.PLATFORM);
                call.enqueue(new Callback<AppService>() {
                    @Override
                    public void onResponse(Call<AppService> call, Response<AppService> response) {
                        try {
                            frame_progress.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            rl_test.setVisibility(View.VISIBLE);
                            AppService tmps = response.body();
                            if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                    && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                                if (ispeding.equalsIgnoreCase("1")) {
                                    for (int i = 0; i < tmps.getStrlist().size(); i++) {
                                        if (tmps.getStrlist().get(i).getThird() == null || tmps.getStrlist().get(i).getThird().equals("0") || tmps.getStrlist().get(i).getThird().equals("")) {
                                            listArrays.add(tmps.getStrlist().get(i));
                                        }
                                    }
                                    setQuestion(0, listArrays);
                                    /*if (listArrays != null) {
                                        if (listArrays.size() == 1) {
                                            isLast = "1";
                                        }
                                        back_opt1.setPadding(15, 0, 15, 0);
                                        back_opt2.setPadding(15, 0, 15, 0);
                                        back_opt3.setPadding(15, 0, 15, 0);
                                        back_opt4.setPadding(15, 0, 15, 0);

                                        opt1.setPadding(15, 0, 0, 0);opt2.setPadding(15, 0, 0, 0);
                                        opt3.setPadding(15, 0, 0, 0);
                                        opt4.setPadding(15, 0, 0, 0);

                                        quesId = listArrays.get(0).getFirst();
                                        correctAns = listArrays.get(0).getFourth();
                                        //    quesNo.setText(listArrays.get(0).getSixth());
                                        String question = listArrays.get(0).getSecond();
                                        *//* String question = "https://espschools.s3.ap-south-1.amazonaws.com/Quiz/Q33_2.png";*//*
                                        if (isURL(question)) {
                                            img_ques.setVisibility(View.VISIBLE);
                                            text_ques.setVisibility(View.GONE);
                                            card_img_ques.setVisibility(View.VISIBLE);
                                            Glide.with(PracticeTestQAActivity.this)
                                                    .load(question).asBitmap().dontAnimate()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.placeholder)
                                                    .error(R.drawable.placeholder)
                                                    .into(img_ques);
                                            ques_no_img.setText(allArrays.get(0).getSixth());
                                        } else {
                                            img_ques.setVisibility(View.GONE);
                                            card_img_ques.setVisibility(View.GONE);
                                            text_ques.setVisibility(View.VISIBLE);
                                            ques.setText(listArrays.get(0).getSixth() + " " + listArrays.get(0).getSecond());
                                        }

                                        String opt_1 = listArrays.get(0).getSeventh();
                                        String opt_2 = listArrays.get(0).getEighth();
                                        String opt_3 = listArrays.get(0).getNineth();
                                        String opt_4 = listArrays.get(0).getTenth();

                                        if (isURL(opt_1)) {
                                            back_opt1.setVisibility(View.VISIBLE);
                                            opt1.setVisibility(View.GONE);
                                            Glide.with(PracticeTestQAActivity.this)
                                                    .load(opt_1).asBitmap().dontAnimate()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.placeholder)
                                                    .error(R.drawable.placeholder)
                                                    .into(img_opt1);

                                            option_no_1.setText("[A] ");
                                           *//* lp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT,
                                                    LinearLayout.LayoutParams.WRAP_CONTENT);

                                            back_opt1.setLayoutParams(lp);*//*
                                            // back_opt1.setPadding(15, 0, 0, 0);} else {
                                            back_opt1.setVisibility(View.GONE);
                                            opt1.setVisibility(View.VISIBLE);
                                            opt1.setText("[A] " + listArrays.get(0).getSeventh());
                                        }

                                        if (isURL(opt_2)) {
                                            back_opt2.setVisibility(View.VISIBLE);
                                            opt2.setVisibility(View.GONE);
                                            Glide.with(PracticeTestQAActivity.this)
                                                    .load(opt_2).asBitmap().dontAnimate()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.placeholder)
                                                    .error(R.drawable.placeholder)
                                                    .into(img_opt2);
                                            option_no_2.setText("[B] ");
                                        } else {
                                            back_opt2.setVisibility(View.GONE);
                                            opt2.setVisibility(View.VISIBLE);
                                            opt2.setText("[B] " + listArrays.get(0).getEighth());
                                        }

                                        if (isURL(opt_3)) {
                                            back_opt3.setVisibility(View.VISIBLE);
                                            opt3.setVisibility(View.GONE);
                                            Glide.with(PracticeTestQAActivity.this)
                                                    .load(opt_3).asBitmap().dontAnimate()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.placeholder)
                                                    .error(R.drawable.placeholder)
                                                    .into(img_opt3);
                                            option_no_3.setText("[C] ");
                                        } else {
                                            back_opt3.setVisibility(View.GONE);
                                            opt3.setVisibility(View.VISIBLE);
                                            opt3.setText("[C] " + listArrays.get(0).getNineth());
                                        }

                                        if (isURL(opt_4)) {
                                            back_opt4.setVisibility(View.VISIBLE);
                                            opt4.setVisibility(View.GONE);
                                            Glide.with(PracticeTestQAActivity.this)
                                                    .load(opt_4).asBitmap().dontAnimate()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.placeholder)
                                                    .error(R.drawable.placeholder)
                                                    .into(img_opt4);
                                            option_no_4.setText("[D] ");
                                        } else {
                                            opt4.setVisibility(View.VISIBLE);
                                            back_opt4.setVisibility(View.GONE);
                                            opt4.setText("[D] " + listArrays.get(0).getTenth());
                                        }

                                   *//* opt1.setText("[A] " + listArrays.get(0).getSeventh());
                                    opt2.setText("[B] " + listArrays.get(0).getEighth());
                                    opt3.setText("[C] " + listArrays.get(0).getNineth());
                                    opt4.setText("[D] " + listArrays.get(0).getTenth());*//*

                                    }*/
                                } else {
                                    allArrays = tmps.getStrlist();
                                    opt1.setPadding(15, 0, 0, 0);
                                    opt2.setPadding(15, 0, 0, 0);
                                    opt3.setPadding(15, 0, 0, 0);
                                    opt4.setPadding(15, 0, 0, 0);
                                    opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                                    quesId = allArrays.get(0).getFirst();
                                    correctAns = allArrays.get(0).getFourth();
                                    givenAns = allArrays.get(0).getThird();
                                    //      quesNo.setText(allArrays.get(0).getSixth());
                                    question = allArrays.get(0).getSecond();
                                    if (isURL(question)) {
                                        card_img_ques.setVisibility(View.VISIBLE);
                                        img_ques.setVisibility(View.VISIBLE);
                                        text_ques.setVisibility(View.GONE);
                                        getImageSize(question, "que");
                                        //test
                                       /* Glide.with(PracticeTestQAActivity.this)
                                                .load(question).asBitmap().dontAnimate()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .placeholder(R.drawable.placeholder)
                                                .error(R.drawable.placeholder)
                                                .into(img_ques);*/
                                        ques_no_img.setText(allArrays.get(0).getSixth());
                                    } else {
                                        card_img_ques.setVisibility(View.GONE);
                                        img_ques.setVisibility(View.GONE);
                                        text_ques.setVisibility(View.VISIBLE);
                                        ques.setText(allArrays.get(0).getSixth() + " " + allArrays.get(0).getSecond());
                                    }
                                    // ques.setText(allArrays.get(0).getSixth() + " " + allArrays.get(0).getSecond());


                                    /*String opt_1 = allArrays.get(0).getSeventh();
                                    String opt_2 = allArrays.get(0).getEighth();
                                    String opt_3 = allArrays.get(0).getNineth();
                                    String opt_4 = allArrays.get(0).getTenth();

                                    if (isURL(opt_1)) {
                                        back_opt1.setVisibility(View.VISIBLE);
                                        opt1.setVisibility(View.GONE);
                                        Glide.with(PracticeTestQAActivity.this)
                                                .load(opt_1).asBitmap().dontAnimate()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .placeholder(R.drawable.placeholder)
                                                .error(R.drawable.placeholder)
                                                .into(img_opt1);
                                        option_no_1.setText("[A] ");
                                    } else {
                                        opt1.setVisibility(View.VISIBLE);
                                        back_opt1.setVisibility(View.GONE);
                                        opt1.setText("[A] " + allArrays.get(0).getSeventh());
                                    }

                                    if (isURL(opt_2)) {
                                        back_opt2.setVisibility(View.VISIBLE);
                                        opt2.setVisibility(View.GONE);
                                        Glide.with(PracticeTestQAActivity.this)
                                                .load(opt_2).asBitmap().dontAnimate()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .placeholder(R.drawable.placeholder)
                                                .error(R.drawable.placeholder)
                                                .into(img_opt2);
                                        option_no_2.setText("[B] ");
                                    } else {
                                        opt2.setVisibility(View.VISIBLE);
                                        back_opt2.setVisibility(View.GONE);
                                        opt2.setText("[B] " + allArrays.get(0).getEighth());
                                    }

                                    if (isURL(opt_3)) {
                                        back_opt3.setVisibility(View.VISIBLE);
                                        opt3.setVisibility(View.GONE);
                                        Glide.with(PracticeTestQAActivity.this)
                                                .load(opt_3).asBitmap().dontAnimate()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .placeholder(R.drawable.placeholder)
                                                .error(R.drawable.placeholder)
                                                .into(img_opt3);
                                        option_no_3.setText("[C] ");
                                    } else {
                                        opt3.setVisibility(View.VISIBLE);
                                        back_opt3.setVisibility(View.GONE);
                                        opt3.setText("[C] " + allArrays.get(0).getNineth());
                                    }

                                    if (isURL(opt_4)) {
                                        back_opt4.setVisibility(View.VISIBLE);
                                        opt4.setVisibility(View.GONE);
                                        Glide.with(PracticeTestQAActivity.this)
                                                .load(opt_4).asBitmap().dontAnimate()
                                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                .placeholder(R.drawable.placeholder)
                                                .error(R.drawable.placeholder)
                                                .into(img_opt4);
                                        option_no_4.setText("[D] ");
                                    } else {
                                        opt4.setVisibility(View.VISIBLE);
                                        back_opt4.setVisibility(View.GONE);
                                        opt4.setText("[D] " + allArrays.get(0).getTenth());
                                    }*/

                               /* opt1.setText("[A] " + allArrays.get(0).getSeventh());
                                opt2.setText("[B] " + allArrays.get(0).getEighth());
                                opt3.setText("[C] " + allArrays.get(0).getNineth());
                                opt4.setText("[D] " + allArrays.get(0).getTenth());*/

                                    /*String correct = "";
                                    String correctImg = "";
                                    String alpha = "";
                                    if (allArrays.get(countAll).getFourth().equals("1")) {
                                        correctImg = allArrays.get(countAll).getSeventh();
                                        alpha = "[A] ";
                                        correct = "[A] " + allArrays.get(countAll).getSeventh();
                                    } else if (allArrays.get(countAll).getFourth().equals("2")) {
                                        correctImg = allArrays.get(countAll).getEighth();
                                        alpha = "[B] ";
                                        correct = "[B] " + allArrays.get(countAll).getEighth();
                                    } else if (allArrays.get(countAll).getFourth().equals("3")) {
                                        correctImg = allArrays.get(countAll).getNineth();
                                        alpha = "[C] ";
                                        correct = "[C] " + allArrays.get(countAll).getNineth();
                                    } else if (allArrays.get(countAll).getFourth().equals("4")) {
                                        correctImg = allArrays.get(countAll).getTenth();
                                        alpha = "[D] ";
                                        correct = "[D] " + allArrays.get(countAll).getTenth();
                                    }

                                    if (correctImg != "") {
                                        if (isURL(correctImg)) {
                                            img_CorrectAns.setVisibility(View.VISIBLE);
                                            correctAnsDisplay.setVisibility(View.GONE);
                                            ans_img_opt.setVisibility(View.VISIBLE);
                                            Glide.with(PracticeTestQAActivity.this)
                                                    .load(correctImg).asBitmap().dontAnimate()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.placeholder)
                                                    .error(R.drawable.placeholder)
                                                    .into(img_CorrectAns);
                                            ans_img_opt.setText(alpha);

                                        } else {
                                            ans_img_opt.setVisibility(View.GONE);
                                            img_CorrectAns.setVisibility(View.GONE);
                                            correctAnsDisplay.setVisibility(View.VISIBLE);
                                            correctAnsDisplay.setText("Correct Ans : " + correct);
                                        }
                                    }*/

                                    previous.setVisibility(View.GONE);
                                    next.setVisibility(View.VISIBLE);

                                    setCorrectAns(0);
                                    //  frame.bringToFront();

                                   /* if (correctAns == null) {
                                        correctAns = "0";
                                    }
                                    givenAns = allArrays.get(0).getThird();
                                    if (givenAns == null) {
                                        givenAns = "0";
                                    }
                                    for (int i = 1; i <= 4; i++) {
                                        int color = 0;
                                        int colorText = 0;

                                        back_opt1.setPadding(15, 0, 15, 0);
                                        back_opt2.setPadding(15, 0, 15, 0);
                                        back_opt3.setPadding(15, 0, 15, 0);
                                        back_opt4.setPadding(15, 0, 15, 0);

                                        opt1.setPadding(15, 0, 0, 0);opt2.setPadding(15, 0, 0, 0);
                                        opt3.setPadding(15, 0, 0, 0);
                                        opt4.setPadding(15, 0, 0, 0);
                                    if (correctAns.equals(i + "") && givenAns.equals(i + "")) {
                                        color = R.drawable.quiz_right;
                                        colorText = R.color.white;
                                    } else if (correctAns.equals(i + "")) {
                                        color = R.drawable.quiz_right_diff;
                                        colorText = R.color.white;
                                    } else
                                        if (givenAns.equals(i + "")) {
                                            color = R.drawable.quiz_box;
                                            colorText = R.color.colorPrimaryDarkbg;
                                        }
                                        if (color != 0) {
                                            if (i == 1) {
                                                if (back_opt1.getVisibility() == View.VISIBLE) {
                                                    back_opt1.setBackgroundResource(color);
                                                    back_opt1.setPadding(15, 0, 15, 0);
                                                } else {
                                                    opt1.setBackgroundResource(color);
                                                    opt1.setTextColor(getResources().getColor(colorText));
                                                    opt1.setPadding(15, 0, 0, 0);}
                                            } else if (i == 2) {
                                                if (back_opt2.getVisibility() == View.VISIBLE) {
                                                    back_opt2.setBackgroundResource(color);
                                                    back_opt2.setPadding(15, 0, 15, 0);
                                                } else {
                                                    opt2.setBackgroundResource(color);
                                                    opt2.setTextColor(getResources().getColor(colorText));
                                                    opt2.setPadding(15, 0, 0, 0);
                                                }
                                            } else if (i == 3) {
                                                if (back_opt3.getVisibility() == View.VISIBLE) {
                                                    back_opt3.setBackgroundResource(color);
                                                    back_opt3.setPadding(15, 0, 15, 0);
                                                } else {
                                                    opt3.setBackgroundResource(color);
                                                    opt3.setTextColor(getResources().getColor(colorText));
                                                    opt3.setPadding(15, 0, 0, 0);
                                                }
                                            } else {
                                                if (back_opt4.getVisibility() == View.VISIBLE) {
                                                    back_opt4.setBackgroundResource(color);
                                                    back_opt4.setPadding(15, 0, 15, 0);
                                                } else {
                                                    opt4.setBackgroundResource(color);
                                                    opt4.setTextColor(getResources().getColor(colorText));
                                                    opt4.setPadding(15, 0, 0, 0);
                                                }
                                            }
                                        }
                                    }*/
                                }
                            }
                        } catch (Exception ex) {
                            frame_progress.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            Constants.writelog("PracticeTestQAActivity", "getQuestion 1382:" + ex.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<AppService> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        frame_progress.setVisibility(View.GONE);
                        Constants.writelog("PracticeTestQAActivity", "getQuestion 1390:" + t.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            Constants.writelog("PracticeTestQAActivity", "getQuestion 1395:" + e.getMessage());
        }
    }

    private void setTestAns() {
        if (isOnline()) {
            long tEnd = System.currentTimeMillis();
            long tDelta = tEnd - tStart;
            if (elapsedSeconds != 0) {
                tdelta = tDelta / 1000;
                elapsedSeconds = elapsedSeconds + tdelta;
            } else {
                elapsedSeconds = tDelta / 1000;
            }
            frame_progress.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            String mStudentId = Datastorage.GetStudentId(getApplicationContext());
            String mSchoolId = Datastorage.GetSchoolId(getApplicationContext());
            String mYearId = Datastorage.GetCurrentYearId(getApplicationContext());
            Call<AppService> call = ((MyApplication) getApplicationContext()).getmRetrofitInterfaceAppService()
                    .SetPracticeTestAns(mStudentId, mSchoolId, mYearId, testId, quesId, String.valueOf(selected), String.valueOf(elapsedSeconds), isLast, Constants.APPname, Constants.CODEVERSION, Constants.PLATFORM);
            call.enqueue(new Callback<AppService>() {
                @Override
                public void onResponse(Call<AppService> call, Response<AppService> response) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        frame_progress.setVisibility(View.GONE);
                        AppService tmps = response.body();
                        if (tmps != null && tmps.getResponse() != null && !tmps.getResponse().isEmpty()
                                && tmps.getResponse().equalsIgnoreCase("1") && !tmps.getStrResult().isEmpty()) {
                            count++;
                            setQuestion(count, listArrays);
                        }
                    } catch (Exception ex) {
                        progressBar.setVisibility(View.GONE);
                        frame_progress.setVisibility(View.GONE);
                        Constants.writelog("PracticeTestQAActivity", "setTestAns 1577:" + ex.getMessage());
                    } finally {
                        selected = 0;
                        tStart = System.currentTimeMillis();
                        elapsedSeconds = 0;
                    }
                }

                @Override
                public void onFailure(Call<AppService> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    frame_progress.setVisibility(View.GONE);
                    Constants.writelog("PracticeTestQAActivity", "setTestAns 1589:" + t.getMessage());
                }
            });
        }
    }

    private void setCorrectAns(int setAnsCount) {
        opt_1 = allArrays.get(setAnsCount).getSeventh();
        opt_2 = allArrays.get(setAnsCount).getEighth();
        opt_3 = allArrays.get(setAnsCount).getNineth();
        opt_4 = allArrays.get(setAnsCount).getTenth();

        if (isURL(opt_1)) {
            back_opt1.setVisibility(View.VISIBLE);
            opt1.setVisibility(View.GONE);

            back_opt1.setBackgroundResource(R.drawable.quiz_opt);
            getImageSize(opt_1, "opt_1");
           /* Glide.with(PracticeTestQAActivity.this)
                    .load(opt_1).asBitmap().dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                   *//* .placeholder(R.drawable.placeholder)*//*
                    .error(R.drawable.placeholder)
                    .into(img_opt1);*/
            //test
         /*   Picasso.with(PracticeTestQAActivity.this).load(opt_1)
                    .placeholder(R.drawable.placeholder)
                    .into(img_opt1);*/
            option_no_1.setText("[A] ");

        } else {
            opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
            back_opt1.setVisibility(View.GONE);
            opt1.setVisibility(View.VISIBLE);
            opt1.setBackgroundResource(R.drawable.quiz_opt);
            opt1.setText("[A] " + allArrays.get(setAnsCount).getSeventh());
        }

        if (isURL(opt_2)) {
            back_opt2.setVisibility(View.VISIBLE);
            opt2.setVisibility(View.GONE);

            back_opt2.setBackgroundResource(R.drawable.quiz_opt);
            getImageSize(opt_2, "opt_2");
            /*Glide.with(PracticeTestQAActivity.this)
                    .load(opt_2).asBitmap().dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(img_opt2);*/
            option_no_2.setText("[B] ");
        } else {
            opt2.setVisibility(View.VISIBLE);
            opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
            back_opt2.setVisibility(View.GONE);
            opt2.setBackgroundResource(R.drawable.quiz_opt);
            opt2.setText("[B] " + allArrays.get(setAnsCount).getEighth());
        }

        if (isURL(opt_3)) {
            back_opt3.setVisibility(View.VISIBLE);
            opt3.setVisibility(View.GONE);

            back_opt3.setBackgroundResource(R.drawable.quiz_opt);
            getImageSize(opt_3, "opt_3");
            //test
           /* Glide.with(PracticeTestQAActivity.this)
                    .load(opt_3).asBitmap().dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(img_opt3);*/
            option_no_3.setText("[C] ");
        } else {
            opt3.setVisibility(View.VISIBLE);
            opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
            back_opt3.setVisibility(View.GONE);
            opt3.setBackgroundResource(R.drawable.quiz_opt);
            opt3.setText("[C] " + allArrays.get(setAnsCount).getNineth());
        }

        if (isURL(opt_4)) {
            back_opt4.setVisibility(View.VISIBLE);
            opt4.setVisibility(View.GONE);
            back_opt4.setBackgroundResource(R.drawable.quiz_opt);
            getImageSize(opt_4, "opt_4");
            //test
            /*Glide.with(PracticeTestQAActivity.this)
                    .load(opt_4).asBitmap().dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(img_opt4);*/
            option_no_4.setText("[D] ");
        } else {
            opt4.setVisibility(View.VISIBLE);
            opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
            back_opt4.setVisibility(View.GONE);
            opt4.setBackgroundResource(R.drawable.quiz_opt);
            opt4.setText("[D] " + allArrays.get(setAnsCount).getTenth());
        }
        String correct = "";
        /* String correctImg = "";*/
        String alpha = "";
        if (allArrays.get(setAnsCount).getFourth().equals("1")) {
            correctImg = allArrays.get(setAnsCount).getSeventh();
            alpha = "Correct Ans : [A] ";
            correct = "[A] " + allArrays.get(setAnsCount).getSeventh();
        } else if (allArrays.get(setAnsCount).getFourth().equals("2")) {
            correctImg = allArrays.get(setAnsCount).getEighth();
            alpha = "Correct Ans : [B] ";
            correct = "[B] " + allArrays.get(setAnsCount).getEighth();
        } else if (allArrays.get(setAnsCount).getFourth().equals("3")) {
            correctImg = allArrays.get(setAnsCount).getNineth();
            alpha = "Correct Ans : [C] ";
            correct = "[C] " + allArrays.get(setAnsCount).getNineth();
        } else if (allArrays.get(setAnsCount).getFourth().equals("4")) {
            correctImg = allArrays.get(setAnsCount).getTenth();
            alpha = "Correct Ans : [D] ";
            correct = "[D] " + allArrays.get(setAnsCount).getTenth();
        }

        if (correctImg != "") {
            if (isURL(correctImg)) {
                img_CorrectAns.setVisibility(View.VISIBLE);
                correctAnsDisplay.setVisibility(View.GONE);
                ans_img_opt.setVisibility(View.VISIBLE);
                getImageSize(correctImg, "correctAns");
                //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(correctImg).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_CorrectAns);*/
                ans_img_opt.setText(alpha);

            } else {
                ans_img_opt.setVisibility(View.GONE);
                img_CorrectAns.setVisibility(View.GONE);
                correctAnsDisplay.setVisibility(View.VISIBLE);
                correctAnsDisplay.setText("Correct Ans : " + correct);
            }
        }

        /*previous.setVisibility(View.GONE);
        next.setVisibility(View.VISIBLE);*/
        //  frame.bringToFront();
        if (correctAns == null) {
            correctAns = "0";
        }
        givenAns = allArrays.get(setAnsCount).getThird();
        if (givenAns == null) {
            givenAns = "0";
        }
        for (int i = 1; i <= 4; i++) {
            int color = 0;
            int colorText = 0;

            back_opt1.setPadding(15, 0, 15, 0);
            back_opt2.setPadding(15, 0, 15, 0);
            back_opt3.setPadding(15, 0, 15, 0);
            back_opt4.setPadding(15, 0, 15, 0);

            opt1.setPadding(15, 0, 0, 0);
            opt2.setPadding(15, 0, 0, 0);
            opt3.setPadding(15, 0, 0, 0);
            opt4.setPadding(15, 0, 0, 0);
                                   /* if (correctAns.equals(i + "") && givenAns.equals(i + "")) {
                                        color = R.drawable.quiz_right;
                                        colorText = R.color.white;
                                    } else if (correctAns.equals(i + "")) {
                                        color = R.drawable.quiz_right_diff;
                                        colorText = R.color.white;
                                    } else */
            if (givenAns.equals(i + "")) {
                color = R.drawable.quiz_box;
                colorText = R.color.colorPrimaryDarkbg;
            }
            if (color != 0) {
                if (i == 1) {
                    if (back_opt1.getVisibility() == View.VISIBLE) {
                        back_opt1.setBackgroundResource(color);
                        back_opt1.setPadding(15, 0, 15, 0);
                    } else {
                        opt1.setBackgroundResource(color);
                        opt1.setTextColor(getResources().getColor(colorText));
                        opt1.setPadding(15, 0, 0, 0);
                    }
                } else if (i == 2) {
                    if (back_opt2.getVisibility() == View.VISIBLE) {
                        back_opt2.setBackgroundResource(color);
                        back_opt2.setPadding(15, 0, 15, 0);
                    } else {
                        opt2.setBackgroundResource(color);
                        opt2.setTextColor(getResources().getColor(colorText));
                        opt2.setPadding(15, 0, 0, 0);
                    }
                } else if (i == 3) {
                    if (back_opt3.getVisibility() == View.VISIBLE) {
                        back_opt3.setBackgroundResource(color);
                        back_opt3.setPadding(15, 0, 15, 0);
                    } else {
                        opt3.setBackgroundResource(color);
                        opt3.setTextColor(getResources().getColor(colorText));
                        opt3.setPadding(15, 0, 0, 0);
                    }
                } else {
                    if (back_opt4.getVisibility() == View.VISIBLE) {
                        back_opt4.setBackgroundResource(color);
                        back_opt4.setPadding(15, 0, 15, 0);
                    } else {
                        opt4.setBackgroundResource(color);
                        opt4.setTextColor(getResources().getColor(colorText));
                        opt4.setPadding(15, 0, 0, 0);
                    }
                }
            }
        }
    }

    private void setQuestion(int count, List<AppService.ListArray> array) {
        if (array.size() > 0 && array.size() > count) {
            if (count == array.size() - 1) {
                isLast = "1";
            }
            //   frame.bringToFront();
            quesId = array.get(count).getFirst();
            correctAns = array.get(count).getFourth();
            //    quesNo.setText(listArrays.get(count).getSixth());
            question = array.get(count).getSecond();
            if (isURL(question)) {
                img_ques.setVisibility(View.VISIBLE);
                text_ques.setVisibility(View.GONE);
                card_img_ques.setVisibility(View.VISIBLE);
                int height = getImageSize(question, "que");

                //  TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, height);
                // img_ques.setLayoutParams(layoutParams);
                //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(question).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_ques);*/
                ques_no_img.setText(array.get(count).getSixth());
            } else {
                img_ques.setVisibility(View.GONE);
                text_ques.setVisibility(View.VISIBLE);
                card_img_ques.setVisibility(View.GONE);
                ques.setText(array.get(count).getSixth() + " " + array.get(count).getSecond());
            }
            //  ques.setText(listArrays.get(count).getSixth() + " " + listArrays.get(count).getSecond());

            opt_1 = array.get(count).getSeventh();
            opt_2 = array.get(count).getEighth();
            opt_3 = array.get(count).getNineth();
            opt_4 = array.get(count).getTenth();

            if (isURL(opt_1)) {
                back_opt1.setVisibility(View.VISIBLE);
                opt1.setVisibility(View.GONE);
                back_opt1.setBackgroundResource(R.drawable.quiz_opt);
                getImageSize(opt_1, "opt_1");
                //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(opt_1).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_opt1);*/
                back_opt1.setPadding(15, 0, 15, 0);
                option_no_1.setText("[A] ");
            } else {
                opt1.setTextColor(getResources().getColor(R.color.colorPrimary));
                back_opt1.setVisibility(View.GONE);
                opt1.setVisibility(View.VISIBLE);
                opt1.setBackgroundResource(R.drawable.quiz_opt);
                opt1.setText("[A] " + array.get(count).getSeventh());
                opt1.setPadding(15, 0, 0, 0);
            }

            if (isURL(opt_2)) {
                back_opt2.setVisibility(View.VISIBLE);
                opt2.setVisibility(View.GONE);

                back_opt2.setBackgroundResource(R.drawable.quiz_opt);
                getImageSize(opt_2, "opt_2");
                //test
                /*Glide.with(PracticeTestQAActivity.this)
                        .load(opt_2).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_opt2);*/
                back_opt2.setPadding(15, 0, 15, 0);
                option_no_2.setText("[B] ");
            } else {
                opt2.setVisibility(View.VISIBLE);
                opt2.setTextColor(getResources().getColor(R.color.colorPrimary));
                back_opt2.setVisibility(View.GONE);
                opt2.setBackgroundResource(R.drawable.quiz_opt);
                opt2.setText("[B] " + array.get(count).getEighth());
                opt2.setPadding(15, 0, 0, 0);
            }

            if (isURL(opt_3)) {
                back_opt3.setVisibility(View.VISIBLE);
                opt3.setVisibility(View.GONE);
                back_opt3.setBackgroundResource(R.drawable.quiz_opt);
                getImageSize(opt_3, "opt_3");
                //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(opt_3).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_opt3);*/
                back_opt3.setPadding(15, 0, 15, 0);
                option_no_3.setText("[C] ");
            } else {
                opt3.setVisibility(View.VISIBLE);
                opt3.setTextColor(getResources().getColor(R.color.colorPrimary));
                back_opt3.setVisibility(View.GONE);
                opt3.setBackgroundResource(R.drawable.quiz_opt);
                opt3.setText("[C] " + array.get(count).getNineth());
                opt3.setPadding(15, 0, 0, 0);
            }

            if (isURL(opt_4)) {
                back_opt4.setVisibility(View.VISIBLE);
                opt4.setVisibility(View.GONE);
                back_opt4.setBackgroundResource(R.drawable.quiz_opt);
                getImageSize(opt_4, "opt_4");
                //test
              /*  Glide.with(PracticeTestQAActivity.this)
                        .load(opt_4).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(img_opt4);*/
                back_opt4.setPadding(15, 0, 15, 0);
                option_no_4.setText("[D]");
            } else {
                opt4.setVisibility(View.VISIBLE);
                opt4.setTextColor(getResources().getColor(R.color.colorPrimary));
                back_opt4.setVisibility(View.GONE);
                opt4.setBackgroundResource(R.drawable.quiz_opt);
                opt4.setText("[D] " + array.get(count).getTenth());
                opt4.setPadding(15, 0, 0, 0);
            }

            tStart = System.currentTimeMillis();
        } else {
            Intent i = new Intent(PracticeTestQAActivity.this, PracticeTestResultActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("testId", testId);
            i.putExtra("testName", testName);
            i.putExtra("refereshflag", "1");
            startActivity(i);
            PracticeTestQAActivity.this.finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        long tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        if (elapsedSeconds != 0) {
            tdelta = tDelta / 1000;
            elapsedSeconds = elapsedSeconds + tdelta;
        } else {
            elapsedSeconds = tDelta / 1000;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tStart = System.currentTimeMillis();
    }

    private boolean isURL(String urlString) {
        try {
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches();
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    super.onBackPressed();
                    if (fromCompleted != "" && fromCompleted.equals("1")) {
                        ((MyApplication) PracticeTestQAActivity.this
                                .getApplicationContext()).getLocalBroadcastInstance().sendBroadcast(
                                new Intent(new Intent(PracticeTestQAActivity.this
                                        .getResources().getString(R.string.broadcast_feedback_key))));
                    }
                    onBackClickAnimation();
                    break;
            }
        } catch (Exception ex) {
            Constants.writelog("onOptionsItemSelected 177:", ex.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }


    private class DownloadTask extends AsyncTask<String, Void, Bitmap> {
        String isFrom = "";

        // Before the tasks execution
        protected void onPreExecute() {
            // Display the progress dialog on async task start
            //   mProgressDialog.show();
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(String... urls) {
            URL url = null;
            try {
                url = new URL(urls[0]);
                isFrom = urls[1];
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection connection = null;

            try {
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                //setImageSize(bmp);
                // Return the downloaded bitmap
                return bmp;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result) {
            // Hide the progress dialog
            //mProgressDialog.dismiss();

            if (result != null) {
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int newWidth = size.x;

//Get actual width and height of image
                int width = result.getWidth();
                int height = result.getHeight();


                float cal = (float) newWidth / width;
                after_cal = Math.round(cal * height);

// Calculate the ratio between height and width of Original Image
                float ratio = (float) height / (float) width;
                float scale = getApplicationContext().getResources().getDisplayMetrics().density;
                int newHeight = Math.round((width * ratio) / scale);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, after_cal);
                if (isFrom.equalsIgnoreCase("que")) {
                    img_ques.setLayoutParams(layoutParams);
                    Glide.with(PracticeTestQAActivity.this)
                            .load(question).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(img_ques);
                } else if (isFrom.equalsIgnoreCase("opt_1")) {
                    img_opt1.setLayoutParams(layoutParams);
                    Glide.with(PracticeTestQAActivity.this)
                            .load(opt_1).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(img_opt1);
                } else if (isFrom.equalsIgnoreCase("opt_2")) {
                    img_opt2.setLayoutParams(layoutParams);
                    Glide.with(PracticeTestQAActivity.this)
                            .load(opt_2).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(img_opt2);
                } else if (isFrom.equalsIgnoreCase("opt_3")) {
                    img_opt3.setLayoutParams(layoutParams);
                    Glide.with(PracticeTestQAActivity.this)
                            .load(opt_3).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(img_opt3);
                } else if (isFrom.equalsIgnoreCase("opt_4")) {
                    img_opt4.setLayoutParams(layoutParams);
                    Glide.with(PracticeTestQAActivity.this)
                            .load(opt_4).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(img_opt4);
                } else if (isFrom.equalsIgnoreCase("correctAns")) {
                    img_CorrectAns.setLayoutParams(layoutParams);
                    Glide.with(PracticeTestQAActivity.this)
                            .load(correctImg).asBitmap().dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.placeholder)
                            .into(img_CorrectAns);
                }
            } else {
                // Notify user that an error occurred while downloading image

            }

        }
    }


    public int getImageSize(String urlImage, String isFrom) {
        try {
            Bitmap bitmap = null;
            try {
                new DownloadTask().execute(urlImage, isFrom);
            } catch (Exception e) {
                System.out.println(e);
            }
         /*   Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int newWidth = size.x;

//Get actual width and height of image
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

// Calculate the ratio between height and width of Original Image
            float ratio = (float) height / (float) width;
            float scale = getApplicationContext().getResources().getDisplayMetrics().density;
            int newHeight = Math.round((width * ratio) / scale);
            return newHeight;*/
        } catch (Exception e) {
            Log.e("getImageSize 1896", e.getMessage());
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_ques:
                setAlertZoom(question);
              /*  mBuilder = new AlertDialog.Builder(PracticeTestQAActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                ImageView photoView = mView.findViewById(R.id.imageView);
                Glide.with(PracticeTestQAActivity.this)
                        .load(question).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(photoView);
                // photoView.setImageURI(Integer.parseInt(question));
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();*/
                break;
            case R.id.img_opt1:
            case R.id.back_opt1:
                setAlertZoom(opt_1);
               /* mBuilder = new AlertDialog.Builder(PracticeTestQAActivity.this);
                mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                photoView = mView.findViewById(R.id.imageView);
                Glide.with(PracticeTestQAActivity.this)
                        .load(opt_1).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(photoView);
                // photoView.setImageURI(Integer.parseInt(question));
                mBuilder.setView(mView);
                mDialog = mBuilder.create();
                mDialog.show();*/
                //   Toast.makeText(this, "opt1 clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_opt2:
            case R.id.back_opt2:
                setAlertZoom(opt_2);
              /*  mBuilder = new AlertDialog.Builder(PracticeTestQAActivity.this);
                mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                photoView = mView.findViewById(R.id.imageView);
                Glide.with(PracticeTestQAActivity.this)
                        .load(opt_2).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(photoView);
                // photoView.setImageURI(Integer.parseInt(question));
                mBuilder.setView(mView);
                mDialog = mBuilder.create();
                mDialog.show();*/

                // Toast.makeText(this, "opt1 clicked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_opt3:
            case R.id.back_opt3:
                setAlertZoom(opt_3);
              /*  mBuilder = new AlertDialog.Builder(PracticeTestQAActivity.this);
                mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                photoView = mView.findViewById(R.id.imageView);
                Glide.with(PracticeTestQAActivity.this)
                        .load(opt_3).asBitmap().dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.placeholder)
                        .into(photoView);
                // photoView.setImageURI(Integer.parseInt(question));
                mBuilder.setView(mView);
                mDialog = mBuilder.create();
                mDialog.show();*/
                // Toast.makeText(this, "opt1 clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_opt4:
            case R.id.back_opt4:
                setAlertZoom(opt_4);
                //Toast.makeText(this, "opt1 clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_CorrectAns:
                setAlertZoom(correctImg);
        }
    }

    public void setAlertZoom(String imgUrl) {
        mBuilder = new AlertDialog.Builder(PracticeTestQAActivity.this);
        mView = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
        photoView = mView.findViewById(R.id.imageView);
        Glide.with(PracticeTestQAActivity.this)
                .load(imgUrl).asBitmap().dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(photoView);
        // photoView.setImageURI(Integer.parseInt(question));
        mBuilder.setView(mView);
        mDialog = mBuilder.create();
        mDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            if (fromCompleted != "" && fromCompleted.equals("1")) {
                ((MyApplication) PracticeTestQAActivity.this
                        .getApplicationContext()).getLocalBroadcastInstance().sendBroadcast(
                        new Intent(new Intent(PracticeTestQAActivity.this
                                .getResources().getString(R.string.broadcast_feedback_key))));
            }
            PracticeTestQAActivity.this.finish();
        } catch (Exception e) {
            Constants.writelog("PracticeTestQAActivity", "onBackPressed 740:" + e.getMessage());
        }

    }


}
