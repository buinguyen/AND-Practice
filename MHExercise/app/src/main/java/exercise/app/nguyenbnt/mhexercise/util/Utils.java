package exercise.app.nguyenbnt.mhexercise.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class Utils {
    private static final String TAG = "Utils";

    /**
     * Notify a message to user by Toast
     * @param activity
     * @param message
     */
    public static void notifyUser(final Activity activity, final String message) {
        if (activity == null) {
            return;
        }
        Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Notify a message to user by Toast
     * @param activity
     * @param messageId
     */
    public static void notifyUser(final Activity activity, final int messageId) {
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), messageId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Notify a message to user by Toast
     * @param appContext
     * @param message
     */
    public static void notifyUser(final Context appContext, final String message) {
        if (appContext == null) {
            return;
        }
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Notify a message to user by Toast
     * @param appContext
     * @param messageId
     */
    public static void notifyUser(final Context appContext, final int messageId) {
        if (appContext == null) {
            return;
        }
        Toast.makeText(appContext, messageId, Toast.LENGTH_SHORT).show();
    }

}
