package com.popo.dailyopen;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyOpenService extends Service {

	private static String TAG = "DailyOpenService";
	private RecordHelper mRecordHelper;
    private SharedPreferences mSettings;
	private ScreenReceiver mScreenReceiver = new ScreenReceiver();
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private NotificationController mNotificationController;
    private GoogleAnalyticController mGoogleAnalyticController;
	
	long mOpenTime = 0;
	long mCloseTime = 0;
	Date mToday = null;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
        mSettings = getSharedPreferences(Def.APP_NAME, MODE_PRIVATE);
        mGoogleAnalyticController = GoogleAnalyticController.getInstance(this);
		registerScreenReceiver();
		mRecordHelper = new RecordHelper(this);
        mNotificationController = new NotificationController(this);
		// init open time and today when service is down.
		if (mToday == null){
			mOpenTime = System.currentTimeMillis();
			mToday = new Date(System.currentTimeMillis());
		}
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	public class ScreenReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				mOpenTime = System.currentTimeMillis();
				mToday = new Date(mOpenTime);
                int limitHits = mSettings.getInt(Def.Setting.LIMIT_HITS, 1000);
                mGoogleAnalyticController.sendEventHit(Def.GA.INTERACTION.USER, Def.GA.Category.TRACE, Def.GA.Action.SCREEN_ACTION, Def.GA.Label.ON, 1);
                // Show notification
                String todayId = mDateFormat.format(mToday);
                OpenDay today = mRecordHelper.getOneDayRecord(todayId);

                // Pop hits notification
                if (mSettings.getBoolean(Def.Setting.POP_HITS_NOTIFICATION, true)) {
                    if (today.getNumberOfTime() > 1) {
                        if (today.getNumberOfTime() > limitHits / 2){
                            mNotificationController.showNotification(String.valueOf(today.getNumberOfTime()) + " " + getResources().getString(R.string.hits_string) + " / " + limitHits + " " + getResources().getString(R.string.hits_string));
                        }else{
                            mNotificationController.showNotification(String.valueOf(today.getNumberOfTime()) + " " + getResources().getString(R.string.hits_string));
                        }

                    }else{
                        mNotificationController.showNotification(String.valueOf(today.getNumberOfTime()) + " " + getResources().getString(R.string.hit_string));
                    }
                }

				Log.v(TAG, "Screen on");

			}
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				mCloseTime = System.currentTimeMillis();
				if (mToday != null && mOpenTime != 0 && mCloseTime != 0) {
					String dateString = mDateFormat.format(mToday);
					mRecordHelper.addDailyRecord(dateString, mOpenTime,
							mCloseTime);
					Log.v(TAG, "Screen off at" + dateString + ", openTime:"
							+ mOpenTime + ", closeTime:" + mCloseTime);
				} else {
					Log.e(TAG, "Date:" + mToday + ", openTime:" + mOpenTime
							+ ", closeTime:" + mCloseTime);
				}
			}
		}
	}

	private void registerScreenReceiver() {
		registerOpenScreen();
		registerOffScreen();
	}

	private void registerOpenScreen() {
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		registerReceiver(mScreenReceiver, filter);
	}

	private void registerOffScreen() {
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mScreenReceiver, filter);
	}
	
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        // If app been removed from history, the service will be restarted.
        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);
    }
}
