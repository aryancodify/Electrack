package com.elecatrach.poc.electrack.client.gcm;

import com.elecatrach.poc.electrack.R;
import com.elecatrach.poc.electrack.admin.activities.AdminMainActivity;
import com.elecatrach.poc.electrack.client.activity.MainActivity;
import com.google.android.gms.gcm.GcmListenerService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class GcmMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");

        createNotification(from, message);
    }

    // Creates notification based on title and body received
    public void createNotification(String title, String body) {

        Context context = getBaseContext();
        PendingIntent resultPendingIntent = null;

        Intent user_intent = new Intent(context,MainActivity.class);
        Intent admin_intent = new Intent(context, AdminMainActivity.class);
        if(body.contains("anomalies")){
            admin_intent.putExtra("anomaly","anomaly");
            resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            admin_intent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );
        }
        else{

           if(body.contains("Bill")){
                user_intent.putExtra("screen","bill");
            }

            else{
               user_intent.putExtra("screen","current");
           }

            resultPendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0,
                            user_intent,
                            PendingIntent.FLAG_CANCEL_CURRENT
                    );

        }


        int mNotificationId = 001;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        Notification notification = mBuilder.setSmallIcon(R.mipmap.app_icon).setTicker(title)
                .setWhen(0).setAutoCancel(true).setContentTitle("Electrack").setStyle(new
                        NotificationCompat.BigTextStyle().bigText(body))
                .setContentIntent(resultPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap
                        .app_icon)).setContentText(body).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService
                (Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationId, notification);


    }
}
