package com.example.mastersrgamerz.Notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.mastersrgamerz.R;
import com.example.mastersrgamerz.ui.gallery.GalleryFragment;

import static com.example.mastersrgamerz.MainActivity.CHANNEL_ID;

public class NotficationHelper {

    public static void displayNotification(Context context, String title, String body) {

        Intent intent = new Intent(context, GalleryFragment.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                100,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.apppngnoti)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setStyle(new NotificationCompat.BigTextStyle());


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, mBuilder.build());
    }
}
