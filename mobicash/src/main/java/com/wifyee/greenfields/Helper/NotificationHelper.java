package com.wifyee.greenfields.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.wifyee.greenfields.Config.Config;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.activity.MobicashDashBoardActivity;

public class NotificationHelper extends ContextWrapper {
    private static String CHANNEL_ID = "com.wifyee.greenfields";
    private static String CHANNEL_NAME = "wifyee";
    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        channel.enableLights(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if(manager == null)
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannel(String title, String body, Bitmap bitmap){

        Intent intent = new Intent(this, MobicashDashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);

        Notification.Style style = new Notification.BigPictureStyle().bigPicture(bitmap);

        /*Notification.BigPictureStyle style = new Notification.BigPictureStyle();
        style.setBigContentTitle(title);
        style.setSummaryText(body);
        style.bigPicture(bitmap);*/

        Uri defaulSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        return new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_oriz)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaulSound)
                .setContentIntent(pendingIntent)
                .setStyle(style)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);

    }
}
