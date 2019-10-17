package com.expedite.apps.nalanda.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.expedite.apps.nalanda.BaseActivity;
import com.expedite.apps.nalanda.R;
import com.expedite.apps.nalanda.common.Common;
import com.expedite.apps.nalanda.constants.Constants;

import java.io.IOException;

public class CurriculumMediaPlayerActivity extends BaseActivity {
    private MediaPlayer mMediaPlayer;
    private SeekBar mSeekBar;
    private ProgressBar mProgressbar;
    private TextView mTotalTime, mCurrentTime;
    private boolean mIsMediaPlayerPrepare = false;
    private String mUrl = "", mId = "", mName = "", Tag = "CurriculumMediaPlayerActivity";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player_layout);
        try {
            if (getIntent() != null && getIntent().getExtras() != null) {
                mId = getIntent().getExtras().getString("Id", "");
                mUrl = getIntent().getExtras().getString("Url", "");
                mName = getIntent().getExtras().getString("Name", "");
                Common.showLog("ImageUrl:", mUrl);
            }
            init();
        } catch (Exception ex) {
            Constants.writelog(Tag, "MSG 144:" + ex.getMessage());
        }
    }

    public void init() {
        try {
            Constants.setActionbar(getSupportActionBar(), this, getApplicationContext(), mName, mName);
            mMediaPlayer = new MediaPlayer();
            mProgressbar = (ProgressBar) findViewById(R.id.progressBar);
            mProgressbar.setVisibility(View.VISIBLE);
            mSeekBar = (SeekBar) findViewById(R.id.seekBar);
            mTotalTime = (TextView) findViewById(R.id.totalTime);
            mCurrentTime = (TextView) findViewById(R.id.currentTime);
            mSeekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            mSeekBar.getThumb().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            mMediaPlayer.setDataSource(mUrl);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
                public void onPrepared(final MediaPlayer mp) {
                    mIsMediaPlayerPrepare = true;
                    mp.start();
                    mRunnable.run();
                    mProgressbar.setVisibility(View.INVISIBLE);
                }
            });

        } catch (IOException e) {
            CurriculumMediaPlayerActivity.this.finish();
            Toast.makeText(this, "File not found.", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (mMediaPlayer != null) {
                    mMediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
                        @Override
                        public void onBufferingUpdate(MediaPlayer mp, int percent) {
                        }
                    });
                    if (mIsMediaPlayerPrepare) {
                        int mDuration = mMediaPlayer.getDuration();
                        mSeekBar.setMax(mDuration);
                        mTotalTime.setText(getTimeDisplayString(mDuration));
                    }
                    mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                    mCurrentTime.setText(getTimeDisplayString(mMediaPlayer.getCurrentPosition()));
                    mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {}

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {}

                        @Override
                        public void onProgressChanged(SeekBar seekBar,
                                                      int progress, boolean fromUser) {
                            try {
                                if (mMediaPlayer != null && fromUser) {
                                    mMediaPlayer.seekTo(progress);
                                }
                            } catch (Exception ex) {
                                Constants.writelog(Tag, "MSG 112:" + ex.getMessage());
                            }
                        }
                    });
                }
                mHandler.postDelayed(this, 10);
            } catch (Exception ex) {
                Constants.writelog(Tag, "MSG 120:" + ex.getMessage());
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case android.R.id.home:
                    CurriculumMediaPlayerActivity.this.finish();
                    onBackClickAnimation();
            }
        } catch (Exception ex) {
            Constants.writelog(Tag, "MSG 126:" + ex.getMessage());
        }
        return super.onOptionsItemSelected(item);
    }

    public void play(View view) {
        try {
            mMediaPlayer.start();
            findViewById(R.id.pause).setVisibility(View.VISIBLE);
            findViewById(R.id.play).setVisibility(View.GONE);
        } catch (Exception ex) {
            Constants.writelog(Tag, "MSG 137:" + ex.getMessage());
        }
    }

    public void pause(View view) {
        try {
            mMediaPlayer.pause();
            findViewById(R.id.pause).setVisibility(View.GONE);
            findViewById(R.id.play).setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            Constants.writelog(Tag, "MSG 147:" + ex.getMessage());
        }
    }

    @Override
    protected void onPause() {
        try {
            mMediaPlayer.pause();
            findViewById(R.id.pause).setVisibility(View.GONE);
            findViewById(R.id.play).setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            Constants.writelog(Tag, "MSG 169:" + ex.getMessage());
        }
        super.onPause();
    }

    public void stop(View view) {
        try {
            mMediaPlayer.seekTo(0);
            mMediaPlayer.pause();
        } catch (Exception ex) {
            Constants.writelog(Tag, "MSG 144:" + ex.getMessage());
        }
    }

  /*  @Override
    protected void onStop() {
        try {
            mMediaPlayer.seekTo(0);
            mMediaPlayer.pause();
            super.onStop();
        } catch (Exception ex) {
            Constants.writelog(Tag, "190 144:" + ex.getMessage());
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        CurriculumMediaPlayerActivity.this.finish();
        onBackClickAnimation();

    }

    private String getTimeDisplayString(long millis) {
        StringBuffer buf = new StringBuffer();

        long hours = millis / (1000 * 60 * 60);
        long minutes = (millis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = ((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000;

        buf.append(String.format("%02d", hours)).append(":")
                .append(String.format("%02d", minutes)).append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }
}