package com.popo.dailyopen;


import android.app.Activity;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class GoogleAnalyticController {

    private Context mContext = null;
    private static int DISPATCH_PERIOD = 60;
    private static GoogleAnalytics mAnalytics = null;
    private static Tracker mTracker = null;
    private static GoogleAnalyticController mGoogleAnalyticController = null;
    private GoogleAnalyticController(Context context){
        mContext = context;
        if (mAnalytics == null) {
            mAnalytics = GoogleAnalytics.getInstance(context);
            mAnalytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }
        if(mTracker == null){
            mTracker = mAnalytics.newTracker(Def.GA_ID);
        }
    }

    public static GoogleAnalyticController getInstance(Context context){
        if (mGoogleAnalyticController == null){
            mGoogleAnalyticController = new GoogleAnalyticController(context);
        }
        return mGoogleAnalyticController;
    }

    public boolean startActivityTracker(Activity activity){
        if (mAnalytics != null && mTracker != null){
            mAnalytics.reportActivityStart(activity);
            return true;
        }
        return false;
    }

    public boolean stopActivityTracker(Activity activity){
        if (mAnalytics != null && mTracker != null){
            mAnalytics.reportActivityStop(activity);
            return true;
        }
        return false;
    }

    public void sendScreenViewHit(String hitValue, String screenName){
        HashMap<String, String> screenMap = new HashMap<String, String>();
        if (mTracker != null){
            screenMap.put("&ni", hitValue);
            screenMap.put("&t", "ScreenView");
            screenMap.put("&cd", screenName);
            mTracker.send(screenMap);
        }
        screenMap.clear();
    }

    public void sendEventHit(String hitValue, String category, String action, String label, long value){
        boolean noninteraction = true;
        if (hitValue == Def.GA.INTERACTION.USER){
            noninteraction = false;
        }
        if (mTracker != null){
            mTracker.send(new HitBuilders.EventBuilder().setNonInteraction(noninteraction).setCategory(category).setAction(action).setLabel(label).setValue(value).build());
        }
    }

}
