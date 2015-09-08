package com.popo.dailyopen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.popo.dailyopen.Def;
import com.popo.dailyopen.OpenDay;
import com.popo.dailyopen.R;
import com.popo.dailyopen.RecordHelper;

public class DetailActivity extends Activity {

	private OpenDay mOpenDay;
	private RecordHelper mRecordHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		mRecordHelper = new RecordHelper(getApplicationContext());

		Intent intent = getIntent();
		Bundle data = intent.getBundleExtra(Def.START_DETAILACTIVITY_BUNDLE);
		String date = data.getString(Def.BundleKey.OPENDAY_DATE);
		mOpenDay = mRecordHelper.getOneDayRecord(date);
		
		initView();
		setViewValue();
	}
	
	private void initView(){
		
	}
	
	private void setViewValue(){
		
	}
}
