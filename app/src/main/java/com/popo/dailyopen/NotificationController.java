package com.popo.dailyopen;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.popo.dailyopen.activity.MainActivity;

public class NotificationController {

    private NotificationManager mNotificationManager;
    private int mNotificationId = 1;
    private Context mContext;
    public NotificationController(Context context){
        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mContext = context;
    }

    public void showNotification(String text){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext);
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher).setContentTitle(mContext.getString(R.string.app_name)).setContentText(text).setContentIntent(pendingIntent).setAutoCancel(true);
        mNotificationManager.notify(mNotificationId, notificationBuilder.build());
    }

}
