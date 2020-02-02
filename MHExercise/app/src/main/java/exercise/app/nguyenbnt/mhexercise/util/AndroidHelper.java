package exercise.app.nguyenbnt.mhexercise.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import exercise.app.nguyenbnt.mhexercise.R;
import exercise.app.nguyenbnt.mhexercise.common.Constant;

public class AndroidHelper {

    /**
     * Vibrate the phone
     */
    public static void vibrate(Context context) {
        if (context == null) {
            return;
        }
        final int duration = 100;
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(duration,VibrationEffect.DEFAULT_AMPLITUDE));
        } else{
            v.vibrate(duration);
        }
    }

    /**
     * Push a notification when reach the location
     * @param context
     * @param location
     * @param notificationId
     */
    public static void pushNotification(Context context, Location location, int notificationId) {
        if (context == null || location == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,
                Constant.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.text_notification_title))
                .setContentText(context.getString(R.string.text_notification_content, location.getProvider()))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, mBuilder.build());
    }
}
