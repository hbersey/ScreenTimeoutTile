package bersey.henry.screentimeouttile.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import bersey.henry.screentimeouttile.R;

public final class NotificationUtils {
    public static final String CHANNEL_ID = "bersey.henry.screentimeouttile-notifications";
    public static final int TIMEOUT_NID = 0;
    public static final String PREFS_KEY = "notifications";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
