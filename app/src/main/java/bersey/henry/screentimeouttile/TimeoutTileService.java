package bersey.henry.screentimeouttile;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

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

    @Override
    public void onStartListening() {
        Tile tile = getQsTile();
        if (tile == null)
            return;

        TimeoutManager timeoutManager = TimeoutManager.getInstance();
        Timeout timeout = timeoutManager.getCurrent();
        String iconText = timeout.getShorthand(getResources());

        tile.setState(Tile.STATE_ACTIVE); // If timeout != never
        tile.setIcon(generateIcon(iconText)); // Get from current timeout
        tile.updateTile();
    }

    @Override
    public void onClick() {
        Tile tile = getQsTile();
        if (tile == null)
            return;

        TimeoutManager timeoutManager = TimeoutManager.getInstance();
        Timeout next = timeoutManager.getNext();
        String iconText = next.getShorthand(getResources());

        tile.setState(Tile.STATE_ACTIVE); // If timeout != never
        tile.setIcon(generateIcon(iconText)); // Get from current timeout
        tile.updateTile();

        timeoutManager.setNext();
    }
}
