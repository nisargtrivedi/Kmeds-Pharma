package Services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import codecanyon.jagatpharma.R;
import util.DosageBroadCastReceiver;
import util.NotificationCreator;

public class AlaramServices extends Service {

    DosageBroadCastReceiver alarm = new DosageBroadCastReceiver();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(1234, builder.build());
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        try {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForeground(NotificationCreator.getNotificationId(),
                        NotificationCreator.getNotification(this.getApplicationContext()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        this.registerReceiver(alarm,new IntentFilter());
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
