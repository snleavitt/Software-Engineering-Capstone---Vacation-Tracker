package my.trackerapp.vacationtracker.UI;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import my.trackerapp.vacationtracker.R;

public class MyReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID_VACATION = "vacation_alert_channel";
    private static final String CHANNEL_ID_EXCURSION = "excursion_alert_channel";
    private static final String CHANNEL_ID_PLANE_DEPARTURE = "plane_departure_alert_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationType = intent.getStringExtra("notificationType");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //set notification type
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationType.equals("vacation")) {
                createNotificationChannel(context, CHANNEL_ID_VACATION);
            } else if (notificationType.equals("excursion")) {
                createNotificationChannel(context, CHANNEL_ID_EXCURSION);
            }
            else if (notificationType.equals("planeDeparture")) {
                createNotificationChannel(context, CHANNEL_ID_PLANE_DEPARTURE);
            }
        }

        String vacationName = intent.getStringExtra("vacationName");
        String notificationText = "";

        //vacation alarm
        if (notificationType.equals("vacation")) {
            notificationText = "Your vacation " + vacationName + " " + (intent.getBooleanExtra("isStart", true) ? "starts today!" : "ends today!");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_VACATION)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle("Vacation Alert")
                    .setContentText(notificationText)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            notificationManager.notify(0, builder.build());
        }

        //excursion alarm
        else if (notificationType.equals("startExcursion")) {
            String excursionName = intent.getStringExtra("excursionName");
            notificationText = "Your excursion for " + vacationName + " - " + excursionName + " starts now!";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_EXCURSION)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle("Excursion Alert")
                    .setContentText(notificationText)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            notificationManager.notify(intent.getIntExtra("excursionID", 0), builder.build());
        }

        //departure alarm
        else if (notificationType.equals("planeDeparture")) {
            notificationText = "Your Plane for " + vacationName + " departs now!";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_EXCURSION)
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setContentTitle("Departure Alert")
                    .setContentText(notificationText)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            notificationManager.notify(intent.getIntExtra("vacationID", 0), builder.build());

        }
    }

    private void createNotificationChannel(Context context, String channelId) {
        CharSequence name = "My Channel Name";
        String description = "My Channel Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelId, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}

