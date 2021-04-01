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

    public long getMS() {
        return this.unit.ms * amount;
    }

    enum Unit {
        SECONDS(1000, R.string.seconds_short, R.string.seconds_long),
        MINUTE(60000, R.string.minutes_short, R.string.minutes_long),
        HOUR(3600000, R.string.hours_short, R.string.hours_long);

        private final int ms;
        private final int shorthand;
        private final int longhand;

        Unit(int ms, int shorthand, int longhand) {
            this.ms = ms;
            this.shorthand = shorthand;
            this.longhand = longhand;
        }

        public String getShorthand(Resources res) {
            return res.getString(shorthand);

        }

        public String getLonghand(Resources res) {
            return res.getString(longhand);
        }
    }

}
