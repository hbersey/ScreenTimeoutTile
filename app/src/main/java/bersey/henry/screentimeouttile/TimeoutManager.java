package bersey.henry.screentimeouttile;

import android.content.ContentResolver;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeoutManager {

    private static TimeoutManager INSTANCE;

    @SuppressWarnings("InstantiationOfUtilityClass")
    public static TimeoutManager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new TimeoutManager();
        return INSTANCE;
    }


    private ArrayList<Timeout> timeouts;
    private int currentIndex;
    private boolean neverEnabled;

    // REPLACE ME!
    private TimeoutManager() {
        this.timeouts = new ArrayList<>(Arrays.asList(
                new Timeout(30, Timeout.Unit.SECONDS),
                new Timeout(60, Timeout.Unit.SECONDS),
                new Timeout(2, Timeout.Unit.MINUTE)
        ));
        this.currentIndex = 0;
        this.neverEnabled = true;
    }


    public int getNextIndex() {
        if (currentIndex >= timeouts.size() - (neverEnabled ? 0 : 1))
            return 0;
        return currentIndex + 1;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }


    public void next() {
        currentIndex = getNextIndex();
    }

    public List<Timeout> getTimeouts() {
        return timeouts;
    }

    public Timeout get(int i) {
        return timeouts.get(i);
    }

    public boolean alreadyExists(Timeout timeout) {
        return timeouts.contains(timeout);
    }

    public int add(Timeout timeout) {
        int i = 0;
        while (i < timeouts.size() && timeout.getMS() > timeouts.get(i).getMS())
            i++;
        timeouts.add(i, timeout);
        return i;
    }

    public void removeAt(int i) {
        timeouts.remove(i);
    }

    public boolean isNeverEnabled() {
        return neverEnabled;
    }

    public void setNeverEnabled(boolean neverEnabled) {
        this.neverEnabled = neverEnabled;
    }
}
