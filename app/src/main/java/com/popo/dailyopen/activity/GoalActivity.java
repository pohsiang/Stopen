package com.popo.dailyopen.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.popo.dailyopen.Def;
import com.popo.dailyopen.GoogleAnalyticController;
import com.popo.dailyopen.R;

public class GoalActivity extends Activity {

    private static String TAG = "GoalActivity";
    private View mMiddleView;
    private TextView mLimitHitsTextView;
    private GoogleAnalyticController mGoogleAnalyticController;
    private SeekBar mLimitHitsProgressbar;
    private Button mConfirmButton;
    private int mSeekBarProgressNumber;
    private boolean isShowConfirmButton;
    private boolean isForSetting;
    private SharedPreferences mSetting;
    private SharedPreferences.Editor mSettingEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        mGoogleAnalyticController = GoogleAnalyticController.getInstance(getApplicationContext());
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Def.DoIntent.INTENT_GOAL_ACTIVITY);
        if (bundle != null) {
            isShowConfirmButton = bundle.getBoolean(Def.DoIntent.INTENT_GOAL_ACTIVITY_SHOW_CONFIRM_BUTTON, false);
            isForSetting = bundle.getBoolean(Def.DoIntent.INTENT_FOR_SETTING, false);
        }else{
            isShowConfirmButton = false;
        }
        mSetting = getSharedPreferences(Def.APP_NAME, MODE_PRIVATE);
        mSettingEditor = mSetting.edit();
        // app status
        if (!isForSetting) {
            int app_status = mSetting.getInt(Def.Setting.USE_APP_STATUS, Def.APP_STATUS.SET_GOAL_NOT_YET);
            if (app_status == Def.APP_STATUS.SET_GOAL_ALREADY) {
                startMainActivity();
            }
        }

        initView();
        setView();
        setViewListener();
    }

    protected void onResume(){
        super.onResume();
        mGoogleAnalyticController.sendScreenViewHit(Def.GA.INTERACTION.USER, Def.GA.Screen.GOAL_ACTIVITY);
        mSeekBarProgressNumber = mSetting.getInt(Def.Setting.LIMIT_HITS, 0);
        mLimitHitsTextView.setText(String.valueOf(mSeekBarProgressNumber));
        mLimitHitsProgressbar.setProgress(mSeekBarProgressNumber);
    }


    protected void onStop(){
        super.onStop();
    }

    private void initView(){
        mMiddleView = findViewById(R.id.goalactivity_middle_view);
        mLimitHitsTextView = (TextView)mMiddleView.findViewById(R.id.goalactivity_limit_hits_textview);
        mLimitHitsProgressbar = (SeekBar)mMiddleView.findViewById(R.id.goalactivity_set_goal_progress_bar);
        mConfirmButton = (Button)findViewById(R.id.goalactivity_confirm_button);
    }

    private void setView(){
        mLimitHitsProgressbar.getThumb().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        if(isShowConfirmButton){
            mConfirmButton.setVisibility(View.VISIBLE);
        }
    }

    private void setViewListener(){
        mLimitHitsProgressbar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        mConfirmButton.setOnClickListener(mConfirmButtonOnClickListener);
    }

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mLimitHitsTextView.setText(String.valueOf(progress));
            mSeekBarProgressNumber = progress;
            if(progress == 0){
                mConfirmButton.setVisibility(View.INVISIBLE);
            }else{
                mConfirmButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private Button.OnClickListener mConfirmButtonOnClickListener = new Button.OnClickListener(){

        public void onClick(View view){
            if(!isForSetting) {
                startMainActivity();
            }else{
                finish();
            }
            mSettingEditor.putInt(Def.Setting.USE_APP_STATUS, Def.APP_STATUS.SET_GOAL_ALREADY);
            mSettingEditor.putInt(Def.Setting.LIMIT_HITS, mSeekBarProgressNumber);
            mSettingEditor.commit();
            mGoogleAnalyticController.sendEventHit(Def.GA.INTERACTION.USER, Def.GA.Category.TRACE, Def.GA.Action.SET_GOAL_ACTION, Def.GA.Label.VALUE, mSeekBarProgressNumber);
            Log.i(TAG, "Confirm");
        }

    };

    private void startMainActivity(){
        Intent startMainActivityIntent = new Intent();
        startMainActivityIntent.setClass(getApplicationContext(), MainActivity.class);
        startActivity(startMainActivityIntent);
    }




}
