package bersey.henry.screentimeouttile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.annotation.Nullable;

import java.util.Locale;

public final class LocaleUtils {
    private static final String KEY = "language";

    @Nullable
    public static String getLangCode(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(KEY, null);
    }

    public static boolean setLanguage(SharedPreferences sharedPreferences, @Nullable String langCode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (langCode == null)
            editor.remove(KEY);
        else
            editor.putString(KEY, langCode);

        return editor.commit();
    }

    public static void updateLanguage(SharedPreferences sharedPreferences, Context context) {
        Resources resources = context.getResources();
        Locale locale;
        boolean auto = !sharedPreferences.contains(KEY);

        if (auto)
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        else
            locale = new Locale(sharedPreferences.getString(KEY, null));


        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
