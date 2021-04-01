package bersey.henry.screentimeouttile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Icon;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

public class TimeoutTileService extends TileService {

    private static final int TEXT_COLOUR = Color.BLACK;
    private static final float TEXT_SIZE = 64f;

    public TimeoutTileService() {

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


    @Override
    public void onStartListening() {
        Tile tile = getQsTile();
        if (tile == null)
            return;

        TimeoutManager timeoutManager = TimeoutManager.getInstance();
        int i = timeoutManager.getCurrentIndex();
        if (timeoutManager.isNeverEnabled() && i == timeoutManager.getTimeouts().size())
            updateTile(tile, null, true);
        else
            updateTile(tile, timeoutManager.get(i), false);
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

        TimeoutManager timeoutManager = TimeoutManager.getInstance();
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

    }
}
