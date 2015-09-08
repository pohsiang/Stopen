package com.popo.dailyopen.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.popo.dailyopen.Def;
import com.popo.dailyopen.GoogleAnalyticController;
import com.popo.dailyopen.R;

public class SettingsActivity extends Activity {

    private static String TAG = "SettingsActivity";
    private Switch mHitNotificationSwitch = null;
    private GoogleAnalyticController mGoogleAnalyticController;
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mSettingEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //init settings
        mSettings = getSharedPreferences(Def.APP_NAME, MODE_PRIVATE);
        mSettingEditor = mSettings.edit();

        mGoogleAnalyticController = GoogleAnalyticController.getInstance(getApplicationContext());

        initView();
        setView();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleAnalyticController.sendScreenViewHit(Def.GA.INTERACTION.USER, Def.GA.Screen.SETTING_ACTIVITY);
    }


    private void initView(){
        mHitNotificationSwitch = (Switch)findViewById(R.id.settings_hits_notification_switch);
    }

    private void setView(){
        boolean popNotification = mSettings.getBoolean(Def.Setting.POP_HITS_NOTIFICATION, true);
        mHitNotificationSwitch.setChecked(popNotification);
    }

    private void setListener(){
        mHitNotificationSwitch.setOnCheckedChangeListener(mHitNotificationOnCheckedChangeListener);
    }

    private Switch.OnCheckedChangeListener mHitNotificationOnCheckedChangeListener = new Switch.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mSettingEditor.putBoolean(Def.Setting.POP_HITS_NOTIFICATION, isChecked);
            mSettingEditor.commit();
        }
    };

}
