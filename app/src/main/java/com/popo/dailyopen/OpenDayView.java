package com.popo.dailyopen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.popo.dailyopen.activity.DetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OpenDayView {

    private static String TAG = "OpenDayView";
    private static int UPDATE_PROGRESS_NUMBER = 1;
    private static int PROGRESS_ANIMATION_PERIOD = 1000;
    private SharedPreferences mSetting;
    private AnimateProgressNumberTask mAnimateProgressNumberTask;
	private Context mContext;
	private OpenDay mOpenDay;
	private View mDayView;
	private View mDayBackView;
	private TextView mDate, mNumber, mAllTime, mHitText;
	private ProgressBar mProgressBar;
	private OnClickListener mOpenDetailActivityClickListener;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private Date mToday = null;
	private String mTodayString;
	public OpenDayView(Context context, OpenDay openday){
		mContext = context;
		mOpenDay = openday;
		mTodayString = mDateFormat.format(new Date(System.currentTimeMillis()));
        mSetting = mContext.getSharedPreferences(Def.APP_NAME, Context.MODE_PRIVATE);
		if (mOpenDay.getDate().equals(mTodayString)){
			initTodayView();
			setTodayViewValue();
            Log.v(TAG, "setTodayView");
		}else{
			initHistoryView();
			setHistoryViewValue();
            Log.v(TAG, "setHistoryView");
		}
	}
	
	private void initTodayView(){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mDayView = inflater.inflate(R.layout.openday_today_view, null);
		mDayBackView = (View)mDayView.findViewById(R.id.opendayview);
		mDate = (TextView)mDayView.findViewById(R.id.opendayview_date_text);
		mNumber = (TextView)mDayView.findViewById(R.id.opendayview_number_text);
		mAllTime = (TextView)mDayView.findViewById(R.id.opendayview_alltime_text);
        mHitText = (TextView)mDayView.findViewById(R.id.opendayview_number_hit_text);
		mProgressBar = (ProgressBar)mDayView.findViewById(R.id.opendayview_number_progressBar);
	}
	
	private void initHistoryView(){
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mDayView = inflater.inflate(R.layout.openday_history_view, null);
		mDayBackView = (View)mDayView.findViewById(R.id.opendayview);
		mDate = (TextView)mDayView.findViewById(R.id.opendayview_date_text);
		mNumber = (TextView)mDayView.findViewById(R.id.opendayview_number_text);
		mAllTime = (TextView)mDayView.findViewById(R.id.opendayview_alltime_text);
        mHitText = (TextView)mDayView.findViewById(R.id.opendayview_number_hit_text);
	}
	
	private void setTodayViewValue(){
        mNumber.setText("0");

		mProgressBar.setMax(mSetting.getInt(Def.Setting.LIMIT_HITS, 500));

        Log.v(TAG, "setTodayViewValue:"+String.valueOf(mOpenDay.getNumberOfTime()));
        mAnimateProgressNumberTask = new AnimateProgressNumberTask(mAnimateProgressNumberHandler, mOpenDay.getNumberOfTime());
        mAnimateProgressNumberTask.start();

        if (mOpenDay.getNumberOfTime() > 1){
            mHitText.setText(mContext.getString(R.string.hits_string));
        }

		if (mOpenDay.getDate().equals(mTodayString)){
			mDate.setText(mContext.getResources().getString(R.string.today_string));
		}else{
			mDate.setText(changeDateFormat(mOpenDay.getDate()));
		}
		long hour = Util.getHours(mOpenDay.getAllMonitorOnTime());
		long min = Util.getMinutes(mOpenDay.getAllMonitorOnTime());
		long seconds = Util.getSeconds(mOpenDay.getAllMonitorOnTime());

		mAllTime.setText(String.valueOf(hour+" "+mContext.getResources().getString(R.string.hr_string)+" "+min+" "+mContext.getResources().getString(R.string.min_string)));
	}
	
	private void setHistoryViewValue(){
		int dayHit = mOpenDay.getNumberOfTime();
		long allTime = mOpenDay.getAllMonitorOnTime();
		mNumber.setText(String.valueOf(dayHit));

        if (dayHit > 1){
            mHitText.setText(mContext.getString(R.string.hits_string));
        }

		if (mOpenDay.getDate().equals(mTodayString)){
			mDate.setText(mContext.getResources().getString(R.string.today_string));
		}else{
			mDate.setText(changeDateFormat(mOpenDay.getDate()));
		}
		long hour = Util.getHours(allTime);
		long min = Util.getMinutes(allTime);
		long seconds = Util.getSeconds(allTime);
		
//		long meanTime = allTime / dayHit;
//		int meanMin = (int)Util.getMinutes(meanTime);
//		int meanSeconds = (int)Util.getSeconds(meanTime);
		
		mAllTime.setText(String.valueOf(hour+" "+mContext.getResources().getString(R.string.hr_string)+" "+min+" "+mContext.getResources().getString(R.string.min_string)));
	}
	
	private void initListener(){
		mOpenDetailActivityClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent startDetailActivityIntent = new Intent(mContext, DetailActivity.class);
				Bundle data = new Bundle();
				data.putString(Def.BundleKey.OPENDAY_DATE, mOpenDay.getDate());
				startDetailActivityIntent.putExtra(Def.START_DETAILACTIVITY_BUNDLE, data);
				startDetailActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(startDetailActivityIntent);
			}
		};
	}
	
	private void setViewListener(){
		mDayBackView.setOnClickListener(mOpenDetailActivityClickListener);
	}
	
	
	public View getView(){
		return mDayView;
	}

	private String changeDateFormat(String date){
		String month = date.split("-")[1];
		String day = date.split("-")[2];
		return month+"-"+day;
	}

    private class AnimateProgressNumberTask extends Thread{

        private Handler animateHandler;
        private int openDailyNumber;
        AnimateProgressNumberTask(Handler handler, int number){
            animateHandler = handler;
            openDailyNumber = number+1;
        }

        public void run(){
            int waitTime = PROGRESS_ANIMATION_PERIOD / openDailyNumber;
            for (int i = 0 ; i < openDailyNumber ; i++){
                try {
                    Thread.sleep(waitTime);
                }catch(InterruptedException e){
                    Log.e(TAG, "InterruptedException:" + e.getMessage().toString());
                }
                publishProgress(i);
            }
        }

        private void publishProgress(int progress){
            Message msg = new Message();
            msg.what = UPDATE_PROGRESS_NUMBER;
            msg.arg1 = progress;
            animateHandler.sendMessage(msg);
        }
    }

    Handler mAnimateProgressNumberHandler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == UPDATE_PROGRESS_NUMBER){
                int progress = msg.arg1;
                mProgressBar.setProgress(progress);
                mNumber.setText(String.valueOf(progress));
            }
        }
    };
}


