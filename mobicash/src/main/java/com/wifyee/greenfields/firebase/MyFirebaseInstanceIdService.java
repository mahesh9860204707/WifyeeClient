package com.wifyee.greenfields.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.android.volley.toolbox.ImageRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wifyee.greenfields.Config.Config;
import com.wifyee.greenfields.Helper.NotificationHelper;
import com.wifyee.greenfields.R;
import com.wifyee.greenfields.Utils.LocalPreferenceUtility;
import com.wifyee.greenfields.activity.MobicashDashBoardActivity;
import com.wifyee.greenfields.constants.Constants;
import com.wifyee.greenfields.constants.NetworkConstant;
import com.wifyee.greenfields.constants.ResponseAttributeConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import timber.log.Timber;

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingServic";
    Bitmap bitmap;

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                showNotificationWithImageLevel26(bitmap);
            }else {
                sendNotification(Config.title,Config.message,bitmap);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };


    @Override
    public void onNewToken(String token) {
        Log.e(TAG, "Refreshed token: " + token);

        FirebaseMessaging.getInstance().subscribeToTopic("All");
        //sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        /*if(remoteMessage.getData().isEmpty()){
            getImage(remoteMessage,remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }else {
            //getImage(remoteMessage,remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));

        }*/

        if(remoteMessage.getData()!=null){
            Log.e("Firebase_Data","Message data payload: " + remoteMessage.getData());
            Config.title  = remoteMessage.getData().get("title");
            Config.message = remoteMessage.getData().get("message");
            Config.imageUrl = remoteMessage.getData().get("image");

            //bitmap = getBitmapfromUrl(Config.imageUrl);
            Log.w(TAG,"Message Notification Title: " +  Config.title);
            Log.w(TAG,"Message Notification Body: " +  Config.message);
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(getApplicationContext())
                            .load( Config.imageUrl)
                            .into(target);
                }
            });

            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                showNotificationWithImageLevel26(bitmap);
            }else {
                sendNotification( Config.title,Config.message,bitmap);
            }*/
        }

        if(remoteMessage.getNotification() !=null) {
            Log.e("Firebase_Noti", "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }

       /*String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String imageUri = remoteMessage.getData().get("image");
        String TrueOrFlase = remoteMessage.getData().get("AnotherActivity");

        bitmap = getBitmapfromUrl(imageUri);
        Log.w(TAG,"Message Notification Title: " + title);
        Log.w(TAG,"Message Notification Body: " + message);
        sendNotification(title,message,bitmap);*/
    }

    private void getImage(final RemoteMessage remoteMessage,String title,String message){
        //Config.message = remoteMessage.getNotification().getBody();
        //Config.title = remoteMessage.getNotification().getTitle();
        Config.message = message;
        Config.title = title;
        //Log.e("msg",Config.message);
        //Log.e("title",Config.title);

        if(remoteMessage.getData()!=null) {
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(getApplicationContext())
                            .load(remoteMessage.getData().get("image"))
                            .into(target);
                }
            });
        }else {
            showNotification();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationWithImageLevel26(Bitmap bitmap) {
        Date now = new Date();
        long uniqueId = now.getTime();

        NotificationHelper helper = new NotificationHelper(getBaseContext());

        Notification.Builder builder = helper.getChannel(Config.title,Config.message,bitmap);
        helper.getManager().notify((int) uniqueId,builder.build());
    }

    private void showNotificationWithImage(Bitmap bitmap) {
        Date now = new Date();
        long uniqueId = now.getTime();

        Notification.BigPictureStyle style = new Notification.BigPictureStyle();
        style.setSummaryText(Config.message);
        style.setBigContentTitle(Config.title);
        style.bigPicture(bitmap);

        Uri defaulSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Config.title)
                .setContentText(Config.message)
                .setAutoCancel(true)
                .setSound(defaulSound)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(style);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify((int) uniqueId,notificationBuilder.build());

    }

    private void showNotification() {
        Date now = new Date();
        long uniqueId = now.getTime();

        Uri defaulSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Config.title)
                .setContentText(Config.message)
                .setAutoCancel(true)
                .setSound(defaulSound)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify((int) uniqueId,notificationBuilder.build());

    }

    private void sendNotification(String title, String messageBody,Bitmap image){
        Date now = new Date();
        long uniqueId = now.getTime();
        Intent intent = new Intent(this, MobicashDashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification.Builder notification = new Notification.Builder(this);
        notification.setContentTitle(title);
        notification.setContentText(messageBody);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setContentIntent(pendingIntent);
        notification.setSound(soundUri);
        notification.setLargeIcon(BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher));
        notification.setStyle(new Notification.BigPictureStyle()
                .bigPicture(image));

        Notification noti = notification.build();
        noti.vibrate = new long[]{150, 300, 150, 400};
        notificationManager.notify((int) uniqueId, noti);
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private void sendRegistrationToServer(String token) {
        if(!LocalPreferenceUtility.getUserCode(getApplicationContext()).isEmpty()) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(ResponseAttributeConstants.USER_TYPE, "client");
                jsonObject.put(ResponseAttributeConstants.CLIENT_ID, LocalPreferenceUtility.getUserCode(getApplicationContext()));
                jsonObject.put(ResponseAttributeConstants.TOKEN, token);
                Log.e("json", jsonObject.toString());
            } catch (JSONException e) {
                Timber.e("JSONException. message : " + e.getMessage());
            }
            AndroidNetworking.post(NetworkConstant.TOKEN_UPDATE)
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("RSP_TOKEN_UPDATE", response.toString());
                            try {
                                if (response.getInt(ResponseAttributeConstants.STATUS) != 0) {
                                    Log.w("TOKEN","token updated");
                                } else {
                                    Log.w("TOKEN","token not updated");
                                }
                            } catch (JSONException e) {
                                Timber.e("JSONException Caught.  Message : " + e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            // handle error
                            Timber.e("called onError of User Login API.");
                            Timber.e("Error Message : " + error.getMessage());
                            Timber.e("Error code : " + error.getErrorCode());
                            Timber.e("Error Body : " + error.getErrorBody());
                            Timber.e("Error Detail : " + error.getErrorDetail());
                        }
                    });
        }else {
            Log.w("TOKEN_UPDATE","UserId is empty");
        }
    }
}