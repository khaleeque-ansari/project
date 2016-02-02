package com.firsteat.firsteat.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.firsteat.firsteat.R;
import com.firsteat.firsteat.activities.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by touchmagics on 12/7/2015.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private String notificationTitle;
    private String notificationSubtext;
    private String notificationSummary;
    private String notificationImg_url;
    private String notificationContentText;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG,"notification json data: "+data.toString());
        notificationContentText = data.getString("contentText");
        notificationTitle=data.getString("title");
        notificationSubtext =data.getString("subText");
        notificationSummary=data.getString("summary");
        notificationImg_url=data.getString("img_url");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "title: " + notificationTitle);
        Log.d(TAG, "subText: " + notificationSubtext);
        Log.d(TAG, "summary: " + notificationSummary);
        Log.d(TAG, "img_url: " + notificationImg_url);


        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        /*
        * start SendNotification async task to build the notification
        * pass big image URL in execute
        * */
        new SendNotification(getApplicationContext()).execute(notificationImg_url);
        // [END_EXCLUDE]
    }
    // [END receive_message]


    private class SendNotification extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        String message;

        public SendNotification(Context context) {
            super();
            this.ctx = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {

                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;




            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            try {
                int icon = R.mipmap.ic_launcher;

                int mNotificationId = 001;
                Intent resultIntent = new Intent(MyGcmListenerService.this, MainActivity.class);
                TaskStackBuilder TSB = TaskStackBuilder.create(getApplicationContext());
                TSB.addParentStack(MainActivity.class);
                // Adds the Intent that starts the Activity to the top of the stack
                TSB.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        TSB.getPendingIntent(
                                0,
                                PendingIntent.FLAG_CANCEL_CURRENT
                        );

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        MyGcmListenerService.this);

                if(notificationImg_url==null){
                    mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationContentText));
                }else{
                    mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result).setSummaryText(notificationSummary));
                }
                Notification notification = mBuilder.setSmallIcon(icon).setTicker(notificationTitle).setWhen(0)
                        .setAutoCancel(true)
                        .setContentTitle(notificationTitle)
                        .setContentIntent(resultPendingIntent)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setLargeIcon(BitmapFactory.decodeResource(MyGcmListenerService.this.getResources(), R.mipmap.ic_launcher))
                        .setContentText(notificationContentText).build();


                NotificationManager notificationManager = (NotificationManager) MyGcmListenerService.this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(mNotificationId, notification);
//                NotificationCompat.Builder nb= new NotificationCompat.Builder(getApplicationContext());
//
//                nb.setSmallIcon(R.mipmap.ic_launcher);
//                nb.setContentTitle(notificationTitle);
//                nb.setContentText(notificationContentText);
//                nb.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationContentText));
//                NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(result);
//                s.setSummaryText(notificationSummary);
//                nb.setStyle(s);
//
//                Intent resultIntent = new Intent(MyGcmListenerService.this, MainActivity.class);
//                TaskStackBuilder TSB = TaskStackBuilder.create(getApplicationContext());
//                TSB.addParentStack(MainActivity.class);
//                // Adds the Intent that starts the Activity to the top of the stack
//                TSB.addNextIntent(resultIntent);
//                PendingIntent resultPendingIntent =
//                        TSB.getPendingIntent(
//                                0,
//                                PendingIntent.FLAG_UPDATE_CURRENT
//                        );
//
//                nb.setContentIntent(resultPendingIntent);
//                nb.setAutoCancel(true);
//                NotificationManager mNotificationManager =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                // mId allows you to update the notification later on.
//                mNotificationManager.notify(11221, nb.build());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
