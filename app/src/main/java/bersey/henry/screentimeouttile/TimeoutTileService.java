package bersey.henry.screentimeouttile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import bersey.henry.screentimeouttile.utils.NotificationUtils;

import static bersey.henry.screentimeouttile.utils.LocaleUtils.updateLanguage;

public class TimeoutTileService extends TileService {

    private static final int TEXT_COLOUR = Color.BLACK;
    private static final float TEXT_SIZE = 64f;

    private final Handler handler;

    public TimeoutTileService() {
        handler = new Handler();
    }

    private static Icon generateIcon(String text) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(TEXT_COLOUR);
        paint.setTextSize(TEXT_SIZE);
        paint.setTextAlign(Paint.Align.LEFT);

        float textWidth = paint.measureText(text);
        float textHeight = paint.descent() - paint.ascent();

        int width = (int) ((textWidth / 0.75f) + 0.5f);
        int height = (int) ((paint.descent() - paint.ascent()) / 0.75f + 0.5f);

        float x = (width - textWidth) * 0.5f;
        float y = (height - textHeight) * 0.5f - paint.ascent();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, x, y, paint);

        return Icon.createWithBitmap(bitmap);
    }

    private void updateTile(Tile tile, Timeout timeout, boolean isNever) {
        tile.setState(isNever ? Tile.STATE_INACTIVE : Tile.STATE_ACTIVE);
        tile.setIcon(generateIcon(isNever ? getString(R.string.never) : timeout.getShorthand(getResources())));
        tile.updateTile();
    }

    public void applyTimeout(long ms) {
        Settings.System.putLong(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, ms);
    }


    private static void sendNotification(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        if (!preferences.getBoolean(NotificationUtils.PREFS_KEY, true))
            return;

        TimeoutManager timeoutManager = TimeoutManager.getInstance(preferences);
        int i = timeoutManager.getCurrentIndex();

        String content;
        if (timeoutManager.isNeverEnabled() && i == timeoutManager.getTimeouts().size())
            content = String.format(context.getString(R.string.notification_content), "\"" + context.getString(R.string.never) + "\"");
        else {
            String timeout = timeoutManager.get(i).getLonghand(context.getResources());
            content = String.format(context.getString(R.string.notification_content), timeout);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(NotificationUtils.TIMEOUT_NID, builder.build());
    }

    @Override
    public void onStartListening() {
        Tile tile = getQsTile();
        if (tile == null)
            return;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        TimeoutManager timeoutManager = TimeoutManager.getInstance(preferences);
        int i = timeoutManager.getCurrentIndex();
        if (timeoutManager.isNeverEnabled() && i == timeoutManager.getTimeouts().size())
            updateTile(tile, null, true);
        else
            updateTile(tile, timeoutManager.get(i), false);

        NotificationUtils.createNotificationChannel(this);
        updateLanguage(preferences, this);
    }

    @Override
    public void onClick() {
        Tile tile = getQsTile();
        if (tile == null)
            return;

        if (!Settings.System.canWrite(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        TimeoutManager timeoutManager = TimeoutManager.getInstance(PreferenceManager.getDefaultSharedPreferences(this));
        int next = timeoutManager.getNextIndex();
        boolean isNever = timeoutManager.isNeverEnabled() && next == timeoutManager.getTimeouts().size();

        if (isNever) {
            applyTimeout(Integer.MAX_VALUE);
            updateTile(tile, null, true);
        } else {
            Timeout timeout = timeoutManager.get(next);
            applyTimeout(timeout.getMS());
            updateTile(tile, timeout, false);
        }

        timeoutManager.next();
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(() -> {
            sendNotification(this);
            timeoutManager.save();
        }, 500);
    }
}
