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

    // REPLACE ME!
    private TimeoutManager() {
        this.timeouts = new ArrayList<>(Arrays.asList(
                new Timeout(30, Timeout.Unit.SECONDS),
                new Timeout(60, Timeout.Unit.SECONDS),
                new Timeout(2, Timeout.Unit.MINUTE),
                Timeout.NEVER
        ));
        this.currentIndex = 0;
    }

    private int previousIndex() {
        if (currentIndex <= 0)
            return timeouts.size() - 1;
        return currentIndex - 1;
    }

    private int nextIndex() {
        if (currentIndex >= timeouts.size() - 1)
            return 0;
        return currentIndex + 1;
    }

    public Timeout getCurrent() {
        return timeouts.get(currentIndex);
    }

    public Timeout getPrevious() {
        return timeouts.get(previousIndex());
    }

    public Timeout getNext() {
        return timeouts.get(nextIndex());
    }

    public void setNext() {
        currentIndex = nextIndex();
    }

    public void applySettings(ContentResolver cr) {
        Timeout timeout = getCurrent();
        Settings.System.putLong(cr, Settings.System.SCREEN_OFF_TIMEOUT, timeout.isNever() ? Integer.MAX_VALUE : timeout.getMS());
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
        while (i < timeouts.size() - 1 && timeout.getMS() > timeouts.get(i).getMS())
            i++;
        timeouts.add(i, timeout);
        return i;
    }

    public void removeAt(int i) {
        timeouts.remove(i);
    }


}
