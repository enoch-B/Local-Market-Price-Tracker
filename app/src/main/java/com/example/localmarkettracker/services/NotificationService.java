package com.example.localmarkettracker.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.localmarkettracker.R;
import com.example.localmarkettracker.activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * FCM handler - receive and show notifications
 */
public class NotificationService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "lm_updates";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title = (remoteMessage.getNotification() != null)
                ? remoteMessage.getNotification().getTitle()
                : "Market Update";
        String body = (remoteMessage.getNotification() != null)
                ? remoteMessage.getNotification().getBody()
                : "";

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel c = new NotificationChannel(
                    CHANNEL_ID,
                    "Market updates",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            nm.createNotificationChannel(c);
        }

        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                ? PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                : PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent pi = PendingIntent.getActivity(this, 0, i, flags);

        NotificationCompat.Builder b = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification) // ✅ updated to your new drawable
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        nm.notify(1000, b.build());
    }
}
