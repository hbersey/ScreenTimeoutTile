package bersey.henry.screentimeouttile;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;

public class Timeout {
    private int amount;
    private Unit unit;


    public Timeout(int amount, Unit unit) {
        this.amount = amount;
        this.unit = unit;
    }


    public String getShorthand(Resources res) {
        return amount + unit.getShorthand(res);
    }

    public String getLonghand(Resources res) {
        return amount + " " + unit.getLonghand(res);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public long getMS() {
        return this.unit.ms * amount;
    }


    enum Unit {
        SECONDS(1000, R.string.seconds_short, R.string.seconds_long, 59),
        MINUTE(60000, R.string.minutes_short, R.string.minutes_long, 59),
        HOUR(3600000, R.string.hours_short, R.string.hours_long, 24);

        private final int ms;
        private final int shorthand;
        private final int longhand;
        private final int max;

        Unit(int ms, int shorthand, int longhand, int max) {
            this.ms = ms;
            this.shorthand = shorthand;
            this.longhand = longhand;
            this.max = max;
        }

        public String getShorthand(Resources res) {
            return res.getString(shorthand);

        }

        public String getLonghand(Resources res) {
            return res.getString(longhand);
        }

        public int getMax() {
            return max;
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Timeout))
            return false;

        Timeout other = (Timeout) obj;

        return other.getMS() == getMS();
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(Arrays.asList(Unit.values()).indexOf(unit)) + getAmount();
    }

    public static Timeout fromString(String string) {
        int unitIndex = Integer.parseInt(string.substring(0, 1));
        int amount = Integer.parseInt(string.substring(1));
        return new Timeout(amount,Unit.values()[unitIndex]);
    }
}
