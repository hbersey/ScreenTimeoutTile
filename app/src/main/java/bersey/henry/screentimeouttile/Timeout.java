package bersey.henry.screentimeouttile;

public class Timeout {
    private int amount;
    private Unit unit;

    public Timeout(int amount, Unit unit) {
        this.amount = amount;
        this.unit = unit;
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
        SECONDS(1),
        MINUTE(60),
        HOUR(3600);

        private final int seconds;

        Unit(int seconds) {
            this.seconds = seconds;
        }

        public int getSeconds() {
            return seconds;
        }
    }

}
