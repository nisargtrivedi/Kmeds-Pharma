package util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import codecanyon.jagatpharma.R;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

//import com.spyapptracker.act.BuildConfig;
//import com.spyapptracker.act.R;

public class NotificationCreator {


    //private static final int NOTIFICATION_ID = 1094;
    private static final int NOTIFICATION_ID = 1;

    private static Notification notification;

    public static Notification getNotification(Context context) {

        if(notification == null) {

            /*notification = new NotificationCompat.Builder(context)
                    .setContentTitle("Try Foreground Service")
                    .setContentText("Yuhu..., I'm trying foreground service")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();*/

//            RemoteViews contentView = new RemoteViews(context.getPackageName(),R.layout.custom_notification);
//            contentView.setTextViewText(R.id.title, "");
//            notification.contentView = contentView;

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId);
            notification = notificationBuilder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_logo_09)
                    .setPriority(PRIORITY_MIN)
                    .setColor(Color.parseColor("#ffffff"))
                    .setShowWhen(false)
                    .setCategory(NotificationCompat.CATEGORY_SERVICE)
                    .build();
        }
        return notification;
    }

    public static int getNotificationId() {
        return NOTIFICATION_ID;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public static String createNotificationChannel(NotificationManager notificationManager) {
        String channelId = "my_service_channelid";
        String channelName = "My Foreground Service";
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        // omitted the LED color
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

}
