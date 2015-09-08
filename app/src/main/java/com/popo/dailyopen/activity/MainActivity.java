package com.popo.dailyopen.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.popo.dailyopen.DailyOpenListAdapter;
import com.popo.dailyopen.DailyOpenService;
import com.popo.dailyopen.Def;
import com.popo.dailyopen.GoogleAnalyticController;
import com.popo.dailyopen.OpenDay;
import com.popo.dailyopen.R;
import com.popo.dailyopen.RecordHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class MainActivity extends Activity {

	private String TAG = "MainActivity";
    private GoogleAnalyticController mGoogleAnalyticController;
	private ListView mOpenDayListView;
    private View mNoRecordView;
	private RecordHelper mRecordHelper;
	private DailyOpenListAdapter mDailyOpenListAdapter;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Date mToday;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "OnCreate");
        mGoogleAnalyticController = GoogleAnalyticController.getInstance(getApplicationContext());
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mToday = new Date(System.currentTimeMillis());
		mRecordHelper = new RecordHelper(getApplicationContext());
		initView();
		startDailyOpenService();
	}

	public void onResume(){
		super.onResume();
        mGoogleAnalyticController.sendScreenViewHit(Def.GA.INTERACTION.USER, Def.GA.Screen.MAIN_ACTIVITY);
        setViewValue();
	}

    public void onDestroy(){
        super.onDestroy();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.menu_action_set_goal) {
			Intent startGoalActivityIntent = new Intent();
            startGoalActivityIntent.setClass(getApplicationContext(), GoalActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(Def.DoIntent.INTENT_GOAL_ACTIVITY_SHOW_CONFIRM_BUTTON, true);
            bundle.putBoolean(Def.DoIntent.INTENT_FOR_SETTING, true);
            startGoalActivityIntent.putExtra(Def.DoIntent.INTENT_GOAL_ACTIVITY, bundle);
            startActivity(startGoalActivityIntent);
		}

        if (id == R.id.menu_settings) {
            Intent startSettingActivityIntent = new Intent();
            startSettingActivityIntent.setClass(getApplicationContext(), SettingsActivity.class);
            startActivity(startSettingActivityIntent);
        }
		return super.onOptionsItemSelected(item);
	}

	private void startDailyOpenService() {
		Intent service = new Intent(getApplicationContext(),
				DailyOpenService.class);
		startService(service);
	}
	
	private void initView(){
		mOpenDayListView = (ListView)findViewById(R.id.mainactivity_openday_listview);
        mNoRecordView = (View)findViewById(R.id.mainactivity_no_record_view);
	}
	
	private void setViewValue(){
        Log.v(TAG, "setViewValue");
		String todayId = mDateFormat.format(mToday);
		LinkedList<OpenDay> openDaies = mRecordHelper.getHistoryRecord();
		//Log.v(TAG, "openDaies:"+openDaies.size());
		if (openDaies != null && openDaies.size()!=0){
			mDailyOpenListAdapter = new DailyOpenListAdapter(getApplicationContext(), openDaies);
			mOpenDayListView.setAdapter(mDailyOpenListAdapter);
            mNoRecordView.setVisibility(View.GONE);
		}else{
            mNoRecordView.setVisibility(View.VISIBLE);
        }
		

	}
}
