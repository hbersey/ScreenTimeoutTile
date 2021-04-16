package bersey.henry.screentimeouttile;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TimeoutManager {

    private static final String PREFS_KEY = "timeout_manager";
    private static final char DELIMITER = ';';
    private static final String DEFAULT_MANAGER_STATE = "1;0;015;030;11;12;15;110;";

    private static TimeoutManager INSTANCE;

    @SuppressWarnings("InstantiationOfUtilityClass")
    public static TimeoutManager getInstance(SharedPreferences sharedPreferences) {
        if (INSTANCE == null)
            INSTANCE = load(sharedPreferences);
        return INSTANCE;
    }


    private boolean neverEnabled;
    private int currentIndex;
    private ArrayList<Timeout> timeouts;
    private final SharedPreferences sharedPreferences;

    // 1st: neverEnabled
    // 2nd: currentIndex
    // rest: timeouts (1st char: index of unit; rest: amount)

    public void save() {
        StringBuilder builder = new StringBuilder();

        builder.append(neverEnabled ? '1' : '0');
        builder.append(DELIMITER);
        builder.append(currentIndex);
        builder.append(DELIMITER);
        timeouts.forEach(timeout -> {
            builder.append(timeout.toString());
            builder.append(DELIMITER);
        });

        String data = builder.toString();
        sharedPreferences.edit().putString(PREFS_KEY, data).apply();
    }

    private static TimeoutManager load(SharedPreferences sharedPreferences) {
        List<String> data = Arrays.stream(sharedPreferences.getString(PREFS_KEY, DEFAULT_MANAGER_STATE).split(String.valueOf(DELIMITER))).collect(Collectors.toList());

        boolean neverEnabled = data.get(0).equals("1");
        int currentIndex = Integer.parseInt(data.get(1));
        ArrayList<Timeout> timeouts = data.subList(2, data.size()).stream().map(Timeout::fromString).collect(Collectors.toCollection(ArrayList::new));

        return new TimeoutManager(neverEnabled, currentIndex, timeouts, sharedPreferences);
    }

    private TimeoutManager(boolean neverEnabled, int currentIndex, ArrayList<Timeout> timeouts, SharedPreferences sharedPreferences) {
        this.neverEnabled = neverEnabled;
        this.currentIndex = currentIndex;
        this.timeouts = timeouts;
        this.sharedPreferences = sharedPreferences;
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

        if (i <= currentIndex)
            currentIndex++;

        return i;
    }

    public int remove(Timeout timeout) {
        int i = timeouts.indexOf(timeout);
        timeouts.remove(i);

        if (i <= currentIndex)
            currentIndex--;

        if (currentIndex > (timeouts.size() - (neverEnabled ? 0 : 1)))
            currentIndex = (timeouts.size() - (neverEnabled ? 0 : 1));
        else if (currentIndex < 0)
            currentIndex = 0;

        save();

        return i;
    }

    public boolean isNeverEnabled() {
        return neverEnabled;
    }

    public void setNeverEnabled(boolean neverEnabled) {
        this.neverEnabled = neverEnabled;
    }
}
