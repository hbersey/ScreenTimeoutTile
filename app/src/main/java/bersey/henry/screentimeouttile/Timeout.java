package bersey.henry.screentimeouttile;

import android.content.res.Resources;

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
        return amount + unit.getLonghand(res);
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

    enum Unit {
        SECONDS(1, R.string.seconds_short, R.string.seconds_long),
        MINUTE(60, R.string.minutes_short, R.string.minutes_long),
        HOUR(3600, R.string.hours_short, R.string.hours_long);

        private final int seconds;
        private final int shorthand;
        private final int longhand;

        Unit(int seconds, int shorthand, int longhand) {
            this.seconds = seconds;
            this.shorthand = shorthand;
            this.longhand = longhand;
        }

        public String getShorthand(Resources res) {
            return res.getString(shorthand);

        }

        public String getLonghand(Resources res) {
            return res.getString(longhand);
        }


        public int getSeconds() {
            return seconds;
        }
    }

}
