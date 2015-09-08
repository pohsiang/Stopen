package com.popo.dailyopen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Mike on 2/8/15.
 */
public class StopenReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent){
        Intent startServiceIntent = new Intent(context, DailyOpenService.class);
        context.startService(startServiceIntent);
    }
}
